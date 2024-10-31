package com.devb.estores.serviceimpl;

import com.devb.estores.mapper.ContactMapper;
import com.devb.estores.model.Address;
import com.devb.estores.model.Contact;
import com.devb.estores.repository.AddressRepo;
import com.devb.estores.repository.ContactRepo;
import com.devb.estores.requestdto.ContactRequest;
import com.devb.estores.util.ResponseStructure;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class ContactServiceImplTest {

    @MockBean
    private ContactRepo contactRepo;

    @MockBean
    private AddressRepo addressRepo;

    @MockBean
    private ContactMapper contactMapper;

    @Autowired
    private ContactServiceImpl contactServiceImpl;

    @Test
    void addContact() {
        // Arrange
        String addressId = "123";
        ContactRequest contactRequest = new ContactRequest();
        Address address = new Address();
        List<Contact> existingContacts = new ArrayList<>();
        address.setContacts(existingContacts);

        Contact contact = new Contact();
        List<Contact> savedContacts = List.of(contact);

        // Mock behavior
        when(addressRepo.findById(addressId)).thenReturn(Optional.of(address));
        when(contactMapper.mapToContactEntity(contactRequest, new Contact())).thenReturn(contact);
        when(contactRepo.save(contact)).thenReturn(contact);
        when(contactRepo.findAllByAddress(address)).thenReturn(savedContacts);

        // Act
        List<Contact> result = contactServiceImpl.addContact(contactRequest, addressId);

        // Assert
        assertEquals(savedContacts, result, "The result should match the list of saved contacts");
        verify(contactRepo).save(contact);
    }
}
