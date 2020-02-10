package ca.jrvs.apps.jdbc;

import ca.jrvs.apps.jdbc.util.DataAccessObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class OrderDAO extends DataAccessObject<Order> {
  private static final String GET_ONE = "SELECT\n"
      + "  c.first_name, c.last_name, c.email, o.order_id,\n"
      + "  o.creation_date, o.total_due, o.status,\n"
      + "  s.first_name, s.last_name, s.email,\n"
      + "  ol.quantity,\n"
      + "  p.code, p.name, p.size, p.variety, p.price\n"
      + "from orders o\n"
      + "  join customer c on o.customer_id = c.customer_id\n"
      + "  join salesperson s on o.salesperson_id=s.salesperson_id\n"
      + "  join order_item ol on ol.order_id=o.order_id\n"
      + "  join product p on ol.product_id = p.product_id\n"
      + "where o.order_id = ?";

  public OrderDAO(Connection connection) {
    super(connection);
  }

  @Override
  public Order findById(long id) {
    Order order = new Order();
    Customer customer = new Customer();
    Salesman salesman = new Salesman();
    OrderItem orderItem = new OrderItem();
    Product product = new Product();

    orderItem.setProduct(product);
    order.setCustomer(customer);
    order.setSalesman(salesman);
    order.setOrderItem(orderItem);

    try(PreparedStatement statement = this.connection.prepareStatement(GET_ONE);){
      statement.setLong(1, id);
      ResultSet rs = statement.executeQuery();
      while (rs.next()){
        order.setId(rs.getLong("order_id"));
        customer.setFirstName(rs.getString(1));
        customer.setLastName(rs.getString(2));
        customer.setEmail(rs.getString(3));
        salesman.setFirstName(rs.getString(8));
        salesman.setLastName(rs.getString(9));
        salesman.setEmail(rs.getString(10));
        order.setCreationDate(rs.getTimestamp("creation_date"));
        order.setTotalDue(rs.getDouble("total_due"));
        order.setStatus(rs.getString("status"));
        orderItem.setQuantity(rs.getInt("quantity"));
        product.setCode(rs.getString("code"));
        product.setName(rs.getString("name"));
        product.setSize(rs.getInt("size"));
        product.setVariety(rs.getString("variety"));
        product.setPrice(rs.getDouble("price"));
      }
    }catch (SQLException e){
      e.printStackTrace();
      throw new RuntimeException(e);
    }

    return order;
  }

  @Override
  public List<Order> findAll() {
    return null;
  }

  @Override
  public Order update(Order dto) {
    return null;
  }

  @Override
  public Order create(Order dto) {
    return null;
  }

  @Override
  public void delete(long id) {

  }
}
