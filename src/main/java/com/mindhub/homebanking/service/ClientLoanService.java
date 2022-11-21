package com.mindhub.homebanking.service;

import com.mindhub.homebanking.dtos.ClientLoanDTO;
import com.mindhub.homebanking.models.ClientLoan;

import java.util.List;

public interface ClientLoanService {

    public List<ClientLoanDTO> getClientLoansDTO();

    public ClientLoan getClientLoanById(long id);

    public void saveClientLoan(ClientLoan clientLoan);

    public void deleteClientLoan(ClientLoan clientLoan);

}
