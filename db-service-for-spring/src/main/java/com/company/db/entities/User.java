package com.company.db.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",unique = true,nullable = false)
    @Basic(optional = false)
    private Integer id;

    @Column(name = "name",unique = true,nullable = false)
    private String name;

}
