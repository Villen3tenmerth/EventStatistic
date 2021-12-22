package statistic;

import java.util.Map;

public interface EventStatistic {
    void incEvent(String name);

    double getEventStatisticByName(String name);

    Map<String, Double> getAllEventsStatistic();

    void printStatistic();
}
