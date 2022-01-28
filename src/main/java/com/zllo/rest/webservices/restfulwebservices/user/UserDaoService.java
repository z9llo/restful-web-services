package com.zllo.rest.webservices.restfulwebservices.user;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class UserDaoService {

    private static List<User> users = new ArrayList<>();
    private static Integer usersCount = 3;

    static {
        users.add(new User(1, "Joao", new Date()));
        users.add(new User(2, "Tiago", new Date()));
        users.add(new User(3, "Pedro", new Date()));
    }

    public List<User> findAll() {
        return users;
    }

    public User save(User user) {
        if (user.getId() == null) {
            user.setId(++usersCount);
        }

        users.add(user);
        return user;
    }

    public User findOne(Integer id) {
        for (User u: users) {
            if (u.getId().equals(id)) {
                return u;
            }
        }

        return null;
    }
}
