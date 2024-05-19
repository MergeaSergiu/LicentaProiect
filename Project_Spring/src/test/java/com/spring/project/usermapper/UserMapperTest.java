package com.spring.project.usermapper;

import com.spring.project.dto.RegistrationRequest;
import com.spring.project.mapper.UserMapper;
import com.spring.project.model.Role;
import com.spring.project.model.User;
import com.spring.project.repository.RoleRepository;
import com.spring.project.repository.UserRepository;
import com.spring.project.service.impl.EmailValidator;
import com.spring.project.service.impl.PasswordValidator;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class
UserMapperTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;
    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        userMapper = new UserMapper(new EmailValidator(), new PasswordValidator());
    }

    @Test
    void throwsEmailNotAvailableException() {

        Role role = Role.builder()
                .id(1L)
                .name("USER").build();
        RegistrationRequest registrationRequest = RegistrationRequest.builder()
                .firstName("Cont")
                .lastName("Test")
                .email(null)
                .password("Password1")
                .build();

        assertThrows(IllegalArgumentException.class, () -> userMapper.convertFromDto(registrationRequest, role));
    }

    @Test
    void throwsEmailNotAvailableException2() {

        Role role = Role.builder()
                .id(1L)
                .name("USER").build();
        RegistrationRequest registrationRequest = RegistrationRequest.builder()
                .firstName("Cont")
                .lastName("Test")
                .email("emailaddress@.com")
                .password("Password1")
                .build();

        assertThrows(IllegalArgumentException.class, () -> userMapper.convertFromDto(registrationRequest, role));
    }

    @Test
    void throwsEmailNotAvailableException3() {

        Role role = Role.builder()
                .id(1L)
                .name("USER").build();
        RegistrationRequest registrationRequest = RegistrationRequest.builder()
                .firstName("Cont")
                .lastName("Test")
                .email("emailaddress@gmail")
                .password("Password1")
                .build();

        assertThrows(IllegalArgumentException.class, () -> userMapper.convertFromDto(registrationRequest, role));
    }

    @Test
    void throwsInvalidCredentialsExceptionForPassword() {

        Role role = Role.builder()
                .id(1L)
                .name("USER").build();
        RegistrationRequest registrationRequest = RegistrationRequest.builder()
                .firstName("Cont")
                .lastName("Test")
                .email("emailaddress@gmail.com")
                .password("adasdasd123")
                .build();

        assertThrows(IllegalArgumentException.class, () -> userMapper.convertFromDto(registrationRequest, role));
    }

    @Test
    void throwsInvalidCredentialsExceptionForPassword2() {

        Role role = Role.builder()
                .id(1L)
                .name("USER").build();
        RegistrationRequest registrationRequest = RegistrationRequest.builder()
                .firstName("Cont")
                .lastName("Test")
                .email("emailaddress@gmail.com")
                .password(null)
                .build();

        assertThrows(IllegalArgumentException.class, () -> userMapper.convertFromDto(registrationRequest, role));
    }

    @Test
    void throwsConstraintViolationExceptionForNames() {

        Role role = Role.builder()
                .id(1L)
                .name("USER")
                        .build();
        roleRepository.save(role);

        RegistrationRequest registrationRequest = RegistrationRequest.builder()
                .firstName("₀₁₂")
                .lastName("Test")
                .email("asdas01213@yahoo123.com")
                .password("Password1")
                .build();

        User user = userMapper.convertFromDto(registrationRequest,role);
        user.setId(1L);
        userRepository.save(user);
        assertThrows(ConstraintViolationException.class, () -> userRepository.findAll());

    }

    @Test
    void throwsConstraintViolationExceptionForNames2() {

        Role role = Role.builder()
                .id(1L)
                .name("USER")
                .build();
        roleRepository.save(role);

        RegistrationRequest registrationRequest = RegistrationRequest.builder()
                .firstName(",。・:*:・゜’( ☻ ω ☻ )。・:*:・゜’")
                .lastName("Test")
                .email("asdas01213@yahoo123.com")
                .password("Password1")
                .build();

        User user = userMapper.convertFromDto(registrationRequest,role);
        user.setId(1L);
        userRepository.save(user);
        assertThrows(ConstraintViolationException.class, () -> userRepository.findAll());

    }

    @Test
    void throwsConstraintViolationExceptionForNames3() {

        Role role = Role.builder()
                .id(1L)
                .name("USER")
                .build();
        roleRepository.save(role);

        RegistrationRequest registrationRequest = RegistrationRequest.builder()
                .firstName("œ∑´®†¥¨ˆøπ“‘")
                .lastName("Test")
                .email("asdas01213@yahoo123.com")
                .password("Password1")
                .build();

        User user = userMapper.convertFromDto(registrationRequest,role);
        user.setId(1L);
        userRepository.save(user);
        assertThrows(ConstraintViolationException.class, () -> userRepository.findAll());

    }

    @Test
    void throwsConstraintViolationExceptionForNames4() {

        Role role = Role.builder()
                .id(1L)
                .name("USER")
                .build();
        roleRepository.save(role);

        RegistrationRequest registrationRequest = RegistrationRequest.builder()
                .firstName("Cont")
                .lastName("T<img src=x onerror=alert('hi') />")
                .email("asdas01213@yahoo123.com")
                .password("Password1")
                .build();

        User user = userMapper.convertFromDto(registrationRequest,role);
        user.setId(1L);
        userRepository.save(user);
        assertThrows(ConstraintViolationException.class, () -> userRepository.findAll());

    }

    @Test
    void throwsConstraintViolationExceptionForNames5() {

        Role role = Role.builder()
                .id(1L)
                .name("USER")
                .build();
        roleRepository.save(role);

        RegistrationRequest registrationRequest = RegistrationRequest.builder()
                .firstName("Cont")
                .lastName("1E02")
                .email("asdas01213@yahoo123.com")
                .password("Password1")
                .build();

        User user = userMapper.convertFromDto(registrationRequest,role);
        user.setId(1L);
        userRepository.save(user);
        assertThrows(ConstraintViolationException.class, () -> userRepository.findAll());

    }

    @Test
    void throwsConstraintViolationExceptionForNames6() {

        Role role = Role.builder()
                .id(1L)
                .name("USER")
                .build();
        roleRepository.save(role);

        RegistrationRequest registrationRequest = RegistrationRequest.builder()
                .firstName("123")
                .lastName("Cont")
                .email("asdas01213@yahoo123.com")
                .password("Password1")
                .build();
        User user = userMapper.convertFromDto(registrationRequest,role);
        user.setId(1L);
        userRepository.save(user);
        assertThrows(ConstraintViolationException.class, () -> userRepository.findAll());

    }

    @Test
    void throwsConstraintViolationExceptionForNames7() {

        Role role = Role.builder()
                .id(1L)
                .name("USER")
                .build();
        roleRepository.save(role);

        RegistrationRequest registrationRequest = RegistrationRequest.builder()
                .firstName("Cont")
                .lastName("Null")
                .email("asdas01213@yahoo123.com")
                .password("Password1")
                .build();
        assertThrows(IllegalArgumentException.class, () -> userMapper.convertFromDto(registrationRequest,role));

    }

    @Test
    void throwsConstraintViolationExceptionForNames8() {

        Role role = Role.builder()
                .id(1L)
                .name("USER")
                .build();
        roleRepository.save(role);

        RegistrationRequest registrationRequest = RegistrationRequest.builder()
                .firstName(null)
                .lastName("Cont")
                .email("asdas01213@yahoo123.com")
                .password("Password1")
                .build();
        assertThrows(IllegalArgumentException.class, () -> userMapper.convertFromDto(registrationRequest,role));

    }

    @Test
    void throwsConstraintViolationExceptionForNames9() {

        Role role = Role.builder()
                .id(1L)
                .name("USER")
                .build();
        roleRepository.save(role);

        RegistrationRequest registrationRequest = RegistrationRequest.builder()
                .firstName("Cont")
                .lastName(null)
                .email("asdas01213@yahoo123.com")
                .password("Password1")
                .build();
        assertThrows(IllegalArgumentException.class, () -> userMapper.convertFromDto(registrationRequest,role));

    }

    @Test
    void saveUserWithValidData(){
        Role role = Role.builder()
                .id(1L)
                .name("USER")
                .build();
        roleRepository.save(role);

        RegistrationRequest registrationRequest = RegistrationRequest.builder()
                .firstName("Cont")
                .lastName("Test")
                .email("mdaneluttie@oakley.com")
                .password("mR3<L<BJz+N")
                .build();

        User user = userMapper.convertFromDto(registrationRequest,role);
        user.setId(1L);
        User savedUser = userRepository.save(user);
        assertNotNull(savedUser);
    }

    @Test
    void saveUserWithValidData2(){
        Role role = Role.builder()
                .id(1L)
                .name("USER")
                .build();
        roleRepository.save(role);

        RegistrationRequest registrationRequest = RegistrationRequest.builder()
                .firstName("Cont-Test")
                .lastName("Test-Cont")
                .email("mdaneluttie@oakley.com")
                .password("mR3<L<BJz+N")
                .build();

        User user = userMapper.convertFromDto(registrationRequest,role);
        user.setId(1L);
        User savedUser = userRepository.save(user);
        assertNotNull(savedUser);
    }


}
