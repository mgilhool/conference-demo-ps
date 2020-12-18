package com.pluralsight.conferencedemo.repositories;

import com.pluralsight.conferencedemo.models.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface SessionRepository extends JpaRepository<Session, Long> {

    @Modifying
    @Transactional(rollbackFor = Exception.class)  //needs to be the spring one - without it I was getting this:  javax.persistence.TransactionRequiredException Executing an update/delete query
    @Query(value="DELETE FROM session_speakers WHERE session_id = :sessionId", nativeQuery = true)
    //without nativeQuery=true I was getting:   org.hibernate.hql.internal.ast.QuerySyntaxException
    void removeSessionSpeakerRelation(@Param("sessionId") Long sessionId);

    @Modifying
    @Transactional(rollbackFor = Exception.class)
    @Query(value="DELETE FROM session_tags WHERE session_id = :sessionId", nativeQuery = true)
    void removeSessionTagRelation(@Param("sessionId") Long sessionId);

    @Modifying
    @Transactional(rollbackFor = Exception.class)
    @Query(value="DELETE FROM session_schedule WHERE session_id = :sessionId", nativeQuery = true)
    void removeSessionScheduleRelation(@Param("sessionId") Long sessionId);


}
