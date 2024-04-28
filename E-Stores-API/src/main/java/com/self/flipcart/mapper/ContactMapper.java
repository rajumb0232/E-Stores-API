package com.self.flipcart.mapper;

import com.self.flipcart.model.Contact;
import com.self.flipcart.requestdto.ContactRequest;
import com.self.flipcart.responsedto.ContactResponse;

public class ContactMapper {

    public static Contact mapToContactEntity(ContactRequest contactRequest, Contact contact) {
        contact.setContactNumber(contactRequest.getContactNumber());
        contact.setContactName(contactRequest.getContactName());
        contact.setPrimary(contactRequest.isPrimary());
        return contact;
    }

    public static ContactResponse mapToContactResponse(Contact contact){
        if(contact != null)
        return ContactResponse.builder()
                .contactId(contact.getContactId())
                .contactName(contact.getContactName())
                .contactNumber(contact.getContactNumber())
                .isPrimary(contact.isPrimary())
                .build();
        else return null;
    }
}
