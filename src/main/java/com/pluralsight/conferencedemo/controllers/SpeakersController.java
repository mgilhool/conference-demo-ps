package com.pluralsight.conferencedemo.controllers;

import com.pluralsight.conferencedemo.models.Speaker;
import com.pluralsight.conferencedemo.repositories.SpeakerRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/speakers")
public class SpeakersController {

    @Autowired
    SpeakerRepository speakerRepository;

    @GetMapping
    public List<Speaker> list() {
        return speakerRepository.findAll();
    }

    @GetMapping
    @RequestMapping("{id}")
    public Speaker get(@PathVariable Long id){
        return speakerRepository.getOne(id);
    }

    @PostMapping
    public Speaker create(@RequestBody final Speaker newSpeaker){
        return speakerRepository.saveAndFlush(newSpeaker);
    }

    @DeleteMapping(value = "{id}")
    public void delete(@PathVariable Long id){
        speakerRepository.deleteById(id);
    }

    @PutMapping(value = "{id}") //combining the two annotations above into one *might* not be correct
    public Speaker update(@PathVariable Long id, @RequestBody final Speaker updatedSpeaker){
        Speaker existingSpeaker = speakerRepository.getOne(id);
        BeanUtils.copyProperties(updatedSpeaker, existingSpeaker, "speaker_id");
        return speakerRepository.saveAndFlush(existingSpeaker); //now has updated props

    }


}
