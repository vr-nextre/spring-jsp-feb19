package it.nextre.academy.springdemo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "email"))
@NoArgsConstructor
@Data
@AllArgsConstructor
public class User extends BaseEntity {

    private String firstName;
    private String lastName;
    private String email;
    private String password;

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;


    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='length=" + password.length() + '\'' +
                ", roles=" + roles +
                '}';
    }
}//end class
