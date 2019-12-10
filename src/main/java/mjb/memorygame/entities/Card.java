package mjb.memorygame.entities;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Card extends BaseEntity {

    @Column
    private int value;

    public Card() {}

    public Card(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

}