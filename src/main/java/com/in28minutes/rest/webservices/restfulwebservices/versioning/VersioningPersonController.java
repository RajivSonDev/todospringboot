package com.in28minutes.rest.webservices.restfulwebservices.versioning;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VersioningPersonController {

    @GetMapping("v1/person")
    public PersonV1 getFirstVersionOfPerson(){
        return new PersonV1("Bob Charlie");
    }

    @GetMapping("v2/person")
    public PersonV2 getSecondVersionOfPerson(){
        return new PersonV2(new Name("Raj","Sonawala"));
    }

    @GetMapping(path="/person",params = "version=1")
    public PersonV1 getPersonVersionOfPerson(){
        return new PersonV1("Bob Charlie");
    }


    @GetMapping(path="/person",params = "version=2")
    public PersonV2 getPersonSecondVersionOfPerson(){
        return new PersonV2(new Name("Raj","Sonawala"));
    }

    // with header for next version you just have to change th v2 in header
    @GetMapping(path="/person/accept",produces = "application/vnd.company.app-v1+json")
    public PersonV2 getFirstVersionOfRequestPerson(){
        return new PersonV2(new Name("Raj","Sonawala"));
    }



}
