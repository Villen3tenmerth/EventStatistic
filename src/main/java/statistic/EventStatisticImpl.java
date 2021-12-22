package statistic;

import сlock.Clock;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class EventStatisticImpl implements EventStatistic {
    private final Clock clock;
    private final Map<String, List<Instant>> events;

    public EventStatisticImpl(Clock clock) {
        this.clock = clock;
        events = new HashMap<>();
    }

    @Override
    public void incEvent(String name) {
        events.putIfAbsent(name, new ArrayList<>());
        events.get(name).add(clock.now());
    }

    @Override
    public double getEventStatisticByName(String name) {
        List<Instant> timestamps = events.getOrDefault(name, Collections.emptyList());
        Instant now = clock.now();
        int cnt = 0;
        for (int i = timestamps.size() - 1; i >= 0; i--) {
            if (timestamps.get(i).plus(1, ChronoUnit.HOURS).compareTo(now) <= 0) {
                break;
            }
            cnt++;
        }
        return cnt / 60.0;
    }

    @Override
    public Map<String, Double> getAllEventsStatistic() {
        Map<String, Double> res = new HashMap<>();
        for (String name : events.keySet()) {
            double rpm = getEventStatisticByName(name);
            if (rpm > 0) {
                res.put(name, rpm);
            }
        }
        return res;
    }

    @Override
    public void printStatistic() {
        Map<String, Double> statistic = getAllEventsStatistic();
        for (var entry : statistic.entrySet()) {
            String name = entry.getKey();
            Double rpm = entry.getValue();
            System.out.println("Event \"" + name + "\" happened with rpm = " + rpm);
        }
    }
}
