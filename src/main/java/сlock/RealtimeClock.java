package сlock;

import java.time.Instant;

public class RealtimeClock implements Clock {
    @Override
    public Instant now() {
        return Instant.now();
    }
}
