package com.zllo.rest.webservices.restfulwebservices.post;

import com.zllo.rest.webservices.restfulwebservices.exception.EntityNotFoundException;
import com.zllo.rest.webservices.restfulwebservices.user.User;
import com.zllo.rest.webservices.restfulwebservices.user.UserRepository;
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
@RequestMapping("jpa")
public class PostController {

    private final PostResource service;
    private final UserRepository userService;

    @Autowired
    public PostController(PostResource service, UserRepository userService) {
        this.service = service;
        this.userService = userService;
    }

    @GetMapping(path = "/users/{id}/posts")
    public List<Post> retrieveUserPosts(@PathVariable Integer id) {
        return getUser(id).getPosts();
    }

    @PostMapping(path = "/users/{id}/posts")
    public ResponseEntity<User> savePost(@PathVariable Integer id, @Valid @RequestBody Post post) {
        post.setUser(getUser(id));
        this.service.save(post);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(post.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping(path = "/users/{user_id}/posts/{id}")
    public EntityModel<Post> retrievePost(@PathVariable(name = "user_id") Integer userId, @PathVariable(name = "id") Integer id) {
        Optional<Post> post = this.service.findById(id);
        if (post.isEmpty()) {
            throw new EntityNotFoundException(String.format("Post nao encontrado id: %d", id));
        }

        if (!post.get().getUser().getId().equals(userId)) {
            throw new EntityNotFoundException(String.format("Post id: %d não encontrado para o usuario id: %d", id, userId));
        }

        EntityModel<Post> model = EntityModel.of(post.get());
        WebMvcLinkBuilder linkToUserPosts = linkTo(methodOn(this.getClass()).retrieveUserPosts(userId));
        model.add(linkToUserPosts.withRel("all-posts"));

        return model;
    }

    private User getUser(Integer id) throws EntityNotFoundException {
        Optional<User> user = this.userService.findById(id);
        if (user.isEmpty()) {
            throw new EntityNotFoundException(String.format("Usuario nao encontrado id: %d", id));
        }

        return user.get();
    }

}
