package mjb.memorygame.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;

@MappedSuperclass
public class BaseEntity {

    @Id
    @GeneratedValue
    public Long id;
  
    @Column(name = "created_at")
    public Date createdAt;
  
    @PrePersist
    void createdAt() {
        this.createdAt = new Date();
    }

	public Long getId() {
		return id;
    }
    
	public void setId(Long id) {
		this.id = id;
	}
    
}