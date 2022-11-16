package com.capgemini.gradebook.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.capgemini.gradebook.domain.TeacherEto;
import com.capgemini.gradebook.domain.mapper.TeacherMapper;
import com.capgemini.gradebook.persistence.entity.TeacherEntity;
import com.capgemini.gradebook.persistence.repo.TeacherRepo;
import com.capgemini.gradebook.service.TeacherService;

@Service
@Transactional
public class TeacherServiceImpl implements TeacherService {

  private final TeacherRepo teacherRepository;

  @Autowired
  public TeacherServiceImpl(final TeacherRepo teacherRepository) {

    this.teacherRepository = teacherRepository;
  }

  @Override
  public List<TeacherEto> findAllTeachers() {

    return TeacherMapper.mapToETOList(this.teacherRepository.findAll());
  }

  @Override
  public List<TeacherEto> findTeacherByLastName(final String lastname) {

    final List<TeacherEntity> teachers = this.teacherRepository.findTeacherByLastName(lastname);
    return teachers.stream().map(teacher -> TeacherMapper.mapToETO(teacher)).collect(Collectors.toList());
  }

  @Override
  public TeacherEto findTeacherById(Long id) {

    final Optional<TeacherEntity> result = this.teacherRepository.findById(id);
    //TODO IMPLEMENT: Throw new custom exception
    return result.map(r -> TeacherMapper.mapToETO(r)).orElseThrow( ()-> new RuntimeException("Teacher not found" + " exception"));
  }

  @Override
  public TeacherEto save(TeacherEto newTeacher) {

    TeacherEntity teacherEntity = TeacherMapper.mapToEntity(newTeacher);
    teacherEntity = this.teacherRepository.save(teacherEntity);
    return TeacherMapper.mapToETO(teacherEntity);
  }

  @Override
  public void delete(Long id) {

    this.teacherRepository.deleteById(id);

  }
}
