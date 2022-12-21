package com.capgemini.gradebook.persistence.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "SUBJECT")
public class SubjectEntity extends AbstractEntity {

  //TODO IMPLEMENT: this should be saved as a concatenation of subjectType and classyear value
  private String name;

  //TODO IMPLEMENT: no implementing here, but google why its better to persist enum values in the database in form of
  // strings or converted value instead of ordinal
  @Enumerated(EnumType.STRING)
  private SubjectType subjectType;

  @ManyToOne(fetch = FetchType.LAZY)
  private TeacherEntity teacherEntity;  //Mozna zmienic nauczyciela

  @ManyToOne(fetch = FetchType.LAZY)
  private ClassYear classYear;

  @OneToMany(mappedBy = "subjectEntity", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  private List<Grade> gradeList;

  public String getName() {
    return this.name;
  }

  public void setName() {

    this.name = this.subjectType + "_" + this.classYear.getClassLevel() + this.classYear.getClassName();

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

  public ClassYear getClassYear() {
    return this.classYear;
  }

  public void setClassYear(ClassYear classYear) {
    this.classYear = classYear;
  }

  public List<Grade> getGradeList() {
    return this.gradeList;
  }

  public void setGradeList(List<Grade> gradeList) {
    this.gradeList = gradeList;
  }
}
