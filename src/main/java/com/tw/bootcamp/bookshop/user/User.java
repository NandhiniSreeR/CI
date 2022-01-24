package com.tw.bootcamp.bookshop.user;

import com.tw.bootcamp.bookshop.user.address.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

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
    private Long id;
    @NotBlank(message = "Email is mandatory")
    private String email;
    @NotBlank(message = "Password is mandatory")
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    @OneToMany(mappedBy = "user")
    private List<Address> addresses;

    private User(String email, String password, Role role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public static User create(CreateUserRequest userRequest) {
        String password = "";
        if (!userRequest.getPassword().isEmpty()) {
            password = PASSWORD_ENCODER.encode(userRequest.getPassword());
        }
        return new User(userRequest.getEmail(), password, Role.USER);
    }
}
