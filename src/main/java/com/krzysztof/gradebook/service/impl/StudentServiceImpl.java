package com.capgemini.gradebook.service.impl;


import com.capgemini.gradebook.domain.GradeContext;
import com.capgemini.gradebook.domain.StudentEto;
import com.capgemini.gradebook.domain.mapper.StudentMapper;
import com.capgemini.gradebook.exceptions.ClassYearNotFoundException;
import com.capgemini.gradebook.exceptions.StudentNotFoundException;
import com.capgemini.gradebook.persistence.entity.ClassYearEntity;
import com.capgemini.gradebook.persistence.entity.StudentEntity;
import com.capgemini.gradebook.persistence.repo.ClassYearRepo;
import com.capgemini.gradebook.persistence.repo.StudentRepo;
import com.capgemini.gradebook.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class StudentServiceImpl implements StudentService {

    private final ClassYearRepo classYearRepository;
    private final StudentRepo studentRepository;
    
    private final Validator validator;


    @Autowired
    public StudentServiceImpl(final ClassYearRepo classYearRepository, final StudentRepo studentRepository, final Validator validator) {

        this.studentRepository = studentRepository;
        this.classYearRepository = classYearRepository;
        this.validator = validator;
    }


    @Override
    public StudentEto findStudentById(Long id) {

        StudentEntity entity = this.studentRepository.findById(id)
                .orElseThrow( ()-> new StudentNotFoundException("Student with id: " + id + " could not be found"));

        return StudentMapper.mapToETO(entity);
    }

    @Override
    public List<StudentEto> findAllStudentsWithGradeFAtCertainDay(LocalDate day) {

        List<StudentEntity> result = this.studentRepository
                .findAllByGradeFAtCertainDay(day);

        return StudentMapper.mapToETOList(result);
    }

    @Override
    public Integer getNumberOfStudents(GradeContext context) {

        List<StudentEntity> number = this.studentRepository
                .findAllByCertainGradeAtCertainDay(context.getGradeType(), context.getDateOfGrade());

        return number.size();
    }

    @Transactional
    @Override
    public StudentEto createNew(StudentEto newStudent) {
        if (newStudent.getId() != null) {
            newStudent.setId(null);
        }

        Set<ConstraintViolation<StudentEto>> violations = this.validator.validate(newStudent);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<StudentEto> constraintViolation : violations) {
                sb.append(constraintViolation.getMessage());
                sb.append("\n");
            }
            throw new ConstraintViolationException("Error occurred: " + sb.toString(), violations);
        }

        ClassYearEntity classYear = this.classYearRepository.findById(newStudent.getClassYearEntityId())
                .orElseThrow( ()-> new ClassYearNotFoundException("ClassYear with id: " + newStudent.getClassYearEntityId() + " could not be found"));
        StudentEntity student = StudentMapper.mapToEntity(newStudent);
        student.setClassYearEntity(classYear);
        student = this.studentRepository.save(student);

        return StudentMapper.mapToETO(student);
    }


    @Transactional
    @Override
    public StudentEto partialUpdate(Long id, Map<String, Object> updateInfo){

        StudentEntity student = this.studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student with id: " + id + " could not be found"));
        StudentEto studentEto = StudentMapper.mapToETO(student);
        updateInfo.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(StudentEto.class, key);
            field.setAccessible(true);
            if(key.equals("classYearEntityId"))
                ReflectionUtils.setField(field, studentEto, Long.valueOf((Integer) value));
            else
                ReflectionUtils.setField(field, studentEto, value);
        });
        student = StudentMapper.mapToEntity(studentEto);
        ClassYearEntity classYear = this.classYearRepository.findById(studentEto.getClassYearEntityId())
                .orElseThrow( ()-> new ClassYearNotFoundException("ClassYear with id: " + studentEto.getClassYearEntityId() + " could not be found"));
        student.setClassYearEntity(classYear);
        this.studentRepository.save(student);

        return StudentMapper.mapToETO(student);

    }

    @Override
    public void delete(Long id) {
        this.studentRepository.deleteById(id);

    }
}
