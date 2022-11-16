package com.capgemini.gradebook.service;

import java.util.List;

import javax.inject.Inject;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.capgemini.gradebook.domain.TeacherEto;
import com.capgemini.gradebook.persistence.entity.TeacherEntity;
import com.capgemini.gradebook.persistence.repo.TeacherRepo;

@SpringBootTest
class TeacherServiceTest {

  @Inject
  private TeacherService testedClass;

  @Inject
  private TeacherRepo repo;

  @BeforeEach
  @AfterEach
  public void cleanUpDbBetweenTests(){
    //TODO IMPLEMENT: implement a body of this method; it should delete all records from your database so before and
    // after each test the database state will be clean
  }

  /**
   * Integration test that persists some test data and checks, if after findAllTeachers the populated list will not
   * be empty
   */
  @Test
  public void findAllTeachersShouldNotBeEmptyAfterInsert(){

    // given
    saveTestData();
    // when
    List<TeacherEto> result = testedClass.findAllTeachers();
    // then
    Assertions.assertThat(result).isNotEmpty();
  }

  //TODO IMPLEMENT: Write other tests, that will test if: save, update, findOne, delete also work properly

  private void saveTestData() {

    TeacherEntity entity = new TeacherEntity();
    entity.setFirstName("Jan");
    entity.setLastName("Kowalski");
    repo.save(entity);
  }

}