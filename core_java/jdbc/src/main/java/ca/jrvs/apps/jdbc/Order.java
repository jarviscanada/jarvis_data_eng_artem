package ca.jrvs.apps.jdbc;

import ca.jrvs.apps.jdbc.util.DataTransferObject;
import java.sql.Timestamp;

public class Order implements DataTransferObject {
  private long id;
  private Timestamp creationDate;
  private double totalDue;
  private String status;
  private Customer customer;
  private Salesman salesman;

  public Customer getCustomer() {
    return customer;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }

  public Salesman getSalesman() {
    return salesman;
  }

  public void setSalesman(Salesman salesman) {
    this.salesman = salesman;
  }

  public OrderItem getOrderItem() {
    return orderItem;
  }

  public void setOrderItem(OrderItem orderItem) {
    this.orderItem = orderItem;
  }

  private OrderItem orderItem;

  public Timestamp getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(Timestamp creationDate) {
    this.creationDate = creationDate;
  }

  public double getTotalDue() {
    return totalDue;
  }

  public void setTotalDue(double totalDue) {
    this.totalDue = totalDue;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public void setId(long id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return "Order{" +
        "\n id=" + id +
        ",\n creationDate=" + creationDate +
        ",\n totalDue=" + totalDue +
        ",\n status='" + status + '\'' +
        ",\n customerFirstName=" + customer.getFirstName() +
        ",\n customerLastName=" + customer.getLastName() +
        ",\n customerEmail=" + customer.getEmail() +
        ",\n salesmanFirstName=" + salesman.getFirstName() +
        ",\n salesmanLastName=" + salesman.getLastName() +
        ",\n salesmanEmail=" + salesman.getEmail() +
        ",\n " + orderItem.toString() +
        '}';
  }

  @Override
  public long getId() {
    return id;
  }
}
