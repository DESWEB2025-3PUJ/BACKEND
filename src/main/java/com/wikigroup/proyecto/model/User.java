package com.wikigroup.proyecto.model;


import org.hibernate.annotations.SQLDelete;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence. Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@Where(clause = "status = 0")
@SQLDelete(sql = "UPDATE application SET status = 1 WHERE id =? ")

public class User {
@Id
@GeneratedValue(strategy = GenerationType. IDENTITY)
private long id;
private String name;
private String url;
private String code;
private String internalCode;
private Status status;
}

// Add this enum definition if Status is meant to be an enum
enum Status {
	ACTIVE,
	INACTIVE
}

