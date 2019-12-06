package mjb.memorygame.services;

import org.springframework.stereotype.Service;

import mjb.memorygame.entities.Seek;

@Service
public class SeekServiceImpl implements SeekService {

    @Override
    public boolean isSeekAccepted(Seek seek) {
        return seek.getSeeker() != null && seek.getAccepter() != null;
    }

}