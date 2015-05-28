package models;

import play.db.ebean.*;
import play.data.validation.Constraints.*;
import javax.persistence.*;


// NIE TWORZY TABELI W BAZIE...
@Entity
public class TestEntity extends Model {

    @Id
    public Integer id;

    @Required
    public String name;

}