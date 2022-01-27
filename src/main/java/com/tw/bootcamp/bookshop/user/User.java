package com.tw.bootcamp.bookshop.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(example = "1", description = "Unique identifier of the user")
    private Long id;
    @NotBlank(message = "Email is mandatory")
    @Schema(example = "user@example.com", description = "email of user")
    private String email;
    @NotBlank(message = "Password is mandatory")
    @Schema(example = "password", description = "password of user")
    @JsonIgnore
    private String password;
    @Schema(example = "USER", description = "Role assigned to user")
    @Enumerated(EnumType.STRING)
    private Role role;

    // NOTE: 25/01/22 : Nandhini + Devesh - Do not uncomment this unless you have better solution, it creates infinite chain of nested objects
    /*@OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Address> addresses;*/

    User(String email, String password, Role role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User(String email, Role role) {
        this.email = email;
        this.role = role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public static User create(CreateUserRequest userRequest) {
        String password = "";
        if (!userRequest.getPassword().isEmpty()) {
            password = PASSWORD_ENCODER.encode(userRequest.getPassword());
        }
        return new User(userRequest.getEmail(), password, Role.USER);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
