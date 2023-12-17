package com.krzysztofapp.gradebook.persistence.entity.utils;

import com.krzysztofapp.gradebook.persistence.entity.ClassYearEntity;
import com.krzysztofapp.gradebook.persistence.entity.SubjectType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;


class SubjectUtilsTest {



    @Test
    public void setCustomNameShouldReturnProperString() {
        //Given
        ClassYearEntity cy = new ClassYearEntity();
        cy.setClassName("D");
        cy.setClassLevel(1);
        cy.setClassYear("2022");

        //When
        String result = SubjectUtils.setCustomName(cy, SubjectType.BIOLOGY);

        //Then
        Assertions.assertThat(result).isEqualTo("BIOLOGY_1D");
    }



}