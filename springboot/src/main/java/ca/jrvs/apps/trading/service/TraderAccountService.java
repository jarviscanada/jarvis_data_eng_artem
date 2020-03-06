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

  public Account withdraw(Integer traderId, Double fund){
    if(traderId != null && fund != null){
      if(fund.doubleValue() > 0){
        Optional<Account> traderAccount = accountDao.findByTraderId(traderId);
        if(traderAccount.isPresent()){
          if(traderAccount.get().getAmount().doubleValue() > fund){
            Double result = traderAccount.get().getAmount() - fund;
            return accountDao.updateAmountById(traderAccount.get().getId(),result);
          }else{
            throw new IllegalArgumentException("Insufficient funds");
          }
        }else{
          throw new IllegalArgumentException("No account associated with this Trader ID");
        }
      }else{
        throw new IllegalArgumentException("Funds must be greater than 0");
      }
    }else{
      throw new IllegalArgumentException("Trader ID and Funds cannot be null");
    }
  }

  public Account deposit(Integer traderId, Double fund){
    if(traderId != null && fund != null){
      if(fund.doubleValue() > 0 ){
        Optional<Account> traderAccount = accountDao.findByTraderId(traderId);
        if(traderAccount.isPresent()){
          return accountDao.updateAmountById(traderAccount.get().getId(),fund);
        }else{
          throw new IllegalArgumentException("No account associated with this Trader ID");
        }
      }else{
        throw new IllegalArgumentException("Funds must be greater than 0");
      }
    }else{
      throw new IllegalArgumentException("Trader ID and Funds cannot be null");
    }
  }

  public void deleteTraderById(Integer traderId){
    if(traderId != null){
      Optional<Trader> trader = traderDao.findById(traderId);
      if(trader.isPresent()){
        Account account = accountDao.findById(traderId).get();
        if(account.getAmount()==0){
          try{
            Optional<Position> position = positionDao.findById(traderId);

            if(position.isPresent()){
              List<SecurityOrder> securityOrderList = securityOrderDao.findFilledOrdersByAccountId(account.getId());
              securityOrderList.stream().forEach(order -> {securityOrderDao.deleteById(order.getId());});
            }
            accountDao.deleteById(account.getId());
            traderDao.deleteById(trader.get().getId());
          }catch(Exception e){
            throw new IllegalArgumentException("Unable to delete");
          }
        }else{
          throw new IllegalArgumentException("Trader balance is not zero");
        }
      }else{
        throw new IllegalArgumentException("Trader with a given id does not exist");
      }
    }else{
      throw new IllegalArgumentException("Id cannot be null");
    }
  }

  public TraderAccountView createTraderAccount(Trader trader){
    Account account;
    TraderAccountView traderAccountView;

    if(isTraderIdNull(trader)){
      if(isTraderFieldsValid(trader)){
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
