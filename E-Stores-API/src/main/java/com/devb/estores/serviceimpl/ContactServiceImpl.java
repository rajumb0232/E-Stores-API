package com.devb.estores.serviceimpl;

import com.devb.estores.mapper.ContactMapper;
import com.devb.estores.model.Address;
import com.devb.estores.model.Contact;
import com.devb.estores.repository.AddressRepo;
import com.devb.estores.repository.ContactRepo;
import com.devb.estores.requestdto.ContactRequest;
import com.devb.estores.service.ContactService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ContactServiceImpl implements ContactService {

    private final ContactRepo contactRepo;
    private final AddressRepo addressRepo;
    private final ContactMapper contactMapper;

    @Override
    public List<Contact> addContact(ContactRequest contactRequest, String addressId) {
        return addressRepo.findById(addressId).map(address -> {
            /* Checking if the address already has up to 2 contacts
             */
            if (address.getContacts().size() == 2)
                throw new RuntimeException();

            Contact contact = contactMapper.mapToContactEntity(contactRequest, new Contact());
            contact.setAddress(address);
            contact = contactRepo.save(contact);
            return contactRepo.findAllByAddress(contact.getAddress());
        }).orElseThrow();
    }

    @Override
    public List<Contact> updateContact(ContactRequest contactRequest, String contactId) {
        return contactRepo.findById(contactId).map(c -> {
            Contact contact = contactRepo.save(contactMapper.mapToContactEntity(contactRequest, c));
            return contactRepo.findAllByAddress(contact.getAddress());
        }).orElseThrow();
    }

    @Override
    public Contact getContactById(String contactId) {
        return contactRepo.findById(contactId).orElseThrow();
    }

    @Override
    public List<Contact> getContactsByAddress(String addressId) {
        return addressRepo.findById(addressId).map(Address::getContacts).orElseThrow();
    }

    @Override
    public List<Contact> deleteContactById(String contactId) {
        return contactRepo.findById(contactId).map(contact -> {

            /* Deleting the contact
             */
            contactRepo.delete(contact);

            /* Finding the contacts based on the address and updating the other address to
            primary if present
             */
            return contactRepo.findAllByAddress(contact.getAddress())
                    .stream().map(exContact -> {
                        exContact.setPrimary(true);
                        return contactRepo.save(exContact);
                    })
                    .toList();

        }).orElseThrow();
    }

}
