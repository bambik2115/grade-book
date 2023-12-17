package com.krzysztofapp.gradebook.persistence.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "SUBJECT")
public class SubjectEntity extends AbstractEntity {

  //TODO IMPLEMENT: this should be saved as a concatenation of subjectType and classYear value
  private String name;

  //TODO IMPLEMENT: no implementing here, but google why its better to persist enum values in the database in form of
  // strings or converted value instead of ordinal
  @Enumerated(EnumType.STRING)
  private SubjectType subjectType;

  @ManyToOne(fetch = FetchType.LAZY)
  private TeacherEntity teacherEntity;

  @ManyToOne(fetch = FetchType.LAZY)
  private ClassYearEntity classYearEntity;

  @OneToMany(mappedBy = "subjectEntity", cascade = CascadeType.REMOVE)
  private List<GradeEntity> gradeList;

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }
  public SubjectType getSubjectType() {
    return this.subjectType;
  }

  public void setSubjectType(SubjectType subjectType) {
    this.subjectType = subjectType;
  }

  public TeacherEntity getTeacherEntity() {
    return this.teacherEntity;
  }

  public void setTeacherEntity(TeacherEntity teacherEntity) {
    this.teacherEntity = teacherEntity;
  }

  public ClassYearEntity getClassYear() {
    return this.classYearEntity;
  }

  public void setClassYear(ClassYearEntity classYearEntity) {
    this.classYearEntity = classYearEntity;
  }

  public List<GradeEntity> getGradeList() {
    return this.gradeList;
  }

  public void setGradeList(List<GradeEntity> gradeList) {
    this.gradeList = gradeList;
  }

}


