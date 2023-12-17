package com.krzysztofapp.gradebook.persistence.entity.utils;

import com.krzysztofapp.gradebook.persistence.entity.ClassYearEntity;
import com.krzysztofapp.gradebook.persistence.entity.SubjectType;

public class SubjectUtils {

    public static final String setCustomName(ClassYearEntity cy, SubjectType st) {
        return st + "_" + cy.getClassLevel() + cy.getClassName();
    }

}
