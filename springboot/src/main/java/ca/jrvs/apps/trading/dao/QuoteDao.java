package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.domain.Quote;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class QuoteDao implements CrudRepository<Quote, String> {

  private static final String TABLE_NAME = "quote";
  private static final String ID_COLUMN_NAME = "ticker";

  private static final Logger logger = LoggerFactory.getLogger(QuoteDao.class);
  private JdbcTemplate jdbcTemplate;
  private SimpleJdbcInsert simpleJdbcInsert;

  @Autowired
  public QuoteDao(DataSource dataSource) {
    jdbcTemplate = new JdbcTemplate(dataSource);
    simpleJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName(TABLE_NAME);
  }

  @Override
  public Quote save(Quote quote) {
    if(existsById(quote.getTicker())){
      int updateRowNo = updateOne(quote);
      if(updateRowNo != 1){
        throw new DataRetrievalFailureException("Unable to update quote");
      }
    } else{
      addOne(quote);
    }
    return quote;
  }

  private void addOne(Quote quote) {
    SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(quote);
    int row = simpleJdbcInsert.execute(parameterSource);
    if(row != 1){
      throw new IncorrectResultSizeDataAccessException("Failed to insert",1, row);
    }
  }

  private int updateOne(Quote quote) {
    String update_sql = "UPDATE quote SET last_price=?, bid_price=?,"
        + "bid_size=?, ask_price=?, ask_size=? WHERE ticker=?";
    return jdbcTemplate.update(update_sql, makeUpdateValues(quote));
  }

  private Object[] makeUpdateValues(Quote quote) {
    Object[] parameters = new Object[6];

    parameters[0] = quote.getLastPrice();
    parameters[1] = quote.getBidPrice();
    parameters[2] = quote.getBidSize();
    parameters[3] = quote.getAskPrice();
    parameters[4] = quote.getAskSize();
    parameters[5] = quote.getTicker();

    return parameters;
  }

  @Override
  public <S extends Quote> List<S> saveAll(Iterable<S> quotes) {
    Iterator quotesIterator = quotes.iterator();
    List quotesList = new ArrayList<Quote>();

    while(quotesIterator.hasNext()){
      quotesList.add(save((Quote) quotesIterator.next()));
    }
    return quotesList;
  }

  @Override
  public Optional<Quote> findById(String id) {
    if (id == null) {
      throw new IllegalArgumentException("ID can't be null");
    }

    String selectSql = "SELECT * FROM " +TABLE_NAME+" WHERE "
        +ID_COLUMN_NAME+" = ?";
    try{
      Quote quote = jdbcTemplate
          .queryForObject(selectSql, BeanPropertyRowMapper.newInstance(Quote.class), id);
      return Optional.of(quote);
    }catch(EmptyResultDataAccessException e){
      logger.debug("Can't find trader id:" + id, e);
    }

    return Optional.empty();
  }

  @Override
  public boolean existsById(String id) {
    if (id == null) {
      throw new IllegalArgumentException("ID can't be null");
    }
    Optional<Quote> quote = findById(id);
    if(quote.isPresent())
      return true;
    else
      return false;
  }

  @Override
  public List<Quote> findAll() {
    String selectSql = "SELECT * FROM " + TABLE_NAME;
    List<Quote> quotes =  jdbcTemplate
        .query(selectSql, BeanPropertyRowMapper.newInstance(Quote.class));
    return quotes;
  }

  @Override
  public Iterable<Quote> findAllById(Iterable<String> iterable) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public long count() {
    return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM "+TABLE_NAME, Long.class);
  }

  @Override
  public void deleteById(String id) {
    if (id == null) {
      throw new IllegalArgumentException("ID can't be null");
    }

    String deleteSql = "DELETE FROM " + TABLE_NAME + " WHERE " + ID_COLUMN_NAME + " =?";
    jdbcTemplate.update(deleteSql, id);
  }

  @Override
  public void delete(Quote quote) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public void deleteAll(Iterable<? extends Quote> iterable) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public void deleteAll() {
    jdbcTemplate.update(
        "DELETE FROM "+TABLE_NAME);
  }
}
