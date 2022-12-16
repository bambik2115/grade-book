package com.capgemini.gradebook.persistence.entity;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "TEACHER")
public class TeacherEntity extends AbstractEntity {


  private String firstName;
  private String lastName;

  //TODO IMPLEMENT: after creating subjectEntity and other subject classes uncomment the lines below, making necessary
  // adjustments; then generate getters and setters and fix the mappers
  @OneToMany(mappedBy = "teacherEntity", fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST })
  private List<SubjectEntity> subjectEntityList;

  @OneToMany(mappedBy = "teacherEntity", fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST })
  private List<Grade> gradeList;

  //TODO IMPLEMENT: create @OneToMany with mappedBy to subjects after you create a base model

  @PreRemove
  public void removeIdFromGradeAndSubject() {
    subjectEntityList.forEach(subject -> subject.setTeacherEntity(null));
    gradeList.forEach(grade -> grade.setTeacherEntity(null));

  }

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


  public List<SubjectEntity> getSubjectEntityList() {
    return this.subjectEntityList;
  }

  public void setSubjectEntityList(List<SubjectEntity> subjectEntityList) {
    this.subjectEntityList = subjectEntityList;
  }

}
