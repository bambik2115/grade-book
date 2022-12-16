package com.capgemini.gradebook.service.impl;

import com.capgemini.gradebook.domain.SubjectEto;
import com.capgemini.gradebook.domain.mapper.SubjectMapper;
import com.capgemini.gradebook.exceptions.ClassYearNotFoundException;
import com.capgemini.gradebook.exceptions.SubjectNotFoundException;
import com.capgemini.gradebook.exceptions.TeacherNotFoundException;
import com.capgemini.gradebook.persistence.entity.ClassYear;
import com.capgemini.gradebook.persistence.entity.SubjectEntity;
import com.capgemini.gradebook.persistence.entity.TeacherEntity;
import com.capgemini.gradebook.persistence.repo.ClassYearRepo;
import com.capgemini.gradebook.persistence.repo.SubjectRepo;
import com.capgemini.gradebook.persistence.repo.TeacherRepo;
import com.capgemini.gradebook.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;




@Service
public class SubjectServiceImpl implements SubjectService {


    private final SubjectRepo subjectRepository;
    private final TeacherRepo teacherRepository;
    private final ClassYearRepo classyearRepository;

    @Autowired
    public SubjectServiceImpl(final SubjectRepo subjectRepository, final TeacherRepo teacherRepository, final ClassYearRepo classyearRepository) {

        this.subjectRepository = subjectRepository;
        this.teacherRepository = teacherRepository;
        this.classyearRepository = classyearRepository;
    }

    @Override
    public SubjectEto findSubjectById(Long id) {

        SubjectEntity result = this.subjectRepository.findById(id)
                .orElseThrow( ()-> new SubjectNotFoundException("Subject with id: " + id + " could not be found"));

        return SubjectMapper.mapToETO(result);
    }

    @Transactional
    @Override
    public SubjectEto createNew(SubjectEto newSubject) {
        if (newSubject.getId() != null) {
            newSubject.setId(null);
        }

        SubjectEntity subjectEntity = SubjectMapper.mapToEntity(newSubject);
        TeacherEntity teacher = this.teacherRepository.findById(newSubject.getTeacherEntityId())
                .orElseThrow( ()-> new SubjectNotFoundException("Subject with id: " + newSubject.getTeacherEntityId() + " could not be found"));
        ClassYear classyear = this.classyearRepository.findById(newSubject.getClassYearId())
                .orElseThrow( ()-> new ClassYearNotFoundException("ClassYear with id: " + newSubject.getClassYearId() + " could not be found"));
        subjectEntity.setTeacherEntity(teacher);
        subjectEntity.setClassYear(classyear);
        subjectEntity.setName();
        subjectEntity = this.subjectRepository.save(subjectEntity);
        return SubjectMapper.mapToETO(subjectEntity);
    }

    @Transactional
    @Override
    public SubjectEto updateSubjectTeacher(Long id, SubjectEto newTeacherId) {

        SubjectEntity subject = this.subjectRepository.findById(id)
                .orElseThrow( ()-> new SubjectNotFoundException("Subject with id: " + id + " could not be found"));
        TeacherEntity teacher = this.teacherRepository.findById(newTeacherId.getTeacherEntityId())
                .orElseThrow( ()-> new TeacherNotFoundException("Teacher with id: " + newTeacherId.getTeacherEntityId() + " could not be found"));
        subject.setTeacherEntity(teacher);

        return SubjectMapper.mapToETO(subject);
    }


    @Override
    public void delete(Long id) {

        this.subjectRepository.deleteById(id);

    }
}
