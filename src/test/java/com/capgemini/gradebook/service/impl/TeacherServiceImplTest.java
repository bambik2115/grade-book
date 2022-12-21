package com.capgemini.gradebook.service.impl;

import com.capgemini.gradebook.domain.TeacherEto;
import com.capgemini.gradebook.domain.mapper.TeacherMapper;
import com.capgemini.gradebook.persistence.entity.TeacherEntity;
import com.capgemini.gradebook.persistence.repo.TeacherRepo;
import com.capgemini.gradebook.service.TeacherService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;
import javax.swing.text.html.Option;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TeacherServiceImplTest {

        @InjectMocks
        private TeacherServiceImpl teacherService;

        @Mock
        private TeacherRepo teacherRepository;


        @Test
        public void getTeacherByIdReturnsTeacher() {

            //Given
            TeacherEntity teacher = new TeacherEntity();
            teacher.setFirstName("Krzysztof");
            teacher.setLastName("Wier");
            teacher.setId(1L);
            when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));

            //When
            TeacherEto test = teacherService.findTeacherById(1L);


            //Then

            assertEquals(test.getFirstName(), "Krzysztof");
            assertEquals(test.getLastName(), "Wier");
            assertEquals(test.getId(), 1L);
        }

    }