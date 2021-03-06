package ca.jrvs.apps.trading.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import ca.jrvs.apps.trading.TestConfig;
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
public class TraderDaoIntTest {
  @Autowired
  private TraderDao traderDao;

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
  }

  @Test
  public void countTest(){
    long count = traderDao.count();
    assertNotEquals(0,count);
  }

  @After
  public void deleteOne(){
    traderDao.deleteById(savedTrader.getId());
  }

  @Test
  public void findAllById(){
    List<Trader> traders = Lists.newArrayList(traderDao.findAllById(
        Arrays.asList(savedTrader.getId(),-1)));
    assertEquals(1,traders.size());
    assertEquals(savedTrader.getCountry(),traders.get(0).getCountry());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void updateOne(){
    savedTrader.setEmail("newemail@gmail.com");
    traderDao.save(savedTrader);
  }

  @Test
  public void findAll(){
    List<Trader> traders = traderDao.findAll();
    assertNotEquals(0,traders.size());
  }
}
