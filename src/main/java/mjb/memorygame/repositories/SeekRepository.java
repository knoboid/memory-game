package mjb.memorygame.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import mjb.memorygame.entities.Seek;

public interface SeekRepository extends JpaRepository<Seek, Long> {

    List<Seek> findBySeekerId(long id);

    void deleteBySeekerId(long id);

}