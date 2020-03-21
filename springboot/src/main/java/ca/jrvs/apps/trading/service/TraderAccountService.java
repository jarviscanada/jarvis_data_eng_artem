package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dao.AccountDao;
import ca.jrvs.apps.trading.dao.PositionDao;
import ca.jrvs.apps.trading.dao.SecurityOrderDao;
import ca.jrvs.apps.trading.dao.TraderDao;
import ca.jrvs.apps.trading.model.domain.Account;
import ca.jrvs.apps.trading.model.domain.Position;
import ca.jrvs.apps.trading.model.domain.SecurityOrder;
import ca.jrvs.apps.trading.model.domain.Trader;
import ca.jrvs.apps.trading.model.domain.TraderAccountView;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TraderAccountService {
  private TraderDao traderDao;
  private AccountDao accountDao;
  private PositionDao positionDao;
  private SecurityOrderDao securityOrderDao;

  @Autowired
  public TraderAccountService(TraderDao traderDao, AccountDao accountDao,
      PositionDao positionDao, SecurityOrderDao securityOrderDao) {
    this.traderDao = traderDao;
    this.accountDao = accountDao;
    this.positionDao = positionDao;
    this.securityOrderDao = securityOrderDao;
  }

  public Account withdraw(Integer traderId, Double fund) {
    verifyTraderIdAndFundNotNull(traderId, fund);

    verifyFundsGreaterThanZero(fund);

    Optional<Account> traderAccount = accountDao.findByTraderId(traderId);

    verifyAccountIsExists(traderAccount);

    verifyFundsAreSufficient(fund, traderAccount);

    Double result = traderAccount.get().getAmount() - fund;
    return accountDao.updateAmountById(traderAccount.get().getId(), result);
  }

  private void verifyFundsAreSufficient(Double fund, Optional<Account> traderAccount) {
    if (traderAccount.get().getAmount().doubleValue() < fund) {
      throw new IllegalArgumentException("Insufficient funds");
    }
  }

  private void verifyAccountIsExists(Optional<Account> traderAccount) {
    if (!traderAccount.isPresent()) {
      throw new IllegalArgumentException("No account associated with this Trader ID");
    }
  }

  private void verifyFundsGreaterThanZero(Double fund) {
    if (fund.doubleValue() < 0) {
      throw new IllegalArgumentException("Funds must be greater than 0");
    }
  }

  private void verifyTraderIdAndFundNotNull(Integer traderId, Double fund) {
    if (traderId == null || fund == null) {
      throw new IllegalArgumentException("Trader ID and Funds cannot be null");
    }
  }

  public Account deposit(Integer traderId, Double fund) {
    verifyTraderIdAndFundNotNull(traderId, fund);

    verifyFundsGreaterThanZero(fund);

    Optional<Account> traderAccount = accountDao.findByTraderId(traderId);

    verifyAccountIsExists(traderAccount);

    return accountDao.updateAmountById(traderAccount.get().getId(), fund);

  }

  public void deleteTraderById(Integer traderId) {
    verifyTraderIdNotNull(traderId);
    Optional<Trader> trader = traderDao.findById(traderId);

    verifyTraderExists(trader);
    Account account = accountDao.findById(traderId).get();

    verifyTraderFundsAreZero(account);

    try {
      Optional<Position> position = positionDao.findById(traderId);
      if (position.isPresent()) {
        removeAllSecureOrdersOfAccount(account);
      }
      accountDao.deleteById(account.getId());
      traderDao.deleteById(trader.get().getId());
    } catch (Exception e) {
      throw new IllegalArgumentException("Unable to delete");
    }
  }

  private void verifyTraderFundsAreZero(Account account) {
    if (account.getAmount() != 0) {
      throw new IllegalArgumentException("Trader balance is not zero");
    }
  }

  private void verifyTraderExists(Optional<Trader> trader) {
    if (trader.isPresent()) {
      throw new IllegalArgumentException("Trader with a given id does not exist");
    }
  }

  private void verifyTraderIdNotNull(Integer traderId) {
    if (traderId == null) {
      throw new IllegalArgumentException("Id cannot be null");
    }
  }

  private void removeAllSecureOrdersOfAccount(Account account) {
    List<SecurityOrder> securityOrderList = securityOrderDao
        .findFilledOrdersByAccountId(account.getId());
    securityOrderList.stream().forEach(order -> {
      securityOrderDao.deleteById(order.getId());
    });
  }

  public TraderAccountView createTraderAccount(Trader trader) {
    Account account;
    TraderAccountView traderAccountView;

    if (isTraderIdNull(trader)) {
      if (isTraderFieldsValid(trader)) {
        trader = traderDao.save(trader);
        account = createNewAccount(trader.getId());
        return createTraderAccountView(trader,account);
      }else{
        throw new IllegalArgumentException("Trader property cannot be null or empty");
      }
    }else{
      throw new IllegalArgumentException("ID is not allowed as it's auto-gen");
    }
  }

  private TraderAccountView createTraderAccountView(Trader trader, Account account){
    TraderAccountView traderAccountView = new TraderAccountView();
    traderAccountView.setTrader(trader);
    traderAccountView.setAccount(account);
    return traderAccountView;
  }

  private Account createNewAccount(Integer traderId){
    Account account = new Account();
    account.setTraderId(traderId);
    account.setAmount(0.0);
    return accountDao.save(account);
  }

  private boolean isTraderIdNull(Trader trader) {
    return trader.getId()==null;
  }

  private boolean isTraderFieldsValid(Trader trader) {
    return trader.getCountry()!=null &&
        trader.getCountry() != "" &&
        trader.getDob() != null &&
        trader.getEmail() != null &&
        trader.getEmail() != "" &&
        trader.getFirstName() != null &&
        trader.getFirstName() != "" &&
        trader.getLastName() != null &&
        trader.getLastName() != "";
  }
}
