package com.stockmarket.analysis.service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.stockmarket.analysis.dao.StockDao;
import com.stockmarket.analysis.dto.ResponseStructure;
import com.stockmarket.analysis.dto.StockHistory;
import com.stockmarket.analysis.dto.StockQuote;
import com.stockmarket.analysis.dto.StockSearch;
import com.stockmarket.analysis.exception.BadRequestException;

@Service
public class StockServices {

    @Autowired
    private StockDao stockDao;

    public ResponseEntity<ResponseStructure<StockQuote>> getStockBySymbol(String symbol, String exchange) {
        if (symbol == null || symbol.isBlank()) {
            throw new BadRequestException("Stock symbol is required");
        }

        String responseData = stockDao.getStockBySymbol(symbol.trim().toUpperCase(), exchange);
        JSONObject jsonObject = new JSONObject(responseData);

        if (jsonObject.has("code") || (jsonObject.has("status") && !"ok".equalsIgnoreCase(jsonObject.optString("status")))) {
            String message = jsonObject.optString("message", "Unable to fetch stock data");
            throw new BadRequestException(message);
        }

        StockQuote stockQuote = new StockQuote();

        stockQuote.setSymbol(jsonObject.optString("symbol"));
        stockQuote.setName(jsonObject.optString("name"));
        stockQuote.setExchange(jsonObject.optString("exchange"));
        stockQuote.setCurrency(jsonObject.optString("currency"));
        stockQuote.setDatetime(jsonObject.optString("datetime"));
        stockQuote.setOpen(getDouble(jsonObject, "open"));
        stockQuote.setHigh(getDouble(jsonObject, "high"));
        stockQuote.setLow(getDouble(jsonObject, "low"));
        stockQuote.setClose(getDouble(jsonObject, "close"));
        stockQuote.setPreviousClose(getDouble(jsonObject, "previous_close"));
        stockQuote.setChange(getDouble(jsonObject, "change"));
        stockQuote.setPercentChange(getDouble(jsonObject, "percent_change"));
        stockQuote.setVolume(getLong(jsonObject, "volume"));
        stockQuote.setMarketOpen(jsonObject.optBoolean("is_market_open", false));

        JSONObject fiftyTwoWeek = jsonObject.optJSONObject("fifty_two_week");

        if (fiftyTwoWeek != null) {
            stockQuote.setWeek52Low(getDouble(fiftyTwoWeek, "low"));
            stockQuote.setWeek52High(getDouble(fiftyTwoWeek, "high"));
        }

        ResponseStructure<StockQuote> response = new ResponseStructure<StockQuote>();
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Stock data fetched successfully");
        response.setData(stockQuote);

        return new ResponseEntity<ResponseStructure<StockQuote>>(response, HttpStatus.OK);
    }

    public ResponseEntity<ResponseStructure<List<StockHistory>>> getStockHistory(String symbol, Integer outputSize, String exchange) {
        if (symbol == null || symbol.isBlank()) {
            throw new BadRequestException("Stock symbol is required");
        }

        if (outputSize == null || outputSize < 1 || outputSize > 5000) {
            throw new BadRequestException("Output size must be between 1 and 5000");
        }

        String responseData = stockDao.getStockHistory(symbol.trim().toUpperCase(), outputSize, exchange);
        JSONObject jsonObject = new JSONObject(responseData);

        if (jsonObject.has("code") || (jsonObject.has("status") && !"ok".equalsIgnoreCase(jsonObject.optString("status")))) {
            String message = jsonObject.optString("message", "Unable to fetch stock history");
            throw new BadRequestException(message);
        }

        JSONArray values = jsonObject.optJSONArray("values");
        List<StockHistory> stockHistoryList = new ArrayList<StockHistory>();

        JSONObject meta = jsonObject.optJSONObject("meta");
        String historySymbol = meta != null ? meta.optString("symbol", symbol.trim().toUpperCase()) : symbol.trim().toUpperCase();

        if (values != null) {
            for (int i = 0; i < values.length(); i++) {
                JSONObject value = values.getJSONObject(i);

                StockHistory stockHistory = new StockHistory();

                stockHistory.setSymbol(historySymbol);
                stockHistory.setDatetime(value.optString("datetime"));
                stockHistory.setOpen(getDouble(value, "open"));
                stockHistory.setHigh(getDouble(value, "high"));
                stockHistory.setLow(getDouble(value, "low"));
                stockHistory.setClose(getDouble(value, "close"));
                stockHistory.setVolume(getLong(value, "volume"));

                stockHistoryList.add(stockHistory);
            }
        }

        ResponseStructure<List<StockHistory>> response = new ResponseStructure<List<StockHistory>>();
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Stock history fetched successfully");
        response.setData(stockHistoryList);

        return new ResponseEntity<ResponseStructure<List<StockHistory>>>(response, HttpStatus.OK);
    }

    public ResponseEntity<ResponseStructure<List<StockSearch>>> searchStock(String query) {
        if (query == null || query.isBlank()) {
            throw new BadRequestException("Search query is required");
        }

        String responseData = stockDao.searchStock(query.trim());
        JSONObject jsonObject = new JSONObject(responseData);
        JSONArray data = jsonObject.optJSONArray("data");

        List<StockSearch> stockSearchList = new ArrayList<StockSearch>();

        if (data != null) {
            for (int i = 0; i < data.length(); i++) {
                JSONObject value = data.getJSONObject(i);

                StockSearch stockSearch = new StockSearch();

                stockSearch.setSymbol(value.optString("symbol"));
                stockSearch.setInstrumentName(value.optString("instrument_name"));
                stockSearch.setExchange(value.optString("exchange"));
                stockSearch.setMicCode(value.optString("mic_code"));
                stockSearch.setCountry(value.optString("country"));
                stockSearch.setCurrency(value.optString("currency"));
                stockSearch.setInstrumentType(value.optString("instrument_type"));

                stockSearchList.add(stockSearch);
            }
        }

        ResponseStructure<List<StockSearch>> response = new ResponseStructure<List<StockSearch>>();
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Stock search completed successfully");
        response.setData(stockSearchList);

        return new ResponseEntity<ResponseStructure<List<StockSearch>>>(response, HttpStatus.OK);
    }

    private Double getDouble(JSONObject jsonObject, String key) {
        if (!jsonObject.has(key) || jsonObject.isNull(key)) {
            return null;
        }

        return jsonObject.optDouble(key);
    }

    private Long getLong(JSONObject jsonObject, String key) {
        if (!jsonObject.has(key) || jsonObject.isNull(key)) {
            return null;
        }

        return jsonObject.optLong(key);
    }
}