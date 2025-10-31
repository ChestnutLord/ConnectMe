package com.dimidev.userservice.service;

import com.dimidev.userservice.exception.NotFoundException;
import com.dimidev.userservice.model.User;
import com.dimidev.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository rep;

    public List<User> getAll() {
        return rep.findAll();
    }

    public User getById(Long id) {
        return rep.findById(id).orElseThrow(() -> new NotFoundException(id, User.class));
    }

    public User create(User user) {
        return rep.save(user);
    }

    public User update(User user) {
        return rep.save(user);
    }

    public void deleteById(Long id) {
        rep.deleteById(id);
    }
}
