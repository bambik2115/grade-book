package com.capgemini.gradebook.service;

import com.capgemini.gradebook.DbCleanUpService;
import com.capgemini.gradebook.TestEntityCreator;
import com.capgemini.gradebook.domain.*;
import com.capgemini.gradebook.exceptions.ClassYearNotFoundException;
import com.capgemini.gradebook.exceptions.StudentNotFoundException;
import com.capgemini.gradebook.persistence.entity.*;
import com.capgemini.gradebook.persistence.repo.GradeRepo;
import com.capgemini.gradebook.persistence.repo.StudentRepo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.inject.Inject;
import javax.validation.ConstraintViolationException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootTest
class StudentServiceTest extends TestEntityCreator {

    @Inject
    private StudentService studentService;

    @Inject
    private DbCleanUpService cleanUpService;

    @Inject
    private StudentRepo stRepo;

    @Inject
    private GradeRepo gRepo;

    @AfterEach
    private void cleanDbBetweenTests() {
        cleanUpService.resetDatabase();
    }

    @Test
    public void findStudentByIdShouldReturnProperEntity() {

        //given
        ClassYearEntity cy = saveTestClassYear();
        StudentEntity ste = saveTestStudent(cy);

        //when
        StudentEto result = studentService.findStudentById(ste.getId());

        //then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId()).isEqualTo(ste.getId());
    }

    @Test
    public void findStudentByIdShouldThrowExceptionIfNotExist() {

        //given
        ClassYearEntity cy = saveTestClassYear();
        StudentEntity ste = saveTestStudent(cy);

        Assertions.assertThatThrownBy(() -> {
                    //when
                    studentService.findStudentById(ste.getId()+1);
                    //then
                }).isInstanceOf(StudentNotFoundException.class)
                .hasMessageContaining("Student with id: " + (ste.getId()+1) + " could not be found");
    }

    @Test
    public void findAllWithGradeFAtCertainDayShouldReturnProperStudentsIfFound() {
        //given
        TeacherEntity te = saveTestTeacher();
        ClassYearEntity cy = saveTestClassYear();
        StudentEntity ste = saveTestStudent(cy);
        StudentEntity ste1 = saveTestStudent(cy);
        SubjectEntity sue = saveTestSubject(cy,te);
        createGrade(te, ste, sue, GradeType.F, 1, LocalDate.parse("2022-12-12"), BigDecimal.valueOf(3.00));
        createGrade(te, ste1, sue, GradeType.F, 1, LocalDate.parse("2022-12-12"), BigDecimal.valueOf(2.00));
        createGrade(te, ste1, sue, GradeType.C, 1, LocalDate.parse("2022-12-12"), BigDecimal.valueOf(2.00));
        //when
        List<StudentEto> result = studentService.findAllStudentsWithGradeFAtCertainDay(LocalDate.parse("2022-12-12"));

        List<Long> listOfIds = result.stream().map(StudentEto::getId).collect(Collectors.toList());

        //then
        Assertions.assertThat(result.size()).isEqualTo(2);
        Assertions.assertThat(listOfIds).contains(1L,2L);
    }

    @Test
    public void findAllWithGradeFAtCertainDayShouldReturnEmptyListIfNotFound() {
        //given
        TeacherEntity te = saveTestTeacher();
        ClassYearEntity cy = saveTestClassYear();
        StudentEntity ste = saveTestStudent(cy);
        SubjectEntity sue = saveTestSubject(cy,te);
        createGrade(te, ste, sue, GradeType.F, 1, LocalDate.parse("2022-12-12"), BigDecimal.valueOf(3.00));
        //when
        List<StudentEto> result = studentService.findAllStudentsWithGradeFAtCertainDay(LocalDate.parse("2022-11-12"));
        //then
        Assertions.assertThat(result).isEmpty();
    }

    @Test
    public void getNumberOfStudentsShouldReturnCorrectNumber() {
        //given
        TeacherEntity te = saveTestTeacher();
        ClassYearEntity cy = saveTestClassYear();
        StudentEntity ste = saveTestStudent(cy);
        StudentEntity ste1 = saveTestStudent(cy);
        SubjectEntity sue = saveTestSubject(cy,te);
        createGrade(te, ste, sue, GradeType.F, 1, LocalDate.parse("2022-12-12"), BigDecimal.valueOf(3.00));
        createGrade(te, ste1, sue, GradeType.F, 1, LocalDate.parse("2022-12-12"), BigDecimal.valueOf(2.00));
        createGrade(te, ste1, sue, GradeType.C, 1, LocalDate.parse("2022-12-12"), BigDecimal.valueOf(2.00));
        GradeContext context = new GradeContext();
        context.setGradeType(GradeType.F);
        context.setDateOfGrade(LocalDate.parse("2022-12-12"));
        //when
        Integer count = studentService.getNumberOfStudents(context);
        //then
        Assertions.assertThat(count).isEqualTo(2);
    }

    @Test
    public void getNumberOfStudentsShouldReturnZeroIfNoneFound() {
        //given
        TeacherEntity te = saveTestTeacher();
        ClassYearEntity cy = saveTestClassYear();
        StudentEntity ste = saveTestStudent(cy);
        StudentEntity ste1 = saveTestStudent(cy);
        SubjectEntity sue = saveTestSubject(cy,te);
        createGrade(te, ste, sue, GradeType.F, 1, LocalDate.parse("2022-12-12"), BigDecimal.valueOf(3.00));
        createGrade(te, ste1, sue, GradeType.F, 1, LocalDate.parse("2022-12-12"), BigDecimal.valueOf(2.00));
        createGrade(te, ste1, sue, GradeType.C, 1, LocalDate.parse("2022-12-12"), BigDecimal.valueOf(2.00));
        GradeContext context = new GradeContext();
        context.setGradeType(GradeType.D);
        context.setDateOfGrade(LocalDate.parse("2022-12-12"));
        //when
        Integer count = studentService.getNumberOfStudents(context);
        //then
        Assertions.assertThat(count).isEqualTo(0);
    }

