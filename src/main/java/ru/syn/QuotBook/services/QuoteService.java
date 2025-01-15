package ru.syn.QuotBook.services;

import jakarta.transaction.Transactional;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.syn.QuotBook.models.Quote;
import ru.syn.QuotBook.repositories.QuoteRepository;

import java.util.*;

@Component
public class QuoteService {
    @Autowired
    BashParser parser;

    @Autowired
    QuoteRepository quoteRepository;

    public void clearQuoteRepository(){
        quoteRepository.deleteAll();
    }

    @NotNull
    public QuoteRepository getQuoteRepository() {
        return quoteRepository;
    }

    @Transactional
    public List<Quote> getPage(int pageCounter) {
        System.out.println("Searching all quotes from page # "+pageCounter+"...");
        List<Quote> quotes = parser.getPage(pageCounter);
        return quotes;
    }

    @Transactional
    public Quote getRandomQuoteFromPage(int pageCounter) {
        System.out.println("Searching a random quote from page # "+pageCounter+"...");
        Map.Entry<Long, String> quoteEntry = parser.getRandomQuoteFromPage(pageCounter);
        return getQuote(quoteEntry);
    }

    @Transactional
    public Quote getQuoteById(long id) {
        System.out.println("Searching the quote with id = "+id+"...");
        Map.Entry<Long, String> quoteEntry = parser.getById(id);
        return getQuote(quoteEntry);
    }

    @Transactional
    public Quote getRandomQuote() {
        System.out.println("Searching a random quote...");
        Map.Entry<Long, String> quoteEntry = parser.getRandom();
        return getQuote(quoteEntry);
    }

    @Transactional
    public Quote getLastQuote() {
        System.out.println("Searching the last quote...");
        Map.Entry<Long, String> quoteEntry = parser.getLast();
        return getQuote(quoteEntry);
    }

    @Transactional
    private Quote getQuote(Map.Entry<Long, String> quoteEntry) {
        if (quoteEntry == null) {
            return null;
        }
        Quote existingQuote = quoteRepository.findByQuoteIdEquals((long)quoteEntry.getKey());

        System.out.println("key: "+quoteEntry.getKey());
        System.out.println("value: "+quoteEntry.getValue());

        if (existingQuote != null) {
            System.out.println("existing quote id: " + existingQuote.getId());
            System.out.println("existing quote text: " + existingQuote.getText());
            return existingQuote;
        } else {
            Quote newQuote = new Quote();
            newQuote.setQuoteId(quoteEntry.getKey());
            newQuote.setText(quoteEntry.getValue());

            newQuote = quoteRepository.save(newQuote);
            System.out.println("new quote id: "+newQuote.getQuoteId());
            System.out.println("new quote text: "+newQuote.getText());

            return newQuote;
        }
    }

    @Transactional
    public void printAllQuotes(){
        ArrayList<Quote> allQuotes = (ArrayList<Quote>)quoteRepository.findAll();
        int i = 1;
        for(Quote quote : allQuotes){
            System.out.println("\nQuote #"+i++);
            System.out.println("id: "+quote.getId());
            System.out.println("quoteId: "+quote.getQuoteId());
            System.out.println("text: "+quote.getText());
        }
        System.out.println();
    }
}
