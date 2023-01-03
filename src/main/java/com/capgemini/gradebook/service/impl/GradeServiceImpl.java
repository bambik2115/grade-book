package com.capgemini.gradebook.service.impl;


import com.capgemini.gradebook.domain.GradeEto;
import com.capgemini.gradebook.domain.GradeSearchCriteria;
import com.capgemini.gradebook.domain.mapper.GradeMapper;
import com.capgemini.gradebook.exceptions.*;
import com.capgemini.gradebook.persistence.entity.Grade;
import com.capgemini.gradebook.persistence.entity.StudentEntity;
import com.capgemini.gradebook.persistence.entity.SubjectEntity;
import com.capgemini.gradebook.persistence.repo.GradeRepo;
import com.capgemini.gradebook.persistence.repo.StudentRepo;
import com.capgemini.gradebook.persistence.repo.SubjectRepo;
import com.capgemini.gradebook.service.GradeService;
import org.apache.commons.math3.util.Precision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class GradeServiceImpl implements GradeService {


    private final GradeRepo gradeRepository;
    private final StudentRepo studentRepository;
    private final SubjectRepo subjectRepository;


    @Autowired
    private Validator validator;

    @Autowired
    public GradeServiceImpl(final GradeRepo gradeRepository, final StudentRepo studentRepository, final SubjectRepo subjectRepository) {

        this.gradeRepository = gradeRepository;
        this.studentRepository = studentRepository;
        this.subjectRepository = subjectRepository;
    }

    @Override
    public GradeEto findGradeById(Long id) {

        Grade grade = this.gradeRepository.findById(id)
                .orElseThrow( ()-> new GradeNotFoundException("Grade with id: " + id + " could not be found"));

        return GradeMapper.mapToETO(grade);
    }

    @Override
    public List<GradeEto> searchGradesByCriteria(GradeSearchCriteria criteria) {

        if(criteria.getCreatedDateTo().isBefore(criteria.getCreatedDateFrom())) {
            throw new InvalidRangeProvidedException("Grade creation date To can't be before From");
        }
        if(criteria.getValueTo() < criteria.getValueFrom()) {
            throw new InvalidRangeProvidedException(("Grade value To can't be lower than From"));
        }
        if(criteria.getWeightTo().compareTo(criteria.getWeightFrom()) < 0) {
            throw new InvalidRangeProvidedException(("Grade weight To can't be lower than From"));
        }

        List<Grade> foundGrades = this.gradeRepository.findByCriteria(criteria);

        return GradeMapper.mapToETOList(foundGrades);
    }


    @Override
    public Double getWeightedAverage(Long studentId, Long subjectId) {

        List<Grade> studentGradeList = this.gradeRepository.findAllGradeByStudentEntityIdAndSubjectEntityId(studentId, subjectId);
        Double sumOfGrades = studentGradeList.stream().mapToDouble(grade -> grade.getValue()*grade.getWeight().doubleValue()).reduce(0, Double::sum);
        Double totalNumberOfGrades = studentGradeList.stream().mapToDouble(grade -> grade.getWeight().intValue()).reduce(0, Double::sum);

        return Precision.round(sumOfGrades/totalNumberOfGrades,2);
    }

    @Transactional
    @Override
    public GradeEto createNew(GradeEto newGrade) {
        if (newGrade.getId() != null) {
            newGrade.setId(null);
        }

        Set<ConstraintViolation<GradeEto>> violations = validator.validate(newGrade);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<GradeEto> constraintViolation : violations) {
                sb.append(constraintViolation.getMessage());
            }
            throw new ConstraintViolationException("Error occurred: " + sb.toString(), violations);
        }

        if(gradeCreatedToday(newGrade).isPresent()) {
            throw new GradeAlreadyCreatedTodayException("Grade of type: " + newGrade.getGradeType() + " has already been inserted today!");
        }

        if((newGrade.getValue() == 1 || newGrade.getValue() == 6) && (newGrade.getComment() == null || newGrade.getComment().isBlank())) {
            throw new GradeCommentIsEmptyException("Comment field for this grade value can't be empty!");
        }

        StudentEntity student = this.studentRepository.findById(newGrade.getStudentEntityId())
                .orElseThrow( ()-> new StudentNotFoundException("Student with id: " + newGrade.getStudentEntityId() + " could not be found"));
        SubjectEntity subject = this.subjectRepository.findById(newGrade.getSubjectEntityId())
                .orElseThrow( ()-> new SubjectNotFoundException("Subject with id: " + newGrade.getSubjectEntityId() + " could not be found"));
        Grade grade = GradeMapper.mapToEntity(newGrade);
        grade.setTeacherEntity(subject.getTeacherEntity());
        grade.setStudentEntity(student);
        grade.setSubjectEntity(subject);
        grade = this.gradeRepository.save(grade);

        return GradeMapper.mapToETO(grade);
    }


    @Transactional
    @Override
    public GradeEto partialUpdate(Long id, Map<String, Object> updateInfo) {

        Grade grade = this.gradeRepository.findById(id).get();
        GradeEto gradeeto = GradeMapper.mapToETO(grade);
        updateInfo.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(GradeEto.class, key);
            field.setAccessible(true);
            if(key.equals("subjectEntityId") || key.equals("studentEntityId"))
                ReflectionUtils.setField(field, gradeeto, Long.valueOf((Integer) value));
            else
                ReflectionUtils.setField(field, gradeeto, value);
        });
        grade = GradeMapper.mapToEntity(gradeeto);
        StudentEntity student = this.studentRepository.findById(gradeeto.getStudentEntityId())
                .orElseThrow( ()-> new StudentNotFoundException("Student with id: " + gradeeto.getStudentEntityId() + " could not be found"));
        SubjectEntity subject = this.subjectRepository.findById(gradeeto.getSubjectEntityId())
                .orElseThrow( ()-> new SubjectNotFoundException("Subject with id: " + gradeeto.getSubjectEntityId() + " could not be found"));
        grade.setSubjectEntity(subject);
        grade.setStudentEntity(student);
        grade.setTeacherEntity(subject.getTeacherEntity());
        this.gradeRepository.save(grade);

        return GradeMapper.mapToETO(grade);
    }

    @Override
    public void delete(Long id) {
        this.gradeRepository.deleteById(id);
    }


    private Optional<Grade> gradeCreatedToday(GradeEto gradeeto) {
        return this.gradeRepository
                .findGradeByDateOfGradeAndGradeType
                        (gradeeto.getDateOfGrade(), gradeeto.getGradeType());
    }
}
