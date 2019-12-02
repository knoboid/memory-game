package mjb.memorygame.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import mjb.memorygame.entities.Seek;

public interface SeekRepository extends JpaRepository<Seek, Long> {}