package com.capgemini.gradebook.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;

import com.capgemini.gradebook.DbCleanUpService;
import com.capgemini.gradebook.TestEntityCreator;
import com.capgemini.gradebook.exceptions.TeacherNotFoundException;
import com.capgemini.gradebook.exceptions.TeacherStillInUseException;
import com.capgemini.gradebook.persistence.entity.*;
import com.capgemini.gradebook.persistence.repo.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.capgemini.gradebook.domain.TeacherEto;

@SpringBootTest
class TeacherServiceTest {


  @Inject
  private TestEntityCreator tec;
  @Inject
  private TeacherService teacherService;
  @Inject
  private TeacherRepo trepo;

  @Inject
  private SubjectRepo surepo;

  @Inject
  private GradeRepo grepo;
  @Inject
  private DbCleanUpService testDbService;


  @AfterEach
  private void cleanDbBetweenTests() {
    testDbService.resetDatabase();
  }

  /**
   * Integration test that persists some test data and checks, if after findAllTeachers the populated list will not
   * be empty
   */
  @Test
  public void findAllTeachersShouldNotBeEmptyAfterInsert(){

    // given
    tec.saveTestTeacher();
    // when
    List<TeacherEto> result = teacherService.findAllTeachers();
    // then
    Assertions.assertThat(result).isNotEmpty();
  }

  //TODO IMPLEMENT: Write other tests, that will test if: save, update, findOne, delete also work properly

  @Test
  public void findAllTeachersShouldReturnEmptyListIfNoEntries() {

    List<TeacherEto> result = teacherService.findAllTeachers();

    Assertions.assertThat(result).isEmpty();
  }

  @Test
  public void findByNameShouldBeEmptyIfNotExist(){

    //given
    tec.saveTestTeacher();

    //when
    List<TeacherEto> result = teacherService.findTeachersByLastName("Kowarski");

    //then
    Assertions.assertThat(result).isEmpty();
  }

  @Test
  public void findTeacherByIdShouldThrowExceptionIfNotExist() {

    //given
    tec.saveTestTeacher();

    Assertions.assertThatThrownBy(() -> {
                      //when
                      TeacherEto result = teacherService.findTeacherById(2L);
                      //then
                    }).isInstanceOf(TeacherNotFoundException.class)
            .hasMessageContaining("Teacher with id: " + 2L + " could not be found");
  }

  @Test
  public void findTeacherByIdShouldReturnProperEntity() {

    //given
    tec.saveTestTeacher();

    //when
    TeacherEto result = teacherService.findTeacherById(1L);

    //then
    Assertions.assertThat(result).isNotNull();
    Assertions.assertThat(result.getId()).isEqualTo(1L);
  }

  @Test
  public void createNewUserShouldAlwaysAssignNewId() {

    //Given
    tec.saveTestTeacher();
    TeacherEto teachereto = new TeacherEto();
    teachereto.setId(1L);
    teachereto.setFirstName("Kamil");
    teachereto.setLastName("Komar");

    //When
    TeacherEto result = teacherService.createNew(teachereto);

    //Then
    Assertions.assertThat(result.getId()).isNotEqualTo(1L);
  }

  @Test
  public void createNewShouldReturnInstanceOfTeacherEto() {

    TeacherEto teachereto = new TeacherEto();

    TeacherEto result = teacherService.createNew(teachereto);

    Assertions.assertThat(result).isInstanceOf(TeacherEto.class);
  }

  @Test
  public void createNewShouldReturnTeacherWithMatchingFields() {

    TeacherEto teachereto = new TeacherEto();
    teachereto.setFirstName("Kamil");
    teachereto.setLastName("Komar");

    TeacherEto result = teacherService.createNew(teachereto);

    Assertions.assertThat(result.getFirstName()).isEqualTo("Kamil");
    Assertions.assertThat(result.getLastName()).isEqualTo("Komar");
  }


  @Test
  public void partialUpdateShouldReturnInstanceOfTeacherEto() {

    tec.saveTestTeacher();
    Map<String, Object> info = new HashMap<>();
    info.put("firstName", "Kuba");

    TeacherEto teachereto = teacherService.partialUpdate(1L, info);

    Assertions.assertThat(teachereto).isInstanceOf(TeacherEto.class);

  }

  @Test
  public void partialUpdateShouldReturnTeacherWithNewValues() {
    //Given
    TeacherEto oldteacher = new TeacherEto();
    oldteacher.setFirstName("Kamil");
    oldteacher.setLastName("Korek");
    this.teacherService.createNew(oldteacher);

    String oldName = this.trepo.findById(1L).get().getFirstName();

    Map<String, Object> info = new HashMap<>();
    info.put("firstName", "Kuba");

    //When
    TeacherEto teachereto = teacherService.partialUpdate(1L, info);

    String newName = this.trepo.findById(1L).get().getFirstName();

    //Then
    Assertions.assertThat(oldName).isNotEqualTo(newName);
    Assertions.assertThat(newName).isEqualTo("Kuba");
  }

