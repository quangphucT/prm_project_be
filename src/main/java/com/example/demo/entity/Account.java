package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Account implements UserDetails {

    @Schema(defaultValue = "false")
    Boolean isDeleted = false;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @NotBlank(message = "Firstname must not be blank!")
    String firstName;

    @NotBlank(message = "Lastname must bot be blank!")
    String lastName;

    @Email(message = "Email is invalid!")
    @NotBlank(message = "Email must not be blank!")
    @Column(unique = true)
    String email;

    @NotBlank(message = "Password must not be blank!")
    @Size(min = 8, message = "Password must be at least 8 characters!")
    String password;

    @NotBlank(message = "Phone must not be blank!")
    @Pattern(regexp = "(84|0[3|5|7|8|9])+([0-9]{8})\\b", message = "Phone is invalid!")
    @Column(unique = true)
    String phone;

    @Enumerated(EnumType.STRING)
    Role role;

    String avatar;
    Date createdAt;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        if(this.role != null) authorities.add(new SimpleGrantedAuthority(this.role.toString()));
        return authorities;
    }

    @Override
    public String getUsername() {
        return this.email;
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


    @OneToMany(mappedBy = "account")
    List<Product> products;


    @OneToMany(mappedBy = "customer")
    List<Orders> orders;
}
