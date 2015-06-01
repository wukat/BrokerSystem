package models;

import java.sql.Date;

import javax.persistence.*;


@Entity
@Table(name="users")
public class User {

    @Id
    @Column(name="email")
    private String email;

    @Column(name="password")
    private String password;

    @OneToOne(mappedBy="user", cascade=CascadeType.ALL)
    @JoinColumn(name = "email")
    private Role role;

    @OneToOne(mappedBy="user", cascade=CascadeType.ALL)
    @JoinColumn(name = "email")
    private UserData userData;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public User() {}
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

}
