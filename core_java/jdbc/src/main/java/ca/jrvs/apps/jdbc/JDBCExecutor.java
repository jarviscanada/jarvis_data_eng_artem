package ca.jrvs.apps.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

public class JDBCExecutor {

  public static void main(String[] args) {
    DatabaseConnectionManager dcm = new DatabaseConnectionManager("localhost:5432","hplussport", "postgres","password");
    try{
      Connection connection = dcm.getConnection();
      OrderDAO orderDAO = new OrderDAO(connection);
      System.out.println(orderDAO.findById(1100));
    }catch (SQLException e){
      e.printStackTrace();
    }
  }
}
