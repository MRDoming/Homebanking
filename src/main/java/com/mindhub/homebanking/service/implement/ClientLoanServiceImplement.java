package com.mindhub.homebanking.service.implement;

import com.mindhub.homebanking.dtos.ClientLoanDTO;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.repositories.ClientLoanRepository;
import com.mindhub.homebanking.service.ClientLoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class ClientLoanServiceImplement implements ClientLoanService {

    @Autowired
    ClientLoanRepository clientLoanRepository;

    @Override
    public List<ClientLoanDTO> getClientLoansDTO() {
        return clientLoanRepository.findAll().stream().map(clientLoan -> new ClientLoanDTO(clientLoan)).collect(toList());
    }

    @Override
    public ClientLoan getClientLoanById(long id) {
        return clientLoanRepository.findById(id).orElse(null);
    }

    @Override
    public void saveClientLoan(ClientLoan clientLoan) {
        clientLoanRepository.save(clientLoan);
    }

    @Override
    public void deleteClientLoan(ClientLoan clientLoan) {
        clientLoanRepository.delete(clientLoan);
    }
}
