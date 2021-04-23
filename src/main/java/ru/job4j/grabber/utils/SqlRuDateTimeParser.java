package ru.job4j.grabber.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SqlRuDateTimeParser implements DateTimeParser {
    @Override
    public LocalDateTime parse(String parse) {
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd MMM yy");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd MMM yy HH:mm");
        boolean flag = true;
        String string = null;
        if (parse.startsWith("сегодня")) {
            string = parse.replace(parse.substring(0, 8), LocalDateTime.now().
                    toLocalDate().format(formatter1));
            flag = false;
        } else if (parse.startsWith("вчера")) {
            string = parse.replace(parse.substring(0, 6), LocalDateTime.now().
                    toLocalDate().minusDays(1).format(formatter1));
            flag = false;
        }
        if (flag) {
            if (parse.length() < 16) {
                parse = 0 + parse;
            }
            String day = parse.substring(3, 6);
            day = switch (day) {
                case ("янв") -> "Jan";
                case ("фев") -> "Feb";
                case ("мар") -> "Mar";
                case ("апр") -> "Apr";
                case ("май") -> "May";
                case ("июн") -> "Jun";
                case ("июл") -> "Jul";
                case ("авг") -> "Aug";
                case ("сеп") -> "Sep";
                case ("окт") -> "Oct";
                case ("ноя") -> "Nov";
                case ("дек") -> "Dec";
                default -> throw new IllegalStateException("Unexpected value: " + day);
            };
            String tmp = parse.replace(parse.substring(3, 6), day);
            string = tmp.replace(",", "");
        }
        return LocalDateTime.parse(string, formatter2);
    }
}
