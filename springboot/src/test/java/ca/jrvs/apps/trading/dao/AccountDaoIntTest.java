package ca.jrvs.apps.trading.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.model.domain.Account;
import ca.jrvs.apps.trading.model.domain.Trader;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.assertj.core.util.Lists;
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
public class AccountDaoIntTest {
  @Autowired
  private AccountDao accountDao;

  @Autowired
  private TraderDao traderDao;

  private Account savedAccount;

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
  }

  @After
  public void deleteOne(){
    accountDao.deleteById(savedAccount.getId());
    traderDao.deleteById(savedTrader.getId());
  }

  @Test
  public void countTest(){
    long count = accountDao.count();
    assertNotEquals(0,count);
  }

  @Test
  public void findAllById(){
    List<Account> accounts = Lists.newArrayList(accountDao.findAllById(
        Arrays.asList(savedAccount.getId(),-1)));
    assertEquals(1,accounts.size());
    assertEquals(savedAccount.getAmount(),accounts.get(0).getAmount());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void updateOne(){
    savedAccount.setAmount(10.0);
    accountDao.save(savedAccount);
  }

  @Test
  public void findAll(){
    List<Account> accounts = accountDao.findAll();
    assertNotEquals(0,accounts.size());
  }
}