  @Test
  public void deleteTeacherWithNewIdShouldLeaveNoNullSubjectTeacherIDs () {

    TeacherEntity te = tec.saveTestTeacher();
    TeacherEntity te2 = new TeacherEntity();
    te2.setFirstName("Piotr");
    te2.setLastName("Łotr");
    trepo.save(te2);
    ClassYear cy = tec.saveTestClassYear();
    tec.saveTestSubject(cy, te);

    teacherService.delete(1L, Optional.of(2L));

    List<SubjectEntity> result = this.surepo.findAllByTeacherEntityIdIsNull();

    Assertions.assertThat(result).isEmpty();
  }

  @Test
  public void deleteTeacherWithNewIdShouldLeaveNoNullGradeTeacherIDs() {

    TeacherEntity te = tec.saveTestTeacher();
    TeacherEntity te2 = new TeacherEntity();
    te2.setFirstName("Piotr");
    te2.setLastName("Łotr");
    trepo.save(te2);
    ClassYear cy = tec.saveTestClassYear();
    SubjectEntity su = tec.saveTestSubject(cy, te);
    StudentEntity st = tec.saveTestStudent(cy);
    tec.saveTestGrade(te, st, su);

    teacherService.delete(1L, Optional.of(2L));

    List<Grade> result = this.grepo.findAllByTeacherEntityIdIsNull();

    Assertions.assertThat(result).isEmpty();

  }

  @Test
  public void deleteTeacherWithNewIdShouldAssignNewTeacherIdToSubject() {

    TeacherEntity te = tec.saveTestTeacher();
    TeacherEntity te2 = new TeacherEntity();
    te2.setFirstName("Piotr");
    te2.setLastName("Łotr");
    trepo.save(te2);
    ClassYear cy = tec.saveTestClassYear();
    SubjectEntity se = tec.saveTestSubject(cy, te);

    Long oldId = surepo.findById(1L).get().getTeacherEntity().getId();

    teacherService.delete(1L, Optional.of(2L));

    Long newId = surepo.findById(1L).get().getTeacherEntity().getId();


    Assertions.assertThat(newId).isNotEqualTo(oldId);
    Assertions.assertThat(newId).isEqualTo(2L);
  }

  @Test
  public void deleteTeacherWithNewIdShouldAssignNewTeacherIdToGrade() {

    TeacherEntity te = tec.saveTestTeacher();
    TeacherEntity te2 = new TeacherEntity();
    te2.setFirstName("Piotr");
    te2.setLastName("Łotr");
    trepo.save(te2);
    ClassYear cy = tec.saveTestClassYear();
    SubjectEntity sue = tec.saveTestSubject(cy, te);
    StudentEntity ste = tec.saveTestStudent(cy);
    tec.saveTestGrade(te, ste, sue);

    Long oldId = grepo.findById(1L).get().getTeacherEntity().getId();

    teacherService.delete(1L, Optional.of(2L));

    Long newId = grepo.findById(1L).get().getTeacherEntity().getId();

    Assertions.assertThat(newId).isNotEqualTo(oldId);
    Assertions.assertThat(newId).isEqualTo(2L);
  }

  @Test
  public void deleteTeacherWithNotExistingNewIdShouldThrowException() {

    TeacherEntity te = tec.saveTestTeacher();
    ClassYear cy = tec.saveTestClassYear();
    tec.saveTestSubject(cy, te);

    Assertions.assertThatThrownBy(() -> {
      teacherService.delete(1L, Optional.of(2L));
    }).isInstanceOf(TeacherNotFoundException.class)
            .hasMessageContaining("Teacher with id: " + 2L + " could not be found");
  }

  @Test
  public void deleteTeacherAndThenGetItShouldThrowException() {
    tec.saveTestTeacher();

    Assertions.assertThatThrownBy(() -> {
      teacherService.delete(1L, Optional.empty());
      TeacherEto result = teacherService.findTeacherById(1L);
    }).isInstanceOf(TeacherNotFoundException.class)
            .hasMessageContaining("Teacher with id: " + 1L + " could not be found");

  }

  @Test
  public void deleteTeacherInUseWithoutNewIdShouldThrowException() {
    TeacherEntity te = tec.saveTestTeacher();
    ClassYear cy = tec.saveTestClassYear();
    tec.saveTestSubject(cy, te);

    Assertions.assertThatThrownBy(() -> {

      this.teacherService.delete(1L, Optional.empty());
    }).isInstanceOf(TeacherStillInUseException.class)
            .hasMessageContaining("Teacher with ID: " + 1L + " is in use, please pass ID to update");
  }

  @Test
  public void deleteTeacherShouldLeaveEmptyDatabaseTable() {
    tec.saveTestTeacher();


    teacherService.delete(1L, Optional.empty());
    List<TeacherEntity> teachers = this.trepo.findAll();

    Assertions.assertThat(teachers).isEmpty();
  }

  }
