import api from "./api";

export const searchStocks = async (query) => {
  const response = await api.get("/stocks/search", {
    params: { query }
  });

  return response.data;
};

export const getStockQuote = async (symbol, exchange) => {
  const response = await api.get(`/stocks/${symbol}`, {
    params: { exchange }
  });

  return response.data;
};

export const getStockHistory = async (symbol, outputSize = 30, exchange) => {
  const response = await api.get(`/stocks/${symbol}/history`, {
    params: {
      outputSize,
      exchange
    }
  });

  return response.data;
};