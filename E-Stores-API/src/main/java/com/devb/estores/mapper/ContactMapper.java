package com.devb.estores.mapper;

import com.devb.estores.model.Contact;
import com.devb.estores.requestdto.ContactRequest;
import com.devb.estores.responsedto.ContactResponse;

public class ContactMapper {

    private ContactMapper() {
        /*
         * Created private constructor to avoid Instantiation of class
         * */
    }

    public static Contact mapToContactEntity(ContactRequest contactRequest, Contact contact) {
        contact.setContactNumber(contactRequest.getContactNumber());
        contact.setContactName(contactRequest.getContactName());
        contact.setPrimary(contactRequest.isPrimary());
        return contact;
    }

    public static ContactResponse mapToContactResponse(Contact contact) {
        return contact != null ?
                ContactResponse.builder()
                        .contactId(contact.getContactId())
                        .contactName(contact.getContactName())
                        .contactNumber(contact.getContactNumber())
                        .isPrimary(contact.isPrimary())
                        .build()
                : null;
    }
}
