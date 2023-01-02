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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StudentServiceTest {

    @Inject
    private StudentService studentService;

    @Inject
    private DbCleanUpService cleanUpService;

    @Inject
    private TestEntityCreator tec;

    @Inject
    private StudentRepo strepo;

    @Inject
    private GradeRepo grepo;

    @AfterEach
    private void cleanDbBetweenTests() {
        cleanUpService.resetDatabase();
    }

    @Test
    public void findStudentByIdShouldReturnProperEntity() {

        //given
        ClassYear cy = tec.saveTestClassYear();
        tec.saveTestStudent(cy);

        //when
        StudentEto result = studentService.findStudentById(1L);

        //then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    public void findStudentByIdShouldThrowExceptionIfNotExist() {

        //given
        ClassYear cy = tec.saveTestClassYear();
        tec.saveTestStudent(cy);

        Assertions.assertThatThrownBy(() -> {
                    //when
                    studentService.findStudentById(2L);
                    //then
                }).isInstanceOf(StudentNotFoundException.class)
                .hasMessageContaining("Student with id: " + 2L + " could not be found");
    }

    @Test
    public void findAllWithGradeFAtCertainDayShouldReturnProperStudentsIfFound() {

        TeacherEntity te = tec.saveTestTeacher();
        ClassYear cy = tec.saveTestClassYear();
        StudentEntity ste = tec.saveTestStudent(cy);
        StudentEntity ste1 = tec.saveTestStudent(cy);
        SubjectEntity sue = tec.saveTestSubject(cy,te);
        createGrade(te, ste, sue, GradeType.F, 1, LocalDate.parse("2022-12-12"), BigDecimal.valueOf(3.00));
        createGrade(te, ste1, sue, GradeType.F, 1, LocalDate.parse("2022-12-12"), BigDecimal.valueOf(2.00));
        createGrade(te, ste1, sue, GradeType.C, 1, LocalDate.parse("2022-12-12"), BigDecimal.valueOf(2.00));

        List<StudentEto> result = studentService.findAllStudentsWithGradeFAtCertainDay(LocalDate.parse("2022-12-12"));

        Assertions.assertThat(result.size()).isEqualTo(2);
        Assertions.assertThat(result.get(0).getId()).isEqualTo(1L);
    }

    @Test
    public void findAllWithGradeFAtCertainDayShouldReturnEmptyListIfNotFound() {

        TeacherEntity te = tec.saveTestTeacher();
        ClassYear cy = tec.saveTestClassYear();
        StudentEntity ste = tec.saveTestStudent(cy);
        SubjectEntity sue = tec.saveTestSubject(cy,te);
        createGrade(te, ste, sue, GradeType.F, 1, LocalDate.parse("2022-12-12"), BigDecimal.valueOf(3.00));

        List<StudentEto> result = studentService.findAllStudentsWithGradeFAtCertainDay(LocalDate.parse("2022-11-12"));

        Assertions.assertThat(result).isEmpty();
    }

    @Test
    public void getNumberOfStudentsShouldReturnCorrectNumber() {

        TeacherEntity te = tec.saveTestTeacher();
        ClassYear cy = tec.saveTestClassYear();
        StudentEntity ste = tec.saveTestStudent(cy);
        StudentEntity ste1 = tec.saveTestStudent(cy);
        SubjectEntity sue = tec.saveTestSubject(cy,te);
        createGrade(te, ste, sue, GradeType.F, 1, LocalDate.parse("2022-12-12"), BigDecimal.valueOf(3.00));
        createGrade(te, ste1, sue, GradeType.F, 1, LocalDate.parse("2022-12-12"), BigDecimal.valueOf(2.00));
        createGrade(te, ste1, sue, GradeType.C, 1, LocalDate.parse("2022-12-12"), BigDecimal.valueOf(2.00));
        GradeContext context = new GradeContext();
        context.setGradeType(GradeType.F);
        context.setDateOfGrade(LocalDate.parse("2022-12-12"));

        Integer count = studentService.getNumberOfStudents(context);

        Assertions.assertThat(count).isEqualTo(2);
    }

    @Test
    public void getNumberOfStudentsShouldReturnZeroIfNoneFound() {

        TeacherEntity te = tec.saveTestTeacher();
        ClassYear cy = tec.saveTestClassYear();
        StudentEntity ste = tec.saveTestStudent(cy);
        StudentEntity ste1 = tec.saveTestStudent(cy);
        SubjectEntity sue = tec.saveTestSubject(cy,te);
        createGrade(te, ste, sue, GradeType.F, 1, LocalDate.parse("2022-12-12"), BigDecimal.valueOf(3.00));
        createGrade(te, ste1, sue, GradeType.F, 1, LocalDate.parse("2022-12-12"), BigDecimal.valueOf(2.00));
        createGrade(te, ste1, sue, GradeType.C, 1, LocalDate.parse("2022-12-12"), BigDecimal.valueOf(2.00));
        GradeContext context = new GradeContext();
        context.setGradeType(GradeType.D);
        context.setDateOfGrade(LocalDate.parse("2022-12-12"));

        Integer count = studentService.getNumberOfStudents(context);

        Assertions.assertThat(count).isEqualTo(0);
    }

    @Test
    public void createNewStudentShouldAlwaysAssignNewId() {

        //Given
        ClassYear cy = tec.saveTestClassYear();
        tec.saveTestStudent(cy);
        StudentEto studentEto = new StudentEto();
        studentEto.setId(1L);
        studentEto.setFirstName("Kamil");
        studentEto.setLastName("Slimak");
        studentEto.setClassYearId(1L);

        //When
        StudentEto result = studentService.createNew(studentEto);

        //Then
        Assertions.assertThat(result.getId()).isNotEqualTo(1L);
    }

    @Test
    public void createNewShouldReturnInstanceOfStudentEto() {

        tec.saveTestClassYear();
        StudentEto studentEto = new StudentEto();
        studentEto.setClassYearId(1L);
        studentEto.setLastName("Kamil");
        studentEto.setFirstName("Slimak");

        StudentEto result = studentService.createNew(studentEto);

        Assertions.assertThat(result).isInstanceOf(StudentEto.class);
    }

    @Test
    public void createNewShouldReturnStudentWithMatchingFields() {

        tec.saveTestClassYear();
        StudentEto studentEto = new StudentEto();
        studentEto.setClassYearId(1L);
        studentEto.setFirstName("Kamil");
        studentEto.setLastName("Slimak");
        studentEto.setAge(20);

        StudentEto result = studentService.createNew(studentEto);

        Assertions.assertThat(result.getFirstName()).isEqualTo("Kamil");
        Assertions.assertThat(result.getLastName()).isEqualTo("Slimak");
        Assertions.assertThat(result.getAge()).isEqualTo(20);
    }


    @Test
    public void createNewShouldThrowExceptionIfClassYearIDNotExist()  {

        StudentEto studentEto = new StudentEto();
        studentEto.setClassYearId(1L);
        studentEto.setFirstName("Kamil");
        studentEto.setLastName("Slimak");

        Assertions.assertThatThrownBy(() -> {

            studentService.createNew(studentEto);

        }).isInstanceOf(ClassYearNotFoundException.class)
                .hasMessageContaining("ClassYear with id: " + 1L + " could not be found");
    }


    @Test
    public void partialUpdateShouldReturnInstanceOfStudentEto() {

        ClassYear cy = tec.saveTestClassYear();
        tec.saveTestStudent(cy);
        Map<String, Object> info = new HashMap<>();
        info.put("firstName", "Kuba");

        StudentEto studenteto = studentService.partialUpdate(1L, info);

        Assertions.assertThat(studenteto).isInstanceOf(StudentEto.class);

    }

    @Test
    public void partialUpdateShouldReturnStudentWithNewValues() {
        //Given
        ClassYear cy = tec.saveTestClassYear();
        tec.saveTestStudent(cy);

        String oldName = this.strepo.findById(1L).get().getFirstName();

        Map<String, Object> info = new HashMap<>();
        info.put("firstName", "Kuba");

        //When
        StudentEto studentEto = studentService.partialUpdate(1L, info);

        String newName = this.strepo.findById(1L).get().getFirstName();
        //Then
        Assertions.assertThat(oldName).isNotEqualTo(newName);
        Assertions.assertThat(newName).isEqualTo("Kuba");
    }

    @Test
    public void partialUpdateWithNotExistingClassYearShouldThrowException() {

        ClassYear cy = tec.saveTestClassYear();
        tec.saveTestStudent(cy);

        Map<String, Object> info = new HashMap<>();
        info.put("classYearId", 2);

        Assertions.assertThatThrownBy(() -> {

            studentService.partialUpdate(1L, info);
        }).isInstanceOf(ClassYearNotFoundException.class)
                .hasMessageContaining("ClassYear with id: " + 2L + " could not be found");
    }

    @Test
    public void deleteStudentAndThenGetItShouldThrowException() {
        ClassYear cy = tec.saveTestClassYear();
        tec.saveTestStudent(cy);

        Assertions.assertThatThrownBy(() -> {
                    studentService.delete(1L);

                    StudentEto result = studentService.findStudentById(1L);

                }).isInstanceOf(StudentNotFoundException.class)
                .hasMessageContaining("Student with id: " + 1L + " could not be found");
    }

    @Test
    public void deleteStudentShouldLeaveEmptyDatabaseTable() {

        ClassYear cy = tec.saveTestClassYear();
        tec.saveTestStudent(cy);


        studentService.delete(1L);

        List<StudentEntity> students = this.strepo.findAll();

        Assertions.assertThat(students).isEmpty();
    }

    @Test
    public void deleteStudentShouldDeleteAllAssignedGrades() {
        TeacherEntity te = tec.saveTestTeacher();
        ClassYear cy = tec.saveTestClassYear();
        StudentEntity ste = tec.saveTestStudent(cy);
        SubjectEntity sue = tec.saveTestSubject(cy,te);
        tec.saveTestGrade(te,ste,sue);
        tec.saveTestGrade(te,ste,sue);

        Integer oldGradesCount = this.grepo.findAllByStudentEntityId(1L).size();
        //when
        studentService.delete(1L);

        //then
        Integer newGradesCount = this.grepo.findAllByStudentEntityId(1L).size();

        Assertions.assertThat(oldGradesCount).isNotEqualTo(newGradesCount);
        Assertions.assertThat(newGradesCount).isEqualTo(0);

    }


    private void createGrade(TeacherEntity te, StudentEntity ste, SubjectEntity sue, GradeType gt, Integer val, LocalDate date, BigDecimal wg) {
        Grade grade = new Grade();
        grade.setSubjectEntity(sue);
        grade.setStudentEntity(ste);
        grade.setTeacherEntity(te);
        grade.setGradeType(gt);
        grade.setValue(val);
        grade.setDateOfGrade(date);
        grade.setWeight(wg);
        this.grepo.save(grade);
    }


}