package com.start.springdatawithmysql.entities;

import javax.persistence.*;
import java.util.List;

/**
 * Created by �Anita on 13/5/2018.
 */
@Entity // This tells Hibernate to make a table out of this class
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer user_id;

    private String name;

    private String email;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List <Address> address;

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Address> getAddress() {
        return address;
    }

    public void setAddress(List<Address> address) {
        this.address = address;
    }
}
