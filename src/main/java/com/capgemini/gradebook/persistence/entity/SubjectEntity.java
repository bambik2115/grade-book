package com.capgemini.gradebook.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "SUBJECT")
public class SubjectEntity extends AbstractEntity {

  //TODO IMPLEMENT: this should saved as a concatenation of subjectType and classyear value
  private String name;

  //TODO IMPLEMENT: no implementing here, but google why its better to persist enum values in the database in form of
  // strings or converted value instead of ordinal
  @Enumerated(EnumType.STRING)
  private SubjectType subjectType;

  @ManyToOne(fetch = FetchType.LAZY)
  private TeacherEntity teacherEntity;




}
