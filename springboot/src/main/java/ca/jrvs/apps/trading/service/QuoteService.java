package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dao.MarketDataDao;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.model.domain.IexQuote;
import ca.jrvs.apps.trading.model.domain.Quote;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class QuoteService {
  private static final Logger logger = LoggerFactory.getLogger(QuoteService.class);
  private QuoteDao quoteDao;
  private MarketDataDao marketDataDao;

  @Autowired
  public QuoteService(QuoteDao quoteDao, MarketDataDao marketDataDao) {
    this.quoteDao = quoteDao;
    this.marketDataDao = marketDataDao;
  }

  public void updateMarketData(){
    List<IexQuote> iexQuoteList = new ArrayList<IexQuote>();

    try{
      List<Quote> quotesList = quoteDao.findAll();
      for(Quote quote : quotesList){
        Optional<IexQuote> iexQuote = marketDataDao.findById(quote.getTicker());
        if(iexQuote.isPresent()){
          iexQuoteList.add(iexQuote.get());
        }else{
          throw new RuntimeException("Resource not found");
        }
      }

      quotesList = new ArrayList<Quote>();

      for(IexQuote iexQuote : iexQuoteList){
        Quote quote = buildQuoteFromIexQuote(iexQuote);
        quotesList.add(quote);
      }

      try{
        quoteDao.saveAll(quotesList);
      }catch (IncorrectResultSizeDataAccessException e){
        throw new IllegalArgumentException("Wrong argument. Unable to save quote.");
      }
    }catch(Exception e){

    }
  }

  protected static Quote buildQuoteFromIexQuote(IexQuote iexQuote) {
    Quote quote = new Quote();
    quote.setId(iexQuote.getSymbol());

    if(iexQuote.getLatestPrice() == null){
      quote.setLastPrice(0d);
    }else{
      quote.setLastPrice(iexQuote.getLatestPrice());
    }

    if(iexQuote.getIexBidPrice() == null){
      quote.setBidPrice(0d);
    }else{
      quote.setBidPrice(iexQuote.getIexBidPrice());
    }

    if(iexQuote.getIexBidSize() == null){
      quote.setBidSize(0);
    }else{
      quote.setBidSize(iexQuote.getIexBidSize());
    }

    if(iexQuote.getIexAskPrice() == null){
      quote.setAskPrice(0d);
    }else{
      quote.setAskPrice(iexQuote.getIexAskPrice());
    }

    if(iexQuote.getIexAskSize() == null){
      quote.setAskSize(0);
    }else{
      quote.setAskSize(iexQuote.getIexAskSize());
    }

    return quote;
  }

  public IexQuote findIexQuoteByTicker(String ticker) {
    return marketDataDao.findById(ticker)
        .orElseThrow(() -> new IllegalArgumentException(ticker + " is invalid!"));
  }

  public List<Quote> saveQuotes(List<String> tickers) {
    List<IexQuote> iexQuoteList = marketDataDao.findAllById(tickers);
    List<Quote> quotes = iexQuoteList.stream().map(iexQuote -> buildQuoteFromIexQuote(iexQuote))
        .collect(
            Collectors.toList());

    return quoteDao.saveAll(quotes);
  }

  public Quote saveQuote(String ticker){
    Optional<IexQuote> iexQuote = marketDataDao.findById(ticker);
    if(iexQuote.isPresent()){
      return saveQuote(buildQuoteFromIexQuote(iexQuote.get()));
    }else{
     throw new IllegalArgumentException("Wrong ticker name");
    }
  }

  public Quote saveQuote(Quote quote) {
    return quoteDao.save(quote);
  }

  public List<Quote> findAllQuotes() {
    return quoteDao.findAll();
  }
}
