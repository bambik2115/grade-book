package com.capgemini.gradebook.service.impl;

import com.capgemini.gradebook.persistence.entity.TeacherEntity;
import com.capgemini.gradebook.persistence.repo.TeacherRepo;
import com.capgemini.gradebook.service.TeacherService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TeacherServiceImplTest {

        @MockBean
        private TeacherService teacherService;

        @MockBean
        private TeacherRepo teacherRepository;

        @Test
        public void getTeacherByIdReturnsTeacher() throws Exception {

            //Given
            TeacherEntity teacher = new TeacherEntity();
            teacher.setFirstName("Krzysztof");
            teacher.setLastName("Wier");
            teacher.setId(1L);

            //When
            when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));

            //Then

            assertEquals(teacherService.findTeacherById(1L).getFirstName(), "Krzysztof");
            assertEquals(teacherService.findTeacherById(1L).getLastName(), "Wier");
            assertEquals(teacherService.findTeacherById(1L).getId(), 1L);
        }

    }