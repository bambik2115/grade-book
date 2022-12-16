package com.capgemini.gradebook.controller;

import com.capgemini.gradebook.domain.StudentEto;
import com.capgemini.gradebook.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/rest")
public class StudentRestController {

    private final StudentService studentService;

    @Autowired
    public StudentRestController(final StudentService studentService) {

        this.studentService = studentService;
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<StudentEto> findStudentById(@PathVariable("id") final Long id) {

        final StudentEto subject = this.studentService.findStudentById(id);
        return ResponseEntity.ok().body(subject);
    }

    @PostMapping("/students")
    public StudentEto addStudent(@Valid @RequestBody StudentEto newStudent) {

        return studentService.createNew(newStudent);
    }

    @PatchMapping("/students/{id}")
    public StudentEto partialUpdate(@PathVariable("id") final Long id, @RequestBody Map<String, Object> updateInfo)  {

        return studentService.partialUpdate(id, updateInfo);
    }

    @DeleteMapping("/students/{id}")
    void deleteStudent(@PathVariable Long id) {
        studentService.delete(id);
    }


}
