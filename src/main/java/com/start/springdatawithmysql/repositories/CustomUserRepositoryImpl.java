package com.start.springdatawithmysql.repositories;

import com.start.springdatawithmysql.entities.User;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CustomUserRepositoryImpl implements CustomUserRepository {

    private  EntityManager entityManager;

    public CustomUserRepositoryImpl(final EntityManager entityManager){

        this.entityManager = entityManager;
    }


    private String[] getKey(String value){
        String[ ] keys = new String[2];
        String k = value.replace(" ", "").contains("$id") ? value.replace("$id","id").toLowerCase() : value.toLowerCase();

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


    public CriteriaBuilder getQuery(final String filter, final Class<?> className) {

        String entityName = className.getSimpleName();
        StringBuilder query = new StringBuilder("Select " + className.getSimpleName().toLowerCase().substring(0,2) + " from " + entityName + " " + entityName.substring(0,2).toLowerCase());
        Set<String> fromQuery = new HashSet<>();
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

                alias = getQueryWithMultipleFetchs(className, fromQuery, where, alias, value, key);

            } else {
                where.append(" where " + entityName.substring(0,2) +"." + key[0] + " = " + "'" + value + "'");
            }

        }

        for(String s : fromQuery ){
            query.append(s);
        }


         String completeQuery = query.toString() + where.toString();
         entityManager.createQuery(completeQuery, className).getResultList();

        return null;

    }

    private String getQueryWithMultipleFetchs(final Class<?> className, final Set<String> fromQuery, final StringBuilder where, final String alias, final String value, final String[] key) {
        String a = alias;
        for(int i = 0; i <= key.length-2 ; i++){
            a = key[i].substring(0,2);
            String keyValue = key[i] + " " + a;

                if(i==0){
                    fromQuery.add(" join fetch " + className.getSimpleName().substring(0,2).toLowerCase() + "." + keyValue);
                } else {
                    fromQuery.add(" join fetch " + key[i-1].substring(0,2) + "." + keyValue);
                }
        }

        String whereValue = a + "." + key[key.length-1];
        if(where.length()==0){
            where.append(" where " + whereValue );
        } else {
            where.append(" and " + whereValue);
        }

        where.append(" = " + "'" + value + "'");
        return a;
    }


    /*private String getQueryWithMultipleFetchs2(Class<?> className, StringBuilder fromQuery, StringBuilder where, String alias, String value, String[] key) {
        List<String> fetchs = new ArrayList<String>();
        for(int i = 0; i <= key.length-2 ; i++){
            alias = key[i].substring(0,2);
            String keyValue = key[i] + " " + alias;


            if(i==0){
                fromQuery.append(" join fetch " + className.getSimpleName().substring(0,2) + "." + keyValue);
                //fetchs.add(" join fetch " + className.getSimpleName().toLowerCase().substring(0,2) + "." + keyValue);
            } else {
                fromQuery.append(" join fetch " + key[i-1].substring(0,2) + "." + keyValue);
                //fetchs.add(" join fetch " + key[i-1].substring(0,2) + "." + keyValue);
            }
        }

        *//*List<String> fetchsWithoutDuplicates = fetchs.stream()
                .distinct()
                .collect(Collectors.toList());

        for(String s : fetchsWithoutDuplicates ){
            fromQuery.append(s);
        }*//*

        String whereValue = alias + "." + key[key.length-1];
        if(where.length()==0){
            where.append(" where " + whereValue );
        } else {
            where.append(" and " + whereValue);
        }

        where.append(" = " + "'" + value + "'");
        return alias;
    }*/

    private String getValue(final String pattern ,final String value) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(value);
        String result = "";

        while (m.find()) {
            result = m.group();
        }
        return result;
    }
}
