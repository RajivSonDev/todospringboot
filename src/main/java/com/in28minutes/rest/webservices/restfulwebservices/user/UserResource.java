package com.in28minutes.rest.webservices.restfulwebservices.user;

import jakarta.validation.Valid;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
public class UserResource {

    private UserDaoService service;

    public UserResource(UserDaoService service)  {
        this.service = service;
    }

    // GET / users
    @GetMapping("/users")
    public List<User> retrieveAllUser(){
        return service.findAll();
    };

    // GET / users
    @GetMapping("/users/{id}")
    public EntityModel<User> retrieveUser(@PathVariable int id){

        User user=service.findOne(id);

        if(user==null){
            throw new UserNotFoundException("id : "+id);
        }

        // to include the link resources
        EntityModel<User> entityModel=EntityModel.of(user);

        WebMvcLinkBuilder link= WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).retrieveAllUser());
        entityModel.add(link.withRel("all-users"));
        return entityModel;
    };


    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable int id){
        service.deleteById(id);
    };

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user){
        User savedUser=service.save(user);
        // you have to create location header
        // /user/4  we should return the url which user created 
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").
                buildAndExpand(savedUser.getId()).toUri();
        return ResponseEntity.created(location).build();
    }


}
