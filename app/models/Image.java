package models;

import play.db.jpa.JPA;

import javax.persistence.*;

/**
 * Created by magdalena on 01.06.15.
 */

@Entity
@Table(name="images")
public class Image {

    @Id
    @GeneratedValue
    @Column(name="image_id")
    private Integer imageId;

    @Column(name="internal_image_id")
    private Integer internalImageId;

    @ManyToOne
    @JoinColumn(name="room_id")
    private Room room;

    @Column(name="content")
    private String content;

    public Image(){}

    public Image(Room room) {
        this.room = room;
    }

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public Integer getInternalImageId() {
        return internalImageId;
    }

    public void setInternalImageId(Integer internalImageId) {
        this.internalImageId = internalImageId;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Image(String content, Room room) {
        this.content = content;
        this.room = room;
    }

    public Image(Integer internalImageId, Room room, String content) {
        this.internalImageId = internalImageId;
        this.room = room;
        this.content = content;
    }

    public static Image getById(Integer imageId) {
        return JPA.em().find(Image.class, imageId);
    }

    @Override
    public String toString() {
        return "Image{" +
                "imageId=" + imageId +
                ", internalImageId=" + internalImageId +
                ", room=" + room +
                ", content='" + content + '\'' +
                '}';
    }
}
