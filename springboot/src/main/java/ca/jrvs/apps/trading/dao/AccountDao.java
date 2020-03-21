package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.domain.Account;
import java.util.Optional;
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
public class AccountDao extends JdbcCrudDao<Account> {
  private static final Logger logger = LoggerFactory.getLogger(AccountDao.class);

  private static final String TABLE_NAME = "account";
  private static final String ID_COLUMN_NAME = "id";

  private JdbcTemplate jdbcTemplate;
  private SimpleJdbcInsert simpleJdbcInsert;

  @Autowired
  public AccountDao(DataSource dataSource) {
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
  Class<Account> getEntityClass() {
    return Account.class;
  }

  @Override
  protected int updateOne(Account entity) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public void deleteAll(Iterable<? extends Account> iterable) {
    throw new UnsupportedOperationException("Not implemented");
  }

  public Optional<Account> findByTraderId(Integer id){
    Optional<Account> entity = Optional.empty();
    String selectSql = "SELECT * FROM "+getTableName()+" WHERE trader_id=?";
    try{
      entity = Optional.ofNullable((Account) getJdbcTemplate().queryForObject(selectSql,
          BeanPropertyRowMapper.newInstance(getEntityClass()),id));
    }catch(IncorrectResultSizeDataAccessException e){
      logger.debug("Can't find account by trader id:" + id, e);
    }
    return entity;
  }

  public Account updateAmountById(Integer id, Double amount){
    String update_sql = "UPDATE account SET amount=? WHERE id=?";
    jdbcTemplate.update(update_sql, new Object[]{amount, id});
    return findById(id).get();
  }
}
