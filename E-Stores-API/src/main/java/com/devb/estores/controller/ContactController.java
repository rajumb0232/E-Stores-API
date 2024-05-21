package com.devb.estores.controller;

import com.devb.estores.model.Contact;
import com.devb.estores.service.ContactService;
import com.devb.estores.util.ResponseStructure;
import com.devb.estores.requestdto.ContactRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${app.base_url}")
@AllArgsConstructor
public class ContactController {

    private ContactService contactService;

    @PostMapping("/addresses/{addressId}/contacts")
    public ResponseEntity<ResponseStructure<List<Contact>>> addContact(@RequestBody ContactRequest contactRequest, @PathVariable String addressId) {
        return contactService.addContact(contactRequest, addressId);
    }

    @PutMapping("/contacts/{contactId}")
    public ResponseEntity<ResponseStructure<List<Contact>>> updateContact(@RequestBody ContactRequest contactRequest, @PathVariable String contactId){
        return contactService.updateContact(contactRequest, contactId);
    }

    @GetMapping("/contacts/{contactId}")
    public ResponseEntity<ResponseStructure<Contact>> getContactById(@PathVariable String contactId){
        return contactService.getContactById(contactId);
    }

    @GetMapping("/addresses/{addressId}/contacts")
    public ResponseEntity<ResponseStructure<List<Contact>>> getContactsByAddress(@PathVariable String addressId){
        return contactService.getContactsByAddress(addressId);
    }

    @DeleteMapping("/contacts/{contactId}")
    public ResponseEntity<ResponseStructure<List<Contact>>> deleteContactById(@PathVariable String contactId){
        return contactService.deleteContactById(contactId);
    }
}
