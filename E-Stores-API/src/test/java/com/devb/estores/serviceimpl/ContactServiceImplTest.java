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

//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }

    @Test
    void addContact() {
        // Arrange
        String addressId = "123";
        ContactRequest contactRequest = ContactRequest.builder().build();
        Contact contact = Contact.builder().build();
        Contact otherContact = Contact.builder().build();
        Address mockAddress = Address.builder().addressId(addressId).build();

        // Mock
        when(addressRepo.findById(addressId)).thenReturn(Optional.of(mockAddress));
        when(contactMapper.mapToContactEntity(any(ContactRequest.class), any(Contact.class))).thenReturn(contact);
        when(contactRepo.save(contact)).thenReturn(contact);
        when(contactRepo.findAllByAddress(mockAddress)).thenReturn(List.of(contact, otherContact));

        // Act
        ResponseEntity<ResponseStructure<List<Contact>>> response = contactServiceImpl.addContact(contactRequest, addressId);

        // Assertion
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        // Verify the interaction with addressRepo.findById
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(addressRepo).findById(captor.capture());
        assertEquals(addressId, captor.getValue());
    }
}
