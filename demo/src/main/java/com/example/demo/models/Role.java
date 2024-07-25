package com.example.demo.models;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name")
    private RoleType name;

    public enum RoleType{
        USER,
        MODERATOR,
        ADMIN
    }
    public Role(){}
    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public RoleType getName(){
        return name;
    }

    public void setName(RoleType name){
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return id == role.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
