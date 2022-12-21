package com.capgemini.gradebook.controller;


import com.capgemini.gradebook.domain.GradeEto;
import com.capgemini.gradebook.domain.GradeSearchCriteria;
import com.capgemini.gradebook.service.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rest")
public class GradeRestController {

    private final GradeService gradeService;

    @Autowired
    public GradeRestController(final GradeService gradeService) {

        this.gradeService = gradeService;
    }

    @GetMapping("/grades/{id}")
    public ResponseEntity<GradeEto> findGradeById(@PathVariable("id") final Long id) {

        final GradeEto grade = this.gradeService.findGradeById(id);
        return ResponseEntity.ok().body(grade);
    }

    @GetMapping("/grades/{studentId}/wgaverage/{subjectId}")
    public Double calculateWeightedAverage(@PathVariable("studentId") final Long studentId,
                                           @PathVariable("subjectId") final Long subjectId) {

        return this.gradeService.getWeightedAverage(studentId, subjectId);
    }

    @GetMapping("/grades")
    public List<GradeEto> findGradesByCriteria(@RequestBody GradeSearchCriteria criteria) {

        return this.gradeService.searchGradesByCriteria(criteria);
    }

    @PostMapping("/grades")
    public GradeEto addGrade(@Valid @RequestBody GradeEto newGrade) {

        return this.gradeService.createNew(newGrade);
    }

    @PatchMapping("/grades/{id}")
    public GradeEto partialUpdate(@PathVariable("id") final Long id, @RequestBody Map<String, Object> updateInfo) {
        return this.gradeService.partialUpdate(id, updateInfo);
    }

    @DeleteMapping("/grades/{id}")
    void deleteGrade(@PathVariable Long id) {
        this.gradeService.delete(id);
    }


}
