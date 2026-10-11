import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import {
  buyStock,
  getHoldings,
  getPortfolio,
  sellStock
} from "../services/portfolioService";

import {
  getStockQuote,
  searchStocks
} from "../services/stockService";

function Portfolio() {

  const navigate = useNavigate();

  const [portfolio, setPortfolio] = useState(null);
  const [holdings, setHoldings] = useState([]);
  const [prices, setPrices] = useState({});

  const [loading, setLoading] = useState(true);
  const [actionLoading, setActionLoading] = useState(false);

  const [error, setError] = useState("");

  const [symbol, setSymbol] = useState("");
  const [selectedStock, setSelectedStock] = useState(null);
  const [quantity, setQuantity] = useState("");
  const [action, setAction] = useState("BUY");

  const [searchResults, setSearchResults] = useState([]);
  const [searchLoading, setSearchLoading] = useState(false);

  const loadPortfolio = async () => {

    try {

      setLoading(true);
      setError("");

      const portfolioResponse = await getPortfolio();
      const holdingsResponse = await getHoldings();

      setPortfolio(portfolioResponse.data);
      setHoldings(holdingsResponse.data || []);

      const priceData = {};

      await Promise.all(
        (holdingsResponse.data || []).map(async (item) => {

          try {

            const response = await getStockQuote(
              item.symbol,
              "NSE"
            );

            priceData[item.symbol] = response.data;

          } catch {

            priceData[item.symbol] = null;

          }

        })
      );

      setPrices(priceData);

    } catch (error) {

      setError(
        error.response?.data?.message ||
        "Unable to load portfolio"
      );

    } finally {

      setLoading(false);

    }

  };

  useEffect(() => {

    loadPortfolio();

  }, []);

  const handleSearch = async (e) => {

    const value = e.target.value;

    setSymbol(value);
    setSelectedStock(null);

    if (value.length < 2) {

      setSearchResults([]);
      return;

    }

    try {

      setSearchLoading(true);

      const response = await searchStocks(value);

      setSearchResults(
        response.data?.slice(0, 8) || []
      );

    } catch {

      setSearchResults([]);

    } finally {

      setSearchLoading(false);

    }

  };

  const selectStock = (stock) => {

    setSymbol(stock.symbol);
    setSelectedStock(stock);
    setSearchResults([]);

  };

  const handleTrade = async (e) => {

    e.preventDefault();

    try {

      setError("");

      if (!selectedStock) {

        setError("Please select stock from dropdown");
        return;

      }

      if (!quantity || Number(quantity) <= 0) {

        setError("Enter valid quantity");
        return;

      }

      setActionLoading(true);

      if (action === "BUY") {

        await buyStock(
  selectedStock.symbol,
  selectedStock.exchange,
  quantity
);

      } else {

        await sellStock(
  selectedStock.symbol,
  selectedStock.exchange,
  quantity
);

      }

      setSymbol("");
      setSelectedStock(null);
      setQuantity("");

      await loadPortfolio();

    } catch (error) {

      setError(
        error.response?.data?.message ||
        "Transaction failed"
      );

    } finally {

      setActionLoading(false);

    }

  };

  const currency = (value) => {

    return new Intl.NumberFormat(
      "en-IN",
      {
        style: "currency",
        currency: "INR"
      }
    ).format(value || 0);

  };

  const investedValue = () => {

    return holdings.reduce(
      (sum, item) =>
        sum +
        Number(item.quantity) *
        Number(item.averageBuyPrice),
      0
    );

  };

  const currentValue = () => {

    return holdings.reduce(
      (sum, item) => {

        const price =
          prices[item.symbol]?.close ||
          item.averageBuyPrice;

        return sum +
          Number(item.quantity) *
          Number(price);

      },
      0
    );

  };

  const profitLoss = () => {

    return currentValue() - investedValue();

  };

  return (

    <div className="dashboard-layout">

      <aside className="sidebar">

        <div className="sidebar-brand">

          <div className="brand-icon">
            ₹
          </div>

          <span>
            StockVision
          </span>

        </div>

        <nav className="sidebar-nav">

          <div
            className="nav-item"
            onClick={() => navigate("/dashboard")}
          >
            ⌂ Dashboard
          </div>

          <div
            className="nav-item"
            onClick={() => navigate("/markets")}
          >
            ▣ Markets
          </div>

          <div className="nav-item active">
            ◈ Portfolio
          </div>

        </nav>

        <button
          className="logout-button"
          onClick={() => {
            localStorage.removeItem("token");
            navigate("/login");
          }}
        >
          Logout
        </button>

      </aside>

      <main className="dashboard-main">

        <header className="dashboard-header">

          <div>

            <p className="dashboard-eyebrow">
              MY INVESTMENTS
            </p>

            <h1>
              My Portfolio
            </h1>

          </div>

        </header>

        {error && (

          <div className="dashboard-error">
            {error}
          </div>

        )}

        {loading ? (

          <div className="market-loading">
            Loading Portfolio...
          </div>

        ) : (

          <>

            <section className="portfolio-summary-grid">

              <div className="portfolio-summary-card">

                <span>
                  Cash Balance
                </span>

                <strong>
                  {currency(portfolio?.cashBalance)}
                </strong>

              </div>

              <div className="portfolio-summary-card">

                <span>
                  Invested
                </span>

                <strong>
                  {currency(investedValue())}
                </strong>

              </div>

              <div className="portfolio-summary-card">

                <span>
                  Current Value
                </span>

                <strong>
                  {currency(currentValue())}
                </strong>

              </div>

              <div className="portfolio-summary-card">

                <span>
                  Profit/Loss
                </span>

                <strong>
                  {currency(profitLoss())}
                </strong>

              </div>

            </section>

            <section className="dashboard-panel portfolio-trade-panel">

              <h2>
                Buy / Sell Stock
              </h2>

              <form
                className="portfolio-trade-form"
                onSubmit={handleTrade}
              >

                <select
                  value={action}
                  onChange={(e) => setAction(e.target.value)}
                >

                  <option value="BUY">
                    BUY
                  </option>

                  <option value="SELL">
                    SELL
                  </option>

                </select>

                <div className="stock-search-field">

                  <input
                    value={symbol}
                    onChange={handleSearch}
                    placeholder="Search stock"
                    autoComplete="off"
                  />

                  {searchLoading && (

                    <div className="portfolio-search-loading">
                      Searching...
                    </div>

                  )}

                  {searchResults.length > 0 && (

                    <div className="portfolio-search-results">

                      {searchResults.map((stock) => (

                        <button
                          type="button"
                          key={
                            stock.symbol +
                            stock.exchange
                          }
                          onClick={() => selectStock(stock)}
                        >

                          <strong>
                            {stock.symbol}
                          </strong>

                          <span>
                            {stock.instrumentName}
                          </span>

                          <small>
                            {stock.exchange}
                          </small>

                        </button>

                      ))}

                    </div>

                  )}

                </div>

                <input
                  type="number"
                  min="0.01"
                  step="0.01"
                  placeholder="Quantity"
                  value={quantity}
                  onChange={(e) => setQuantity(e.target.value)}
                />

                <button
                  type="submit"
                  disabled={actionLoading}
                  className={
                    action === "BUY"
                      ? "portfolio-buy-button"
                      : "portfolio-sell-button"
                  }
                >

                  {actionLoading
                    ? "Processing..."
                    : action}

                </button>

              </form>

            </section>

            <section className="dashboard-panel portfolio-holdings-panel">

              <h2>
                Holdings
              </h2>

              {holdings.length === 0 ? (

                <div className="empty-panel">
                  <p>
                    No stocks in your portfolio yet.
                  </p>
                </div>

              ) : (

                <div className="transactions-table-wrapper">

                  <table className="transactions-table">

                    <thead>

                      <tr>

                        <th>
                          Symbol
                        </th>

                        <th>
                          Quantity
                        </th>

                        <th>
                          Avg Price
                        </th>

                        <th>
                          Current
                        </th>

                      </tr>

                    </thead>

                    <tbody>

                      {holdings.map((item) => (

                        <tr key={item.id}>

                          <td>
                            {item.symbol}
                          </td>

                          <td>
                            {item.quantity}
                          </td>

                          <td>
                            {currency(
                              item.averageBuyPrice
                            )}
                          </td>

                          <td>
                            {currency(
                              prices[item.symbol]?.close ||
                              item.averageBuyPrice
                            )}
                          </td>

                        </tr>

                      ))}

                    </tbody>

                  </table>

                </div>

              )}

            </section>

          </>

        )}

      </main>

    </div>

  );

}

export default Portfolio;