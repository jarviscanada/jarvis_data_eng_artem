package ca.jrvs.apps.trading.model.domain;

public class Account implements Entity<Integer>  {
  private Double amount;
  private Integer id;
  private Integer traderId;

  @Override
  public Integer getId() {
    return id;
  }

  @Override
  public void setId(Integer integer) {
    id = integer;
  }

  public Double getAmount() {
    return amount;
  }

  public void setAmount(Double amount) {
    this.amount = amount;
  }

  public Integer getTraderId() {
    return traderId;
  }

  public void setTraderId(Integer traderId) {
    this.traderId = traderId;
  }
}
