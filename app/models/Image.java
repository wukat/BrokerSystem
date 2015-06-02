package models;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by magdalena on 01.06.15.
 */

@Entity
@Table(name="images")
public class Image {

    @Id
    @GeneratedValue
    @Column(name="image_id")
    private Long imageId;

    @ManyToOne
    @JoinColumn(name="offer_id")
    private Offer offer;

    @Column(name="content")
    private Byte[] content;


    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }

    public Byte[] getContent() {
        return content;
    }

    public void setContent(Byte[] content) {
        this.content = content;
    }

    public Image(){}
    public Image(Offer offer, Byte[] content){
        this.offer = offer;
        this.content = content;
    }
}