    @Test
    public void createNewShouldReturnStudentWithMatchingFields() {
        //given
        ClassYearEntity cye = saveTestClassYear();
        StudentEto studentEto = new StudentEto();
        studentEto.setClassYearEntityId(cye.getId());
        studentEto.setFirstName("Kamil");
        studentEto.setLastName("Slimak");
        studentEto.setAge(20);
        //when
        StudentEto result = studentService.createNew(studentEto);
        //then
        Assertions.assertThat(result.getFirstName()).isEqualTo("Kamil");
        Assertions.assertThat(result.getLastName()).isEqualTo("Slimak");
        Assertions.assertThat(result.getAge()).isEqualTo(20);
    }


    @Test
    public void createNewShouldThrowExceptionIfClassYearIDNotExist()  {
        //given
        ClassYearEntity cye = saveTestClassYear();
        StudentEto studentEto = new StudentEto();
        studentEto.setClassYearEntityId(cye.getId()+1);
        studentEto.setFirstName("Kamil");
        studentEto.setLastName("Slimak");

        Assertions.assertThatThrownBy(() -> {
                    //when
            studentService.createNew(studentEto);
                    //then
        }).isInstanceOf(ClassYearNotFoundException.class)
                .hasMessageContaining("ClassYear with id: " + (cye.getId()+1) + " could not be found");
    }


    @Test
    public void createNewShouldThrowExceptionWhenClassYearIdNotProvided() {
        //given
        StudentEto studentEto = new StudentEto();
        studentEto.setFirstName("Kamil");
        studentEto.setLastName("Slimak");

        Assertions.assertThatThrownBy(() -> {
            //when
            studentService.createNew(studentEto);
            //then
        }).isInstanceOf(ConstraintViolationException.class);
    }


    @Test
    public void partialUpdateShouldReturnStudentWithNewValues() {
        //Given
        ClassYearEntity cy = saveTestClassYear();
        StudentEntity ste = saveTestStudent(cy);

        Map<String, Object> info = new HashMap<>();
        info.put("firstName", "Kuba");

        //When
        StudentEto studentEto = studentService.partialUpdate(ste.getId(), info);

        String newName = stRepo.findById(ste.getId()).get().getFirstName();
        //Then
        Assertions.assertThat(newName).isEqualTo("Kuba");
    }

    @Test
    public void partialUpdateWithNotExistingClassYearShouldThrowException() {
        //given
        ClassYearEntity cy = saveTestClassYear();
        StudentEntity ste = saveTestStudent(cy);

        Map<String, Object> info = new HashMap<>();
        info.put("classYearEntityId", 2);

        Assertions.assertThatThrownBy(() -> {
                    //when
            studentService.partialUpdate(ste.getId(), info);
                    //then
        }).isInstanceOf(ClassYearNotFoundException.class)
                .hasMessageContaining("ClassYear with id: " + 2L + " could not be found");
    }

    @Test
    public void findingStudentAfterDeleteShouldThrowException() {

        //given
        ClassYearEntity cy = saveTestClassYear();
        StudentEntity ste = saveTestStudent(cy);

        Assertions.assertThatThrownBy(() -> {
                    //when
                    studentService.delete(ste.getId());
                    StudentEto result = studentService.findStudentById(ste.getId());
                    //then
                }).isInstanceOf(StudentNotFoundException.class)
                .hasMessageContaining("Student with id: " + ste.getId() + " could not be found");
    }

    @Test
    public void deleteStudentShouldLeaveEmptyDatabaseTable() {
        //given
        ClassYearEntity cy = saveTestClassYear();
        StudentEntity ste = saveTestStudent(cy);

        //when
        studentService.delete(ste.getId());

        List<StudentEntity> students = stRepo.findAll();
        //then
        Assertions.assertThat(students).isEmpty();
    }

    @Test
    public void deleteStudentShouldDeleteAllAssignedGrades() {
        //given
        TeacherEntity te = saveTestTeacher();
        ClassYearEntity cy = saveTestClassYear();
        StudentEntity ste = saveTestStudent(cy);
        SubjectEntity sue = saveTestSubject(cy,te);
        saveTestGrade(te,ste,sue);
        saveTestGrade(te,ste,sue);

        Integer oldGradesCount = gRepo.findAllByStudentEntityId(ste.getId()).size();
        //when
        studentService.delete(ste.getId());

        //then
        Integer newGradesCount = gRepo.findAllByStudentEntityId(ste.getId()).size();

        Assertions.assertThat(oldGradesCount).isNotEqualTo(newGradesCount);
        Assertions.assertThat(newGradesCount).isEqualTo(0);

    }


    private void createGrade(TeacherEntity te, StudentEntity ste, SubjectEntity sue, GradeType gt, Integer val, LocalDate date, BigDecimal wg) {
        GradeEntity grade = new GradeEntity();
        grade.setSubjectEntity(sue);
        grade.setStudentEntity(ste);
        grade.setTeacherEntity(te);
        grade.setGradeType(gt);
        grade.setValue(val);
        grade.setDateOfGrade(date);
        grade.setWeight(wg);
        gRepo.save(grade);
    }


}