package com.section9.chatapp.repos;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.section9.chatapp.entities.Cookie;


@Repository
public interface CookieRepository extends JpaRepository<Cookie, UUID> {

	@Query(value = "SELECT * FROM COOKIE c WHERE c.COOKIE_ID = :cookieId", nativeQuery = true)
	Cookie findByCookieId( @Param("cookieId") UUID cookieId);

	@Query(value = "SELECT * FROM COOKIE c WHERE c.USER_ID = :contactId", nativeQuery = true)
	List<Cookie> findByContactId(@Param("contactId") UUID contactId);
	
}
