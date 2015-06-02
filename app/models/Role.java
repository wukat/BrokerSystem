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
    private Client client;

    @Column(name="role")
    private String role;

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Role(){}
    public Role(Client client, String role){
        this.client = client;
        this.role = role;
    }
}
