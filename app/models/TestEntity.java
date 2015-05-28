package models;

import play.db.ebean.*;
import play.data.validation.Constraints.*;
import javax.persistence.*;


// NIE TWORZY TABELI W BAZIE...
@Entity
public class TestEntity extends Model {

    public TestEntity() {}
    @Id
    @GeneratedValue
    public Integer id;

    @Required
    public String name = "MAGDA";

}