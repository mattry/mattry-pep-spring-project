package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.*;
import com.example.exception.InvalidInputException;
import com.example.exception.UserNotFoundException;
import com.example.repository.*;

@Service
public class MessageService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private MessageRepository messageRepository;

    public Message createMessage(Message message){
        
        // Check that user posted_by exists
        Optional<Account> accountOptional = accountRepository.findById(message.getPostedBy());
        if (!accountOptional.isPresent()){
            throw new UserNotFoundException("User with that id does not exist");
        }

        //Check that message text is valid
        if(message.getMessageText() == null || message.getMessageText().isBlank() || message.getMessageText().length() > 255) {
            throw new InvalidInputException("Invalid message text");
        }

        return messageRepository.save(message);

    }


    // Our API should be able to retrieve all messages.
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    // Our API should be able to retrieve a message by its ID.
    public Message getMessageById(int id) {

        // Check if message exists
        Optional<Message> messageOptional = messageRepository.findById(id);
        if (messageOptional.isPresent()) {
            return messageOptional.get();
        } else {
            throw new InvalidInputException("Invalid message ID");
        }
    }

    // Our API should be able to delete a message identified by a message ID.
    public void deleteMessageById(int id) {

        //check if message exists
        Optional<Message> messageOptional = messageRepository.findById(id);
        if (messageOptional.isPresent()) {
            messageRepository.delete(messageOptional.get());
        } else {
            throw new InvalidInputException("Invalid message ID");
        }
    }


    
}
