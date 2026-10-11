import { useEffect, useRef, useState } from "react";
import { useNavigate } from "react-router-dom";
import {
  getHoldings,
  getPortfolio,
  getTransactions
} from "../services/portfolioService";
import { getStockQuote, searchStocks } from "../services/stockService";

function Dashboard() {
  const navigate = useNavigate();

  const [portfolio, setPortfolio] = useState(null);
  const [holdings, setHoldings] = useState([]);
  const [transactions, setTransactions] = useState([]);
  const [search, setSearch] = useState("");
  const [searchResults, setSearchResults] = useState([]);
  const [selectedStock, setSelectedStock] = useState(null);
  const [loading, setLoading] = useState(true);
  const [stockLoading, setStockLoading] = useState(false);
  const [error, setError] = useState("");

  const searchRequestRef = useRef(0);

  useEffect(() => {
    loadDashboard();
  }, []);

  const loadDashboard = async () => {
    try {
      setLoading(true);
      setError("");

      const [portfolioResponse, holdingsResponse, transactionsResponse] =
        await Promise.all([
          getPortfolio(),
          getHoldings(),
          getTransactions()
        ]);

      setPortfolio(portfolioResponse.data);
      setHoldings(holdingsResponse.data || []);
      setTransactions(transactionsResponse.data || []);
    } catch (error) {
      if (error.response?.status === 401) {
        localStorage.removeItem("token");
        navigate("/login");
        return;
      }

      setError(
        error.response?.data?.message ||
          "Unable to load dashboard data"
      );
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = async (event) => {
    const value = event.target.value;
    const requestId = ++searchRequestRef.current;

    setSearch(value);
    setError("");

    if (value.trim().length < 2) {
      setSearchResults([]);
      return;
    }

    try {
      const response = await searchStocks(value);

      if (requestId !== searchRequestRef.current) {
        return;
      }

      setSearchResults(response.data || []);
    } catch (error) {
      if (requestId === searchRequestRef.current) {
        setSearchResults([]);
      }
    }
  };

  const handleStockSelect = async (stock) => {
    try {
      setStockLoading(true);
      setSearchResults([]);

      const response = await getStockQuote(stock.symbol, stock.exchange);

      setSelectedStock(response.data);
      setSearch(stock.symbol);
    } catch (error) {
      setError(
        error.response?.data?.message ||
          "Unable to fetch stock information"
      );
    } finally {
      setStockLoading(false);
    }
  };

  const handleLogout = () => {
    localStorage.removeItem("token");
    navigate("/login");
  };

  const formatCurrency = (value) => {
    return new Intl.NumberFormat("en-IN", {
      style: "currency",
      currency: "INR",
      maximumFractionDigits: 2
    }).format(value || 0);
  };

  const formatNumber = (value) => {
    return new Intl.NumberFormat("en-IN", {
      maximumFractionDigits: 2
    }).format(value || 0);
  };

  if (loading) {
    return (
      <div className="dashboard-loading">
        <div className="loading-spinner"></div>
        <p>Loading your market dashboard...</p>
      </div>
    );
  }

  return (
    <div className="dashboard-layout">
      <aside className="sidebar">
        <div className="sidebar-brand">
          <div className="brand-icon">₹</div>
          <span>StockVision</span>
        </div>

        <nav className="sidebar-nav">
          <div className="nav-item active">
            <span>⌂</span>
            Dashboard
          </div>

         <div className="nav-item" onClick={() => navigate("/markets")}>
            <span>▣</span>
            Markets
         </div>

          <div className="nav-item" onClick={() => navigate("/portfolio")}>
            <span>◈</span>
            Portfolio
          </div>

          <div className="nav-item">
            <span>↔</span>
            Transactions
          </div>
        </nav>

        <button className="logout-button" onClick={handleLogout}>
          <span>↪</span>
          Logout
        </button>
      </aside>

      <main className="dashboard-main">
        <header className="dashboard-header">
          <div>
            <p className="dashboard-eyebrow">MARKET OVERVIEW</p>
            <h1>Good to see you again 👋</h1>
            <p className="dashboard-subtitle">
              Track your portfolio and explore live market data.
            </p>
          </div>

          <div className="live-status">
            <span></span>
            Live Market
          </div>
        </header>

        {error && <div className="dashboard-error">{error}</div>}

        <section className="search-section">
          <div className="search-box">
            <span>⌕</span>

            <input
              value={search}
              onChange={handleSearch}
              placeholder="Search stocks by symbol or company..."
            />
          </div>

          {searchResults.length > 0 && search.trim().length >= 2 && (
            <div className="search-results">
              {searchResults.slice(0, 7).map((stock) => (
                <button
                  key={`${stock.symbol}-${stock.exchange}`}
                  onClick={() => handleStockSelect(stock)}
                >
                  <div>
                    <strong>{stock.symbol}</strong>
                    <span>{stock.instrumentName}</span>
                  </div>

                  <div className="search-exchange">
                    {stock.exchange}
                  </div>
                </button>
              ))}
            </div>
          )}
        </section>

        <section className="summary-grid">
          <div className="summary-card">
            <div className="summary-icon balance-icon">₹</div>

            <div>
              <p>Available Balance</p>
              <h2>{formatCurrency(portfolio?.cashBalance)}</h2>
            </div>
          </div>

          <div className="summary-card">
            <div className="summary-icon holding-icon">◈</div>

            <div>
              <p>Total Holdings</p>
              <h2>{holdings.length}</h2>
              <span className="summary-small">
                Stocks in portfolio
              </span>
            </div>
          </div>

          <div className="summary-card">
            <div className="summary-icon transaction-icon">↔</div>

            <div>
              <p>Transactions</p>
              <h2>{transactions.length}</h2>
              <span className="summary-small">
                Recorded transactions
              </span>
            </div>
          </div>
        </section>

        <section className="dashboard-grid">
          <div className="dashboard-panel stock-panel">
            <div className="panel-header">
              <div>
                <p className="panel-eyebrow">LIVE MARKET</p>
                <h2>Stock Analysis</h2>
              </div>

              {selectedStock && (
                <button className="stock-symbol stock-open-button" onClick={() => navigate(`/stock/${selectedStock.symbol}`)}>
                  Open Analysis ↗
                </button>
              )}
            </div>

            {!selectedStock && !stockLoading && (
              <div className="empty-stock">
                <div className="empty-icon">⌕</div>

                <h3>Search for a stock</h3>

                <p>
                  Search for a company or stock symbol above to view
                  live market information.
                </p>
              </div>
            )}

            {stockLoading && (
              <div className="empty-stock">
                <div className="loading-spinner small"></div>
                <p>Fetching live stock data...</p>
              </div>
            )}

            {selectedStock && !stockLoading && (
              <div className="stock-details">
                <div className="stock-main">
                  <div>
                    <span className="stock-name">
                      {selectedStock.name}
                    </span>

                    <span className="stock-exchange">
                      {selectedStock.exchange} ·{" "}
                      {selectedStock.currency}
                    </span>
                  </div>

                  <div className="stock-price">
                    <strong>
                      {formatCurrency(selectedStock.close)}
                    </strong>

                    <span
                      className={
                        selectedStock.change >= 0
                          ? "stock-positive"
                          : "stock-negative"
                      }
                    >
                      {selectedStock.change >= 0 ? "+" : ""}
                      {formatNumber(selectedStock.change)} (
                      {selectedStock.percentChange >= 0 ? "+" : ""}
                      {formatNumber(selectedStock.percentChange)}%)
                    </span>
                  </div>
                </div>

                <div className="stock-stats">
                  <div>
                    <span>Open</span>
                    <strong>
                      {formatCurrency(selectedStock.open)}
                    </strong>
                  </div>

                  <div>
                    <span>High</span>
                    <strong>
                      {formatCurrency(selectedStock.high)}
                    </strong>
                  </div>

                  <div>
                    <span>Low</span>
                    <strong>
                      {formatCurrency(selectedStock.low)}
                    </strong>
                  </div>

                  <div>
                    <span>Previous Close</span>
                    <strong>
                      {formatCurrency(selectedStock.previousClose)}
                    </strong>
                  </div>

                  <div>
                    <span>52W High</span>
                    <strong>
                      {formatCurrency(selectedStock.week52High)}
                    </strong>
                  </div>

                  <div>
                    <span>52W Low</span>
                    <strong>
                      {formatCurrency(selectedStock.week52Low)}
                    </strong>
                  </div>
                </div>
              

                <div className="stock-analysis-strip">
                  <div>
                    <span>Market</span>
                    <strong>{selectedStock.marketOpen ? "OPEN" : "CLOSED"}</strong>
                  </div>
                  <div>
                    <span>Volume</span>
                    <strong>{formatNumber(selectedStock.volume)}</strong>
                  </div>
                  <div>
                    <span>52W Range</span>
                    <strong>{formatCurrency(selectedStock.week52Low)} — {formatCurrency(selectedStock.week52High)}</strong>
                  </div>
                </div>
                  </div>
            )}
          </div>

          <div className="dashboard-panel">
            <div className="panel-header">
              <div>
                <p className="panel-eyebrow">YOUR ACCOUNT</p>
                <h2>Portfolio Holdings</h2>
              </div>
            </div>

            {holdings.length === 0 ? (
              <div className="empty-panel">
                <div className="empty-icon">◈</div>
                <p>No stocks in your portfolio yet.</p>
              </div>
            ) : (
              <div className="holdings-list">
                {holdings.map((holding) => (
                  <div className="holding-row" key={holding.id}>
                    <div className="holding-symbol">
                      {holding.symbol.substring(0, 2)}
                    </div>

                    <div className="holding-info">
                      <strong>{holding.symbol}</strong>
                      <span>{holding.companyName}</span>
                    </div>

                    <div className="holding-value">
                      <strong>
                        {formatNumber(holding.quantity)}
                      </strong>
                      <span>Shares</span>
                    </div>

                    <div className="holding-price">
                      <strong>
                        {formatCurrency(
                          holding.averageBuyPrice
                        )}
                      </strong>
                      <span>Avg. Price</span>
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>
        </section>

        <section className="dashboard-panel market-tools-panel">
          <div className="panel-header">
            <div>
              <p className="panel-eyebrow">ANALYSIS WORKSPACE</p>
              <h2>Explore StockVision</h2>
            </div>
          </div>

          <div className="analysis-tools-grid">
            <button onClick={() => navigate("/markets")}>
              <span>▣</span>
              <strong>Markets</strong>
              <small>Live prices and historical charts</small>
            </button>
            <button onClick={() => navigate("/portfolio")}>
              <span>◈</span>
              <strong>Portfolio</strong>
              <small>Holdings, P/L and transactions</small>
            </button>
            <button onClick={() => document.querySelector(".search-box input")?.focus()}>
              <span>⌕</span>
              <strong>Stock Search</strong>
              <small>Find a company or market symbol</small>
            </button>
          </div>
        </section>

        <section className="dashboard-panel transactions-panel">
          <div className="panel-header">
            <div>
              <p className="panel-eyebrow">ACTIVITY</p>
              <h2>Recent Transactions</h2>
            </div>
          </div>

          {transactions.length === 0 ? (
            <div className="empty-panel">
              <div className="empty-icon">↔</div>
              <p>No transactions available.</p>
            </div>
          ) : (
            <div className="transactions-table-wrapper">
              <table className="transactions-table">
                <thead>
                  <tr>
                    <th>Stock</th>
                    <th>Type</th>
                    <th>Quantity</th>
                    <th>Price</th>
                    <th>Total</th>
                    <th>Date</th>
                  </tr>
                </thead>

                <tbody>
                  {transactions.slice(0, 6).map((transaction) => (
                    <tr key={transaction.id}>
                      <td>
                        <strong>{transaction.symbol}</strong>
                      </td>

                      <td>
                        <span
                          className={
                            transaction.transactionType === "BUY"
                              ? "transaction-buy"
                              : "transaction-sell"
                          }
                        >
                          {transaction.transactionType}
                        </span>
                      </td>

                      <td>
                        {formatNumber(transaction.quantity)}
                      </td>

                      <td>
                        {formatCurrency(transaction.price)}
                      </td>

                      <td>
                        <strong>
                          {formatCurrency(transaction.totalAmount)}
                        </strong>
                      </td>

                      <td>
                        {transaction.createdAt
                          ? new Date(
                              transaction.createdAt
                            ).toLocaleDateString("en-IN")
                          : "-"}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </section>
      </main>
    </div>
  );
}

export default Dashboard;