package ca.jrvs.apps.trading.service;


import ca.jrvs.apps.trading.dao.AccountDao;
import ca.jrvs.apps.trading.dao.PositionDao;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.dao.SecurityOrderDao;
import ca.jrvs.apps.trading.model.domain.Account;
import ca.jrvs.apps.trading.model.domain.MarketOrderDto;
import ca.jrvs.apps.trading.model.domain.OrderStatus;
import ca.jrvs.apps.trading.model.domain.Position;
import ca.jrvs.apps.trading.model.domain.SecurityOrder;
import java.util.Optional;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class OrderService {
  private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

  private AccountDao accountDao;
  private SecurityOrderDao securityOrderDao;
  private QuoteDao quoteDao;
  private PositionDao positionDao;

  @Autowired
  public OrderService(AccountDao accountDao,
      SecurityOrderDao securityOrderDao, QuoteDao quoteDao,
      PositionDao positionDao) {
    this.accountDao = accountDao;
    this.securityOrderDao = securityOrderDao;
    this.quoteDao = quoteDao;
    this.positionDao = positionDao;
  }

  public SecurityOrder executeMarketOrder(MarketOrderDto orderDto){
    verifyOrderSize(orderDto);
    verifyTickerId(orderDto);

    Optional<Account> account = accountDao.findById(orderDto.getAccountId());

    if (account.isPresent()) {
      throw new IllegalArgumentException("No account associated with given Id");
    }

    SecurityOrder securityOrder = getNewSecurityOrder(orderDto);

    if (isPositiveSize(orderDto)) {
      handleBuyMarketOrder(orderDto, securityOrder, account.get());
    } else {
      handleSellMarketOrder(orderDto, securityOrder, account.get());
    }
    return securityOrderDao.save(securityOrder);
  }

  private void verifyTickerId(MarketOrderDto orderDto) {
    if (!isTickerValid(orderDto)) {
      throw new IllegalArgumentException("Invalid ticker id");
    }
  }

  private void verifyOrderSize(MarketOrderDto orderDto) {
    if (orderDto.getSize() == 0) {
      throw new IllegalArgumentException("Invalid order size");
    }
  }

  private void handleSellMarketOrder(MarketOrderDto orderDto, SecurityOrder securityOrder,
      Account account) {
    Position position = positionDao.findById(account.getId()).get();
    Integer positionAfterOrder = position.getPosition() + orderDto.getSize();
    Double pricePerUnit = quoteDao.findById(orderDto.getTicker()).get().getBidPrice();
    Double totalPrice = orderDto.getSize() * pricePerUnit;
    Double balance = account.getAmount();

    securityOrder.setPrice(pricePerUnit);

    if(position != null && positionAfterOrder >= 0){
      accountDao.updateAmountById(orderDto.getAccountId(), balance + totalPrice);
      securityOrder.setStatus(OrderStatus.FILLED);
    }else{
      securityOrder.setStatus(OrderStatus.CANCELED);
      securityOrder.setNotes("Insufficient position");
    }

  }

  private boolean isPositiveSize(MarketOrderDto orderDto) {
    return orderDto.getSize() > 0;
  }

  private boolean isTickerValid(MarketOrderDto orderDto) {
    return quoteDao.findById(orderDto.getTicker().toUpperCase()).isPresent();
  }

  private void handleBuyMarketOrder(MarketOrderDto orderDto, SecurityOrder securityOrder, Account account) {
    Double pricePerUnit = quoteDao.findById(orderDto.getTicker()).get().getAskPrice();
    Double totalPrice = pricePerUnit * securityOrder.getSize();
    Double balance = account.getAmount();

    securityOrder.setPrice(pricePerUnit);

    if(balance.doubleValue() >= totalPrice.doubleValue()){
      Double newBalance = balance - totalPrice;
      accountDao.updateAmountById(orderDto.getAccountId(),newBalance);
      securityOrder.setStatus(OrderStatus.FILLED);
    }else{
      securityOrder.setStatus(OrderStatus.CANCELED);
      securityOrder.setNotes("Insufficient funds on account balance");
    }
  }

  private SecurityOrder getNewSecurityOrder(MarketOrderDto orderDto) {
    SecurityOrder securityOrder = new SecurityOrder();
    securityOrder.setTicker(orderDto.getTicker());
    securityOrder.setSize(orderDto.getSize());
    securityOrder.setAccountId(orderDto.getAccountId());
    return securityOrder;
  }
}
