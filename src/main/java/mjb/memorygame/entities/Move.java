package mjb.memorygame.entities;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Move extends BaseEntity {

    @Column
    private int cardIndex;

    public Move() {}
    
    public Move(int cardIndex) {
        setCardIndex(cardIndex);
    }

    public int getCardIndex() {
        return cardIndex;
    }

    public void setCardIndex(int cardIndex) {
        this.cardIndex = cardIndex;
    }
    
}