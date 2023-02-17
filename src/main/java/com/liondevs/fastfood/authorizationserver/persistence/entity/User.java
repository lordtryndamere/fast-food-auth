package com.liondevs.fastfood.authorizationserver.persistence.entity;

import com.liondevs.fastfood.authorizationserver.persistence.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor //genero automaticamente getter y setter
@Entity
@Valid
@Table(name = "user")
public class User implements UserDetails {
    @Id
    @GeneratedValue
    @Column(unique = true, nullable = false)
    private Long id;

    @Column( nullable = false)

    private String firstName;
    @Column( nullable = false)

    private String lastName;
    @Column( nullable = false)

    private String email;

    @NotEmpty(message = "Phone is required")

    @Column( nullable = false)
    private String phone;


    @Column( nullable = false)

    private String password;
    @CreatedDate
    private LocalDateTime created_at;

    @CreatedDate
    private LocalDateTime updated_at;
    @Enumerated(EnumType.STRING)
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
