package com.example.jwt.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class UserRole {
    @Id
    @GeneratedValue
    private long id;

    @NonNull
    @Column(unique = true)
    private String roleName;
}
