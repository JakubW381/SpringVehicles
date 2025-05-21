package dev.jakubw.rent.service;

import dev.jakubw.rent.dto.LoginRequest;
import dev.jakubw.rent.model.User;
import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> findAll();
    Optional<User> findByLogin(String login);
    void register(LoginRequest req);
    void deleteById(String id);
    void save(User user);


}
