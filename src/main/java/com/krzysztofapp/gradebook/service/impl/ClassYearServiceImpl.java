package com.krzysztofapp.gradebook.service.impl;

import com.krzysztofapp.gradebook.domain.ClassYearEto;
import com.krzysztofapp.gradebook.domain.mapper.ClassYearMapper;
import com.krzysztofapp.gradebook.exceptions.ClassYearNotFoundException;
import com.krzysztofapp.gradebook.persistence.entity.ClassYearEntity;
import com.krzysztofapp.gradebook.persistence.entity.SubjectEntity;
import com.krzysztofapp.gradebook.persistence.entity.utils.SubjectUtils;
import com.krzysztofapp.gradebook.persistence.repo.ClassYearRepo;
import com.krzysztofapp.gradebook.persistence.repo.SubjectRepo;
import com.krzysztofapp.gradebook.service.ClassYearService;
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
import java.util.Set;

@Service
public class ClassYearServiceImpl implements ClassYearService {


    private final ClassYearRepo classYearRepository;
    private final SubjectRepo subjectRepository;

    private final Validator validator;


    @Autowired
    public ClassYearServiceImpl(final ClassYearRepo classYearRepository, final SubjectRepo subjectRepository, final Validator validator) {

        this.classYearRepository = classYearRepository;
        this.subjectRepository = subjectRepository;
        this.validator = validator;
    }

    @Override
    public ClassYearEto findClassYearById(Long id) {
        ClassYearEntity result = this.classYearRepository.findById(id)
                .orElseThrow( ()-> new ClassYearNotFoundException("ClassYear with id: " + id + " could not be found"));

        return ClassYearMapper.mapToETO(result);
    }

    @Override
    public ClassYearEto createNew(ClassYearEto newClassYear) {
        if (newClassYear.getId() != null) {
            newClassYear.setId(null);
        }

        Set<ConstraintViolation<ClassYearEto>> violations = validator.validate(newClassYear);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<ClassYearEto> constraintViolation : violations) {
                sb.append(constraintViolation.getMessage());
                sb.append("\n");
            }
            throw new ConstraintViolationException("Error occurred: " + sb.toString(), violations);
        }

        ClassYearEntity classYearEntity = ClassYearMapper.mapToEntity(newClassYear);
        classYearEntity = this.classYearRepository.save(classYearEntity);
        return ClassYearMapper.mapToETO(classYearEntity);
    }

    @Transactional
    @Override
    public ClassYearEto partialUpdate(Long id, Map<String, Object> updateInfo) {

        ClassYearEntity classYear = this.classYearRepository.findById(id)
                .orElseThrow(() -> new ClassYearNotFoundException("ClassYear with id: " + id + " could not be found"));
        ClassYearEto classYearEto = ClassYearMapper.mapToETO(classYear);
        updateInfo.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(ClassYearEto.class, key);
            field.setAccessible(true);
            ReflectionUtils.setField(field, classYearEto, value);
        });
        classYear = ClassYearMapper.mapToEntity(classYearEto);
        this.classYearRepository.save(classYear);
        if(updateInfo.containsKey("className") || updateInfo.containsKey("classLevel")){
            List<SubjectEntity> subjects = this.subjectRepository.findAllStudentEntityByClassYearEntityId(id);
            subjects.forEach(subject -> subject.setName(SubjectUtils.setCustomName(subject.getClassYear(), subject.getSubjectType())));
        }

        return ClassYearMapper.mapToETO(classYear);
    }

    @Override
    public void delete(Long id) {
        this.classYearRepository.deleteById(id);
    }
}
