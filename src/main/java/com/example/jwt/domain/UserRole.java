package com.example.jwt.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserRole )) return false;
        return this.roleName != null && this.roleName.equals(((UserRole) o).roleName) && this.id != ((UserRole) o).id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this);
    }

}
