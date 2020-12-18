package com.pluralsight.conferencedemo.controllers;

import com.pluralsight.conferencedemo.models.Session;
import com.pluralsight.conferencedemo.repositories.SessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/sessions")
public class SessionsController {

    //Use Spring's default logging
    Logger logger = LoggerFactory.getLogger(SessionsController.class);
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
        //Will only delete Sessions with no children records - ie no assigned Speakers for now.  COMMENTING OUT
        //sessionRepository.deleteById(id);

        //when we delete a session we want to first delete the relationships that use that session_id (session_speakers, session_tag, session_schedule)
        //then once those are deleted, we can delete the actual session without running in to a foreign key constraint issue.
        //remove relations
        sessionRepository.removeSessionSpeakerRelation(id);
        sessionRepository.removeSessionTagRelation(id);
        sessionRepository.removeSessionScheduleRelation(id);
        //finally delete session.
        sessionRepository.deleteById(id);

        //^ There is probably a better *cleaner* way to do this rather than with Native Queries, but don't know enough about using cascades
        // this looks promising tho:https://stackoverflow.com/questions/43235303/how-to-delete-a-row-in-join-table-with-jpa
        // this also good reference https://www.baeldung.com/spring-data-jpa-delete
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public ResponseEntity<Session> update(@PathVariable Long id, @RequestBody Session session){
        //because this is a PUT, we expect all attributes to be passed in.  A PATCH would only need what you want to change
        //Added validation that all attributes are passed in, otherwise return a 400 bad payload - see below for implementation

        //changed from getOne to findOne because getOne is lazily evaluated, meaning it will not actually initialize the object until it it first used, so .getOne(id) will not equal null for an non-existant record,
        //instead it will throw a EntityNotFoundException when the object is accessed.   If you wanted to with get one it would be something like:
//        Session existingSessionWithGetOne = sessionRepository.getOne(id);
//        try {
//            BeanUtils.copyProperties(session,existingSessionWithGetOne, "sessionId");
//            // do validation etc, finally save the record with:
//            sessionRepository.saveAndFlush(existingSessionWithGetOne);
//        }
//        catch(FatalBeanException e){
//            //BeanUtils.copyProperties will cause this to be thrown when existingSessionWithGetOne is accessed
//            //Looking at the nested exception you can see that the NESTED Exception is an EntityNotFoundException
//            logger.error("Cannot process update. Session with id {} does not exist", id, e);
//            //return unprocessable entity indicating that no record is present.
//            return ResponseEntity.unprocessableEntity().build();
//        }

        Optional<Session> existingSession = sessionRepository.findById(id);
        logger.info("Session update method");

        //If the session id does not exist in the first place return a 422 unprocessible Entity
        if (existingSession.isEmpty()) return ResponseEntity.unprocessableEntity().build();
        Session actualExistingSession = existingSession.get();

        //Use reflection to see if incoming session has any null properties (other than the ones we expect).  If it does, then reject the payload and return a 400
        if (areAnyPropertiesNull(session, List.of("sessionId", "speakers"))) return ResponseEntity.status(400).body(null);
        else{
            //copy all of the attributes of the passed in Session to the existing session, ignoring the session_id of the passed in session since that will be null when passed in
            BeanUtils.copyProperties(session, actualExistingSession, "sessionId");
            //save the update and return the response
            return ResponseEntity.ok().body(sessionRepository.saveAndFlush(actualExistingSession));
        }

    }

    private boolean areAnyPropertiesNull(Object obj, List<String> ignoreFieldList)
    {
        //strip out fields we are going to ignore.  For example, in the case of Session sessionId won't be in the payload, and for now, let's ignore updating the speakers associated with the session
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        List<Field> declaredFieldsWithoutIgnored = Arrays.stream(declaredFields).filter(field -> !ignoreFieldList.contains(field.getName())).collect(Collectors.toList());

       //Allow access to all private fields
        for (Field f: declaredFieldsWithoutIgnored)
            f.setAccessible(true);
        List<Field> nullFields = declaredFieldsWithoutIgnored.stream().filter(fieldValue -> {
            try {
                return Objects.isNull(fieldValue.get(obj));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return false; //filter out any fields that throw and IllegalAccessException
            }
        }).collect(Collectors.toList());
        //if nullIFields is NOT empty then we have a property that is null that should not be
        return !nullFields.isEmpty();

    }

}
