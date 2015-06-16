package models;


import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by magdalena on 01.06.15.
 */
@Entity
@Table(name = "client_data")
public class ClientData implements Serializable {// extends Model {

    @Id
    @OneToOne
    private Client client;

    @Column(name = "name")
    private String name;

    @Column(name = "telephone")
    private String telephone;

    @Column(name = "address")
    private String address;

    @Column(name="paymentData")
    private String paymentData;

    @Column(name="endpoint")
    private String endpoint;

    public String getPaymentData() {
        return paymentData;
    }

    public void setPaymentData(String paymentData) {
        this.paymentData = paymentData;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
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

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public ClientData(){}
    public ClientData(Client client, String name, String telephone, String address){
        this.client = client;
        this.name = name;
        this.telephone = telephone;
        this.address = address;
    }

}