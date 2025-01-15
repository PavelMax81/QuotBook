package ru.syn.QuotBook.models;
import jakarta.persistence.*;

@Entity
@Table(name = "\"Chats\"")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "chat_id", nullable = false)
    private Long chatId;

    @Column(name = "last_id", nullable = false)
    private Long lastId;

    public Long getId(){return id;}

    public void setId(Long id){this.id = id;}

    public Long getChatId(){return chatId;}

    public void setChatId(Long chatId){this.chatId = chatId;}

    public Long getLastId(){return lastId;}

    public void setLastId(Long lastId){this.lastId = lastId;}
}
