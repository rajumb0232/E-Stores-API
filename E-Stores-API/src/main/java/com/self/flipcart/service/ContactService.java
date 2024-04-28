package com.self.flipcart.service;

import com.self.flipcart.model.Contact;
import com.self.flipcart.requestdto.ContactRequest;
import com.self.flipcart.util.ResponseStructure;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ContactService {
    ResponseEntity<ResponseStructure<List<Contact>>> addContact(ContactRequest contactRequest, String addressId);

    ResponseEntity<ResponseStructure<List<Contact>>> updateContact(ContactRequest contactRequest, String contactId);

    ResponseEntity<ResponseStructure<Contact>> getContactById(String contactId);

    ResponseEntity<ResponseStructure<List<Contact>>> getContactsByAddress(String addressId);

    ResponseEntity<ResponseStructure<List<Contact>>> deleteContactById(String contactId);
}
