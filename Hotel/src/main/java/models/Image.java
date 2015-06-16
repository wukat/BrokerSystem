package models;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Created by magdalena on 01.06.15.
 */

@Entity
@Table(name = "images")
public class Image {

    @Column(name = "image_id")
    private Integer imageId;

    @Id
    @GeneratedValue
    @Column(name="internal_image_id")
    private Integer internalImageId;

    @XmlTransient
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @Column(name="content")
    private String content;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getInternalImageId() {
        return internalImageId;
    }

    public void setInternalImageId(Integer internalImageId) {
        this.internalImageId = internalImageId;
    }

    public Image() {
    }

    public Image(Room room) {
        this.room = room;
    }

    public Image(Room room, String content) {
        this.room = room;
        this.content = content;
    }
}
