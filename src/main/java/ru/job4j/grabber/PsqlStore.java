package ru.job4j.grabber;

import java.io.InputStream;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements  Store, AutoCloseable {
    private Connection cnn;

    public PsqlStore(Properties cfg) {
        ClassLoader loader = PsqlStore.class.getClassLoader();
        try (InputStream io = loader.getResourceAsStream("rabbit.properties")){
            cfg.load(io);
            Class.forName(cfg.getProperty("jdbc.driver"));
            String url = cfg.getProperty("jdbc.url");
            String login = cfg.getProperty("jdbc.login");
            String password = cfg.getProperty("jdbc.password");
            cnn = DriverManager.getConnection(url, login, password);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void save(Post post) throws SQLException {
        String text = post.getText();
        LocalDateTime date = post.getCreated();
        try(PreparedStatement specify = cnn.prepareStatement("select * from post where text = ?")) {
            specify.setString(1, text);
            ResultSet resultSet = specify.executeQuery();
            if (resultSet.next()) {
                if (!date.equals(resultSet.getTimestamp(5).toLocalDateTime())) {
                    try (PreparedStatement update = cnn.
                            prepareStatement("update post set created = ? where text = ?")) {
                        update.setObject(1, date);
                        update.setString(2, text);
                    }
                }
            } else {
                try(PreparedStatement ps = cnn.
                        prepareStatement("insert into post(name, text, link, created) values(?, ?, ?, ?)")) {
                    ps.setString(1, post.getName());
                    ps.setString(2, post.getText());
                    ps.setString(3, post.getLink());
                    ps.setObject(4, date);
                    ps.execute();
                }
            }
        }
    }

    @Override
    public List<Post> getAll() throws SQLException {
        List<Post> posts = new ArrayList<>();
        try (Statement statement = cnn.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM post");
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                String text = resultSet.getString(3);
                String link = resultSet.getString(4);
                LocalDateTime created = resultSet.getTimestamp(5).toLocalDateTime();
                Post post = new Post(name, text, link, created);
                post.setId(id);
                posts.add(post);
            }
            return posts;
        }
    }

    @Override
    public Post findById(String id) throws SQLException {
        Post post = null;
        try(PreparedStatement ps = cnn.
                prepareStatement("SELECT * FROM post WHERE id = ?")) {
            ps.setInt(1, Integer.parseInt(id));
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                int idPost = resultSet.getInt(1);
                String name = resultSet.getString(2);
                String text = resultSet.getString(3);
                String link = resultSet.getString(4);
                LocalDateTime created = resultSet.getTimestamp(5).toLocalDateTime();
                post = new Post(name, text, link, created);
                post.setId(idPost);
            }
        }
        return post;
    }

    @Override
    public void close() throws Exception {
        if (cnn != null) {
            cnn.close();
        }
    }

    public static void main(String[] args) throws SQLException {
        String text = "Уважаемые соискатели, приглашаем Вас в ТПК "
                + "на вакансию - Администратор баз данных в Отдел управления ИТ инфраструктурой.\n"
                + "ТПК - является генеральным подрядчиком компании "
                + "Газпромнефть по предоставлению услуг процессинга топливных,"
                + " бонусных и банковских пластиковых карт на АЗС.\n"
                + "Обязанности:\n"
                + "Поддержка работоспособности и управление СУБД, устранение инцидентов.\n"
                + "Администрирование СУБД в основном Oracle, в меньшей степени MS SQL.\n"
                + "Возможно в будущем MySQL, PostgreSQL.\n"
                + "Сопровождение БД: резервное копирование, решение инцидентов, работа с Support.\n"
                + "Участие в проектах по модернизации, оптимизации и развитию СУБД, переход на новые версии.\n"
                + "Требования:\n"
                + "—Опыт работы в должности Администратора БД Oracle не менее 3-х лет.\n"
                + "—Опыт администрирования СУБД Oracle, MS SQL .\n"
                + "—Ответственность, работа в команде";
        String link = "https://www.sql.ru/forum/1335672/vakansiya-programmist-postgresql-v-instat";
        Post post = new Post("Jax", text, link, LocalDateTime.now());
        PsqlStore psqlStore = new PsqlStore(new Properties());
        psqlStore.save(post);
        psqlStore.getAll().forEach(System.out::println);
        System.out.println(psqlStore.findById("2"));
    }
}
