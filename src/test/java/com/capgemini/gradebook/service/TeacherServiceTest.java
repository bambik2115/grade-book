package com.capgemini.gradebook.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.ConstraintViolationException;

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
class TeacherServiceTest extends TestEntityCreator {


  @Inject
  private TeacherService teacherService;

  @Inject
  private TeacherRepo tRepo;

  @Inject
  private SubjectRepo suRepo;

  @Inject
  private GradeRepo gRepo;

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
    saveTestTeacher();
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
    saveTestTeacher();

    //when
    List<TeacherEto> result = teacherService.findTeachersByLastName("Kowarski");

    //then
    Assertions.assertThat(result).isEmpty();
  }

  @Test
  public void findTeacherByIdShouldThrowExceptionIfNotExist() {

    //given
    TeacherEntity te = saveTestTeacher();

    Assertions.assertThatThrownBy(() -> {
                      //when
                      TeacherEto result = teacherService.findTeacherById(te.getId()+1);
                      //then
                    }).isInstanceOf(TeacherNotFoundException.class)
            .hasMessageContaining("Teacher with id: " + (te.getId()+1) + " could not be found");
  }

  @Test
  public void findTeacherByIdShouldReturnProperEntity() {

    //given
    TeacherEntity te = saveTestTeacher();

    //when
    TeacherEto result = teacherService.findTeacherById(te.getId());

    //then
    Assertions.assertThat(result).isNotNull();
    Assertions.assertThat(result.getId()).isEqualTo(te.getId());
  }


  @Test
  public void createNewShouldReturnTeacherWithMatchingFields() {

    TeacherEto teacherEto = new TeacherEto();
    teacherEto.setFirstName("Kamil");
    teacherEto.setLastName("Komar");
    //when
    TeacherEto result = teacherService.createNew(teacherEto);
    //then
    Assertions.assertThat(result.getFirstName()).isEqualTo("Kamil");
    Assertions.assertThat(result.getLastName()).isEqualTo("Komar");
  }

  @Test
  public void createNewShouldThrowExceptionWhenNoFirstNameProvided() {

    TeacherEto teacherEto = new TeacherEto();
    teacherEto.setLastName("Komar");


    Assertions.assertThatThrownBy(() -> {
      //when
      teacherService.createNew(teacherEto);
      //then
    }).isInstanceOf(ConstraintViolationException.class);
  }

  @Test
  public void createNewShouldThrowExceptionWhenNoLastNameProvided() {

    TeacherEto teacherEto = new TeacherEto();
    teacherEto.setFirstName("Kamil");


    Assertions.assertThatThrownBy(() -> {
      //when
      teacherService.createNew(teacherEto);
      //then
    }).isInstanceOf(ConstraintViolationException.class);
  }


  @Test
  public void partialUpdateShouldReturnTeacherWithNewValues() {
    //Given
    TeacherEntity te = saveTestTeacher();

    String oldName = tRepo.findById(te.getId()).get().getFirstName();

    Map<String, Object> info = new HashMap<>();
    info.put("firstName", "Marcin");

    //When
    TeacherEto teacherEto = teacherService.partialUpdate(1L, info);

    String newName = tRepo.findById(te.getId()).get().getFirstName();

    //Then
    Assertions.assertThat(oldName).isNotEqualTo(newName);
    Assertions.assertThat(newName).isEqualTo("Marcin");
  }

  @Test
  public void deleteTeacherWithNewIdShouldLeaveNoNullSubjectTeacherIDs () {
    //Given
    TeacherEntity te = saveTestTeacher();
    TeacherEntity te2 = saveTestTeacher();
    ClassYearEntity cy = saveTestClassYear();
    saveTestSubject(cy, te);
    //when
    teacherService.delete(te.getId(), Optional.of(te2.getId()));

    List<SubjectEntity> result = suRepo.findAllByTeacherEntityIdIsNull();
    //then
    Assertions.assertThat(result).isEmpty();
  }

  @Test
  public void deleteTeacherWithNewIdShouldLeaveNoNullGradeTeacherIDs() {
    //Given
    TeacherEntity te = saveTestTeacher();
    TeacherEntity te2 = saveTestTeacher();
    ClassYearEntity cy = saveTestClassYear();
    SubjectEntity su = saveTestSubject(cy, te);
    StudentEntity st = saveTestStudent(cy);
    saveTestGrade(te, st, su);

    //when
    teacherService.delete(te.getId(), Optional.of(te2.getId()));

    List<GradeEntity> result = gRepo.findAllByTeacherEntityIdIsNull();
    //then
    Assertions.assertThat(result).isEmpty();

  }

  @Test
  public void deleteTeacherWithNewIdShouldAssignNewTeacherIdToSubject() {
    //Given
    TeacherEntity te = saveTestTeacher();
    TeacherEntity te2 = saveTestTeacher();
    ClassYearEntity cy = saveTestClassYear();
    SubjectEntity sue = saveTestSubject(cy, te);

    Long oldId = suRepo.findById(sue.getId()).get().getTeacherEntity().getId();

    //when
    teacherService.delete(te.getId(), Optional.of(te2.getId()));

    Long newId = suRepo.findById(sue.getId()).get().getTeacherEntity().getId();

    //then
    Assertions.assertThat(newId).isNotEqualTo(oldId);
    Assertions.assertThat(newId).isEqualTo(te2.getId());
  }

  @Test
  public void deleteTeacherWithNewIdShouldAssignNewTeacherIdToGrade() {
    //Given
    TeacherEntity te = saveTestTeacher();
    TeacherEntity te2 = saveTestTeacher();
    ClassYearEntity cy = saveTestClassYear();
    SubjectEntity sue = saveTestSubject(cy, te);
    StudentEntity ste = saveTestStudent(cy);
    GradeEntity ge = saveTestGrade(te, ste, sue);

    Long oldId = gRepo.findById(ge.getId()).get().getTeacherEntity().getId();

    //when
    teacherService.delete(te.getId(), Optional.of(te2.getId()));

    Long newId = gRepo.findById(ge.getId()).get().getTeacherEntity().getId();
    //then
    Assertions.assertThat(newId).isNotEqualTo(oldId);
    Assertions.assertThat(newId).isEqualTo(te2.getId());
  }

  @Test
  public void deleteTeacherWithNotExistingNewIdShouldThrowException() {
    //Given
    TeacherEntity te = saveTestTeacher();
    ClassYearEntity cy = saveTestClassYear();
    saveTestSubject(cy, te);

    Assertions.assertThatThrownBy(() -> {

              //when
      teacherService.delete(te.getId(), Optional.of(te.getId()+1));
              //then
    }).isInstanceOf(TeacherNotFoundException.class)
            .hasMessageContaining("Teacher with id: " + (te.getId()+1) + " could not be found");
  }

  @Test
  public void findingTeacherAfterDeleteShouldThrowException() {
    //Given
    TeacherEntity te = saveTestTeacher();

    Assertions.assertThatThrownBy(() -> {

              //when
      teacherService.delete(te.getId(), Optional.empty());

      TeacherEto result = teacherService.findTeacherById(te.getId());
              //then
    }).isInstanceOf(TeacherNotFoundException.class)
            .hasMessageContaining("Teacher with id: " + te.getId() + " could not be found");

  }

  @Test
  public void deleteTeacherInUseWithoutNewIdShouldThrowException() {
    //Given
    TeacherEntity te = saveTestTeacher();
    ClassYearEntity cy = saveTestClassYear();
    saveTestSubject(cy, te);

    Assertions.assertThatThrownBy(() -> {
              //when
      teacherService.delete(te.getId(), Optional.empty());
              //then
    }).isInstanceOf(TeacherStillInUseException.class)
            .hasMessageContaining("Teacher with ID: " + te.getId() + " is in use, please pass ID to update");
  }

  @Test
  public void deleteTeacherShouldLeaveEmptyDatabaseTable() {
    //Given
    TeacherEntity te = saveTestTeacher();

    //when
    teacherService.delete(te.getId(), Optional.empty());

    List<TeacherEntity> teachers = tRepo.findAll();
    //then
    Assertions.assertThat(teachers).isEmpty();
  }

  }
