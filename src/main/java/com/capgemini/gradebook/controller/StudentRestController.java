package com.capgemini.gradebook.controller;

import com.capgemini.gradebook.domain.GradeContext;
import com.capgemini.gradebook.domain.StudentEto;
import com.capgemini.gradebook.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rest")
public class StudentRestController {

    private final StudentService studentService;

    @Autowired
    public StudentRestController(final StudentService studentService) {

        this.studentService = studentService;
    }

    @GetMapping("/students/get/{id}")
    public ResponseEntity<StudentEto> findStudentById(@PathVariable("id") final Long id) {

        final StudentEto subject = this.studentService.findStudentById(id);
        return ResponseEntity.ok().body(subject);
    }

    @GetMapping("/students/studentsWithGrade")
    public List<StudentEto> getAllStudentsWithGradeFAtCertainDay(@RequestBody LocalDate day) {

        return this.studentService.findAllStudentsWithGradeFAtCertainDay(day);
    }

    @GetMapping("/students/numberOfStudents")
    public Integer getNumberOfStudentsWithGradeAtDay(@RequestBody GradeContext gradeContext) {

        return this.studentService.getNumberOfStudents(gradeContext);
    }
    @PostMapping("/students/new")
    public StudentEto addStudent(@RequestBody StudentEto newStudent) {

        return studentService.createNew(newStudent);
    }

    @PatchMapping("/students/update/{id}")
    public StudentEto partialUpdate(@PathVariable("id") final Long id, @RequestBody Map<String, Object> updateInfo)  {

        return studentService.partialUpdate(id, updateInfo);
    }

    @DeleteMapping("/students/delete/{id}")
    void deleteStudent(@PathVariable Long id) {
        studentService.delete(id);
    }


}
