package com.krzysztofapp.gradebook.service.impl;

import com.krzysztofapp.gradebook.domain.SubjectEto;
import com.krzysztofapp.gradebook.domain.mapper.SubjectMapper;
import com.krzysztofapp.gradebook.exceptions.ClassYearNotFoundException;
import com.krzysztofapp.gradebook.exceptions.SubjectNotFoundException;
import com.krzysztofapp.gradebook.exceptions.TeacherNotFoundException;
import com.krzysztofapp.gradebook.persistence.entity.ClassYearEntity;
import com.krzysztofapp.gradebook.persistence.entity.SubjectEntity;
import com.krzysztofapp.gradebook.persistence.entity.TeacherEntity;
import com.krzysztofapp.gradebook.persistence.entity.utils.SubjectUtils;
import com.krzysztofapp.gradebook.persistence.repo.ClassYearRepo;
import com.krzysztofapp.gradebook.persistence.repo.SubjectRepo;
import com.krzysztofapp.gradebook.persistence.repo.TeacherRepo;
import com.krzysztofapp.gradebook.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Set;


@Service
public class SubjectServiceImpl implements SubjectService {


    private final SubjectRepo subjectRepository;
    private final TeacherRepo teacherRepository;
    private final ClassYearRepo classYearRepository;

    private final Validator validator;


    @Autowired
    public SubjectServiceImpl(final SubjectRepo subjectRepository, final TeacherRepo teacherRepository, final ClassYearRepo classYearRepository, final Validator validator) {

        this.subjectRepository = subjectRepository;
        this.teacherRepository = teacherRepository;
        this.classYearRepository = classYearRepository;
        this.validator = validator;
    }

    @Override
    public SubjectEto findSubjectById(Long id) {

        SubjectEntity result = this.subjectRepository.findById(id)
                .orElseThrow( ()-> new SubjectNotFoundException("Subject with id: " + id + " could not be found"));

        return SubjectMapper.mapToETO(result);
    }

    @Transactional
    @Override
    public SubjectEto createNew(SubjectEto newSubject) {
        if (newSubject.getId() != null) {
            newSubject.setId(null);
        }

        Set<ConstraintViolation<SubjectEto>> violations = this.validator.validate(newSubject);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<SubjectEto> constraintViolation : violations) {
                sb.append(constraintViolation.getMessage());
                sb.append("\n");
            }
            throw new ConstraintViolationException("Error occurred: " + sb.toString(), violations);
        }

        SubjectEntity subjectEntity = SubjectMapper.mapToEntity(newSubject);
        TeacherEntity teacher = this.teacherRepository.findById(newSubject.getTeacherEntityId())
                .orElseThrow( ()-> new TeacherNotFoundException("Teacher with id: " + newSubject.getTeacherEntityId() + " could not be found"));
        ClassYearEntity classYear = this.classYearRepository.findById(newSubject.getClassYearEntityId())
                .orElseThrow( ()-> new ClassYearNotFoundException("ClassYear with id: " + newSubject.getClassYearEntityId() + " could not be found"));
        subjectEntity.setTeacherEntity(teacher);
        subjectEntity.setClassYear(classYear);
        subjectEntity.setName(SubjectUtils.setCustomName(classYear, newSubject.getSubjectType()));
        subjectEntity = this.subjectRepository.save(subjectEntity);
        return SubjectMapper.mapToETO(subjectEntity);
    }

    @Transactional
    @Override
    public SubjectEto updateSubjectTeacher(Long id, Long newTeacherId) {

        SubjectEntity subject = this.subjectRepository.findById(id)
                .orElseThrow(() -> new SubjectNotFoundException("Subject with id: " + id + " could not be found"));
        TeacherEntity teacher = this.teacherRepository.findById(newTeacherId)
                .orElseThrow( ()-> new TeacherNotFoundException("Teacher with id: " + newTeacherId + " could not be found"));
        subject.setTeacherEntity(teacher);

        return SubjectMapper.mapToETO(subject);
    }


    @Override
    public void delete(Long id) {

        this.subjectRepository.deleteById(id);

    }


}
