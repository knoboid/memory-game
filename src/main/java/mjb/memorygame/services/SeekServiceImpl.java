package mjb.memorygame.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mjb.memorygame.entities.Seek;
import mjb.memorygame.events.SeeksEventHandler;
import mjb.memorygame.repositories.SeekRepository;

@Service
public class SeekServiceImpl implements SeekService {

    private final static int SEEKS_KEEP_ALIVE_MINUTES = 20;

    @Autowired
    SeekRepository seeksRepository;

    @Autowired
	private SeeksEventHandler seeksEventHandler;

    @Override
    public boolean isSeekAccepted(Seek seek) {
        return seek.getSeeker() != null && seek.getAccepter() != null;
    }

    @Override
    public void removeOldSeeks() {
        boolean wereSeeksDeleted = false;
        List<Seek> seeks = seeksRepository.findAll();
        Date now = new Date();
        long id = -1;
        for(Seek seek : seeks) {
            long ageInMilis = now.getTime() - seek.getCreatedAt().getTime();
            if (ageInMilis > SEEKS_KEEP_ALIVE_MINUTES * 60 * 1000) {
                id = seek.id;
                seeksRepository.deleteById(id);
                wereSeeksDeleted = true;
            }
        }
        if (wereSeeksDeleted) {
            seeksEventHandler.deleteSeek(id);
        }
    }

}