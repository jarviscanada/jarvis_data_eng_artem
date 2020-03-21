package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.domain.SecurityOrder;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class SecurityOrderDao extends JdbcCrudDao<SecurityOrder> {
  private static final Logger logger = LoggerFactory.getLogger(SecurityOrderDao.class);

  private static final String TABLE_NAME = "security_order";
  private static final String ID_COLUMN_NAME = "id";

  private JdbcTemplate jdbcTemplate;
  private SimpleJdbcInsert simpleJdbcInsert;

  @Autowired
  public SecurityOrderDao(DataSource dataSource) {
    jdbcTemplate = new JdbcTemplate(dataSource);
    simpleJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName(TABLE_NAME)
        .usingGeneratedKeyColumns(ID_COLUMN_NAME);
  }

  @Override
  public JdbcTemplate getJdbcTemplate() {
    return jdbcTemplate;
  }

  @Override
  public SimpleJdbcInsert getSimpleJdbcInsert() {
    return simpleJdbcInsert;
  }

  @Override
  public String getTableName() {
    return TABLE_NAME;
  }

  @Override
  public String getIdColumnName() {
    return ID_COLUMN_NAME;
  }

  @Override
  Class<SecurityOrder> getEntityClass() {
    return SecurityOrder.class;
  }

  @Override
  protected int updateOne(SecurityOrder entity) {
    throw new UnsupportedOperationException("Not implemented");
  }

  public List<SecurityOrder> findFilledOrdersByAccountId(Integer accountId) {
    List<SecurityOrder> securityOrders = new ArrayList<SecurityOrder>();
    String selectSql = "SELECT * FROM "+getTableName()+" WHERE account_id=? AND status='FILLED'";
    try{
      securityOrders =  getJdbcTemplate().query(selectSql,
          BeanPropertyRowMapper.newInstance(getEntityClass()),accountId);
    }catch(IncorrectResultSizeDataAccessException e){
      logger.debug("Can't find security orders associated with this account id:" + accountId, e);
    }
    return securityOrders;
  }
}
