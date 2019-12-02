package mjb.memorygame.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import mjb.memorygame.entities.Game;

public interface GameRepository extends JpaRepository<Game, Long> {}