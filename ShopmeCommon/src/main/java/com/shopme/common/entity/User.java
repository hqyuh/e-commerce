package com.shopme.common.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 128, nullable = false, unique = true)
    private String email;

    @Column(length = 64, nullable = false)
    private String password;

    @Column(name = "first_name", length = 45, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 45, nullable = false)
    private String lastName;

    @Column(length = 64)
    private String photos;
    private boolean enabled;

    // select object from database then it will not get related objects
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            // table trung gian
            name = "users_roles",
            // table hiện tại
            joinColumns = @JoinColumn(name = "user_id"),
            // column mapping với table hiện tại
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    // add role
    public void addRole(Role role){
        this.roles.add(role);
    }

    public User(String email, String password, String firstName, String lastName) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", roles=" + roles +
                '}';
    }

    @Transient
    public String getPhotosImagePath(){
        if(id == null || photos == null)
            return "/images/default-user.png";
        return "/user-photos/" + this.id + "/" + this.photos;
    }

    // full name
    @Transient
    public String getFullName(){
        return firstName + " " + lastName;
    }
}
