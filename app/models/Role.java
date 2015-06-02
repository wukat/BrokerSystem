package models;

import org.hibernate.annotations.*;
//import play.db.ebean.*;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "roles")
public class Role{// extends Model {
//TODO remove id from Role, UserData
    @Id
    @Column(name="email", unique=true, nullable=false)
    @GeneratedValue(generator="gen")
    @GenericGenerator(name="gen", strategy="foreign", parameters=@org.hibernate.annotations.Parameter(name="property", value="user"))
    private String email;

    @OneToOne
    @PrimaryKeyJoinColumn
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
