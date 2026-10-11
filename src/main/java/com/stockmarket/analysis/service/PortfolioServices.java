package com.stockmarket.analysis.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stockmarket.analysis.dao.PortfolioDao;
import com.stockmarket.analysis.dao.PortfolioHoldingDao;
import com.stockmarket.analysis.dao.PortfolioTransactionDao;
import com.stockmarket.analysis.dao.StockDao;
import com.stockmarket.analysis.dao.UserDao;
import com.stockmarket.analysis.dto.BuyStockRequest;
import com.stockmarket.analysis.dto.HoldingResponse;
import com.stockmarket.analysis.dto.PortfolioResponse;
import com.stockmarket.analysis.dto.ResponseStructure;
import com.stockmarket.analysis.dto.SellStockRequest;
import com.stockmarket.analysis.dto.TransactionResponse;
import com.stockmarket.analysis.entity.Portfolio;
import com.stockmarket.analysis.entity.PortfolioHolding;
import com.stockmarket.analysis.entity.PortfolioTransaction;
import com.stockmarket.analysis.entity.User;
import com.stockmarket.analysis.enums.TransactionType;
import com.stockmarket.analysis.exception.BadRequestException;
import com.stockmarket.analysis.exception.ResourceNotFoundException;

@Service
public class PortfolioServices {

    @Autowired
    private PortfolioDao portfolioDao;

    @Autowired
    private PortfolioHoldingDao portfolioHoldingDao;

    @Autowired
    private PortfolioTransactionDao portfolioTransactionDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private StockDao stockDao;

