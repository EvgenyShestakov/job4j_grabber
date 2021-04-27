package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.Parse;
import ru.job4j.grabber.Post;
import ru.job4j.grabber.utils.SqlRuDateTimeParser;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SqlRuParse implements Parse {
    @Override
    public List<Post> list(String link) throws IOException {
        List<Post> posts = new ArrayList<>();
        SqlRuDateTimeParser sqlRuDateTimeParser = new SqlRuDateTimeParser();
        Document doc = Jsoup.connect(link).get();
        Elements row = doc.select(".postslisttopic");
        for (Element td : row) {
            Element href = td.child(0);
            String link1 = href.attr("href");
            String text = href.text();
            LocalDateTime created = sqlRuDateTimeParser.parse(td.parent().child(5).text());
            String name = td.parent().child(2).text();
            posts.add(new Post(name, text, link1, created));
        }
        return posts;
    }

    @Override
    public Post detail(String link) throws IOException {
        SqlRuDateTimeParser sqlRuDateTimeParser = new SqlRuDateTimeParser();
        Document doc = Jsoup.connect(link).get();
        Elements elements = doc.getElementsByClass("msgTable");
        LocalDateTime date = sqlRuDateTimeParser.
                parse(elements.last().getElementsByClass("msgFooter").text().substring(0, 16).trim());
        String text = elements.first().getElementsByClass("msgBody").last().text();
        String profile = elements.first().getElementsByClass("msgBody").first().child(0).attr("href");
        String name = elements.first().getElementsByClass("msgBody").first().child(0).text();
        return new Post(name, text, profile, date);
    }

    public static void main(String[] args) throws Exception {
        SqlRuDateTimeParser sqlRuDateTimeParser = new SqlRuDateTimeParser();
        for (int page = 1; page < 6; page++) {
            Document doc = Jsoup.connect(String.format("https://www.sql.ru/forum/job-offers/%d", page)).get();
            Elements row = doc.select(".postslisttopic");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy HH:mm");
            for (Element td : row) {
                Element href = td.child(0);
                System.out.println(href.attr("href"));
                System.out.println(href.text());
                System.out.println(sqlRuDateTimeParser.parse(td.parent().child(5).text()).format(formatter));
            }
        }
    }
}
