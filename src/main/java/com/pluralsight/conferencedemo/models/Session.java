package com.pluralsight.conferencedemo.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

@Entity(name = "sessions")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
//added this to prevent this error: com.fasterxml.jackson.databind.exc.InvalidDefinitionException: No serializer found for class org.hibernate.proxy.pojo.bytebuddy.ByteBuddyInterceptor and no properties discovered to create BeanSerializer (to avoid exception, disable SerializationFeature.FAIL_ON_EMPTY_BEANS) (through reference chain: com.pluralsight.conferencedemo.models.Session$HibernateProxy$hEZsMzCI["hibernateLazyInitializer"])
//when using Hibernate, these properties are silently added to Entities, for Hibernate's convenience, but they
//will cause errors when serialized, so tell Jackson to ignore them when outputting the Json
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="session_id")
    private Long sessionId;
    @Column(name="session_name")
    private String sessionName;
    @Column(name="session_description")
    private String sessionDescription;
    @Column(name="session_length")
    private Integer sessionLength;

    //owner of the relationship
    @ManyToMany
    @JoinTable(
                name = "session_speakers",
                joinColumns = @JoinColumn(name = "session_id"),
                inverseJoinColumns = @JoinColumn(name = "speaker_id"))
    private Set<Speaker> speakers = new HashSet<>();

    public Session(){
        //Explicit default constructor
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long session_id) {
        this.sessionId = session_id;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String session_name) {
        this.sessionName = session_name;
    }

    public String getSessionDescription() {
        return sessionDescription;
    }

    public void setSessionDescription(String session_description) {
        this.sessionDescription = session_description;
    }

    public Integer getSessionLength() {
        return sessionLength;
    }

    public void setSessionLength(Integer session_length) {
        this.sessionLength = session_length;
    }

    public Set<Speaker> getSpeakers() {
        return speakers;
    }

    public void setSpeakers(SortedSet<Speaker> speakers) {
        this.speakers = speakers;
    }
}
