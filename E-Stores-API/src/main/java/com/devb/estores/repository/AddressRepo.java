package com.devb.estores.repository;

import com.devb.estores.model.Address;
import com.devb.estores.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepo extends JpaRepository<Address, String> {
    List<Contact> findContactsByAddressId(String addressId);
}
