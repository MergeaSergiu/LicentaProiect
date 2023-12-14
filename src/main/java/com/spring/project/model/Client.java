package com.spring.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name="_client")
public class Client implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @NotBlank(message = "First Name cannot be blank")
    private String firstName;

    @NotBlank(message = "Last Name cannot be blank")
    private String lastName;

    @NotBlank(message = "Email cannot be blank")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @Enumerated(EnumType.STRING)
    private ClientRole clientRole;
    private Boolean enabled = false;


    public Client(String firstName,
                  String lastName,
                  String email,
                  String password,
                  ClientRole clientRole) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.clientRole = clientRole;
    }

    public static class Builder{
        private final Client client;

        public Builder(String firstName,
                       String lastName,
                       String email,
                       String password,
                       ClientRole clientRole) {
            this.client = new Client();
            this.client.firstName = firstName;
            this.client.lastName = lastName;
            this.client.email = email;
            this.client.password = password;
            this.client.clientRole = clientRole;

        }
        public Client build() {
            // Validation logic, if any
            return client;
        }

    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + clientRole.name());
        return Arrays.asList(authority);
    }

    public ClientRole getClientRole(){
        return clientRole;
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
        return enabled;
    }
}
