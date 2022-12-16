package com.capgemini.gradebook.controller;

import com.capgemini.gradebook.domain.SubjectEto;
import com.capgemini.gradebook.domain.TeacherEto;
import com.capgemini.gradebook.service.SubjectService;
import com.capgemini.gradebook.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * This is an example how to write some REST endpoints
 */
@RestController
@RequestMapping("/rest")
public class TeacherRestController {

  private final TeacherService teacherService;
  private final SubjectService subjectService;

  @Autowired
  public TeacherRestController(final TeacherService teacherService, final SubjectService subjectService) {

    this.teacherService = teacherService;
    this.subjectService = subjectService;
  }

  @GetMapping("/teachers")
  public List<TeacherEto> findAllTeachers() {

    return this.teacherService.findAllTeachers();
  }

  @GetMapping("/teacherSubjectList/{id}")
  public List<String> getAllTeacherSubjectNames(@PathVariable("id") final Long id) {
    return this.teacherService.getSubjects(id);
  }

  @GetMapping("/teachers/{id}")
  public ResponseEntity<TeacherEto> findTeacherById(@PathVariable("id") final Long id) {

    final TeacherEto teacher = this.teacherService.findTeacherById(id);
    return ResponseEntity.ok().body(teacher);
  }

  @GetMapping("/teachers/name/{name}")
  public ResponseEntity<List<TeacherEto>> findTeacherByLastname(@PathVariable("name") final String name) {

    final List<TeacherEto> teachers = this.teacherService.findTeacherByLastName(name);
    return ResponseEntity.ok().body(teachers);
  }


  @PostMapping("/teachers")
  public TeacherEto addTeacher(@RequestBody TeacherEto newTeacher) {
    //TODO IMPLEMENT: Post should always create a new entry in database. Add a new SERVICE method that ensures by
    // either throwing an exception if ID is present, or removing the given ID from ETO before save. Currently, this
    // method  may also be used to update existing entities (DONE)
    return teacherService.createNew(newTeacher);
  }

  @PatchMapping("/teachers/{id}")
  TeacherEto updatePartially(@PathVariable("id") final Long id, @RequestBody Map<String, Object> updateInfo) {
    return teacherService.partialUpdate(id, updateInfo);
  }


  @DeleteMapping("/teachers/{id}")
  void deleteTeacher(@PathVariable Long id, @RequestBody SubjectEto newTeacherId) {
    teacherService.delete(id, newTeacherId.getTeacherEntityId());
  }

}
