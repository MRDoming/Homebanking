package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import com.mindhub.homebanking.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class LoanController {

    @Autowired
    LoanService loanService;

    @Autowired
    ClientService clientService;

    @Autowired
    AccountService accountService;

    @Autowired
    ClientLoanService clientLoanService;

    @Autowired
    TransactionService transactionService;

    @GetMapping("/loans")
    public List<LoanDTO> getLoansDTO() {
        return loanService.getLoansDTO();
    }

    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<?> addLoan(@RequestBody LoanApplicationDTO loanApplicationDTO, Authentication authentication) {

        //el cliente
        Client client = clientService.getClientByEmail(authentication.getName());

        //prestamos del cliente
        List<ClientLoan> listClientLoan = client.getLoan().stream().filter(item -> item.getLoan().getId() == loanApplicationDTO.getId() && (item.getStatus().matches("ok") || item.getStatus().matches("outstanding"))).collect(Collectors.toList());

        if (loanApplicationDTO.getId() == 0) {
            return new ResponseEntity<>("Missing id", HttpStatus.FORBIDDEN);
        }

        if (listClientLoan.size() >= 1) {
            return new ResponseEntity<>("Loan already in use", HttpStatus.FORBIDDEN);
        }

        //el prestamo
        Loan loan = loanService.findLoanById(loanApplicationDTO.getId());

        if(loan == null) {
            return new ResponseEntity<>("Invalid Loan", HttpStatus.FORBIDDEN);
        }

        //la cuenta
        Account account = accountService.getAccountByNumber(loanApplicationDTO.getAccount());


        if(loanApplicationDTO.getAccount().isEmpty()){
            return new ResponseEntity<>("Missing account", HttpStatus.FORBIDDEN);
        }

        //la cuenta del cliente
        Set<Account> setAccount = client.getAccount().stream().filter(item -> item.getNumber().equals(loanApplicationDTO.getAccount())).collect(Collectors.toSet());

        if (setAccount.isEmpty()) {
            return new ResponseEntity<>("Invalid Account", HttpStatus.FORBIDDEN);
        }

        if(loanApplicationDTO.getPayments() <= 0){
            return new ResponseEntity<>("Missing payments", HttpStatus.FORBIDDEN);
        }
        //cuotas
        List<Integer> listPayment = loan.getPayments().stream().filter(item -> item.equals(loanApplicationDTO.getPayments())).collect(Collectors.toList());

        if (listPayment.isEmpty()) {
            return new ResponseEntity<>("Invalid Payment", HttpStatus.FORBIDDEN);
        }

        if(loanApplicationDTO.getAmount() <= 0){
            return new ResponseEntity<>("Missing amount", HttpStatus.FORBIDDEN);
        }

        if(loanApplicationDTO.getAmount() > loan.getMaxAmount()){
            return new ResponseEntity<>("Max max amount", HttpStatus.FORBIDDEN);
        }

        if(client != null && account != null) {

            double finalAmount = 0;

            finalAmount = loanApplicationDTO.getAmount() * loan.getInterest();

            /*if(loan.getName().equals("Hipotecario")) {
                finalAmount = loanApplicationDTO.getAmount() * 1.2;
            } else if(loan.getName().equals("Personal")) {
                finalAmount = loanApplicationDTO.getAmount() * 1.13;
            } else if(loan.getName().equals("Automotriz")){
                finalAmount = loanApplicationDTO.getAmount() * 1.1;
            } else {
                finalAmount = loanApplicationDTO.getAmount() * loan.getInterest();
            }*/

            if(loanApplicationDTO.getAmount() < 100000){

                ClientLoan loan1 = new ClientLoan(finalAmount, loanApplicationDTO.getPayments(), client, loan);
                loan1.setCuenta(loanApplicationDTO.getAccount());

            clientLoanService.saveClientLoan(loan1);

            account.setBalance(account.getBalance() + loanApplicationDTO.getAmount());
            accountService.saveAccount(account);

            transactionService.saveTransaction(new Transaction(TransactionType.CREDITO, loanApplicationDTO.getAmount(), loan.getName() + " (prestamo aprobado)", LocalDateTime.now(), account, account.getBalance() + loanApplicationDTO.getAmount()));

            return new ResponseEntity<>("Success", HttpStatus.CREATED);

            } else{
                ClientLoan loan1 = new ClientLoan(finalAmount, loanApplicationDTO.getPayments(), client, loan);
                loan1.setStatus("outstanding");
                loan1.setCuenta(loanApplicationDTO.getAccount());

                clientLoanService.saveClientLoan(loan1);

                return new ResponseEntity<>("Manager request", HttpStatus.ACCEPTED);
            }

        } else {
            return new ResponseEntity<>("Fail", HttpStatus.FORBIDDEN);
        }

    }

    @PostMapping("/loan")
    public ResponseEntity<?> createLoan(@RequestParam String name, @RequestParam Double maxAmount, @RequestParam List<Integer> payments, @RequestParam Double interest, Authentication authentication) {
        Client admin = clientService.getClientByEmail(authentication.getName());

        if (admin != null && admin.getEmail().contains("admin")) {

            if(name.isEmpty()){
                return new ResponseEntity<>("Name is empty", HttpStatus.FORBIDDEN);
            }

            if(maxAmount <= 0){
                return new ResponseEntity<>("missing maxAmount", HttpStatus.FORBIDDEN);
            }

            if(payments.size() == 0){
                return new ResponseEntity<>("missing payments", HttpStatus.FORBIDDEN);
            }

            if(interest <= 0){
                return new ResponseEntity<>("missing interest", HttpStatus.FORBIDDEN);
            }

            Loan loan1 = new Loan(name, maxAmount, payments, interest);
            loanService.saveLoan(loan1);

            return new ResponseEntity<>("Loan created", HttpStatus.CREATED);
        }

        return new ResponseEntity<>("Fail create loan", HttpStatus.FORBIDDEN);
    }


}
