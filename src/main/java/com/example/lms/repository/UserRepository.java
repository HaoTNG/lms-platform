package com.example.lms.repository;

import com.example.lms.model.Mentee;
import com.example.lms.model.Tutor;
import com.example.lms.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
    @Query("select u from User u where type(u) = Mentee")
    List<User> findAllMentees();

    @Query("select u from User u where type(u) = Tutor")
    List<User> findAllTutors();


    Page<User> findAll(Specification<User> spec, Pageable pageable);
}
