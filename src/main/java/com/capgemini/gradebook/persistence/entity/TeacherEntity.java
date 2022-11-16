package com.capgemini.gradebook.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "TEACHER")
public class TeacherEntity extends AbstractEntity {

  private String firstName;
  private String lastName;


  //TODO IMPLEMENT: after creating subjectEntity and other subject classes uncomment the lines below, making necessary
  // adjustments; then generate getters and setters and fix the mappers
//  @OneToMany(mappedBy = "teacherEntity", fetch = FetchType.LAZY)
//  private List<SubjectEntity> subjectEntityList;


  //TODO IMPLEMENT: create @OneToMany with mappedBy to subjects after you create a base model

  public String getFirstName() {

    return this.firstName;
  }


  public void setFirstName(String firstName) {

    this.firstName = firstName;
  }


  public String getLastName() {

    return this.lastName;
  }


  public void setLastName(String lastName) {

    this.lastName = lastName;
  }
}
