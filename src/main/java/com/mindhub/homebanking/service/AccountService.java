package com.mindhub.homebanking.service;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface AccountService {

    public List<AccountDTO> getAccountsDTO();

    public AccountDTO getAccountDTO(Long id);

    public void saveAccount(Account account);

    public Account getAccountByNumber(String string);

}
