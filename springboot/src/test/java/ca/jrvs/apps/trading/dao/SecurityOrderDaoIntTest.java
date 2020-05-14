package ca.jrvs.apps.trading.dao;

import static org.junit.Assert.assertEquals;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.model.domain.Account;
import ca.jrvs.apps.trading.model.domain.OrderStatus;
import ca.jrvs.apps.trading.model.domain.Quote;
import ca.jrvs.apps.trading.model.domain.SecurityOrder;
import ca.jrvs.apps.trading.model.domain.Trader;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
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
public class SecurityOrderDaoIntTest {

  @Autowired
  private SecurityOrderDao securityOrderDao;

  @Autowired
  private AccountDao accountDao;

  @Autowired
  private QuoteDao quoteDao;

  @Autowired
  private TraderDao traderDao;

  private SecurityOrder savedSecurityOrder;

  private Account savedAccount;

  private Quote savedQuote;

  private Trader savedTrader;

  @Before
  public void insertOne(){
    savedTrader = new Trader();
    savedTrader.setFirstName("Homer");
    savedTrader.setLastName("Simpson");
    savedTrader.setCountry("USA");
    savedTrader.setEmail("homersimpson@gmail.com");
    savedTrader.setDob(Date.valueOf(LocalDate.now()));
    traderDao.save(savedTrader);

    savedAccount = new Account();
    savedAccount.setTraderId(savedTrader.getId());
    savedAccount.setAmount(1000.0);
    accountDao.save(savedAccount);

    savedQuote = new Quote();
    savedQuote.setAskPrice(10d);
    savedQuote.setAskSize(10);
    savedQuote.setBidPrice(10.2d);
    savedQuote.setBidSize(10);
    savedQuote.setTicker("aapl");
    savedQuote.setLastPrice(10.1d);
    quoteDao.save(savedQuote);

    savedSecurityOrder = new SecurityOrder();
    savedSecurityOrder.setAccountId(savedAccount.getId());
    savedSecurityOrder.setNotes("No special instructions");
    savedSecurityOrder.setPrice(150.0);
    savedSecurityOrder.setSize(1000);
    savedSecurityOrder.setStatus(OrderStatus.FILLED);
    savedSecurityOrder.setTicker(savedQuote.getTicker());
    securityOrderDao.save(savedSecurityOrder);
  }

  @Test
  public void findFilledOrdersByAccountId(){
    SecurityOrder securityOrder = new SecurityOrder();
    securityOrder.setAccountId(savedAccount.getId());
    securityOrder.setNotes("This order will be deleted");
    securityOrder.setPrice(250.0);
    securityOrder.setSize(2000);
    securityOrder.setStatus(OrderStatus.CANCELED);
    securityOrder.setTicker(savedQuote.getTicker());
    securityOrderDao.save(securityOrder);

    List<SecurityOrder> securityOrders = securityOrderDao.findFilledOrdersByAccountId(savedAccount.getId());

    assertEquals(1, securityOrders.size());
    assertEquals(savedSecurityOrder.getId(),securityOrders.get(0).getId());
    assertEquals(savedAccount.getId(),securityOrders.get(0).getAccountId());

    securityOrderDao.deleteById(securityOrder.getId());
  }

  @After
  public void deleteOne(){
    securityOrderDao.deleteById(savedSecurityOrder.getId());
    quoteDao.deleteById(savedQuote.getId());
    accountDao.deleteById(savedAccount.getId());
    traderDao.deleteById(savedTrader.getId());
  }

}
