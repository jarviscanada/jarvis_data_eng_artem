package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.JsonTranslator;
import ca.jrvs.apps.trading.model.config.MarketDataConfig;
import ca.jrvs.apps.trading.model.domain.IexQuote;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public class MarketDataDao implements CrudRepository<IexQuote, String> {

  private static final String IEX_BATCH_PATH = "/stock/market/batch?symbols=%s&types=quote&token=";
  private final String IEX_BATCH_URL;
  private Logger logger = LoggerFactory.getLogger(MarketDataDao.class);
  private HttpClientConnectionManager httpClientConnectionManager;

  @Autowired
  public MarketDataDao(HttpClientConnectionManager httpClientConnectionManager,
      MarketDataConfig marketDataConfig) {
    this.httpClientConnectionManager = httpClientConnectionManager;
    IEX_BATCH_URL = marketDataConfig.getHost() + IEX_BATCH_PATH + marketDataConfig.getToken();
  }

  @Override
  public Optional<IexQuote> findById(String ticker) {
    Optional<IexQuote> iexQuote;
    List<IexQuote> quotes = findAllById(Collections.singletonList(ticker));

    if (quotes.size() == 0) {
      return Optional.empty();
    } else if (quotes.size() == 1) {
      iexQuote = Optional.of(quotes.get(0));
    } else {
      throw new DataRetrievalFailureException("Unexpected number of quotes");
    }
    return iexQuote;
  }

  @Override
  public List<IexQuote> findAllById(Iterable<String> tickers) {
    String tickersString = getStringOfTickers(tickers);
    Iterator tickersIterator = tickers.iterator();
    String url = String.format(IEX_BATCH_URL, tickersString);
    List<IexQuote> quotes = new ArrayList<IexQuote>();

    Optional<String> responseBody = executeHttpGet(url);

    if (responseBody.isPresent()) {
      JSONObject quotesJson = new JSONObject(responseBody.get());

      while (tickersIterator.hasNext()) {
        try {
          JSONObject tickerJson = quotesJson.getJSONObject(tickersIterator.next().toString());
          IexQuote quote = JsonTranslator
              .toObject(tickerJson.get("quote").toString(), IexQuote.class, true);
          quotes.add(quote);
        } catch (JSONException e) {
          throw new IllegalArgumentException("Wrong ticker name");
        } catch (Exception e) {
          e.printStackTrace();
          throw new DataRetrievalFailureException(
              "Failed to convert response body to the list of objects");
        }
      }
    } else {
      throw new DataRetrievalFailureException("Failed to retrieve the quotes");
    }

    return quotes;
  }

  private String getStringOfTickers(Iterable<String> tickers) {
    Iterator tickersIterator = tickers.iterator();
    String tickersString = "";

    while (tickersIterator.hasNext()) {
      tickersString += tickersIterator.next();
      if (tickersIterator.hasNext()) {
        tickersString += ",";
      }
    }
    return tickersString;
  }

  /**
   * Execute the GET request and return http body as string
   * <p>
   * TIP: use EntiryUtils.toString() to process HttpBody
   *
   * @param url
   * @return
   */
  private Optional<String> executeHttpGet(String url) {
    HttpGet getRequest = new HttpGet(url);
    CloseableHttpClient client = getHttpClient();
    try {
      HttpResponse response = client.execute(getRequest);
      int status = response.getStatusLine().getStatusCode();
      if (status == 200) {
        return Optional.of(EntityUtils.toString(response.getEntity()));
      } else if (status == 404) {
        return Optional.empty();
      } else {
        throw new DataRetrievalFailureException("Unexpected status code");
      }
    } catch (IOException e) {
      e.printStackTrace();
      throw new DataRetrievalFailureException("Failed to execute GET request");
    }
  }

  /**
   * Borrow HTTP client from the httpClientConnectionManager
   *
   * @return httpClient
   */
  private CloseableHttpClient getHttpClient() {
    return HttpClients.custom()
        .setConnectionManager(httpClientConnectionManager)
        .setConnectionManagerShared(true).build();
  }

  @Override
  public boolean existsById(String s) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public Iterable<IexQuote> findAll() {
    throw new UnsupportedOperationException("Not implemented");
  }


  @Override
  public long count() {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public void deleteById(String s) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public void delete(IexQuote entity) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public void deleteAll(Iterable<? extends IexQuote> entities) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public void deleteAll() {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public <S extends IexQuote> S save(S entity) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public <S extends IexQuote> Iterable<S> saveAll(Iterable<S> entities) {
    throw new UnsupportedOperationException("Not implemented");
  }
}
