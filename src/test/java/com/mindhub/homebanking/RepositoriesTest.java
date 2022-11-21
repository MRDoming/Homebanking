package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RepositoriesTest {

    @MockBean
    PasswordEncoder passwordEncoder;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CardRepository cardRepository;

    @Autowired
    ClientLoanRepository clientLoanRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    LoanRepository loanRepository;

    @Autowired
    TransactionRepository transactionRepository;


    @Test
    public void existAccounts(){
        List<Account> accounts = accountRepository.findAll();
        assertThat(accounts,is(not(empty())));
    }

    @Test
    public void totalNumberAccounts(){
        List<Account> accounts = accountRepository.findAll();
        assertThat(accounts.size(),is(not(16)));
    }

    @Test
    public void existAccountVIN001(){
        List<Account> accounts = accountRepository.findAll();
        assertThat(accounts, hasItem(hasProperty("number", is("VIN001"))));
    }

    @Test
    public void existCard(){
        List<Card> cards = cardRepository.findAll();
        assertThat(cards,is(not(empty())));
    }

    @Test
    public void existCardCvv289(){
        List<Card> cards = cardRepository.findAll();
        assertThat(cards, hasItem(hasProperty("cvv", is(289))));
    }

    @Test
    public void existClientLoans(){
        List<ClientLoan> clientLoans = clientLoanRepository.findAll();
        assertThat(clientLoans,is(not(empty())));
    }

    @Test
    public void existClientLoanStatus(){
        List<ClientLoan> clientLoans = clientLoanRepository.findAll();
        assertThat(clientLoans, hasItem(hasProperty("status")));
    }

    @Test
    public void existClients(){
        List<Client> clients = clientRepository.findAll();
        assertThat(clients,is(not(empty())));
    }

    @Test
    public void existClientMelba(){
        List<Client> clients = clientRepository.findAll();
        assertThat(clients, hasItem(hasProperty("firstName", is("Melba"))));
    }

    @Test
    public void existLoans(){
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans,is(not(empty())));
    }

    @Test
    public void existClientLoan(){
        List<ClientLoan> clientLoans = clientLoanRepository.findAll();
        assertThat(clientLoans,is(not(empty())));
    }

    @Test
    public void existPersonalLoan(){
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans, hasItem(hasProperty("name", is("Personal"))));
    }

    @Test
    public void existTransaction(){
        List<Transaction> transactions = transactionRepository.findAll();
        assertThat(transactions,is(not(empty())));
    }

    @Test
    public void existTransactionDescription(){
        List<Transaction> transactions = transactionRepository.findAll();
        assertThat(transactions, hasItem(hasProperty("description", is("Carpinteria"))));
    }
}
