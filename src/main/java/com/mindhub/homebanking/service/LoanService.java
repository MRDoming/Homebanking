package com.mindhub.homebanking.service;

import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.Loan;

import java.util.List;

public interface LoanService {

    public List<LoanDTO> getLoansDTO();

    Loan findLoanById(long id);

    void saveLoan(Loan loan);
}
