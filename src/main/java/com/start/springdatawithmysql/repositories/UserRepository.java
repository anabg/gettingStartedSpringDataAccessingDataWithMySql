package com.start.springdatawithmysql.repositories;

import org.springframework.data.repository.CrudRepository;
import com.start.springdatawithmysql.entities.User;

/**
 * Created by �Anita on 13/5/2018.
 */
// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface UserRepository extends CrudRepository<User, Long>,CustomUserRepository {

}