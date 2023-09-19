package com.in28minutes.rest.webservices.restfulwebservices.jpa;

import com.in28minutes.rest.webservices.restfulwebservices.user.Post;
import com.in28minutes.rest.webservices.restfulwebservices.user.User;
import com.in28minutes.rest.webservices.restfulwebservices.user.UserDaoService;
import com.in28minutes.rest.webservices.restfulwebservices.user.UserNotFoundException;
import jakarta.validation.Valid;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
public class UserJpaResource {

    private UserRepository repository;
    private PostRepository postrepository;


    public UserJpaResource(UserRepository repository,PostRepository postrepository)  {
        this.repository=repository;
        this.postrepository=postrepository;
    }

    // GET / users
    @GetMapping("/jpa/users")
    public List<User> retrieveAllUser(){
        return repository.findAll();
    };

    // GET / users
    @GetMapping("/jpa/users/{id}")
    public EntityModel<Optional<User>> retrieveUser(@PathVariable int id){

        Optional<User> user=repository.findById(id);

        if(user==null){
            throw new UserNotFoundException("id : "+id);
        }

        // to include the link resources
        EntityModel<Optional<User>> entityModel=EntityModel.of(user);

        WebMvcLinkBuilder link= WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).retrieveAllUser());
        entityModel.add(link.withRel("all-users"));
        return entityModel;
    };


    @DeleteMapping("/jpa/users/{id}")
    public void deleteUser(@PathVariable int id){
        repository.deleteById(id);
    };

    @PostMapping("/jpa/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user){
        User savedUser=repository.save(user);
        // you have to create location header
        // /user/4  we should return the url which user created 
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").
                buildAndExpand(savedUser.getId()).toUri();
        return ResponseEntity.created(location).build();
    }


    @GetMapping("/jpa/users/{id}/posts")
    public List<Post> retrievePostsForUser(@PathVariable int id){

        // we are using Optional Here to handle null pointer exception
        Optional<User> user=repository.findById(id);

        if(user.isEmpty()){
            throw new UserNotFoundException("id:"+id);
        }
        return user.get().getPosts();
    }



    @PostMapping("/jpa/users/{id}/posts")
    public ResponseEntity<Post> createPostForUser(@PathVariable int id, @Valid @RequestBody Post post){
        Optional<User> user=repository.findById(id);

        if(user.isEmpty()){
            throw new UserNotFoundException("id:"+id);
        }

        post.setUser(user.get());

        Post savedPost=postrepository.save(post);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").
                buildAndExpand(savedPost.getId()).toUri();

        return ResponseEntity.created(location).build();
    }


}
