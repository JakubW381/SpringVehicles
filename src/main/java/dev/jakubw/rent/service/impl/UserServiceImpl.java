package dev.jakubw.rent.service.impl;

import dev.jakubw.rent.dto.LoginRequest;
import dev.jakubw.rent.model.Role;
import dev.jakubw.rent.model.User;
import dev.jakubw.rent.repository.RoleRepository;
import dev.jakubw.rent.repository.UserRepository;
import dev.jakubw.rent.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private RoleRepository roleRepo;
    @Autowired
    private PasswordEncoder encoder;

    @Override
    public List<User> findAll() {
        return userRepo.findAll();
    }

    @Override
    public Optional<User> findByLogin(String login){
        return userRepo.findByLogin(login);
    }

    @Override
    public void register(LoginRequest req) {
        if(userRepo.findByLogin(req.getLogin()).isPresent()){
            throw new IllegalArgumentException("Username already exists");
        }
        Role userRole = roleRepo.findByName("ROLE_USER")
                .orElseThrow(() -> new IllegalStateException("There is no role ROLE_USER"));

        User u = User.builder()
                .id(UUID.randomUUID().toString())
                .login(req.getLogin())
                .password(encoder.encode(req.getPassword()))
                .roles(Set.of(userRole))
                .isSoftDeleted(false)
                .build();
        userRepo.save(u);
    }

    @Override
    public void deleteById(String id){
        User user = userRepo.findById(id)
                            .orElseThrow(() -> new IllegalArgumentException("No user with id: "+ id));
        user.setSoftDeleted(true);
        userRepo.save(user);
    }

    @Override
    public void save(User user){
        userRepo.save(user);
    }

}
