package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.service.AccountService;
import com.mindhub.homebanking.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    AccountService accountService;

    @Autowired
    ClientService clientService;

    @GetMapping("/accounts")
    public List<AccountDTO> getAccountsDTO(){
        return accountService.getAccountsDTO();
    }

    @GetMapping("/accounts/{id}")
    public AccountDTO getAccountDTO(@PathVariable Long id){
        return accountService.getAccountDTO(id);
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> newAccount(Authentication authentication) {

        Client client = clientService.getClientByEmail(authentication.getName());

        if(client != null) {
            if (clientService.getClientByEmail(authentication.getName()).getAccount().toArray().length >= 3) {
                return new ResponseEntity<>("Max accounts", HttpStatus.FORBIDDEN);
            }

            accountService.saveAccount(new Account("VIN" + getRandomNumber(10000000, 100000000), LocalDateTime.now(), 0.00, clientService.getClientByEmail(authentication.getName())));
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }


}
