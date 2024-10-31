package com.devb.estores.service;

import com.devb.estores.model.Contact;
import com.devb.estores.requestdto.ContactRequest;

import java.util.List;

public interface ContactService {
    List<Contact> addContact(ContactRequest contactRequest, String addressId);

    List<Contact> updateContact(ContactRequest contactRequest, String contactId);

    Contact getContactById(String contactId);

    List<Contact> getContactsByAddress(String addressId);

    List<Contact> deleteContactById(String contactId);
}
