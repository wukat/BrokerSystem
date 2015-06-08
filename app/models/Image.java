package models;

import javax.persistence.*;

/**
 * Created by magdalena on 01.06.15.
 */

@Entity
@Table(name="images")
public class Image {

    @Id
    @GeneratedValue
    @Column(name="imageId")
    private Integer imageId;

    @ManyToOne
    @JoinColumn(name="offer_offerId")
    private Offer offer;

    @Column(name="content")
    private String name;

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }

    public String getName() {
        return name;
    }

    public void setName(String content) {
        this.name = content;
    }

    public Image(){}
    public Image(Offer offer, String name){
        this.offer = offer;
        this.name = name;
    }
}
