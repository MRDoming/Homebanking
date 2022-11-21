package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientLoanDTO;
import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import com.mindhub.homebanking.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class ClientLoanController {

    @Autowired
    AccountService accountService;

    @Autowired
    ClientLoanService clientLoanService;

    @Autowired
    TransactionService transactionService;

    @Autowired
    ClientService clientService;

   @Autowired
    LoanService loanService;

    @GetMapping("/clientloan")
    public List<ClientLoanDTO> getClientLoansDTO() {
        return clientLoanService.getClientLoansDTO();
    }

    @Transactional
    @PatchMapping("/clientloan")
    public ResponseEntity<?> addClientLoan (@RequestParam long id, @RequestParam String status){

        ClientLoan clientLoan = clientLoanService.getClientLoanById(id);
        clientLoan.setStatus(status);
        clientLoanService.saveClientLoan(clientLoan);

        Account accountLoan = accountService.getAccountByNumber(clientLoan.getCuenta());

        if(status.equals("rejected")){
            return new ResponseEntity<>("Loan rejected", HttpStatus.ACCEPTED);
        }

        if(status.equals("ok")) {

            double finalAmount = 0;

            finalAmount = clientLoan.getAmount() / clientLoan.getLoan().getInterest();

            accountLoan.setBalance(accountLoan.getBalance() + finalAmount);
            accountService.saveAccount(accountLoan);

            transactionService.saveTransaction(new Transaction(TransactionType.CREDITO, finalAmount, clientLoan.getNameLoan() + " (prestamo aprobado)", LocalDateTime.now(), accountLoan,accountLoan.getBalance() + finalAmount));

            return new ResponseEntity<>("Loan aprobed", HttpStatus.CREATED);
        }

        return new ResponseEntity<>("fail", HttpStatus.FORBIDDEN);
    }

    @PatchMapping("/clientloanelim")
    public ResponseEntity<?> deleteClientLoan (@RequestParam long id) {
            ClientLoan clientLoan = clientLoanService.getClientLoanById(id);
            clientLoan.setStatus("deleted");
            clientLoanService.saveClientLoan(clientLoan);
        return new ResponseEntity<>("Loan deleted", HttpStatus.CREATED);
    }


}
