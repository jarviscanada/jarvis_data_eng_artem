package ca.jrvs.apps.jdbc;

import ca.jrvs.apps.jdbc.util.DataTransferObject;

public class Product implements DataTransferObject {
  private long id;
  private String code;
  private String name;
  private int size;
  private String variety;
  private double price;

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = size;
  }

  public String getVariety() {
    return variety;
  }

  public void setVariety(String variety) {
    this.variety = variety;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public void setId(long id) {
    this.id = id;
  }

  @Override
  public long getId() {
    return id;
  }
}
