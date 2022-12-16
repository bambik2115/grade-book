package com.capgemini.gradebook.service.impl;


import com.capgemini.gradebook.domain.StudentEto;
import com.capgemini.gradebook.domain.mapper.StudentMapper;
import com.capgemini.gradebook.exceptions.ClassYearNotFoundException;
import com.capgemini.gradebook.persistence.entity.ClassYear;
import com.capgemini.gradebook.persistence.entity.StudentEntity;
import com.capgemini.gradebook.persistence.repo.ClassYearRepo;
import com.capgemini.gradebook.persistence.repo.StudentRepo;
import com.capgemini.gradebook.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {

    private final ClassYearRepo classyearRepository;
    private final StudentRepo studentRepository;

    @Autowired
    public StudentServiceImpl(final ClassYearRepo classyearRepository, final StudentRepo studentRepository) {

        this.studentRepository = studentRepository;
        this.classyearRepository = classyearRepository;
    }


    @Override
    public StudentEto findStudentById(Long id) {

        Optional<StudentEntity> entity = studentRepository.findById(id);

        return StudentMapper.mapToETO(entity.get());
    }

    @Transactional
    @Override
    public StudentEto createNew(StudentEto newStudent) {
        if (newStudent.getId() != null) {
            newStudent.setId(null);
        }
        ClassYear classyear = classyearRepository.findById(newStudent.getClassYearId())
                .orElseThrow( ()-> new ClassYearNotFoundException("ClassYear with id: " + newStudent.getClassYearId() + " could not be found"));
        StudentEntity student = StudentMapper.mapToEntity(newStudent);
        student.setClassYear(classyear);
        student = this.studentRepository.save(student);

        return StudentMapper.mapToETO(student);
    }


    @Transactional
    @Override
    public StudentEto partialUpdate(Long id, Map<String, Object> updateInfo){

        StudentEntity student = studentRepository.findById(id).get();
        StudentEto studenteto = StudentMapper.mapToETO(student);
        updateInfo.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(StudentEto.class, key);
            field.setAccessible(true);
            if(key.equals("classYearId"))
                ReflectionUtils.setField(field, studenteto, Long.valueOf((Integer) value));
            else
                ReflectionUtils.setField(field, studenteto, value);
        });
        student = StudentMapper.mapToEntity(studenteto);
        ClassYear classyear = classyearRepository.findById(studenteto.getClassYearId())
                .orElseThrow( ()-> new ClassYearNotFoundException("ClassYear with id: " + studenteto.getClassYearId() + " could not be found"));
        student.setClassYear(classyear);
        studentRepository.save(student);

        return StudentMapper.mapToETO(student);

    }

    @Override
    public void delete(Long id) {
        this.studentRepository.deleteById(id);

    }
}
