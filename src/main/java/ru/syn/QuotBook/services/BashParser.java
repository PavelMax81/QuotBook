package ru.syn.QuotBook.services;

import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import ru.syn.QuotBook.models.Quote;

import java.io.IOException;
import java.util.*;

@Component
public class BashParser {
    public ArrayList<Quote> getPage(int page) {
    //    Map<Long, String> quotes = null;
        ArrayList<Quote> quoteList = null;

        try {
            Document doc = Jsoup.connect("http://ibash.org.ru/?page=" + page).get();
            Elements sourceQuotes = doc.select(".quote");
        //    quotes = new HashMap<>();
            quoteList = new ArrayList<Quote>();

            for (Element quoteElement : sourceQuotes) {
                String stringId = quoteElement.select("b").first().text().substring(1);
                Long quoteId = Long.parseLong(stringId);
                String text = quoteElement.select(".quotbody").first().text();
                //      String textUnified = StringEscapeUtils.unescapeHtml4(text);
            //    quotes.put(quoteId, text);

                Quote quote = new Quote();
                quote.setQuoteId(quoteId);
                quote.setText(text);
                quoteList.add(quote);
            }
        } catch (IOException ignored) {
        }
        return quoteList;
    }

    public Map.Entry<Long, String> getRandomQuoteFromPage(int page) {
        try {
            Document doc = Jsoup.connect("http://ibash.org.ru/?page=" + page).get();
            Elements sourceQuotes = doc.select(".quote");
            int random = new Random().nextInt(sourceQuotes.size());

            Element randomQuoteElement = sourceQuotes.get(random);
            String stringId = randomQuoteElement.select("b").first().text().substring(1);
            Long id = Long.parseLong(stringId);
            String text = randomQuoteElement.select(".quotbody").first().text();
            // String textUnified = StringEscapeUtils.unescapeHtml4(text);
            return new AbstractMap.SimpleEntry<Long, String>(id, text);
        } catch (IOException ignored) {
        }
        return null;
    }

    public Map.Entry<Long, String> getById(long id) {
        try {
            Document doc = Jsoup.connect("http://ibash.org.ru/quote.php?id=" + id).get();
            Element quoteElement = doc.select(".quote").first();
            String realId = quoteElement.select("b").first().text();

            if (realId.equals("#???")) {
                return null;
            }
            String text = quoteElement.select(".quotbody").first().text();
            //     String textUnified = StringEscapeUtils.unescapeHtml4(text);

            return new AbstractMap.SimpleEntry<Long, String>(id, text);
        } catch (IOException ignored) {
        }
        return null;
    }

    public Map.Entry<Long, String> getRandom() {
        try {
            Document doc = Jsoup.connect("http://ibash.org.ru/random.php").get();
            Element quoteElement = doc.select(".quote").first();
            String realId = quoteElement.select("b").first().text();

            if (realId.equals("#???")) {
                return null;
            }
            Long id = Long.parseLong(realId.substring(1));
            String text = quoteElement.select(".quotbody").first().text();
            //     String textUnified = StringEscapeUtils.unescapeHtml4(text);

            return new AbstractMap.SimpleEntry<>(id, text);
        } catch (IOException ignored) {
        }
        return null;
    }

    public Map.Entry<Long, String> getLast() {
        try {
            Document doc = Jsoup.connect("http://ibash.org.ru").get();
            Elements sourceQuotes = doc.select(".quote");
            int first = 0;

            Element randomQuoteElement = sourceQuotes.get(first);
            String stringId = randomQuoteElement.select("b").first().text().substring(1);
            Long id = Long.parseLong(stringId);
            String text = randomQuoteElement.select(".quotbody").first().text();
            // String textUnified = StringEscapeUtils.unescapeHtml4(text);
            return new AbstractMap.SimpleEntry<Long, String>(id, text);
        } catch (IOException ignored) {
        }
        return null;
    }
}
