package com.section9.chatapp.repos;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.section9.chatapp.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>{

}
