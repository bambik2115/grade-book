package com.capgemini.gradebook.controller;


import com.capgemini.gradebook.domain.ClassYearEto;
import com.capgemini.gradebook.service.ClassYearService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/rest")
public class ClassYearRestController {

    private final ClassYearService classYearService;

    @Autowired
    public ClassYearRestController(final ClassYearService classYearService) {

        this.classYearService = classYearService;
    }

    @GetMapping("/classyear/get/{id}")
    public ResponseEntity<ClassYearEto> findClassYearById(@PathVariable("id") final Long id) {

        final ClassYearEto classyear = this.classYearService.findClassYearById(id);
        return ResponseEntity.ok().body(classyear);
    }

    @PostMapping("/classyear/new")
    public ClassYearEto addClassYear(@RequestBody ClassYearEto newClassYear) {

        return classYearService.createNew(newClassYear);
    }

    @PatchMapping("/classyear/update/{id}")
    ClassYearEto partialUpdate(@PathVariable("id") final Long id, @RequestBody Map<String, Object> updateInfo) {

        return classYearService.partialUpdate(id, updateInfo);
    }

    @DeleteMapping("/classyear/delete/{id}")
    void deleteClassYear(@PathVariable Long id) {
        classYearService.delete(id);
    }



}
