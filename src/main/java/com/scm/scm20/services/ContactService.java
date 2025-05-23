package com.scm.scm20.services;

import java.util.List;

import org.springframework.data.domain.Page;

import com.scm.scm20.entity.Contact;
import com.scm.scm20.entity.User;

public interface ContactService {
    // save contact
    Contact saveContact(Contact contact);
    // update contact
    Contact updateContact(Contact contact);
    //  get all contacts
    List<Contact> getAllContacts();
    // get contact by id
    Contact getContactById(String id);
    // delete contact
    void deleteContact(String id);
    //  seach contact
    List<Contact> searchContact(String name, String email, String phoneNumber);
    // search contact by name
    Page<Contact> searchContactByUserAndName(User user,String nameKeyword,int page, int size, String sortBy, String order);
    // search conact by email
    Page<Contact> searchContactByUserAndEmail(User user,String emailKeyword,int page, int size, String sortBy, String order);
    // search contact by phone number
    Page<Contact> searchContactByUserAndPhoneNumber(User user,String phoneNumberKeyword,int page, int size, String sortBy, String order);
    // get all contacts by user (userId)
    List<Contact> getAllContactsByUser(String userId); 
    // get all contacts by user 
    Page<Contact> getContactByUser(User user, int page, int size, String sortBy, String order);

}
