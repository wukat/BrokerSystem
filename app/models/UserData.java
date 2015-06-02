package models;


import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by magdalena on 01.06.15.
 */
@Entity
@Table(name = "user_data")
public class UserData implements Serializable {// extends Model {

    @Id
    @OneToOne
    private User user;

    @Column(name = "name")
    private String name;

    @Column(name = "telephone")
    private String telephone;

    @Column(name = "address")
    private String address;


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public UserData(){}
    public UserData(User user, String name, String telephone, String address){
        this.user = user;
        this.name = name;
        this.telephone = telephone;
        this.address = address;
    }

}