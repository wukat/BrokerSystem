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
    @Column(name="image_id")
    private Integer imageId;

    @ManyToOne
    @JoinColumn(name="room_id")
    private Room room;

    @Column(name="content")
    private String content;

    public Image(){}

    public Image(Room room) {
        this.room = room;
    }

    public Image(String content, Room room) {
        this.content = content;
        this.room = room;
    }
}
