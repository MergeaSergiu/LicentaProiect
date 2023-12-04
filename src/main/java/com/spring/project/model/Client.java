package com.spring.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

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


    public static Builder builder() {
        return new Builder();
    }

    public static class Builder{
        private final Client client = new Client();

        public Builder firstName(String firstName) {
            client.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            client.lastName = lastName;
            return this;
        }

        public Builder password(String password) {
            client.password = password;
            return this;
        }
        public Builder enable(Boolean enable) {
            client.enabled = enable;
            return this;
        }

        public Builder role(ClientRole role) {
            client.clientRole = role;
            return this;
        }

        public Builder email(String email) {
            client.email = email;
            return this;
        }

        public Client build() {
            // Validation logic, if any
            return client;
        }


    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(clientRole.name());
        return Collections.singleton(authority);
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
