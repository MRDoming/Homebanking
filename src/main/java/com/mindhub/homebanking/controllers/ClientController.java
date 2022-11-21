package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    ClientService clientService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountRepository accountRepository;

                                                //entre el maximo y minimo, incluyendo el minimo
    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }


    @GetMapping("/clients")
    public List<ClientDTO> getClientsDTO() {
        return clientService.getClientsDTO();
    }


    @GetMapping("/clients/{id}")
    public ClientDTO getClientDTO(@PathVariable Long id){
        return clientService.getClientDTO(id);
    }


    @GetMapping("/clients/current")
    public ResponseEntity<?> getAuthenticatedClient(Authentication authentication) {
        Client client = clientService.getClientByEmail(authentication.getName());

        if(client != null) {
            ClientDTO clientCurrent = new ClientDTO(client);

            if (client.getAccount().size() >= 4) {
                clientCurrent.setStatus("Max-accounts");
                return new ResponseEntity<>(clientCurrent, HttpStatus.FORBIDDEN);
            }
            return new ResponseEntity<>(clientCurrent, HttpStatus.ACCEPTED);

        } else {
            return new ResponseEntity<>("Missing client", HttpStatus.FORBIDDEN);
        }
    }


    @PatchMapping("/clients/current")
    public ResponseEntity<?> editClient(Authentication authentication, @RequestParam String password, @RequestParam String email) {

        Client clientCurrent = clientService.getClientByEmail(authentication.getName());

        if(email.isEmpty() && password.isEmpty()) {
            return new ResponseEntity<>("No changes", HttpStatus.FORBIDDEN);
        }

        if(!email.isEmpty()){
            Client clientRegist = clientService.getClientByEmail(email);
            if (clientRegist != null) {
                return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);
            }
        }

        if (email.isEmpty()) {
            email = clientCurrent.getEmail();
        }

        if (password.isEmpty()) {
            password = clientCurrent.getPassword();
        }

        clientCurrent.setEmail(email);
        clientCurrent.setPassword(passwordEncoder.encode(password));
        clientService.saveClient(clientCurrent);
        return new ResponseEntity<>("Success", HttpStatus.CREATED);
    }

    @PostMapping("/clients")
    public ResponseEntity<Object> register(
            @RequestParam String firstName, @RequestParam String lastName,

            @RequestParam String email, @RequestParam String password) {

        if (firstName.isEmpty()) {
            return new ResponseEntity<>("Missing first name", HttpStatus.FORBIDDEN);
        }

        if(lastName.isEmpty()){
            return new ResponseEntity<>("Missing last name", HttpStatus.FORBIDDEN);
        }

        if(email.isEmpty()){
            return new ResponseEntity<>("Missing email", HttpStatus.FORBIDDEN);
        }

        if(password.isEmpty()){
            return new ResponseEntity<>("Missing password", HttpStatus.FORBIDDEN);
        }

        if (clientService.getClientByEmail(email) !=  null) {
            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);
        }

        clientService.saveClient(new Client(firstName, lastName, email, passwordEncoder.encode(password)));

        accountRepository.save(new Account("VIN"+ getRandomNumber(10000000,100000000), LocalDateTime.now(), 0.00, clientService.getClientByEmail(email)));

        return new ResponseEntity<>(HttpStatus.CREATED);

    }

}
