package com.pluralsight.conferencedemo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "speakers")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Speaker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="speaker_id")
    private Long speakerId;

    @Column(name="first_name")
    private String firstName;
    @Column(name="last_name")
    private String lastName;
    @Column(name="title")
    private String title;
    @Column(name="company")
    private String company;
    @Column(name="speaker_bio")
    private String speakerBio;

    @Lob
    @Type(type="org.hibernate.type.BinaryType")
    @Column(name="speaker_photo")
    private byte[] speakerPhoto;

    //other side (not owning side) of relationship
    @ManyToMany(mappedBy = "speakers")
    @JsonIgnore
    //added to prevent "back-serialization" recursion between speaker and session going deeper and deeper on the join
    //prior to this addition got error like: nested exception is com.fasterxml.jackson.databind.JsonMappingException: Infinite recursion (StackOverflowError) (through reference chain: org.hibernate.collection.internal.PersistentBag[0]
    // Can also address this with DTOs (somehow - not sure) but adding to the non-dominant side of the join also works.
    private Set<Session> sessions;

    public Speaker() {
        //Explicit default constructor
    }

    public Long getSpeakerId() {
        return speakerId;
    }

    public void setSpeakerId(Long speaker_id) {
        this.speakerId = speaker_id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String first_name) {
        this.firstName = first_name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String last_name) {
        this.lastName = last_name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getSpeakerBio() {
        return speakerBio;
    }

    public void setSpeakerBio(String speaker_bio) {
        this.speakerBio = speaker_bio;
    }

    public byte[] getSpeakerPhoto() {
        return speakerPhoto;
    }

    public void setSpeakerPhoto(byte[] speaker_photo) {
        this.speakerPhoto = speaker_photo;
    }

    public Set<Session> getSessions() {
        return sessions;
    }

    public void setSessions(Set<Session> sessions) {
        this.sessions = sessions;
    }
}
