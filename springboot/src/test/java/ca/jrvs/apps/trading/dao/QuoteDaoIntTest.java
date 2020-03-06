package ca.jrvs.apps.trading.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.model.domain.Quote;
import java.util.List;
import java.util.Optional;
import org.junit.After;
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
public class QuoteDaoIntTest {

  @Autowired
  private QuoteDao dao;

  private Quote savedQuote;

  @Before
  public void insertOne(){
    savedQuote = new Quote();
    savedQuote.setAskPrice(10d);
    savedQuote.setAskSize(10);
    savedQuote.setBidPrice(10.2d);
    savedQuote.setBidSize(10);
    savedQuote.setTicker("aapl");
    savedQuote.setLastPrice(10.1d);
    dao.save(savedQuote);
  }

  @Test
  public void findOne(){
    Optional<Quote> outQuote = dao.findById(savedQuote.getId());
    assertTrue(outQuote.isPresent());
  }

  @Test
  public void findAll(){
    List<Quote> quotes = dao.findAll();
    assertEquals(1,quotes.size());
  }

  @Test
  public void updateOne(){
    savedQuote.setAskSize(100);
    dao.save(savedQuote);
    Quote quote = dao.findById(savedQuote.getId()).get();
    assertEquals(Integer.valueOf(100),quote.getAskSize());
  }

  @Test
  public void countQuotes(){
    long count = dao.count();
    assertEquals(1,count);
  }
  @After
  public void deleteOne(){
    dao.deleteById(savedQuote.getId());
  }

}
