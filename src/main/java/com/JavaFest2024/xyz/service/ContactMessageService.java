package com.JavaFest2024.xyz.service;

import com.JavaFest2024.xyz.model.ContactMessage;
import com.JavaFest2024.xyz.repository.ContactMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ContactMessageService {

    @Autowired
    ContactMessageRepository contactMessageRepository;

    public List<ContactMessage> findAllContactMessages() {
        return contactMessageRepository.findAll();
    }

    public ContactMessage saveContactMessage(ContactMessage contactMessage) {
        if (contactMessage.getSendDate() == null) {
            contactMessage.setSendDate(LocalDate.now());
        }
        return contactMessageRepository.save(contactMessage);
    }

    public ContactMessage getContactMessageById(Long id) {
        return contactMessageRepository.findById(id).orElse(null);
    }

    public void deleteContactMessage(Long id) {
        contactMessageRepository.deleteById(id);
    }


    // New method to count total messages for AdminDashBoard
    public long getTotalMessageCount() {
        return contactMessageRepository.count();
    }

}
