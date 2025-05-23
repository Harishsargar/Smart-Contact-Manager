package com.scm.scm20.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scm.scm20.entity.Contact;
import com.scm.scm20.services.ContactService;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private ContactService contactService;

    @RequestMapping("/contact/{contactId}")
    public Contact getContact(@PathVariable String contactId) {
        System.out.println("contactId: " + contactId);
        return contactService.getContactById(contactId);
    }

    @DeleteMapping("/contact/delete/{contactId}")
public ResponseEntity<String> deleteContact(@PathVariable String contactId) {
    try {
        contactService.deleteContact(contactId);
        return ResponseEntity.ok("Contact deleted successfully");
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete contact");
    }
}


}
