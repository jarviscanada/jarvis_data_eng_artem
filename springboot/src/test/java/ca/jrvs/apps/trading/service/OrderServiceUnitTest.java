package ca.jrvs.apps.trading.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ca.jrvs.apps.trading.dao.AccountDao;
import ca.jrvs.apps.trading.dao.PositionDao;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.dao.SecurityOrderDao;
import ca.jrvs.apps.trading.model.domain.Account;
import ca.jrvs.apps.trading.model.domain.MarketOrderDto;
import ca.jrvs.apps.trading.model.domain.OrderStatus;
import ca.jrvs.apps.trading.model.domain.Position;
import ca.jrvs.apps.trading.model.domain.Quote;
import ca.jrvs.apps.trading.model.domain.SecurityOrder;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceUnitTest {
  @Captor
  ArgumentCaptor<SecurityOrder> captorSecurityOrder;

  @Mock
  private AccountDao accountDao;

  @Mock
  private SecurityOrderDao securityOrderDao;

  @Mock
  private QuoteDao quoteDao;

  @Mock
  private PositionDao positionDao;

  @InjectMocks
  private OrderService orderService;

  @Test
  public void executeBuyMarketOrder(){
    MarketOrderDto marketOrderDto = new MarketOrderDto();
    marketOrderDto.setAccountId(1);
    marketOrderDto.setSize(100);
    marketOrderDto.setTicker("AAPL");

    Account account = new Account();
    account.setAmount(10000d);
    account.setId(1);
    account.setTraderId(1);

    Quote quote = new Quote();
    quote.setTicker("AAPL");
    quote.setAskPrice(50d);

    when(accountDao.findById(1)).thenReturn(Optional.of(account));
    when(quoteDao.findById("AAPL")).thenReturn(Optional.of(quote));

    orderService.executeMarketOrder(marketOrderDto);
    verify(securityOrderDao).save(captorSecurityOrder.capture());

    assertEquals(OrderStatus.FILLED,captorSecurityOrder.getValue().getStatus());
    assertEquals(Integer.valueOf(100),captorSecurityOrder.getValue().getSize());
    assertEquals(Double.valueOf(50),captorSecurityOrder.getValue().getPrice());
  }

  @Test
  public void executeSellOrder(){
    MarketOrderDto marketOrderDto = new MarketOrderDto();
    marketOrderDto.setAccountId(1);
    marketOrderDto.setSize(-100);
    marketOrderDto.setTicker("AAPL");

    Account account = new Account();
    account.setAmount(10000d);
    account.setId(1);
    account.setTraderId(1);

    Quote quote = new Quote();
    quote.setTicker("AAPL");
    quote.setBidPrice(50d);

    Position position = new Position();
    position.setAccountId(1);
    position.setPosition(100);
    position.setTicker("AAPL");

    when(accountDao.findById(1)).thenReturn(Optional.of(account));
    when(quoteDao.findById("AAPL")).thenReturn(Optional.of(quote));
    when(positionDao.findById(1)).thenReturn(Optional.of(position));

    orderService.executeMarketOrder(marketOrderDto);
    verify(securityOrderDao).save(captorSecurityOrder.capture());

    assertEquals(OrderStatus.FILLED,captorSecurityOrder.getValue().getStatus());
    assertEquals(Integer.valueOf(-100),captorSecurityOrder.getValue().getSize());
    assertEquals(Double.valueOf(50),captorSecurityOrder.getValue().getPrice());
  }
}
