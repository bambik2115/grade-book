package com.krzysztofapp.gradebook.service.impl;


import com.krzysztofapp.gradebook.domain.GradeEto;
import com.krzysztofapp.gradebook.domain.GradeSearchCriteria;
import com.krzysztofapp.gradebook.domain.mapper.GradeMapper;
import com.capgemini.gradebook.exceptions.*;
import com.krzysztofapp.gradebook.persistence.entity.GradeEntity;
import com.krzysztofapp.gradebook.persistence.entity.StudentEntity;
import com.krzysztofapp.gradebook.persistence.entity.SubjectEntity;
import com.krzysztofapp.gradebook.persistence.repo.GradeRepo;
import com.krzysztofapp.gradebook.persistence.repo.StudentRepo;
import com.krzysztofapp.gradebook.persistence.repo.SubjectRepo;
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

    private final Validator validator;

    @Autowired
    public GradeServiceImpl(final GradeRepo gradeRepository, final StudentRepo studentRepository, final SubjectRepo subjectRepository, final Validator validator) {

        this.gradeRepository = gradeRepository;
        this.studentRepository = studentRepository;
        this.subjectRepository = subjectRepository;
        this.validator = validator;
    }

    @Override
    public GradeEto findGradeById(Long id) {

        GradeEntity grade = this.gradeRepository.findById(id)
                .orElseThrow( ()-> new GradeNotFoundException("Grade with id: " + id + " could not be found"));

        return GradeMapper.mapToETO(grade);
    }

    @Override
    public List<GradeEto> searchGradesByCriteria(GradeSearchCriteria criteria) {

        if(criteria.getCreatedDateFrom() != null && criteria.getCreatedDateTo() != null && criteria.getCreatedDateTo().isBefore(criteria.getCreatedDateFrom())) {
            throw new InvalidRangeProvidedException("Grade creation date To can't be before From");
        }
        if(criteria.getValueFrom() != null && criteria.getValueTo() != null && criteria.getValueTo() < criteria.getValueFrom()) {
            throw new InvalidRangeProvidedException(("Grade value To can't be lower than From"));
        }
        if(criteria.getWeightFrom() != null && criteria.getWeightTo() != null && criteria.getWeightTo().compareTo(criteria.getWeightFrom()) < 0) {
            throw new InvalidRangeProvidedException(("Grade weight To can't be lower than From"));
        }

        List<GradeEntity> foundGrades = this.gradeRepository.findByCriteria(criteria);

        return GradeMapper.mapToETOList(foundGrades);
    }


    @Override
    public Double getWeightedAverage(Long studentId, Long subjectId) {

        List<GradeEntity> studentGradeList = this.gradeRepository.findAllGradeByStudentEntityIdAndSubjectEntityId(studentId, subjectId);
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

        Set<ConstraintViolation<GradeEto>> violations = this.validator.validate(newGrade);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<GradeEto> constraintViolation : violations) {
                sb.append(constraintViolation.getMessage());
                sb.append("\n");
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
        GradeEntity grade = GradeMapper.mapToEntity(newGrade);
        grade.setTeacherEntity(subject.getTeacherEntity());
        grade.setStudentEntity(student);
        grade.setSubjectEntity(subject);
        grade = this.gradeRepository.save(grade);

        return GradeMapper.mapToETO(grade);
    }


    @Transactional
    @Override
    public GradeEto partialUpdate(Long id, Map<String, Object> updateInfo) {

        GradeEntity grade = this.gradeRepository.findById(id)
                .orElseThrow(() -> new GradeNotFoundException("Grade with id: " + id + " could not be found"));
        GradeEto gradeEto = GradeMapper.mapToETO(grade);
        updateInfo.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(GradeEto.class, key);
            field.setAccessible(true);
            if(key.equals("subjectEntityId") || key.equals("studentEntityId"))
                ReflectionUtils.setField(field, gradeEto, Long.valueOf((Integer) value));
            else
                ReflectionUtils.setField(field, gradeEto, value);
        });
        grade = GradeMapper.mapToEntity(gradeEto);
        StudentEntity student = this.studentRepository.findById(gradeEto.getStudentEntityId())
                .orElseThrow( ()-> new StudentNotFoundException("Student with id: " + gradeEto.getStudentEntityId() + " could not be found"));
        SubjectEntity subject = this.subjectRepository.findById(gradeEto.getSubjectEntityId())
                .orElseThrow( ()-> new SubjectNotFoundException("Subject with id: " + gradeEto.getSubjectEntityId() + " could not be found"));
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


    private Optional<GradeEntity> gradeCreatedToday(GradeEto gradeEto) {
        return this.gradeRepository
                .findGradeByDateOfGradeAndGradeType
                        (gradeEto.getDateOfGrade(), gradeEto.getGradeType());
    }
}
