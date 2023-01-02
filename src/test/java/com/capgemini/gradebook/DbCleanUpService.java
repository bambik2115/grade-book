package com.capgemini.gradebook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.Table;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class DbCleanUpService {

    private final EntityManager em;
    private List<String> tableNames;

    @Autowired
    public DbCleanUpService(EntityManager em) {
        this.em = em;
    }

    @PostConstruct
    void afterPropertiesSet() {
        tableNames = em.getMetamodel().getEntities().stream()
                .filter(entityType -> entityType.getJavaType().getAnnotation(Table.class) != null)
                .map(entityType -> entityType.getJavaType().getAnnotation(Table.class))
                .map(Table::name)
                .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public void resetDatabase() {
        em.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();

        this.tableNames.forEach(table -> em
                .createNativeQuery("TRUNCATE TABLE " + table + "; ALTER TABLE " + table + " ALTER COLUMN ID RESTART WITH 1")
                .executeUpdate());

        em.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
    }

}
