package com.capgemini.gradebook.service;

import com.capgemini.gradebook.domain.SubjectEto;

public interface SubjectService {

    SubjectEto findSubjectById(Long id);

    SubjectEto createNew(SubjectEto newSubject);

    SubjectEto updateSubjectTeacher(Long id, SubjectEto newTeacherId);

    void delete(Long id);
}
