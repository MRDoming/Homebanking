package com.mindhub.homebanking;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository, CardRepository cardRepository) {
		return args -> {

			/*Client client1 = new Client("Melba", "Morel", "melba@mindhub.com", passwordEncoder.encode("a"));
			Client client2 = new Client("Copernico", "Dominguez", "admin@mindhub.com", passwordEncoder.encode("a"));
			Client client3 = new Client("Anastasia", "Dominguez", "anana@mindhub.com", passwordEncoder.encode("a"));
			Client client4 = new Client("Minerva", "Morelli", "minerva@mindhub.com", passwordEncoder.encode("a"));
			Client client5 = new Client("Marcelo", "Martinez", "marcelo@mindhub.com", passwordEncoder.encode("a"));
			Client client6 = new Client("Marcos", "Miguelez", "marco@mindhub.com", passwordEncoder.encode("a"));

			clientRepository.save(client1);
			clientRepository.save(client2);
			clientRepository.save(client3);
			clientRepository.save(client4);
			clientRepository.save(client5);
			clientRepository.save(client6);


			Account cuenta1 = new Account("VIN001", LocalDateTime.now(), 5000.0, client1);
			Account cuenta2 = new Account("VIN002", LocalDateTime.now().plusDays(1), 7500.0, client1);
			Account cuenta3 = new Account("VIN003", LocalDateTime.now(), 4000.0, client3);
			Account cuenta4 = new Account("VIN004", LocalDateTime.now(), 4000.0, client3);
			Account cuenta5 = new Account("VIN005", LocalDateTime.now(), 4000.0, client3);
			Account cuenta6 = new Account("VIN006", LocalDateTime.now(), 4000.0, client3);

			accountRepository.save(cuenta1);
			accountRepository.save(cuenta2);
			accountRepository.save(cuenta3);
			accountRepository.save(cuenta4);
			accountRepository.save(cuenta5);
			accountRepository.save(cuenta6);*/

			/*Transaction trans1 = new Transaction(TransactionType.DEBITO, -23.50, "Burger King", LocalDateTime.now(), cuenta1);
			Transaction trans2 = new Transaction(TransactionType.CREDITO, 275.50, "Zara", LocalDateTime.now(), cuenta1);
			Transaction trans3 = new Transaction(TransactionType.CREDITO, 9275.50, "Carpinteria", LocalDateTime.now(), cuenta1);


			Transaction trans4 = new Transaction(TransactionType.DEBITO, -563.50, "McDonalds", LocalDateTime.now(), cuenta2);
			Transaction trans5 = new Transaction(TransactionType.CREDITO, 895.50, "Swatch", LocalDateTime.now(), cuenta2);
			Transaction trans6 = new Transaction(TransactionType.DEBITO, -675.50, "Coto", LocalDateTime.now(), cuenta1);

			transactionRepository.save(trans1);
			transactionRepository.save(trans2);
			transactionRepository.save(trans3);
			transactionRepository.save(trans4);
			transactionRepository.save(trans5);
			transactionRepository.save(trans6);*/

			List<Integer> cuotasH = List.of(12, 24, 36, 48, 60);
			List<Integer> cuotasP = List.of(6, 12, 24);
			List<Integer> cuotasA = List.of(6, 12, 24, 36);


			Loan hipotecario = new Loan("Hipotecario", 500000.00, cuotasH, 1.20);
			Loan personal = new Loan("Personal", 100000.00, cuotasP, 1.30);
			Loan auntomotriz = new Loan("Automotriz", 300000.00, cuotasA, 1.10);

			loanRepository.save(hipotecario);
			loanRepository.save(personal);
			loanRepository.save(auntomotriz);

			/*ClientLoan prestMelva1 = new ClientLoan(400000.00, 60, client1, hipotecario);
			ClientLoan prestMelva2 = new ClientLoan(50000.00, 12, client1, personal);

			ClientLoan prestCoper1 = new ClientLoan(10000.00, 24, client2, personal);
			ClientLoan prestCoper2 = new ClientLoan(200000.00, 36, client2, auntomotriz);

			clientLoanRepository.save(prestMelva1);
			clientLoanRepository.save(prestMelva2);
			clientLoanRepository.save(prestCoper1);
			clientLoanRepository.save(prestCoper2);

			Card card1 = new Card(CardType.DEBIT, CardColor.GOLD, "1234-5678-9101-1121", 590, LocalDate.now(), LocalDate.now().plusMonths(60), client1);
			Card card2 = new Card(CardType.CREDIT, CardColor.TITANIUM, "1121-1109-8765-4321", 740, LocalDate.now(), LocalDate.now().plusMonths(60), client1);
			Card card3 = new Card(CardType.CREDIT, CardColor.SILVER, "4567-1234-7890-6789", 254, LocalDate.now(), LocalDate.now().plusMonths(60), client2);
			Card card4 = new Card(CardType.CREDIT, CardColor.SILVER, "4567-1234-7890-6789", 254, LocalDate.now(), LocalDate.now().plusMonths(60), client1);

			cardRepository.save(card1);
			cardRepository.save(card2);
			cardRepository.save(card3);
			cardRepository.save(card4);

			ClientDTO client45 = new ClientDTO();
			ClientDTO client46 = new ClientDTO(client1);

			System.out.println(client45);
			System.out.println(client46);*/

			Client client = new Client("Copernico", "Dominguez", "admin@mindhub.com", passwordEncoder.encode("a"));

			clientRepository.save(client);
			System.out.println(client.toString());

		};
	}

}
