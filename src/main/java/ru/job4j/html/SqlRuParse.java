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
            String link2 = href.attr("href");
            Post post = detail(link2);
            posts.add(post);
        }
        return posts;
    }

    @Override
    public Post detail(String link) throws IOException {
        SqlRuDateTimeParser sqlRuDateTimeParser = new SqlRuDateTimeParser();
        Document doc = Jsoup.connect(link).get();
        Elements elements = doc.getElementsByClass("msgTable");
        String tmpData = elements.last().getElementsByClass("msgFooter").text();
        String dataTime = tmpData.substring(0, (tmpData.indexOf("[") - 1));
        LocalDateTime data = sqlRuDateTimeParser.
                parse(dataTime);
        String heading = elements.first().getElementsByClass("messageHeader").text();
        String headingReplace = heading.replace(heading.substring(heading.length() - 6), "");
        String text = elements.first().getElementsByClass("msgBody").last().text();
        String titleText = String.format("%s%n%s", headingReplace, text);
        String linkToProfile = elements.first().getElementsByClass("msgBody").
                first().child(0).attr("href");
        String name = elements.first().getElementsByClass("msgBody").first().child(0).text();
        return new Post(name, titleText, linkToProfile, data);
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
