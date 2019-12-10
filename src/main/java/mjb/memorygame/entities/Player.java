package mjb.memorygame.entities;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Player extends BaseEntity {

    @Column(unique=true, length=30, nullable=false)
    private String name;

    public Player() {}

    public Player(String name) {
        setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}