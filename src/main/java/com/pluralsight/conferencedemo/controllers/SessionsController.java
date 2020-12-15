package com.pluralsight.conferencedemo.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.pluralsight.conferencedemo.models.Session;
import com.pluralsight.conferencedemo.repositories.SessionRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/sessions")
public class SessionsController {

    @Autowired
    private SessionRepository sessionRepository;

    @GetMapping
    public List<Session> list(){
        return sessionRepository.findAll();
    }

    @GetMapping
    @RequestMapping("{id}")  //composites this with the class RequestMapping
                            // This means that the path would be /api/v1/sessions/{id}
    public Session get(@PathVariable Long id){
        return sessionRepository.getOne(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) //Will return a 201 rather than a stock 200
    public Session create(@RequestBody final Session session){ //why final here  ? immutable ?
        return sessionRepository.saveAndFlush(session);  //using JPA when you save it doesn't get committed until the Flush, so this does both
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)  //Spring only provides specific post and get annotations, other are specified by "method" UPDATE new SpringBoto has @DeleteMapping and @PutMapping etc
    public void delete(@PathVariable Long id) {
        //In JPA Have to deal with Cascades -
        // if you try and delete a session with child records you will get a ForeignKeyConstraint violation
        //Also need to check fo children records before deleting.
        //How to handle ?  Homework
        //Will only delete Sessions with no children records - ie no assigned Speakers for now.
        sessionRepository.deleteById(id);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public Session update(@PathVariable Long id, @RequestBody Session session){
        //because this is a PUT, we expect all attributes to be passed in.  A PATCH would only need what you want to change
        //TODO: Add validation that all attributes are passed in, otherwise return a 400 bad payload
        Session existingSession = sessionRepository.getOne(id);
        BeanUtils.copyProperties(session, existingSession, "session_id"); //copy all of the attributes of the passed in Session to the existing session, ignoring the session_id of the passed in session since that will be null when passed in
        return sessionRepository.saveAndFlush(existingSession);

    }
}
