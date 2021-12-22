package statistic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import сlock.SetableClock;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class EventStatisticImplTest {
    private final static double EPS = 1e-6;
    private Instant startTime;
    private SetableClock clock;
    private EventStatistic eventStatistic;

    @BeforeEach
    public void beforeEach() {
        startTime = Instant.now();
        clock = new SetableClock(startTime);
        eventStatistic = new EventStatisticImpl(clock);
    }

    @Test
    void noEvents() {
        assertEquals(eventStatistic.getEventStatisticByName("name"), 0, EPS);
        assertTrue(eventStatistic.getAllEventsStatistic().isEmpty());
    }

    @Test
    void oneEvent() {
        eventStatistic.incEvent("event1");
        assertEquals(eventStatistic.getEventStatisticByName("event1"), 1.0 / 60, EPS);
        assertFalse(eventStatistic.getAllEventsStatistic().isEmpty());
    }

    @Test
    void oldEvents() {
        eventStatistic.incEvent("event1");
        eventStatistic.incEvent("event2");
        clock.setNow(startTime.plus(1, ChronoUnit.HOURS));
        eventStatistic.incEvent("event1");
        clock.setNow(clock.now().plus(30, ChronoUnit.MINUTES));
        assertEquals(eventStatistic.getEventStatisticByName("event1"), 1.0 / 60, EPS);
        assertFalse(eventStatistic.getAllEventsStatistic().containsKey("event2"));
    }

    @Test
    void simpleTest() {
        Map<String, Double> expected = new HashMap<>();
        final Double STEP = 1.0 / 60;
        for (int i = 0; i < 27; i++) {
            String name = "event" + (i % 10);
            expected.put(name, expected.getOrDefault(name, 0.0) + STEP);
            eventStatistic.incEvent(name);
            clock.setNow(startTime.plus(10 * i, ChronoUnit.SECONDS));
        }

        var result = eventStatistic.getAllEventsStatistic();
        for (var entry : expected.entrySet()) {
            assertTrue(result.containsKey(entry.getKey()));
            assertEquals(entry.getValue(), result.get(entry.getKey()), EPS);
        }
    }
}