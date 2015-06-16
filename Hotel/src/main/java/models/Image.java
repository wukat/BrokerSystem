package models;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Created by magdalena on 01.06.15.
 */

@Entity
@Table(name = "images")
public class Image {

    @Id
    @GeneratedValue
    @Column(name = "image_id")
    private Integer imageId;

    @XmlTransient
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

//    @Column(name="content")
//    private String name;

    @XmlTransient
    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Integer getImageId() {

        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public Image() {
    }

    public Image(Room room) {
        this.room = room;
    }
}
