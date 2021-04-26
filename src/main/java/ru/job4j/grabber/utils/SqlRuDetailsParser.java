package ru.job4j.grabber.utils;

import org.jsoup.nodes.Element;
import ru.job4j.grabber.Post;
import java.time.LocalDateTime;

public class SqlRuDetailsParser {
    public Post details(Element td) {
        SqlRuDateTimeParser sqlRuDateTimeParser = new SqlRuDateTimeParser();
        Element href = td.child(0);
        String link = href.attr("href");
        String text = href.text();
        LocalDateTime created = sqlRuDateTimeParser.parse(td.parent().child(5).text());
        String name = td.parent().child(2).text();
        return new Post(name, text, link, created);
    }
}
