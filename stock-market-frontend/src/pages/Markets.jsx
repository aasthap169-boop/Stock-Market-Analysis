import { useEffect, useRef, useState } from "react";
import { useNavigate } from "react-router-dom";
import { getStockHistory, getStockQuote, searchStocks } from "../services/stockService";

function Markets() {
  const navigate = useNavigate();
  const searchRequestRef = useRef(0);

  const [search, setSearch] = useState("");
  const [searchResults, setSearchResults] = useState([]);
  const [selectedStock, setSelectedStock] = useState(null);
  const [history, setHistory] = useState([]);
  const [loading, setLoading] = useState(false);
  const [historyLoading, setHistoryLoading] = useState(false);
  const [error, setError] = useState("");
  const [historyError, setHistoryError] = useState("");

  useEffect(() => {
    loadStock("INFY", "NSE");
  }, []);

  const loadStock = async (symbol, exchange) => {
    try {
      setLoading(true);
      setHistory([]);
      setError("");
      setHistoryError("");
      setSearch(symbol);

      const quoteResponse = await getStockQuote(symbol, exchange);
      setSelectedStock(quoteResponse.data);
      setSearchResults([]);
      setLoading(false);
      setHistoryLoading(true);

      try {
        const historyResponse = await getStockHistory(symbol, 30, exchange);
        setHistory(historyResponse.data || []);
      } catch (error) {
        setHistory([]);
        setHistoryError(error.response?.data?.message || "Historical data is temporarily unavailable");
      } finally {
        setHistoryLoading(false);
      }
    } catch (error) {
      setLoading(false);

      if (error.response?.status === 401) {
        localStorage.removeItem("token");
        navigate("/login");
        return;
      }

      setSelectedStock(null);
      setError(error.response?.data?.message || "Unable to load stock information");
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
    } catch {
      if (requestId === searchRequestRef.current) {
        setSearchResults([]);
      }
    }
  };

  const handleSelectStock = (symbol, exchange) => {
    loadStock(symbol, exchange);
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

  const getChartPoints = () => {
    if (!history.length) {
      return "";
    }

    const values = history.map((item) => Number(item.close)).filter((value) => !Number.isNaN(value)).reverse();

    if (!values.length) {
      return "";
    }

    const width = 900;
    const height = 300;
    const padding = 25;
    const min = Math.min(...values);
    const max = Math.max(...values);
    const range = max - min || 1;

    return values.map((value, index) => {
      const x = padding + (index / Math.max(values.length - 1, 1)) * (width - padding * 2);
      const y = height - padding - ((value - min) / range) * (height - padding * 2);
      return `${x},${y}`;
    }).join(" ");
  };

  const getChartAreaPoints = () => {
    const points = getChartPoints();

    if (!points) {
      return "";
    }

    return `25,275 ${points} 875,275`;
  };

  return (
    <div className="dashboard-layout">
      <aside className="sidebar">
        <div className="sidebar-brand">
          <div className="brand-icon">₹</div>
          <span>StockVision</span>
        </div>

        <nav className="sidebar-nav">
          <div className="nav-item" onClick={() => navigate("/dashboard")}>
            <span>⌂</span>
            Dashboard
          </div>

          <div className="nav-item active">
            <span>▣</span>
            Markets
          </div>

          <div className="nav-item" onClick={() => navigate("/portfolio")}>
            <span>◈</span>
            Portfolio
          </div>

          <div className="nav-item" onClick={() => navigate("/transactions")}>
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
            <p className="dashboard-eyebrow">MARKET ANALYSIS</p>
            <h1>Explore the Markets</h1>
            <p className="dashboard-subtitle">Search stocks and analyze live market information.</p>
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
            <input value={search} onChange={handleSearch} placeholder="Search stock symbol or company..." />
          </div>

          {searchResults.length > 0 && search.trim().length >= 2 && (
            <div className="search-results">
              {searchResults.slice(0, 8).map((stock) => (
                <button key={`${stock.symbol}-${stock.exchange}-${stock.micCode}`} onClick={() => handleSelectStock(stock.symbol, stock.exchange)}>
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

        {loading ? (
          <div className="market-loading">
            <div className="loading-spinner"></div>
            <p>Loading live market data...</p>
          </div>
        ) : selectedStock ? (
          <>
            <section className="market-stock-header">
              <div className="market-company">
                <div className="large-stock-symbol">
                  {selectedStock.symbol.substring(0, 2)}
                </div>

                <div>
                  <p className="market-symbol">{selectedStock.symbol}</p>
                  <h2>{selectedStock.name}</h2>
                  <span>{selectedStock.exchange} · {selectedStock.currency}</span>
                </div>
              </div>

              <div className="market-price">
                <strong>{formatCurrency(selectedStock.close)}</strong>

                <span className={selectedStock.change >= 0 ? "stock-positive" : "stock-negative"}>
                  {selectedStock.change >= 0 ? "+" : ""}
                  {formatNumber(selectedStock.change)} (
                  {selectedStock.percentChange >= 0 ? "+" : ""}
                  {formatNumber(selectedStock.percentChange)}%)
                </span>
              </div>
            </section>

            <section className="market-stats-grid">
              <div className="market-stat-card">
                <span>Open</span>
                <strong>{formatCurrency(selectedStock.open)}</strong>
              </div>

              <div className="market-stat-card">
                <span>High</span>
                <strong>{formatCurrency(selectedStock.high)}</strong>
              </div>

              <div className="market-stat-card">
                <span>Low</span>
                <strong>{formatCurrency(selectedStock.low)}</strong>
              </div>

              <div className="market-stat-card">
                <span>Previous Close</span>
                <strong>{formatCurrency(selectedStock.previousClose)}</strong>
              </div>

              <div className="market-stat-card">
                <span>52 Week High</span>
                <strong>{formatCurrency(selectedStock.week52High)}</strong>
              </div>

              <div className="market-stat-card">
                <span>52 Week Low</span>
                <strong>{formatCurrency(selectedStock.week52Low)}</strong>
              </div>

              <div className="market-stat-card">
                <span>Volume</span>
                <strong>{formatNumber(selectedStock.volume)}</strong>
              </div>

              <div className="market-stat-card">
                <span>Market Status</span>
                <strong>{selectedStock.marketOpen ? "Open" : "Closed"}</strong>
              </div>
            </section>

            <section className="dashboard-panel chart-panel">
              <div className="panel-header">
                <div>
                  <p className="panel-eyebrow">PRICE HISTORY</p>
                  <h2>30 Day Price Movement</h2>
                </div>

                <span className="chart-badge">Daily</span>
              </div>

              {historyLoading ? (
                <div className="market-loading chart-loading">
                  <div className="loading-spinner small"></div>
                  <p>Loading historical prices...</p>
                </div>
              ) : history.length === 0 ? (
                <div className="empty-panel">
                  <div className="empty-icon">⌁</div>
                  <p>{historyError || "No historical data available."}</p>
                </div>
              ) : (
                <div className="chart-container">
                  <svg viewBox="0 0 900 300" preserveAspectRatio="none" className="price-chart">
                    <line x1="25" y1="75" x2="875" y2="75" className="chart-grid-line" />
                    <line x1="25" y1="150" x2="875" y2="150" className="chart-grid-line" />
                    <line x1="25" y1="225" x2="875" y2="225" className="chart-grid-line" />
                    <polygon points={getChartAreaPoints()} className="chart-area" />
                    <polyline points={getChartPoints()} fill="none" className="chart-line" />
                  </svg>

                  <div className="chart-labels">
                    <span>{history[history.length - 1]?.datetime}</span>
                    <span>{history[Math.floor(history.length / 2)]?.datetime}</span>
                    <span>{history[0]?.datetime}</span>
                  </div>
                </div>
              )}
            </section>

            <section className="dashboard-panel history-table-panel">
              <div className="panel-header">
                <div>
                  <p className="panel-eyebrow">HISTORICAL DATA</p>
                  <h2>Recent Price History</h2>
                </div>
              </div>

              {history.length === 0 ? (
                <div className="empty-panel">
                  <p>No historical records available.</p>
                </div>
              ) : (
                <div className="transactions-table-wrapper">
                  <table className="transactions-table">
                    <thead>
                      <tr>
                        <th>Date</th>
                        <th>Open</th>
                        <th>High</th>
                        <th>Low</th>
                        <th>Close</th>
                        <th>Volume</th>
                      </tr>
                    </thead>

                    <tbody>
                      {history.slice(0, 10).map((item) => (
                        <tr key={item.datetime}>
                          <td><strong>{item.datetime}</strong></td>
                          <td>{formatCurrency(item.open)}</td>
                          <td>{formatCurrency(item.high)}</td>
                          <td>{formatCurrency(item.low)}</td>
                          <td><strong>{formatCurrency(item.close)}</strong></td>
                          <td>{formatNumber(item.volume)}</td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              )}
            </section>
          </>
        ) : (
          <div className="market-empty">
            <div className="empty-icon">⌕</div>
            <h2>Search for a stock</h2>
            <p>Enter a company name or stock symbol to explore live market data.</p>
          </div>
        )}
      </main>
    </div>
  );
}

export default Markets;