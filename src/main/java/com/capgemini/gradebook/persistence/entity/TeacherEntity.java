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
  @OneToMany(mappedBy = "teacherEntity", cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST })
  private List<SubjectEntity> subjectList;

  @OneToMany(mappedBy = "teacherEntity", cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST })
  private List<GradeEntity> gradeList;

  //TODO IMPLEMENT: create @OneToMany with mappedBy to subjects after you create a base model

  @PreRemove
  public void removeIdFromGradeAndSubject() {
    subjectList.forEach(subject -> subject.setTeacherEntity(null));
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


  public List<SubjectEntity> getSubjectList() {
    return this.subjectList;
  }

  public void setSubjectList(List<SubjectEntity> subjectEntityList) {
    this.subjectList = subjectEntityList;
  }

  public List<GradeEntity> getGradeList() {
    return this.gradeList;
  }

  public void setGradeList(List<GradeEntity> gradeEntityList) {
    this.gradeList = gradeEntityList;
  }
}
