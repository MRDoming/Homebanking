package com.mindhub.homebanking.service.implement;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class AccountServiceImplement implements AccountService  {

    @Autowired
    AccountRepository accountRepository;

    @Override
    public List<AccountDTO> getAccountsDTO() {
        return accountRepository.findAll().stream().map(account -> new AccountDTO(account)).collect(toList());
    }

    @Override
    public AccountDTO getAccountDTO(Long id) {
        return accountRepository.findById(id).map(account -> new AccountDTO(account)).orElse(null);
    }

    @Override
    public void saveAccount(Account account) {
        accountRepository.save(account);
    }

    @Override
    public Account getAccountByNumber(String string) {
        return accountRepository.findByNumber(string);
    }
}
