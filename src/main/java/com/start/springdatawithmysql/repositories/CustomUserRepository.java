package com.start.springdatawithmysql.repositories;

import javax.persistence.criteria.CriteriaBuilder;

public interface CustomUserRepository {

    //public CriteriaBuilder getQuery(final String filter);
    public CriteriaBuilder getQuery(final String filter, final Class<?> o);

}
