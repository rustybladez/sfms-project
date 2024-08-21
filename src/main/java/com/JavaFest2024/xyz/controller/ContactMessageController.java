package com.JavaFest2024.xyz.controller;

import com.JavaFest2024.xyz.model.ContactMessage;
import com.JavaFest2024.xyz.service.ContactMessageService;
import com.JavaFest2024.xyz.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ContactMessageController {

    private final ContactMessageService contactMessageService;
    private final EmailService emailService;

    @Autowired
    public ContactMessageController(ContactMessageService contactMessageService, EmailService emailService) {
        this.contactMessageService = contactMessageService;
        this.emailService = emailService;
    }

    // Any user can send a message to Admin
    @PostMapping("/")
    public String sendContactMessage(@ModelAttribute ContactMessage contactMessage) {
        contactMessageService.saveContactMessage(contactMessage);
        return "home";
    }

    // Table view of messages in the Admin panel
    @GetMapping("/admin/features/MessageView")
    public String getAllContactMessages(Model model) {
        List<ContactMessage> contactMessages = contactMessageService.findAllContactMessages();
        model.addAttribute("contactMessages", contactMessages);
        return "admin/features/MessageView";
    }

    // View message content one by one
    @GetMapping("/admin/features/MessageView/view/{id}")
    public String getContactMessagesById(@PathVariable Long id, Model model) {
        ContactMessage contactMessage = contactMessageService.getContactMessageById(id);
        if (contactMessage != null) {
            model.addAttribute("contactMessage", contactMessage);
        }
        return "admin/features/MessageView";
    }

    // Delete message from admin side
    @PostMapping("/admin/features/MessageView/delete/{id}")
    public String deleteContactMessage(@PathVariable Long id) {
        contactMessageService.deleteContactMessage(id);
        return "redirect:/admin/features/MessageView";
    }

    // New method to handle reply
    @PostMapping("/admin/features/MessageView/reply")
    public String replyToMessage(@RequestParam Long id, @RequestParam String replyContent) {
        ContactMessage message = contactMessageService.getContactMessageById(id);
        if (message != null) {
            String subject = "Re: " + message.getSubject();
            boolean sent = emailService.sendEmail(message.getEmail(), subject, replyContent);
            if (sent) {
                // Optionally, you can save the reply in the database or update the message status
                return "redirect:/admin/features/MessageView?replySuccess=true";
            } else {
                return "redirect:/admin/features/MessageView?replyError=true";
            }
        }
        return "redirect:/admin/features/MessageView?replyError=true";
    }

    // New endpoint to get total message count  for AdminDashBoard
    @GetMapping("/api/messages/count")
    @ResponseBody
    public long getTotalMessageCount() {
        return contactMessageService.getTotalMessageCount();
    }
}