package ru.syn.QuotBook.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.syn.QuotBook.models.Chat;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    Optional<Chat> findByIdEquals(Long chatId);
}
