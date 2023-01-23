package com.capgemini.gradebook.service.impl;

import com.capgemini.gradebook.domain.SubjectEto;
import com.capgemini.gradebook.domain.TeacherEto;
import com.capgemini.gradebook.domain.mapper.TeacherMapper;
import com.capgemini.gradebook.exceptions.ClassYearNotFoundException;
import com.capgemini.gradebook.exceptions.TeacherNotFoundException;
import com.capgemini.gradebook.exceptions.TeacherStillInUseException;
import com.capgemini.gradebook.persistence.entity.GradeEntity;
import com.capgemini.gradebook.persistence.entity.SubjectEntity;
import com.capgemini.gradebook.persistence.entity.TeacherEntity;
import com.capgemini.gradebook.persistence.repo.GradeRepo;
import com.capgemini.gradebook.persistence.repo.SubjectRepo;
import com.capgemini.gradebook.persistence.repo.TeacherRepo;
import com.capgemini.gradebook.service.TeacherService;
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
import java.util.stream.Collectors;

@Service
public class TeacherServiceImpl implements TeacherService {

  private final TeacherRepo teacherRepository;
  private final SubjectRepo subjectRepository;
  private final GradeRepo gradeRepository;

  private final Validator validator;

  @Autowired
  public TeacherServiceImpl(final TeacherRepo teacherRepository, final SubjectRepo subjectRepository, final GradeRepo gradeRepository, final Validator validator) {

    this.teacherRepository = teacherRepository;
    this.subjectRepository = subjectRepository;
    this.gradeRepository = gradeRepository;
    this.validator = validator;
  }

  @Override
  public List<TeacherEto> findAllTeachers() {

    return TeacherMapper.mapToETOList(this.teacherRepository.findAll());
  }

  @Override
  public List<TeacherEto> findTeachersByLastName(final String lastname) {

    final List<TeacherEntity> teachers = this.teacherRepository.findTeachersByLastName(lastname);
    return teachers.stream().map(teacher -> TeacherMapper.mapToETO(teacher)).collect(Collectors.toList());
  }

  @Override
  public List<String> getSubjects(Long id) {
    final TeacherEntity result = this.teacherRepository.findById(id)
            .orElseThrow( ()-> new TeacherNotFoundException("Teacher with id: " + id + " could not be found"));
    return result.getSubjectList().stream().map(SubjectEntity::getName).collect(Collectors.toList());
  }

  @Override
  public TeacherEto findTeacherById(Long id) {

    final Optional<TeacherEntity> result = this.teacherRepository.findById(id);
    //TODO IMPLEMENT: Throw new custom exception (DONE)
    return result.map(r -> TeacherMapper.mapToETO(r)).orElseThrow( ()-> new TeacherNotFoundException("Teacher with id: " + id + " could not be found"));
  }

  @Override
  public TeacherEto createNew(TeacherEto newTeacher) {
    if (newTeacher.getId() != null) {
      newTeacher.setId(null);
    }
    Set<ConstraintViolation<TeacherEto>> violations = this.validator.validate(newTeacher);
    if (!violations.isEmpty()) {
      StringBuilder sb = new StringBuilder();
      for (ConstraintViolation<TeacherEto> constraintViolation : violations) {
        sb.append(constraintViolation.getMessage());
        sb.append("\n");
      }
      throw new ConstraintViolationException("Error occurred: " + sb.toString(), violations);
    }

      TeacherEntity teacherEntity = TeacherMapper.mapToEntity(newTeacher);
      teacherEntity = this.teacherRepository.save(teacherEntity);
      return TeacherMapper.mapToETO(teacherEntity);
  }

  @Override
  public TeacherEto partialUpdate(Long id, Map<String, Object> updateInfo) {

    TeacherEntity teacher = this.teacherRepository.findById(id)
            .orElseThrow(() -> new TeacherNotFoundException("Teacher with id: " + id + " could not be found"));
    TeacherEto teacherEto = TeacherMapper.mapToETO(teacher);
    updateInfo.forEach((key, value) -> {
      Field field = ReflectionUtils.findField(TeacherEto.class, key);
      field.setAccessible(true);
      ReflectionUtils.setField(field, teacherEto, value);
    });
    teacher = TeacherMapper.mapToEntity(teacherEto);
    this.teacherRepository.save(teacher);

    return TeacherMapper.mapToETO(teacher);
  }


  @Transactional
  @Override
  public void delete(Long id, Optional<Long> newTeacherId) {

    List<SubjectEntity> teacherInUse = this.subjectRepository.findAllByTeacherEntityId(id);
    if(!teacherInUse.isEmpty() && newTeacherId.isPresent()) {
      this.teacherRepository.deleteById(id);
      TeacherEntity entity = this.teacherRepository.findById(newTeacherId.get())
              .orElseThrow(() -> new TeacherNotFoundException("Teacher with id: " + newTeacherId.get() + " could not be found"));
      List<SubjectEntity> subjects = this.subjectRepository.findAllByTeacherEntityIdIsNull();
      subjects.forEach(subject -> subject.setTeacherEntity(entity));
      List<GradeEntity> grades = this.gradeRepository.findAllByTeacherEntityIdIsNull();
      grades.forEach(grade -> grade.setTeacherEntity(entity));
    }
    else if(!teacherInUse.isEmpty()) {
      throw new TeacherStillInUseException("Teacher with ID: " + id + " is in use, please pass ID to update");
    } else {
      this.teacherRepository.deleteById(id);
    }
  }
}