    public ResponseEntity<ResponseStructure<PortfolioResponse>> getPortfolio() {

        Portfolio portfolio = getLoggedInPortfolio();

        PortfolioResponse portfolioResponse = new PortfolioResponse();

        portfolioResponse.setId(portfolio.getId());
        portfolioResponse.setCashBalance(portfolio.getCashBalance());

        ResponseStructure<PortfolioResponse> response = new ResponseStructure<PortfolioResponse>();

        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Portfolio fetched successfully");
        response.setData(portfolioResponse);

        return new ResponseEntity<ResponseStructure<PortfolioResponse>>(response, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<ResponseStructure<TransactionResponse>> buyStock(BuyStockRequest request) {

        Portfolio portfolio = getLoggedInPortfolio();

        String symbol = request.getSymbol()
                .trim()
                .toUpperCase();

        String exchange = request.getExchange()
                .trim()
                .toUpperCase();

        JSONObject stock = getLiveStock(symbol, exchange);
        
        BigDecimal price = getStockPrice(stock);
        String companyName = stock.optString("name");

        BigDecimal quantity = request.getQuantity();

        BigDecimal totalAmount = price.multiply(quantity).setScale(2, RoundingMode.HALF_UP);

        if (portfolio.getCashBalance().compareTo(totalAmount) < 0) {
            throw new BadRequestException("Insufficient portfolio balance");
        }

        Optional<PortfolioHolding> optionalHolding = portfolioHoldingDao.getHoldingByPortfolioIdAndSymbol(portfolio.getId(), symbol);

        PortfolioHolding holding;

        if (optionalHolding.isPresent()) {

            holding = optionalHolding.get();

            BigDecimal oldQuantity = holding.getQuantity();
            BigDecimal oldAveragePrice = holding.getAverageBuyPrice();

            BigDecimal oldValue = oldQuantity.multiply(oldAveragePrice);
            BigDecimal newQuantity = oldQuantity.add(quantity);

            BigDecimal newAveragePrice = oldValue.add(totalAmount).divide(newQuantity, 4, RoundingMode.HALF_UP);

            holding.setQuantity(newQuantity);
            holding.setAverageBuyPrice(newAveragePrice);

        } else {

            holding = new PortfolioHolding();

            holding.setPortfolio(portfolio);
            holding.setSymbol(symbol);
            holding.setCompanyName(companyName);
            holding.setQuantity(quantity);
            holding.setAverageBuyPrice(price);
        }

        portfolio.setCashBalance(portfolio.getCashBalance().subtract(totalAmount));

        portfolioHoldingDao.saveHolding(holding);
        portfolioDao.savePortfolio(portfolio);

        PortfolioTransaction transaction = new PortfolioTransaction();

        transaction.setPortfolio(portfolio);
        transaction.setSymbol(symbol);
        transaction.setCompanyName(companyName);
        transaction.setTransactionType(TransactionType.BUY);
        transaction.setQuantity(quantity);
        transaction.setPrice(price);
        transaction.setTotalAmount(totalAmount);

        PortfolioTransaction savedTransaction = portfolioTransactionDao.saveTransaction(transaction);

        TransactionResponse transactionResponse = convertTransaction(savedTransaction);

        ResponseStructure<TransactionResponse> response = new ResponseStructure<TransactionResponse>();

        response.setStatusCode(HttpStatus.CREATED.value());
        response.setMessage("Stock purchased successfully");
        response.setData(transactionResponse);

        return new ResponseEntity<ResponseStructure<TransactionResponse>>(response, HttpStatus.CREATED);
    }

  @Transactional
public ResponseEntity<ResponseStructure<TransactionResponse>> sellStock(SellStockRequest request) {

    Portfolio portfolio = getLoggedInPortfolio();

    String symbol = request.getSymbol().trim().toUpperCase();

    String exchange = request.getExchange() == null
            || request.getExchange().isBlank()
            ? "NSE"
            : request.getExchange().trim().toUpperCase();

    BigDecimal quantity = request.getQuantity();

    Optional<PortfolioHolding> optionalHolding =
            portfolioHoldingDao.getHoldingByPortfolioIdAndSymbol(
                    portfolio.getId(),
                    symbol
            );

    if (optionalHolding.isEmpty()) {
        throw new ResourceNotFoundException("Stock is not available in portfolio");
    }

    PortfolioHolding holding = optionalHolding.get();

    if (holding.getQuantity().compareTo(quantity) < 0) {
        throw new BadRequestException("Insufficient stock quantity");
    }

    JSONObject stock = getLiveStock(symbol, exchange);

    BigDecimal price = getStockPrice(stock);
    String companyName = stock.optString("name");

    BigDecimal totalAmount =
            price.multiply(quantity).setScale(2, RoundingMode.HALF_UP);

    BigDecimal remainingQuantity =
            holding.getQuantity().subtract(quantity);

    if (remainingQuantity.compareTo(BigDecimal.ZERO) == 0) {
        portfolioHoldingDao.deleteHolding(holding);
    } else {
        holding.setQuantity(remainingQuantity);
        portfolioHoldingDao.saveHolding(holding);
    }

    portfolio.setCashBalance(
            portfolio.getCashBalance().add(totalAmount)
    );

    portfolioDao.savePortfolio(portfolio);

    PortfolioTransaction transaction = new PortfolioTransaction();

    transaction.setPortfolio(portfolio);
    transaction.setSymbol(symbol);
    transaction.setCompanyName(companyName);
    transaction.setTransactionType(TransactionType.SELL);
    transaction.setQuantity(quantity);
    transaction.setPrice(price);
    transaction.setTotalAmount(totalAmount);

    PortfolioTransaction savedTransaction =
            portfolioTransactionDao.saveTransaction(transaction);

    TransactionResponse transactionResponse =
            convertTransaction(savedTransaction);

    ResponseStructure<TransactionResponse> response =
            new ResponseStructure<TransactionResponse>();

    response.setStatusCode(HttpStatus.OK.value());
    response.setMessage("Stock sold successfully");
    response.setData(transactionResponse);

    return new ResponseEntity<ResponseStructure<TransactionResponse>>(
            response,
            HttpStatus.OK
    );
}

    public ResponseEntity<ResponseStructure<List<HoldingResponse>>> getHoldings() {

        Portfolio portfolio = getLoggedInPortfolio();

        List<PortfolioHolding> holdings = portfolioHoldingDao.getHoldingsByPortfolioId(portfolio.getId());

        List<HoldingResponse> holdingResponses = new ArrayList<HoldingResponse>();

        for (PortfolioHolding holding : holdings) {
            holdingResponses.add(convertHolding(holding));
        }

        ResponseStructure<List<HoldingResponse>> response = new ResponseStructure<List<HoldingResponse>>();

        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Portfolio holdings fetched successfully");
        response.setData(holdingResponses);

        return new ResponseEntity<ResponseStructure<List<HoldingResponse>>>(response, HttpStatus.OK);
    }

    public ResponseEntity<ResponseStructure<List<TransactionResponse>>> getTransactions() {

        Portfolio portfolio = getLoggedInPortfolio();

        List<PortfolioTransaction> transactions = portfolioTransactionDao.getTransactionsByPortfolioId(portfolio.getId());

        List<TransactionResponse> transactionResponses = new ArrayList<TransactionResponse>();

        for (PortfolioTransaction transaction : transactions) {
            transactionResponses.add(convertTransaction(transaction));
        }

        ResponseStructure<List<TransactionResponse>> response = new ResponseStructure<List<TransactionResponse>>();

        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Portfolio transactions fetched successfully");
        response.setData(transactionResponses);

        return new ResponseEntity<ResponseStructure<List<TransactionResponse>>>(response, HttpStatus.OK);
    }

    private Portfolio getLoggedInPortfolio() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            throw new BadRequestException("User is not authenticated");
        }

        String email = authentication.getName();

        Optional<User> optionalUser = userDao.getUserByEmail(email);

        if (optionalUser.isEmpty()) {
            throw new ResourceNotFoundException("User not found");
        }

        Optional<Portfolio> optionalPortfolio = portfolioDao.getPortfolioByUserId(optionalUser.get().getId());

        if (optionalPortfolio.isEmpty()) {
            throw new ResourceNotFoundException("Portfolio not found");
        }

        return optionalPortfolio.get();
    }

