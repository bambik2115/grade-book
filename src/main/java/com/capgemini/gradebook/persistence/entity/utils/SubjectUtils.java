package com.capgemini.gradebook.persistence.entity.utils;

import com.capgemini.gradebook.persistence.entity.ClassYearEntity;
import com.capgemini.gradebook.persistence.entity.SubjectType;

public class SubjectUtils {

    public static final String setCustomName(ClassYearEntity cy, SubjectType st) {
        return st + "_" + cy.getClassLevel() + cy.getClassName();
    }

}
