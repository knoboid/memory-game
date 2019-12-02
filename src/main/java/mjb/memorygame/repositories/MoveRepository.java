package mjb.memorygame.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import mjb.memorygame.entities.Move;

public interface MoveRepository extends JpaRepository<Move, Long> {}