   private JSONObject getLiveStock(String symbol, String exchange) {
    String selectedExchange = exchange == null || exchange.isBlank()
            ? "NSE"
            : exchange.trim();

    String result = stockDao.getStockBySymbol(
            symbol.trim().toUpperCase(),
            selectedExchange
    );

    JSONObject json = new JSONObject(result);

    if (json.has("code")
            || "error".equalsIgnoreCase(json.optString("status"))
            || (json.has("message") && !json.has("symbol"))) {
        throw new BadRequestException(
                "Twelve Data error for " + symbol + ": "
                        + json.optString("message", "Stock data unavailable")
        );
    }

    return json;
}
    private BigDecimal getStockPrice(JSONObject stock) {

        String close = stock.optString("close", "");

        if (close.isEmpty()) {
            throw new BadRequestException("Live stock price is not available");
        }

        return new BigDecimal(close).setScale(2, RoundingMode.HALF_UP);
    }

    private HoldingResponse convertHolding(PortfolioHolding holding) {

        HoldingResponse response = new HoldingResponse();

        response.setId(holding.getId());
        response.setSymbol(holding.getSymbol());
        response.setCompanyName(holding.getCompanyName());
        response.setQuantity(holding.getQuantity());
        response.setAverageBuyPrice(holding.getAverageBuyPrice());

        return response;
    }

    private TransactionResponse convertTransaction(PortfolioTransaction transaction) {

        TransactionResponse response = new TransactionResponse();

        response.setId(transaction.getId());
        response.setSymbol(transaction.getSymbol());
        response.setCompanyName(transaction.getCompanyName());
        response.setTransactionType(transaction.getTransactionType());
        response.setQuantity(transaction.getQuantity());
        response.setPrice(transaction.getPrice());
        response.setTotalAmount(transaction.getTotalAmount());
        response.setCreatedAt(transaction.getCreatedAt());

        return response;
    }
}