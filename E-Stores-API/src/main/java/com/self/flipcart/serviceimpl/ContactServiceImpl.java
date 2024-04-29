package com.self.flipcart.serviceimpl;

import com.self.flipcart.mapper.ContactMapper;
import com.self.flipcart.model.Contact;
import com.self.flipcart.repository.AddressRepo;
import com.self.flipcart.repository.ContactRepo;
import com.self.flipcart.requestdto.ContactRequest;
import com.self.flipcart.service.ContactService;
import com.self.flipcart.util.ResponseStructure;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ContactServiceImpl implements ContactService {

    private ContactRepo contactRepo;
    private AddressRepo addressRepo;

    @Override
    public ResponseEntity<ResponseStructure<List<Contact>>> addContact(ContactRequest contactRequest, String addressId) {
        return addressRepo.findById(addressId).map(address -> {
            Contact contact = ContactMapper.mapToContactEntity(contactRequest, new Contact());
            contact.setAddress(address);
            contact = contactRepo.save(contact);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseStructure<List<Contact>>()
                    .setStatus(HttpStatus.CREATED.value())
                    .setMessage("Successfully saved contact")
                    .setData(contactRepo.findAllByAddress(contact.getAddress())));
        }).orElseThrow();
    }

    @Override
    public ResponseEntity<ResponseStructure<List<Contact>>> updateContact(ContactRequest contactRequest, String contactId) {
        return contactRepo.findById(contactId).map(c -> {
            Contact contact = contactRepo.save(ContactMapper.mapToContactEntity(contactRequest, c));
            return ResponseEntity.ok(new ResponseStructure<List<Contact>>()
                    .setStatus(HttpStatus.OK.value())
                    .setMessage("Successfully updated contact")
                    .setData(contactRepo.findAllByAddress(contact.getAddress())));
        }).orElseThrow();
    }

    @Override
    public ResponseEntity<ResponseStructure<Contact>> getContactById(String contactId) {
        return contactRepo.findById(contactId).map(contact -> ResponseEntity
                .status(HttpStatus.FOUND)
                .body(new ResponseStructure<Contact>()
                        .setStatus(HttpStatus.FOUND.value())
                        .setMessage("contact found")
                        .setData(contact))).orElseThrow();
    }

    @Override
    public ResponseEntity<ResponseStructure<List<Contact>>> getContactsByAddress(String addressId) {
        return addressRepo.findById(addressId).map(address -> new ResponseEntity<>(new ResponseStructure<List<Contact>>()
                .setStatus(HttpStatus.FOUND.value())
                .setMessage("contacts found b address")
                .setData(contactRepo.findAllByAddress(address)), HttpStatus.FOUND)).orElseThrow();

    }

    @Override
    public ResponseEntity<ResponseStructure<List<Contact>>> deleteContactById(String contactId) {
        return contactRepo.findById(contactId).map(contact -> {
            contactRepo.delete(contact);
            List<Contact> contacts = contactRepo.findAllByAddress(contact.getAddress());
            if (contacts.size() > 0) {
                if (!contacts.get(0).isPrimary()) {
                    contacts.get(0).setPrimary(true);
                    contactRepo.save(contacts.get(0));
                }
            }
            return ResponseEntity.ok(new ResponseStructure<List<Contact>>()
                    .setStatus(HttpStatus.OK.value())
                    .setMessage("Successfully deleted the contact")
                    .setData(contacts));
        }).orElseThrow();
    }

}
