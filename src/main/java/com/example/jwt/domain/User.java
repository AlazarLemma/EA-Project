package com.example.jwt.domain;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class User {
    @Id
    @GeneratedValue
    private long id;

    @NonNull
    @Column(unique = true)
    private String username;
    @NonNull
    private String password;
    @NonNull
    private boolean active;

    @OneToMany
    @JoinTable(
        name = "user_roles",
        joinColumns = {@JoinColumn(name="userId")},
        inverseJoinColumns = {@JoinColumn(name="roleId")}
    )
    @Fetch(FetchMode.JOIN)
    private Collection<UserRole> roles;
}
