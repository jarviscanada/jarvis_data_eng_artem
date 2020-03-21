package ca.jrvs.apps.trading.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.dao.MarketDataDao;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.model.domain.IexQuote;
import ca.jrvs.apps.trading.model.domain.Quote;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestConfig.class})
@Sql({"classpath:schema.sql"})
public class QuoteServiceIntTest {
  @Autowired
  private QuoteService quoteService;

  @Autowired
  private QuoteDao quoteDao;

  @Autowired
  private MarketDataDao marketDataDao;

  @Before
  public void setup(){quoteDao.deleteAll();}

  @Test
  public void findIexQuoteByTicker(){
    IexQuote iexQuote = quoteService.findIexQuoteByTicker("AAPL");
    assertNotNull(iexQuote);
  }

  @Test
  public void updateMarketData(){
    Quote quote = new Quote();
    quote.setAskPrice(-1d);
    quote.setAskSize(-1);
    quote.setBidPrice(-10.2d);
    quote.setBidSize(-10);
    quote.setTicker("AAPL");
    quote.setLastPrice(-10.1d);
    quoteDao.save(quote);

    quoteService.updateMarketData();

    Quote newQuote = quoteDao.findById(quote.getId()).get();

    assertNotEquals(Integer.valueOf(-1), newQuote.getAskSize());
    assertNotEquals(Integer.valueOf(-10), newQuote.getBidSize());
    assertNotEquals(-1d, newQuote.getAskPrice());
    assertNotEquals(-10.2d, newQuote.getBidPrice());
    assertNotEquals(-10.1d, newQuote.getLastPrice());
  }

  @Test
  public void saveQuotes(){
    List<String> tickers = new ArrayList<String>();
    tickers.add("AAPL");
    tickers.add("FB");
    tickers.add("GOOGL");
    tickers.add("AMZN");

    List<Quote> quotes = quoteService.saveQuotes(tickers);

    assertEquals(4,quotes.size());
    assertEquals("AAPL", quotes.get(0).getTicker());
    assertEquals("FB", quotes.get(1).getTicker());
    assertEquals("GOOGL", quotes.get(2).getTicker());
    assertEquals("AMZN", quotes.get(3).getTicker());
  }

  @Test
  public void saveQuote(){
    Quote quote = new Quote();
    quote.setAskPrice(-1d);
    quote.setAskSize(-1);
    quote.setBidPrice(-10.2d);
    quote.setBidSize(-10);
    quote.setTicker("FB");
    quote.setLastPrice(-10.1d);

    Quote savedQuote = quoteService.saveQuote(quote);

    assertNotNull(savedQuote);
    assertEquals("FB",savedQuote.getTicker());
  }

  @Test
  public void findAllQuotes(){
    long count = quoteDao.count();

    List<Quote> quotes = quoteService.findAllQuotes();

    assertEquals(count, quotes.size());
  }
}
