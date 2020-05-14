package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.domain.Entity;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

public abstract class JdbcCrudDao<T extends Entity<Integer>> implements CrudRepository<T, Integer> {
  private static final Logger logger = LoggerFactory.getLogger(JdbcCrudDao.class);

  abstract public JdbcTemplate getJdbcTemplate();

  abstract public SimpleJdbcInsert getSimpleJdbcInsert();

  abstract public String getTableName();

  abstract public String getIdColumnName();

  abstract Class<T> getEntityClass();

  @Override
  public <S extends T> S save(S entity) {
    if(existsById(entity.getId())){
      if(updateOne(entity) != 1){
        throw new DataRetrievalFailureException("Unable to update quote");
      }
    }else{
      addOne(entity);
    }
    return entity;
  }

  protected abstract int updateOne(T entity);

  private <S extends T> void addOne(S entity) {
    SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(entity);

    Number newId = getSimpleJdbcInsert().executeAndReturnKey(parameterSource);
    entity.setId(newId.intValue());
  }

  @Override
  public <S extends T> Iterable<S> saveAll(Iterable<S> entities) {
    Iterator iterator = entities.iterator();
    List<S> entityList = new ArrayList<S>();

    while(iterator.hasNext()){
      entityList.add(save((S) iterator.next()));
    }
    return entityList;
  }

  @Override
  public Optional<T> findById(Integer id) {
    Optional<T> entity = Optional.empty();
    String selectSql = "SELECT * FROM "+getTableName()+" WHERE "+getIdColumnName()+" =?";

    try{
      entity = Optional.ofNullable((T) getJdbcTemplate().queryForObject(selectSql,
          BeanPropertyRowMapper.newInstance(getEntityClass()),id));
    }catch(IncorrectResultSizeDataAccessException e){
      logger.debug("Can't find trader id:" + id, e);
    }

    return entity;
  }

  @Override
  public boolean existsById(Integer id) {
    if (id == null || !findById(id).isPresent()) {
      return false;
    }else{
      return true;
    }
  }

  @Override
  public List<T> findAll() {
    String selectSql = "SELECT * FROM " + getTableName();
    List<T> entity =  getJdbcTemplate()
        .query(selectSql, BeanPropertyRowMapper.newInstance(getEntityClass()));
    return entity;
  }

  @Override
  public List<T> findAllById(Iterable<Integer> ids) {
    Iterator iterator = ids.iterator();
    List<T> entityList = new ArrayList<T>();

    while(iterator.hasNext()){
      Optional<T> entity = findById((Integer) iterator.next());
      if(entity.isPresent())
        entityList.add(entity.get());
    }
    return entityList;
  }

  @Override
  public long count() {
    return getJdbcTemplate().queryForObject("SELECT COUNT(*) FROM "+getTableName(), Long.class);
  }

  @Override
  public void deleteById(Integer id) {
    if (id == null) {
      throw new IllegalArgumentException("ID can't be null");
    }

    String deleteSql = "DELETE FROM " + getTableName() + " WHERE " + getIdColumnName() + " =?";
    getJdbcTemplate().update(deleteSql, id);
  }

  @Override
  public void delete(T t) { throw new UnsupportedOperationException("Not implemented");}

  @Override
  public void deleteAll(Iterable<? extends T> iterable) { throw new UnsupportedOperationException("Not implemented");}

  @Override
  public void deleteAll() {
    getJdbcTemplate().update(
        "DELETE FROM "+getTableName());
  }
}
