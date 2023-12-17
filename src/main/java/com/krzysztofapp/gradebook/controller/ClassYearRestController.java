package com.krzysztofapp.gradebook.controller;


import com.krzysztofapp.gradebook.domain.ClassYearEto;
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

        final ClassYearEto classYear = this.classYearService.findClassYearById(id);
        return ResponseEntity.ok().body(classYear);
    }

    @PostMapping("/classyear/new")
    public ClassYearEto addClassYear(@RequestBody ClassYearEto newClassYear) {

        return this.classYearService.createNew(newClassYear);
    }

    @PatchMapping("/classyear/update/{id}")
    public ClassYearEto partialUpdate(@PathVariable("id") final Long id, @RequestBody Map<String, Object> updateInfo) {

        return this.classYearService.partialUpdate(id, updateInfo);
    }

    @DeleteMapping("/classyear/delete/{id}")
    void deleteClassYear(@PathVariable Long id) {
        this.classYearService.delete(id);
    }



}
