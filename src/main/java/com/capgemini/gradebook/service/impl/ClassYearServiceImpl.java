package com.capgemini.gradebook.service.impl;

import com.capgemini.gradebook.domain.ClassYearEto;
import com.capgemini.gradebook.domain.mapper.ClassYearMapper;
import com.capgemini.gradebook.exceptions.ClassYearNotFoundException;
import com.capgemini.gradebook.persistence.entity.ClassYear;
import com.capgemini.gradebook.persistence.entity.SubjectEntity;
import com.capgemini.gradebook.persistence.repo.ClassYearRepo;
import com.capgemini.gradebook.persistence.repo.SubjectRepo;
import com.capgemini.gradebook.service.ClassYearService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@Service
public class ClassYearServiceImpl implements ClassYearService {


    private final ClassYearRepo classyearRepository;
    private final SubjectRepo subjectRepository;

    @Autowired
    public ClassYearServiceImpl(final ClassYearRepo classyearRepository, final SubjectRepo subjectRepository) {

        this.classyearRepository = classyearRepository;
        this.subjectRepository = subjectRepository;
    }

    @Override
    public ClassYearEto findClassYearById(Long id) {
        ClassYear result = this.classyearRepository.findById(id)
                .orElseThrow( ()-> new ClassYearNotFoundException("ClassYear with id: " + id + " could not be found"));

        return ClassYearMapper.mapToETO(result);
    }

    @Override
    public ClassYearEto createNew(ClassYearEto newClassYear) {
        if (newClassYear.getId() != null) {
            newClassYear.setId(null);
        }

        ClassYear classyearEntity = ClassYearMapper.mapToEntity(newClassYear);
        classyearEntity = this.classyearRepository.save(classyearEntity);
        return ClassYearMapper.mapToETO(classyearEntity);
    }

    @Transactional
    @Override
    public ClassYearEto partialUpdate(Long id, Map<String, Object> updateInfo) {

        ClassYear classyear = this.classyearRepository.findById(id)
                .orElseThrow( ()-> new ClassYearNotFoundException("ClassYear with id: " + id + " could not be found"));
        ClassYearEto classyeareto = ClassYearMapper.mapToETO(classyear);
        updateInfo.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(ClassYearEto.class, key);
            field.setAccessible(true);
            ReflectionUtils.setField(field, classyeareto, value);
        });
        classyear = ClassYearMapper.mapToEntity(classyeareto);
        this.classyearRepository.save(classyear);
        if(updateInfo.containsKey("classYear")){
            List<SubjectEntity> subjects = this.subjectRepository.findAllStudentEntityByClassYearId(id);
            subjects.forEach(subject -> subject.setName());
        }

        return ClassYearMapper.mapToETO(classyear);
    }

    @Override
    public void delete(Long id) {
        this.classyearRepository.deleteById(id);
    }
}
