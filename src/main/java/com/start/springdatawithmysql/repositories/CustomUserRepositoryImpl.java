package com.start.springdatawithmysql.repositories;

import com.start.springdatawithmysql.entities.User;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomUserRepositoryImpl implements CustomUserRepository {

    private  EntityManager entityManager;

    public CustomUserRepositoryImpl(final EntityManager entityManager){

        this.entityManager = entityManager;
    }


    private String[] getKey(String value){
        String[ ] keys = new String[2];
        String k = value.replace(" ", "").contains("$id") ? value.replace("$id","id") : value;

        if(k.contains(".")){
            keys = k.split("\\.");
        } else {
            keys[ 0 ] = k;
        }

        String[] removedNull = Arrays.stream(keys)
                .filter(v ->
                        v != null && v.length() > 0
                )
                .toArray(size -> new String[size]);

        return removedNull;
    }


    public CriteriaBuilder getQuery2(final String filter) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);
        Root<User> root = query.from( User.class );

        String[] splitedQuery = null;

        String f = filter.replace("{", "").replace("}", "").replace("'","");
        for ( String q : f.split(",")) {
            splitedQuery = q.split(":");
            String value = splitedQuery[1];
            String keyValue = splitedQuery[0].replace(" ", "");

            String[] key = getKey(keyValue);
            Fetch fa = null;
            if(key.length > 1){

                for(int i = 0; i <= key.length-2 ; i++){
                    if(i>0){
                        fa = fa.fetch(key[i]);
                    } else
                    {
                         root.fetch(key[i]);
                    }

                }

                if(fa!=null){
                    query.select(root).where(
                            criteriaBuilder.equal(root.get(key[key.length-1]),value));
                }
                else {
                    query.select(root).where(
                            criteriaBuilder.equal(root.get(key[key.length-1]),value));
                }

            } else {
                query.select(root).where(
                        criteriaBuilder.equal(root.get(key[0]),value));
            }


        }
        return null;
    }


    public CriteriaBuilder getQuery(final String filter) {

        int count = 0;
        StringBuilder fromQuery = new StringBuilder("Select s from User s ");
        StringBuilder where = new StringBuilder();
        String[] splitedQuery = null;
        String value = "";
        String keyValue = "";

        String f = filter.replace("{", "").replace("}", "").replace("'","");

        String alias  ="";
        for ( String q : f.split(",")) {
            splitedQuery = q.split(":");
            value = splitedQuery[1];
            keyValue = splitedQuery[0].replace(" ", "");
            String[] key = getKey(keyValue);

            if(key.length > 1){

                alias = getQueryWithMultipleFetchs(fromQuery, where, alias, value, key);

            } else {
                where.append(" where s." + key[0] + " = " + "'" + value + "'");
            }

        }

         String completeQuery = fromQuery.toString() + where.toString();
         entityManager.createQuery(completeQuery, User.class).getResultList();

        return null;

    }

    private String getQueryWithMultipleFetchs(StringBuilder fromQuery, StringBuilder where, String alias, String value, String[] key) {
        for(int i = 0; i <= key.length-2 ; i++){
            alias = key[i].substring(0,2);
            String keyValue = key[i] + " " + alias;
            if(i==0){
                fromQuery.append(" join fetch s." + keyValue);
            } else {
                fromQuery.append(" join fetch " + key[i-1].substring(0,2) + "." + keyValue);
            }
        }

        String whereValue = alias + "." + key[key.length-1];
        if(where.length()==0){
            where.append(" where " + whereValue );
        } else {
            where.append(" and " + whereValue);
        }

        where.append(" = " + "'" + value + "'");
        return alias;
    }

    private String getValue(String pattern , String value) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(value);

        while (m.find()) {
            value = m.group();
        }
        return value;
    }
}
