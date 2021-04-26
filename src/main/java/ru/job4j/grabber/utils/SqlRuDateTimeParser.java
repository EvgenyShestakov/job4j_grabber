package ru.job4j.grabber.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class SqlRuDateTimeParser implements DateTimeParser {
    private static final DateTimeFormatter FORMATTER_1 = DateTimeFormatter.ofPattern("dd MMM yy");
    private static final DateTimeFormatter FORMATTER_2 = DateTimeFormatter.ofPattern("dd MMM yy HH:mm");
    private static final Map<String,String> MONTHS = Map.ofEntries(Map.entry("янв", "Jan"),
            Map.entry("фев", "Feb"),
            Map.entry("мар", "Mar"), Map.entry("апр", "Apr"), Map.entry("май", "May"),
            Map.entry("июн", "Jun"), Map.entry("июл", "Jul"), Map.entry("авг", "Aug"),
            Map.entry("сен", "Sep"), Map.entry("окт", "Oct"), Map.entry("ноя", "Nov"),
            Map.entry("дек", "Dec"));

    @Override
    public LocalDateTime parse(String parse) {
        boolean flag = true;
        String string = null;
        if (parse.startsWith("сегодня")) {
            string = parse.replace(parse.substring(0, 8), LocalDateTime.now().
                    toLocalDate().format(FORMATTER_1));
            flag = false;
        } else if (parse.startsWith("вчера")) {
            string = parse.replace(parse.substring(0, 6), LocalDateTime.now().
                    toLocalDate().minusDays(1).format(FORMATTER_1));
            flag = false;
        }
        if (flag) {
            if (parse.length() < 16) {
                parse = 0 + parse;
            }
            String month = parse.substring(3, 6);
            String tmp = parse.replace(parse.substring(3, 6), MONTHS.get(month));
            string = tmp.replace(",", "");
        }
        return LocalDateTime.parse(string, FORMATTER_2);
    }
}
