package mjb.memorygame.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import mjb.memorygame.entities.Cards;

public interface CardsRepository extends JpaRepository<Cards, Long> {}