package ru.job4j.grabber;

import java.sql.SQLException;
import java.util.List;

public interface Store {
    void save(Post post) throws SQLException;

    List<Post> getAll() throws SQLException;

    Post findById(String id) throws SQLException;
}