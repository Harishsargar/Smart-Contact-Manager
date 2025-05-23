package com.scm.scm20.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.scm.scm20.entity.Contact;
import com.scm.scm20.entity.User;

@Repository
public interface ContactRepo extends JpaRepository<Contact, String> {

    // custom finder method
    // List<Contact> findByUser(User user);
    Page<Contact> findByUser(User user, Pageable pageable);


    // custom query method
    @Query("SELECT c FROM Contact c WHERE c.user.id=:userId")
    List<Contact> findByUserId(@Param("userId") String userId);


    // List<Contact> findByName(String name);
    // List<Contact> findByEmail(String email);
    // List<Contact> findByPhoneNumber(String phoneNumber);


    // search contact by name
    Page<Contact> findByUserAndNameContaining(User user, String nameKeyword, Pageable pageable);

    // search contact by email
    Page<Contact> findByUserAndEmailContaining(User user, String emailKeyword, Pageable pageable);

    // search contact by phone number
    Page<Contact> findByUserAndPhoneNumberContaining(User user, String phoneNumberKeyword, Pageable pageable);
}
