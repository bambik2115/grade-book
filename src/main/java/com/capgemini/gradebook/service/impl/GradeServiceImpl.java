package com.capgemini.gradebook.service.impl;


import com.capgemini.gradebook.domain.GradeEto;
import com.capgemini.gradebook.domain.mapper.GradeMapper;
import com.capgemini.gradebook.exceptions.GradeNotFoundException;
import com.capgemini.gradebook.exceptions.StudentNotFoundException;
import com.capgemini.gradebook.exceptions.SubjectNotFoundException;
import com.capgemini.gradebook.persistence.entity.Grade;
import com.capgemini.gradebook.persistence.entity.StudentEntity;
import com.capgemini.gradebook.persistence.entity.SubjectEntity;
import com.capgemini.gradebook.persistence.repo.GradeRepo;
import com.capgemini.gradebook.persistence.repo.StudentRepo;
import com.capgemini.gradebook.persistence.repo.SubjectRepo;
import com.capgemini.gradebook.service.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Map;

@Service
public class GradeServiceImpl implements GradeService {


    private final GradeRepo gradeRepository;
    private final StudentRepo studentRepository;
    private final SubjectRepo subjectRepository;

    @Autowired
    public GradeServiceImpl(final GradeRepo gradeRepository, final StudentRepo studentRepository, final SubjectRepo subjectRepository) {

        this.gradeRepository = gradeRepository;
        this.studentRepository = studentRepository;
        this.subjectRepository = subjectRepository;
    }

    @Override
    public GradeEto findGradeById(Long id) {

        Grade grade = this.gradeRepository.findById(id)
                .orElseThrow( ()-> new GradeNotFoundException("Student with id: " + id + " could not be found"));

        return GradeMapper.mapToETO(grade);
    }

    @Transactional
    @Override
    public GradeEto createNew(GradeEto newGrade) {
        if (newGrade.getId() != null) {
            newGrade.setId(null);
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

        Grade grade = this.gradeRepository.findById(id)
                .orElseThrow( ()-> new GradeNotFoundException("Student with id: " + id + " could not be found"));
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
}
