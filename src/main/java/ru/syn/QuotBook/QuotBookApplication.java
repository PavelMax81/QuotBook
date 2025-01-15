package ru.syn.QuotBook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.syn.QuotBook.models.Quote;
import ru.syn.QuotBook.services.QuoteService;

@SpringBootApplication
public class QuotBookApplication implements CommandLineRunner {
	@Autowired
	private static QuoteService service;

	public static void main(String[] args) {
		SpringApplication.run(QuotBookApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// Quote randQuote = service.getRandom();
	}
}
