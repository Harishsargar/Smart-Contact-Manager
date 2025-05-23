package com.scm.scm20.services.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.scm.scm20.entity.Contact;
import com.scm.scm20.entity.User;
import com.scm.scm20.helper.ResourceNotFoundException;
import com.scm.scm20.repositories.ContactRepo;
import com.scm.scm20.services.ContactService;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactRepo contactRepo;

    @Override
    public Contact saveContact(Contact contact) {

        // contact id : have to generate
        String id = UUID.randomUUID().toString();
        contact.setId(id);
        return contactRepo.save(contact);
       
    }

    @Override
    public Contact updateContact(Contact newContact) {
        Contact oldContact = contactRepo.findById(newContact.getId()).orElseThrow(()-> new ResourceNotFoundException("Contact not found"));
        oldContact.setName(newContact.getName());
        oldContact.setEmail(newContact.getEmail());
        oldContact.setPhoneNumber(newContact.getPhoneNumber());
        oldContact.setAddress(newContact.getAddress());
        oldContact.setDescription(newContact.getDescription());
        oldContact.setFavorite(newContact.isFavorite());
        oldContact.setWebsiteLink(newContact.getWebsiteLink());
        oldContact.setLinkedInLink(newContact.getLinkedInLink());
        oldContact.setCloudinaryImageId(newContact.getCloudinaryImageId());
        oldContact.setPicture(newContact.getPicture());

        
        return contactRepo.save(oldContact);
    }

    @Override
    public List<Contact> getAllContacts() {
        return contactRepo.findAll();
    }



    @Override
    public Contact getContactById(String id) {
        return contactRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Contact not found"));
    }

    @Override
    public void deleteContact(String id) {
        Contact contact = contactRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Contact not found"));
        contactRepo.delete(contact);

    }

    @Override
    public List<Contact> searchContact(String name, String email, String phoneNumber) {
        // TODO Auto-generated method stub
        
        return null;
    }

    @Override
    public List<Contact> getAllContactsByUser(String userId) {
        return contactRepo.findByUserId(userId);
        
    }

    @Override
    public Page<Contact> getContactByUser(User user, int page, int size, String sortBy, String order) {
        Sort sort = order.equals("desc")? Sort.by(sortBy).descending(): Sort.by(sortBy).ascending();
        PageRequest pageable = PageRequest.of(page, size,sort);
        return contactRepo.findByUser(user, pageable);
    }

    @Override
    public Page<Contact> searchContactByUserAndName(User user,String nameKeyword, int page, int size, String sortBy, String order) {
        Sort sort = order.equals("desc")? Sort.by(sortBy).descending(): Sort.by(sortBy).ascending();
        PageRequest pageable = PageRequest.of(page, size,sort);
        return contactRepo.findByUserAndNameContaining(user,nameKeyword,pageable);
    }

    @Override
    public Page<Contact> searchContactByUserAndEmail(User user, String emailKeyword, int page, int size, String sortBy, String order) {
        Sort sort = order.equals("desc")? Sort.by(sortBy).descending(): Sort.by(sortBy).ascending();
        PageRequest pageable = PageRequest.of(page, size,sort);
        return contactRepo.findByUserAndEmailContaining(user, emailKeyword, pageable);        
    }

    @Override
    public Page<Contact> searchContactByUserAndPhoneNumber(User user, String phoneNumberKeyword, int page, int size, String sortBy, String order) {
        Sort sort = order.equals("desc")? Sort.by(sortBy).descending(): Sort.by(sortBy).ascending();
        PageRequest pageable = PageRequest.of(page, size,sort);
        return contactRepo.findByUserAndPhoneNumberContaining(user, phoneNumberKeyword, pageable);
    }



}
