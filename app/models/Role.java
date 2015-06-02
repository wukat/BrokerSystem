package models;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;


@Entity
@Table(name = "roles")
public class Role implements Serializable {// extends Model {

    @Id
    @OneToOne
    private User user;

    @Column(name="role")
    private String role;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Role(){}
    public Role(User user, String role){
        this.user = user;
        this.role = role;
    }
}
