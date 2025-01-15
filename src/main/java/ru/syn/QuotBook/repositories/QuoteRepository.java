package ru.syn.QuotBook.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.syn.QuotBook.models.Quote;

@Transactional
public interface QuoteRepository extends JpaRepository<Quote, Integer> {
    Quote findByQuoteIdEquals(Long quoteId);
}
