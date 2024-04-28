package com.self.flipcart.repository;

import com.self.flipcart.model.Address;
import com.self.flipcart.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactRepo extends JpaRepository<Contact, String> {

    public List<Contact> findAllByAddress(Address address);
}
