package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Card;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class AccountDTO {

    private long id;
    private String number;
    private LocalDateTime creationDate;
    private Double balance;

    private Set<TransactionDto> transaction = new HashSet<>();

    //private Set<CardDTO> cards = new HashSet<>();

    public AccountDTO(){}

    public AccountDTO(Account account){
        this.id = account.getId();
        this.number = account.getNumber();;
        this.creationDate = account.getCreationDate();
        this.balance = account.getBalance();
        this.transaction = account.getTransactions().stream().map(transaction -> new TransactionDto(transaction)).collect(Collectors.toSet());
        //this.cards = account.getCards().stream().map(card -> new CardDTO(card)).collect(Collectors.toSet());
    }

    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Set<TransactionDto> getTransaction() {
        return transaction;
    }

    public void setTransaction(Set<TransactionDto> transaction) {
        this.transaction = transaction;
    }

    /*public Set<CardDTO> getCards() {
        return cards;
    }

    public void setCards(Set<CardDTO> cards) {
        this.cards = cards;
    }*/

    @Override
    public String toString() {
        return "AccountDTO{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", creationDate=" + creationDate +
                ", balance=" + balance +
                '}';
    }
}
