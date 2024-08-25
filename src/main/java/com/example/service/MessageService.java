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

        // Check if message exists
        Optional<Message> messageOptional = messageRepository.findById(id);
        if (messageOptional.isPresent()) {
            messageRepository.delete(messageOptional.get());
        } else {
            throw new InvalidInputException("Invalid message ID");
        }
    }

    // Our API should be able to update a message text identified by a message ID.
    public Message updateMessage(int messageId, String messageText) {

        System.out.println("String input is: " + messageText);
        
        // Check if message exists
        Optional<Message> messageOptional = messageRepository.findById(messageId);
        if (messageOptional.isPresent()) {
            Message message = messageOptional.get();
            
            // Check if message_text is valid
            if (messageText == null || messageText.isEmpty() || messageText.length() > 255) {
                throw new InvalidInputException("Invalid message text");
            }
            
            message.setMessageText(messageText);
            return messageRepository.save(message);

        } else {
            throw new InvalidInputException("Invalid message ID");
        }
    }

    // Our API should be able to retrieve all messages written by a particular user.
    public List<Message> getMessagesByPoster(int id) {

        // Check if account exists
        Optional<Account> accountOptional = accountRepository.findById(id);
        if (accountOptional.isPresent()) {
            return messageRepository.findByPostedBy(id);
        }
        else {
            throw new InvalidInputException("Invalid user id");
        }
    }

    
}
