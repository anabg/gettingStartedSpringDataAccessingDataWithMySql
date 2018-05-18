package com.start.springdatawithmysql.entities;

import javax.persistence.*;

@Entity
public class Mail {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer email_id;
    private String value;
    @ManyToOne(optional = false)
    private User user;

    public Integer getEmail_id() {
        return email_id;
    }

    public void setEmail_id(Integer email_id) {
        this.email_id = email_id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
