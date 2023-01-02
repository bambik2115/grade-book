package com.capgemini.gradebook.controller;


import com.capgemini.gradebook.domain.SubjectEto;
import com.capgemini.gradebook.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/rest")
public class SubjectRestController {

    private final SubjectService subjectService;

    @Autowired
    public SubjectRestController(final SubjectService subjectService) {

        this.subjectService = subjectService;
    }

    @GetMapping("/subjects/get/{id}")
    public ResponseEntity<SubjectEto> findSubjectById(@PathVariable("id") final Long id) {

        final SubjectEto subject = this.subjectService.findSubjectById(id);
        return ResponseEntity.ok().body(subject);
    }

    @PostMapping("/subjects/new")
    public SubjectEto addSubject(@RequestBody SubjectEto newSubject) {

        return subjectService.createNew(newSubject);
    }

    @PatchMapping("/subjects/update/{id}")
    public SubjectEto updateSubjectTeacher(@PathVariable("id") final Long id, @RequestBody Long newTeacherId) {

        return subjectService.updateSubjectTeacher(id, newTeacherId);
    }

    @DeleteMapping("/subjects/delete/{id}")
    void deleteSubject(@PathVariable Long id) {
        subjectService.delete(id);
    }
}
