package ca.jrvs.apps.jdbc;

import ca.jrvs.apps.jdbc.util.DataTransferObject;

public class OrderItem implements DataTransferObject {
  private long id;
  private int quantity;
  private Product product;

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  public void setId(long id) {
    this.id = id;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  @Override
  public long getId() {
    return id;
  }

  @Override
  public String toString() {
    return "OrderItem{" +
        "\n quantity=" + quantity +
        ",\n productCode=" + product.getCode() +
        ",\n productName=" + product.getName() +
        ",\n productSize=" + product.getSize() +
        ",\n productVariety=" + product.getVariety() +
        ",\n productPrice=" + product.getPrice() +
        '}';
  }
}
