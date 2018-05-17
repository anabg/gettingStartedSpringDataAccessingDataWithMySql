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
                /**
                 * CriteriaBuilder builder = entityManager.getCriteriaBuilder();
                 CriteriaQuery<MyEntity> query = builder.createQuery(MyEntity.class);
                 Root<MyEntity> root = query.from(MyEntity.class);
                 Join<MyEntity, RelatedEntity> join = root.join("relatedEntity");
                 query.select(root).where(builder.equals(join.get("id"), 3));
                 EntityGraph<MyEntity> fetchGraph = entityManager.createEntityGraph(MyEntity.class);
                 fetchGraph.addSubgraph("relatedEntity");
                 entityManager.createQuery(query).setHint("javax.persistence.loadgraph", fetchGraph);
                 */
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


        User user = entityManager.createQuery( query ).getSingleResult();

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
}
