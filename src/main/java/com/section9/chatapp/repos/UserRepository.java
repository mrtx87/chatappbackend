package com.section9.chatapp.repos;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.section9.chatapp.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

	@Query(value = "SELECT * FROM USER u WHERE u.name = :name", nativeQuery = true)
	List<User> findByName( @Param("name") String name);
	

	
	@Query(value = "SELECT COUNT(ID) FROM USER u WHERE u.name = :name", nativeQuery = true)
	public int existsByName( @Param("name") String name);
	
	@Query(value = "SELECT * FROM USER u WHERE u.name = :name AND u.password = :pw", nativeQuery = true)
	public User getUserByLogin( @Param("name") String name, @Param("pw") String password);
}
