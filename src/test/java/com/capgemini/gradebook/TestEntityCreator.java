package com.capgemini.gradebook;

import com.capgemini.gradebook.persistence.entity.*;
import com.capgemini.gradebook.persistence.entity.utils.SubjectUtils;
import com.capgemini.gradebook.persistence.repo.*;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class TestEntityCreator {

    @Inject
    private TeacherRepo tRepo;

    @Inject
    private ClassYearRepo cyRepo;

    @Inject
    private StudentRepo stRepo;

    @Inject
    private SubjectRepo suRepo;

    @Inject
    private GradeRepo gRepo;


    public TeacherEntity saveTestTeacher() {

        TeacherEntity entity = new TeacherEntity();
        entity.setFirstName("Jan");
        entity.setLastName("Kowalski");
        return this.tRepo.save(entity);
    }

    public ClassYearEntity saveTestClassYear() {

        ClassYearEntity classYear = new ClassYearEntity();
        classYear.setClassLevel(1);
        classYear.setClassYear("2020");
        classYear.setClassName("D");
        return this.cyRepo.save(classYear);

    }

    public SubjectEntity saveTestSubject(ClassYearEntity classYear, TeacherEntity teacher) {

        SubjectEntity suentity = new SubjectEntity();
        suentity.setTeacherEntity(teacher);
        suentity.setClassYear(classYear);
        suentity.setSubjectType(SubjectType.BIOLOGY);
        suentity.setName(SubjectUtils.setCustomName(classYear, suentity.getSubjectType()));
        return this.suRepo.save(suentity);
    }


    public StudentEntity saveTestStudent(ClassYearEntity classYear){

        StudentEntity stentity = new StudentEntity();
        stentity.setFirstName("Kamil");
        stentity.setLastName("Komar");
        stentity.setClassYearEntity(classYear);
        stentity.setAge(20);
        return this.stRepo.save(stentity);
    }

    public GradeEntity saveTestGrade(TeacherEntity tentity, StudentEntity stentity, SubjectEntity suentity) {

        GradeEntity grade = new GradeEntity();
        grade.setTeacherEntity(tentity);
        grade.setStudentEntity(stentity);
        grade.setSubjectEntity(suentity);
        grade.setValue(1);
        grade.setGradeType(GradeType.F);
        return this.gRepo.save(grade);
    }

}
