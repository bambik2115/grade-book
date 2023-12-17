package com.krzysztofapp.gradebook.service;

import com.krzysztofapp.gradebook.domain.SubjectEto;

public interface SubjectService {

    SubjectEto findSubjectById(Long id);

    SubjectEto createNew(SubjectEto newSubject);

    SubjectEto updateSubjectTeacher(Long id, Long newTeacherId);

    void delete(Long id);
}
