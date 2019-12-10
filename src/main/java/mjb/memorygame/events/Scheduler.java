package mjb.memorygame.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import mjb.memorygame.services.SeekService;

@Component
public class Scheduler {

    @Autowired
    SeekService seekService;

    @Scheduled(cron = " * * * * *")
    public void removeOldSeeks() {
        seekService.removeOldSeeks();
    }
    
}