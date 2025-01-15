package ru.syn.QuotBook.controllers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.springframework.stereotype.Service;
import ru.syn.QuotBook.models.Chat;
import ru.syn.QuotBook.models.Quote;
import ru.syn.QuotBook.repositories.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import ru.syn.QuotBook.services.QuoteService;

@Service
public class BotController {
    @Autowired
    ChatRepository chatRepository;

    @Autowired
    QuoteService service;

    private final TelegramBot bot;

    private final String botToken = "6849029042:AAHWTSpX9GO9Z8il0DK2DKS1BRIwU_CcHOs";

    private final String botUserName = "JavaTest20240520Bot";

    private Chat chat;
    private boolean botIsStarted = true;
    private long MAX_QUOTE_ID;

    public BotController() {
        // Create your bot passing the token received from @BotFather
        bot = new TelegramBot(botToken);

        chat = new Chat();
        chat.setLastId(0l);
        chat.setId(1l);

        // Register for updates
        bot.setUpdatesListener(updates -> {
            for(Update update:updates){
                handleUpdate(update);
            }
            // ... process updates
            // return id of last processed update or confirm them all
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    private void handleUpdate(Update update) {
        if(botIsStarted) {
            MAX_QUOTE_ID = calcMaxQuoteId();
            botIsStarted = false;
        }

        String text = update.message().text();
        long chatId = update.message().chat().id();
/*
        Optional<Chat> rawChat = chatRepository.findByChatIdEquals(chatId);
        Chat chat;

        if(rawChat.isEmpty()){
            Chat _chat = new Chat();
            _chat.setChatId(chatId);
            _chat.setLastId(0);
            _chat.setId(chatRepository.count() + 1l);
            System.out.println(_chat.getId()+" "+_chat.getChatId()+" "+_chat.getLastId());
            chat = chatRepository.save(_chat);
        } else {
            chat = rawChat.get();
        }
 */
        chat.setChatId(chatId);
        boolean quoteIsSend = false;

        switch (text){
            case "/start":
                quoteIsSend = sendFirstQuote(chat);
                break;
            case "/next":
                quoteIsSend = sendNextQuote(chat);
                break;
            case "/prev":
                quoteIsSend = sendPrevQuote(chat);
                break;
            case "/rand":
                quoteIsSend = sendRandomQuote(chat);
                break;
            case "/last":
                quoteIsSend = sendLastQuote(chat);
                break;
            case "/all":
                service.printAllQuotes();
                break;
        }
        if(!quoteIsSend && isNaturalNumber(text)){
            int pageNumber = Integer.parseInt(text);
            sendRandomQuoteFromPage(chat, pageNumber);
        }
        System.out.println("Last quote id: "+chat.getLastId());
        System.out.println("The number of saved quotes in repository: "+service.getQuoteRepository().count());
    }

    private long calcMaxQuoteId(){
        Quote lastQuote = service.getLastQuote();
        long lastQuoteId = lastQuote.getQuoteId();
        System.out.println("The last quote id: "+lastQuoteId);
        return lastQuoteId;
    }

    private boolean isNaturalNumber(String text) {
        if(text.length() == 0)
            return false;

        for(int i = 0; i < text.length(); i++){
            char symbol = text.charAt(i);

            if(!"1234567890".contains(symbol+"")){
                return false;
            } else {
                if(i == 0 && symbol == '0')
                    return false;
            }
        }
        return true;
    }

    private void sendRandomQuoteFromPage(Chat chat, int pageNumber){
        Quote randomQuote = service.getRandomQuoteFromPage(pageNumber);
        sendText(chat.getChatId(), randomQuote.getText());
        chat.setLastId(randomQuote.getQuoteId());
    }

    private boolean sendRandomQuote(Chat chat) {
        Quote quote = service.getRandomQuote();
        sendText(chat.getChatId(), quote.getText());
        return true;
    }

    private boolean sendLastQuote(Chat chat) {
        Quote quote = service.getLastQuote();
        this.chat.setLastId(quote.getQuoteId());
        sendText(chat.getChatId(), quote.getText());
        return true;
    }

    private boolean sendPrevQuote(Chat chat) {
        Quote quote = null;
        long newId = chat.getLastId();

        while(quote == null){
            newId--;
            if(newId < 1l){
                newId = MAX_QUOTE_ID;
            }
            quote = service.getQuoteById(newId);
        }
        this.chat.setLastId(quote.getQuoteId());
        sendText(chat.getChatId(), quote.getText());
        return true;
    }

    private boolean sendFirstQuote(Chat chat) {
        Quote quote = null;
        int newId = 0;

        while(quote == null){
            newId++;
            quote = service.getQuoteById(newId);
        }
        this.chat.setLastId(quote.getQuoteId());
        sendText(chat.getChatId(), quote.getText());
        return true;
    }

    private boolean sendNextQuote(Chat chat) {
        Quote quote = null;
        long newId = chat.getLastId();

        while(quote == null){
            newId++;

            if(newId > MAX_QUOTE_ID){
                newId = 1;
            }
            quote = service.getQuoteById(newId);
        }
        this.chat.setLastId(quote.getQuoteId());
        sendText(chat.getChatId(), quote.getText());
        return true;
    }

    private boolean sendText(long chatId, String text){
        SendResponse response = bot.execute(new SendMessage(chatId, text));
        System.out.println("Message text: \n"+response.message().text());
        return true;
    }

    public String getBotUsername() {
        return botUserName;
    }

    public String getBotToken() {
        return botToken;
    }
}
