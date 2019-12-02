package mjb.memorygame.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import mjb.memorygame.entities.Player;

public interface PlayerRepository extends JpaRepository<Player, Long> {
}
