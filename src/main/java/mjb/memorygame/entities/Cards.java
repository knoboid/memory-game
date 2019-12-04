package mjb.memorygame.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity
public class Cards {

    @Id
    private long gameId;

    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    @JoinColumn(name="card_id", nullable=false)
    private List<Card> cards;

    public Cards() {}
    
    public Cards(long gameId, List<Integer> values) {
        this.gameId = gameId;
        cards = new ArrayList<Card>();
        for(int value : values) {
            cards.add(new Card(value));
        }
    }

    public long getId() {
        return gameId;
    }

    public void setId(long gameId) {
        this.gameId = gameId;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public List<Integer> asList() {
        List<Integer> cardsAsList = new ArrayList<Integer>();
        for(Card card : getCards()) {
            cardsAsList.add(card.getValue());
        }
        return cardsAsList;
    }

}