package com.zllo.rest.webservices.restfulwebservices.user;

import com.zllo.rest.webservices.restfulwebservices.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("jpa/users")
public class UserJPAResource {

    private final UserRepository service;

    @Autowired
    public UserJPAResource(UserRepository service) {
        this.service = service;
    }

    @GetMapping
    public List<User> retrieveAllUsers() {
        return this.service.findAll();
    }

    @GetMapping(path = "/{id}")
    public EntityModel<User> retrieveUser(@PathVariable Integer id) {
        EntityModel<User> model = EntityModel.of(getUser(id));
        WebMvcLinkBuilder linkToUsers = linkTo(methodOn(this.getClass()).retrieveAllUsers());
        model.add(linkToUsers.withRel("all-users"));

        return model;
    }

    @PostMapping
    public ResponseEntity<User> saveUser(@Valid @RequestBody User user) {
        User savedUser = this.service.save(user);

        URI location = ServletUriComponentsBuilder
                        .fromCurrentRequest().path("/{id}")
                        .buildAndExpand(savedUser.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping(path = "/{id}")
    public void deleteUser(@PathVariable Integer id) {
        this.service.deleteById(id);
    }

    private User getUser(Integer id) throws EntityNotFoundException {
        Optional<User> user = this.service.findById(id);
        if (user.isEmpty()) {
            throw new EntityNotFoundException(String.format("Usuario nao encontrado id: %d", id));
        }

        return user.get();
    }
}
