package com.example.demo.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue()
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column
    private RoleEnum name;
}