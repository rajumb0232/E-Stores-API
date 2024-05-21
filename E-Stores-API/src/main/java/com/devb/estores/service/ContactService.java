package com.devb.estores.service;

import com.devb.estores.model.Contact;
import com.devb.estores.util.ResponseStructure;
import com.devb.estores.requestdto.ContactRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ContactService {
    ResponseEntity<ResponseStructure<List<Contact>>> addContact(ContactRequest contactRequest, String addressId);

    ResponseEntity<ResponseStructure<List<Contact>>> updateContact(ContactRequest contactRequest, String contactId);

    ResponseEntity<ResponseStructure<Contact>> getContactById(String contactId);

    ResponseEntity<ResponseStructure<List<Contact>>> getContactsByAddress(String addressId);

    ResponseEntity<ResponseStructure<List<Contact>>> deleteContactById(String contactId);
}
