package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.service.AccountService;
import com.mindhub.homebanking.service.ClientService;
import com.mindhub.homebanking.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @Autowired
    AccountService accountService;

    @Autowired
    ClientService clientService;

    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<?> getTransaction (@RequestParam Double amount, @RequestParam String description,
                                       @RequestParam String originNum, @RequestParam String destinyNum,
                                       Authentication authentication){

        Client clientCurrent = clientService.getClientByEmail(authentication.getName());

        if (clientCurrent != null) {
            Account originAccount = accountService.getAccountByNumber(originNum);
            Account destinyAccount = accountService.getAccountByNumber(destinyNum);

            if (amount <= 0) {
                return new ResponseEntity<>("Missing amount", HttpStatus.FORBIDDEN);
            }

            if (description.isEmpty()) {
                return new ResponseEntity<>("Missing description", HttpStatus.FORBIDDEN);
            }

            if (originNum.isEmpty()) {
                return new ResponseEntity<>("Missing source account", HttpStatus.FORBIDDEN);
            }

            if (destinyNum.isEmpty()) {
                return new ResponseEntity<>("Missing destination account", HttpStatus.FORBIDDEN);
            }

            if (originNum.equals(destinyNum)) {
                return new ResponseEntity<>("The destination account is the same", HttpStatus.FORBIDDEN);
            }

            if (originAccount == null) {
                return new ResponseEntity<>("The origin account does not exist", HttpStatus.FORBIDDEN);
            }

            if (destinyAccount == null) {
                return new ResponseEntity<>("The destination account does not exist", HttpStatus.FORBIDDEN);
            }

            if (!originAccount.getCliente().getEmail().equals(authentication.getName())) {
                return new ResponseEntity<>("la cuenta no pertenece al cliente registrado", HttpStatus.FORBIDDEN);
            }

            if (originAccount.getBalance() < amount) {
                return new ResponseEntity<>("Insufficient balance", HttpStatus.FORBIDDEN);
            }


            originAccount.setBalance(originAccount.getBalance() - amount);
            destinyAccount.setBalance(destinyAccount.getBalance() + amount);


            accountService.saveAccount(originAccount);
            accountService.saveAccount(destinyAccount);

            Transaction transactionCredit = new Transaction(TransactionType.CREDITO, amount, description + " " + originAccount.getNumber(), LocalDateTime.now(), destinyAccount, destinyAccount.getBalance() + amount);
            Transaction transactionDebit = new Transaction(TransactionType.DEBITO, -amount, description + " " + destinyAccount.getNumber(), LocalDateTime.now(), originAccount, originAccount.getBalance() - amount);

            transactionService.saveTransaction(transactionDebit);
            transactionService.saveTransaction(transactionCredit);


            return new ResponseEntity<>("Success", HttpStatus.CREATED);

        } else {
            return new ResponseEntity<>("Missing client", HttpStatus.FORBIDDEN);
        }
    };

}
