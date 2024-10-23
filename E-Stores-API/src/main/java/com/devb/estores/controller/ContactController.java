package com.devb.estores.controller;

import com.devb.estores.model.Contact;
import com.devb.estores.service.ContactService;
import com.devb.estores.util.AppResponseBuilder;
import com.devb.estores.util.ResponseStructure;
import com.devb.estores.requestdto.ContactRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${app.base_url}")
@AllArgsConstructor
public class ContactController {

    private final ContactService contactService;
    private final AppResponseBuilder responseBuilder;

    @PostMapping("/addresses/{addressId}/contacts")
    public ResponseEntity<ResponseStructure<List<Contact>>> addContact(@RequestBody ContactRequest contactRequest, @PathVariable String addressId) {
        List<Contact> contacts = contactService.addContact(contactRequest, addressId);
        return responseBuilder.success(HttpStatus.CREATED, "Contact Added", contacts);
    }

    @PutMapping("/contacts/{contactId}")
    public ResponseEntity<ResponseStructure<List<Contact>>> updateContact(@RequestBody ContactRequest contactRequest, @PathVariable String contactId){
        List<Contact> contacts = contactService.updateContact(contactRequest, contactId);
        return responseBuilder.success(HttpStatus.OK, "Contact Updated", contacts);
    }

    @GetMapping("/contacts/{contactId}")
    public ResponseEntity<ResponseStructure<Contact>> getContactById(@PathVariable String contactId){
        Contact contact = contactService.getContactById(contactId);
        return responseBuilder.success(HttpStatus.FOUND, "Contact Found", contact);
    }

    @GetMapping("/addresses/{addressId}/contacts")
    public ResponseEntity<ResponseStructure<List<Contact>>> getContactsByAddress(@PathVariable String addressId){
        List<Contact> contacts = contactService.getContactsByAddress(addressId);
        return responseBuilder.success(HttpStatus.FOUND, "Contacts found by address", contacts);
    }

    @DeleteMapping("/contacts/{contactId}")
    public ResponseEntity<ResponseStructure<List<Contact>>> deleteContactById(@PathVariable String contactId){
        List<Contact> contacts = contactService.deleteContactById(contactId);
        return  responseBuilder.success(HttpStatus.OK, "Contact deleted", contacts);
    }
}
