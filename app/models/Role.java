package models;

import play.db.ebean.*;
import play.data.validation.Constraints.*;
import javax.persistence.*;


@Entity
@Table(name = "roles")
public class Role extends Model {

    @Id
    @OneToOne
//    @PrimaryKeyJoinColumn
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
