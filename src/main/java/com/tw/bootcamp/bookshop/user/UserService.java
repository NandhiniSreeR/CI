package com.tw.bootcamp.bookshop.user;

import com.tw.bootcamp.bookshop.error.EmailDoesNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.validation.Validator;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UserService implements UserDetailsService {
    public static final String EMAIL_REGEX = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
    public static final String PASSWORD_REGEX = "^(?=.*[A-Z])"
            + "(?=.*[@#$*!%^&+=])"
            + "(?=\\S+$).{8,}$";
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Validator validator;

    public UserService() {
    }

    public User create(CreateUserRequest userRequest) throws InvalidEmailException, InvalidEmailPatternException, PasswordEmptyException, InvalidPasswordPatternException {
        Optional<User> user = userRepository.findByEmail(userRequest.getEmail());
        if (user.isPresent()) {
            throw new InvalidEmailException();
        }
        User newUser = User.create(userRequest);
        validator.validate(newUser);

        if (!Pattern.matches(EMAIL_REGEX, userRequest.getEmail())) {
            throw new InvalidEmailPatternException();
        }

        if (userRequest.getPassword().isEmpty()) {
            throw new PasswordEmptyException();
        }

        if (!Pattern.matches(PASSWORD_REGEX, userRequest.getPassword())) {
            throw new InvalidPasswordPatternException();
        }
        return userRepository.save(newUser);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                AuthorityUtils.createAuthorityList(user.getRole().authority())
        );
    }

    public User updateRole(User user) {
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (!existingUser.isPresent()) {
            throw new EmailDoesNotExistException();
        }
        existingUser.get().setRole(user.getRole());
        userRepository.save(existingUser.get());
        return existingUser.get();
    }
}
