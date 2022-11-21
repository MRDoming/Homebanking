package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.PaymentsApplicationDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.service.AccountService;
import com.mindhub.homebanking.service.CardService;
import com.mindhub.homebanking.service.ClientService;
import com.mindhub.homebanking.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class PaymentsController {

    @Autowired
    ClientService clientService;

    @Autowired
    CardService cardService;

    @Autowired
    AccountService accountService;

    @Autowired
    TransactionService transactionService;

    @Transactional
    @PostMapping("/payments")
    public ResponseEntity<?> createPayment(@RequestBody PaymentsApplicationDTO paymentsApplicationDTO) {

        if(paymentsApplicationDTO.getNumber().equals("")){
            return new ResponseEntity<>("Missing card number", HttpStatus.FORBIDDEN);
        }
        String string1 = paymentsApplicationDTO.getNumber().substring(0,4);
        String string2 = paymentsApplicationDTO.getNumber().substring(4,8);
        String string3 = paymentsApplicationDTO.getNumber().substring(8,12);
        String string4 = paymentsApplicationDTO.getNumber().substring(12,16);

        String numCard = string1 + "-" + string2 + "-" + string3 + "-" + string4;
        Card card = cardService.getCardByNumber(numCard);
        Account account = accountService.getAccountByNumber(card.getNumAccount());

        if (card != null) {

            if(paymentsApplicationDTO.getCvv() == 0){
                return new ResponseEntity<>("Missing cvv", HttpStatus.FORBIDDEN);
            }

            if(!card.getCvv().equals(paymentsApplicationDTO.getCvv())){
                return new ResponseEntity<>("wrong cvv", HttpStatus.FORBIDDEN);
            }

            if(paymentsApplicationDTO.getAmount() <= 0) {
                return new ResponseEntity<>("Missing amount", HttpStatus.FORBIDDEN);
            }

            if(paymentsApplicationDTO.getDescription().equals("")){
                return new ResponseEntity<>("Missing description", HttpStatus.FORBIDDEN);
            }

            if(paymentsApplicationDTO.getAmount() > account.getBalance() && card.getType().equals(CardType.DEBIT)) {

                return new ResponseEntity<>("insufficient balance", HttpStatus.FORBIDDEN);
            }

            if(card.getType().equals(CardType.DEBIT)){

                account.setBalance(account.getBalance() - paymentsApplicationDTO.getAmount());
                accountService.saveAccount(account);

                Transaction transaction = new Transaction(TransactionType.DEBITO, -paymentsApplicationDTO.getAmount(), "DEBITO " + paymentsApplicationDTO.getDescription(), LocalDateTime.now(), account, account.getBalance() - paymentsApplicationDTO.getAmount());
                transactionService.saveTransaction(transaction);


                return new ResponseEntity<>("payment debit ok", HttpStatus.ACCEPTED);

            } else if(card.getType().equals(CardType.CREDIT)){

                Transaction transaction = new Transaction(TransactionType.CREDITO, paymentsApplicationDTO.getAmount(), "CREDITO " + paymentsApplicationDTO.getDescription(), LocalDateTime.now(), account, account.getBalance() + paymentsApplicationDTO.getAmount());
                transactionService.saveTransaction(transaction);

                return new ResponseEntity<>("payment credit ok", HttpStatus.ACCEPTED);
            }
        }


        return new ResponseEntity<>("wrong card", HttpStatus.FORBIDDEN);

    }

    }
