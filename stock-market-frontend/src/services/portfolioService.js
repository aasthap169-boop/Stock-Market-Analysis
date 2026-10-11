import api from "./api";

export const getPortfolio = async () => {
  const response = await api.get("/portfolio");
  return response.data;
};

export const getHoldings = async () => {
  const response = await api.get("/portfolio/holdings");
  return response.data;
};

export const getTransactions = async () => {
  const response = await api.get("/portfolio/transactions");
  return response.data;
};

export const buyStock = async (symbol, exchange, quantity) => {
  const response = await api.post("/portfolio/buy", {
    symbol,
    exchange,
    quantity
  });
  return response.data;
};

export const sellStock = async (symbol, exchange, quantity) => {
  const response = await api.post("/portfolio/sell", {
    symbol,
    exchange,
    quantity
  });

  return response.data;
};
