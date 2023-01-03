package com.capgemini.gradebook;

import com.capgemini.gradebook.persistence.entity.*;
import com.capgemini.gradebook.persistence.entity.utils.SubjectUtils;
import com.capgemini.gradebook.persistence.repo.*;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class TestEntityCreator {

    @Inject
    private TeacherRepo trepo;

    @Inject
    private ClassYearRepo cyrepo;

    @Inject
    private StudentRepo strepo;

    @Inject
    private SubjectRepo surepo;

    @Inject
    private GradeRepo grepo;


    public TeacherEntity saveTestTeacher() {

        TeacherEntity entity = new TeacherEntity();
        entity.setFirstName("Jan");
        entity.setLastName("Kowalski");
        return this.trepo.save(entity);
    }

    public ClassYear saveTestClassYear() {

        ClassYear classyear = new ClassYear();
        classyear.setClassLevel(1);
        classyear.setClassYear("2020");
        classyear.setClassName("D");
        return this.cyrepo.save(classyear);

    }

    public SubjectEntity saveTestSubject(ClassYear classyear, TeacherEntity teacher) {

        SubjectEntity suentity = new SubjectEntity();
        suentity.setTeacherEntity(teacher);
        suentity.setClassYear(classyear);
        suentity.setSubjectType(SubjectType.BIOLOGY);
        suentity.setName(SubjectUtils.setCustomName(classyear, suentity.getSubjectType()));
        return this.surepo.save(suentity);
    }


    public StudentEntity saveTestStudent(ClassYear classyear){

        StudentEntity stentity = new StudentEntity();
        stentity.setFirstName("Kamil");
        stentity.setLastName("Komar");
        stentity.setClassYear(classyear);
        stentity.setAge(20);
        return this.strepo.save(stentity);
    }

    public Grade saveTestGrade(TeacherEntity tentity, StudentEntity stentity, SubjectEntity suentity) {

        Grade grade = new Grade();
        grade.setTeacherEntity(tentity);
        grade.setStudentEntity(stentity);
        grade.setSubjectEntity(suentity);
        grade.setValue(1);
        grade.setGradeType(GradeType.F);
        return this.grepo.save(grade);
    }

}
