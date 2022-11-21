package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.service.AccountService;
import com.mindhub.homebanking.service.CardService;
import com.mindhub.homebanking.service.ClientService;
import com.mindhub.homebanking.utils.CardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CardController {

    @Autowired
    ClientService clientService;

    @Autowired
    CardService cardService;

    @Autowired
    AccountService accountService;


    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> newCard(Authentication authentication,
                                          @RequestParam CardColor color, @RequestParam CardType typo, @RequestParam String numAccount) {

        Client client = clientService.getClientByEmail(authentication.getName());

        if(client != null) {

            Set<Card> setCard = clientService.getClientByEmail(authentication.getName()).getCard().stream().filter(item -> item.getType() == typo).collect(Collectors.toSet());
            Set<Card> setCardColor = setCard.stream().filter(card -> card.getColor() == color).collect(Collectors.toSet());

            if (setCardColor.size() == 1) {
                return new ResponseEntity<>("Max cards color", HttpStatus.FORBIDDEN);
            }

            if (setCard.size() >= 3) {
                return new ResponseEntity<>("Max cards typo", HttpStatus.FORBIDDEN);
            }

            if(numAccount.equals("")){
                return new ResponseEntity<>("Missing account number", HttpStatus.FORBIDDEN);
            }

            String cardNumber = CardUtils.getCardNumber();

            int cvv = CardUtils.getCvv();

            cardService.saveCard(new Card(typo, color, cardNumber, cvv , LocalDate.now(), LocalDate.now().plusYears(5), clientService.getClientByEmail(authentication.getName()), numAccount));

            return new ResponseEntity<>(HttpStatus.CREATED);

        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

    }


}
