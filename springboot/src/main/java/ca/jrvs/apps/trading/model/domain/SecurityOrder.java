package ca.jrvs.apps.trading.model.domain;

public class SecurityOrder implements Entity<Integer> {
  private Integer accountId;
  private Integer id;
  private String notes;
  private Double price;
  private Integer size;
  private String ticker;
  private OrderStatus status;

  public Integer getAccountId() {
    return accountId;
  }

  public void setAccountId(Integer accountId) {
    this.accountId = accountId;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public Double getPrice() {
    return price;
  }

  public void setPrice(Double price) {
    this.price = price;
  }

  public Integer getSize() {
    return size;
  }

  public void setSize(Integer size) {
    this.size = size;
  }

  public String getTicker() {
    return ticker;
  }

  public void setTicker(String ticker) {
    this.ticker = ticker;
  }

  public OrderStatus getStatus() {
    return status;
  }

  public void setStatus(OrderStatus status) {
    this.status = status;
  }

  @Override
  public Integer getId() {
    return id;
  }

  @Override
  public void setId(Integer integer) {
    id = integer;
  }
}
