package com.start.springdatawithmysql;//package main;


import com.start.springdatawithmysql.entities.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

public class Test4 {

    @PersistenceContext
    private EntityManager entityManager;

    private String[] getKey(String value){
        String[ ] keys = null;
        String k = value.replace(" ", "").contains("$id") ? value.replace("$id","id") : value;

        if(k.contains(".")){
            keys = k.split("\\.");
        } else {
            keys[ 0 ] = k;
        }

        return keys;
    }


    public CriteriaBuilder getQuery(final String filter) {

        /*EntityManagerFactory factory =
                Persistence.createEntityManagerFactory("User");
        EntityManager entityManager = factory.createEntityManager();*/
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);
        Root<User> root = query.from( User.class );
        //root.fetch("", JoinType.LEFT);
        //entityManager.createQuery(criteriaQuery).setFirstResult(index).setMaxResults(pageSize);

        String[] splitedQuery = null;

        String f = filter.replace("{", "").replace("}", "").replace("'","");
        for ( String q : f.split(",")) {
            splitedQuery = q.split(":");
            String value = splitedQuery[1];
            String keyValue = splitedQuery[0].replace(" ", "");

            String[] key = getKey(keyValue);

            if(key.length > 1){
                for(int i = 0; i <= key.length-1 ; i++){

                    root.fetch(key[i], JoinType.LEFT);
                }
                query.select(root).where(
                        criteriaBuilder.equal(root.get(key[key.length-1]),value));
            } else {
                query.select(root).where(
                        criteriaBuilder.equal(root.get(key[0]),value));
            }


        }
        return criteriaBuilder;

    }

    private String getValue(String pattern , String value) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(value);

        while (m.find()) {
            value = m.group();
        }
        return value;
    }


 /*   public static void main (String [] args){

        Test4 t = new Test4();

        String filter = "{'capability.platform.type': 'Buitin'}";

        String filter1 = "{'hidden':false}";

        String filter2 = "{ projections.collector.$id : '56969a35e4b02c7944a06e45', temporal : false}";

        //t.getQuery(query);
        t.getQuery(filter);
        //t.getQuery(filter1);
        //t.getQuery(filter2);

    }*/
}


