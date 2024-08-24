package com.example.service;

import com.example.entity.Account;
import com.example.exception.InvalidInputException;
import com.example.exception.UsernameTakenException;
import com.example.repository.AccountRepository;

import javassist.tools.web.BadHttpRequest;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    // Our API should be able to process new User registrations.
    public Account registerAccount(Account account) {

        // Check if an account with desired username exists 
        Optional<Account> accountOptional = accountRepository.findByUsername(account.getUsername());
        if(accountOptional.isPresent()){ 
            throw new UsernameTakenException("Username is already taken");
        }

        // Check if the username is valid
        if(account.getUsername() == null || account.getUsername().isBlank()){
            throw new InvalidInputException("Invalid username");
        }

        // Check if password is valid
        if(account.getPassword() == null || account.getPassword().isBlank() || account.getPassword().length() < 4){
            throw new InvalidInputException("Invalid password");
        }

        return accountRepository.save(account);
    }

}
