package com.example.clientservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class ClientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int sessionsRemaining;

    public ClientEntity() {}

    public ClientEntity(String name, int sessionsRemaining) {
        this.name = name;
        this.sessionsRemaining = sessionsRemaining;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSessionsRemaining() {
        return sessionsRemaining;
    }

    public void setSessionsRemaining(int sessionsRemaining) {
        this.sessionsRemaining = sessionsRemaining;
    }

    @Override
    public String toString() {
        return "ClientEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sessionsRemaining=" + sessionsRemaining +
                '}';
    }

}
