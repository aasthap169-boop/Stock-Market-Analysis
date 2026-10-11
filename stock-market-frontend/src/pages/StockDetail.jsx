import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { getStockHistory, getStockQuote } from "../services/stockService";

function StockDetail() {
  const navigate = useNavigate();
  const { symbol } = useParams();

  const [stock, setStock] = useState(null);
  const [history, setHistory] = useState([]);
  const [loading, setLoading] = useState(true);
  const [historyLoading, setHistoryLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    loadStock();
  }, [symbol]);

  const loadStock = async () => {
    try {
      setLoading(true);
      setHistoryLoading(true);
      setError("");

      const quoteResponse = await getStockQuote(symbol, "NSE");
      setStock(quoteResponse.data);

      try {
        const historyResponse = await getStockHistory(symbol, 90, "NSE");
        setHistory(historyResponse.data || []);
      } catch {
        setHistory([]);
      }
    } catch (error) {
      if (error.response?.status === 401) {
        localStorage.removeItem("token");
        navigate("/login");
        return;
      }
      setError(error.response?.data?.message || "Unable to load stock analysis");
    } finally {
      setLoading(false);
      setHistoryLoading(false);
    }
  };

  const currency = (value) => new Intl.NumberFormat("en-IN", {
    style: "currency",
    currency: "INR",
    maximumFractionDigits: 2
  }).format(value || 0);

  const number = (value) => new Intl.NumberFormat("en-IN", {
    maximumFractionDigits: 2
  }).format(value || 0);

  const points = () => {
    const values = history.map(item => Number(item.close)).filter(value => !Number.isNaN(value)).reverse();
    if (!values.length) return "";
    const width = 1000;
    const height = 330;
    const padding = 28;
    const min = Math.min(...values);
    const max = Math.max(...values);
    const range = max - min || 1;
    return values.map((value, index) => {
      const x = padding + (index / Math.max(values.length - 1, 1)) * (width - padding * 2);
      const y = height - padding - ((value - min) / range) * (height - padding * 2);
      return `${x},${y}`;
    }).join(" ");
  };

  const areaPoints = () => {
    const value = points();
    return value ? `28,302 ${value} 972,302` : "";
  };

  return (
    <div className="dashboard-layout">
      <aside className="sidebar">
        <div className="sidebar-brand">
          <div className="brand-icon">₹</div>
          <span>StockVision</span>
        </div>

        <nav className="sidebar-nav">
          <div className="nav-item" onClick={() => navigate("/dashboard")}><span>⌂</span>Dashboard</div>
          <div className="nav-item" onClick={() => navigate("/markets")}><span>▣</span>Markets</div>
          <div className="nav-item" onClick={() => navigate("/portfolio")}><span>◈</span>Portfolio</div>
        </nav>

        <button className="logout-button" onClick={() => { localStorage.removeItem("token"); navigate("/login"); }}>
          <span>↪</span>Logout
        </button>
      </aside>

      <main className="dashboard-main">
        <header className="dashboard-header">
          <div>
            <p className="dashboard-eyebrow">STOCK ANALYSIS</p>
            <h1>{symbol?.toUpperCase()} Research</h1>
            <p className="dashboard-subtitle">Live quote, price trend and key market statistics.</p>
          </div>
          <button className="detail-back-button" onClick={() => navigate("/markets")}>← Markets</button>
        </header>

        {error && <div className="dashboard-error">{error}</div>}

        {loading ? (
          <div className="market-loading"><div className="loading-spinner"></div><p>Loading stock analysis...</p></div>
        ) : stock ? (
          <>
            <section className="detail-hero">
              <div className="detail-company">
                <div className="large-stock-symbol">{stock.symbol.substring(0, 2)}</div>
                <div>
                  <p className="market-symbol">{stock.symbol}</p>
                  <h2>{stock.name}</h2>
                  <span>{stock.exchange} · {stock.currency}</span>
                </div>
              </div>
              <div className="detail-price">
                <strong>{currency(stock.close)}</strong>
                <span className={stock.change >= 0 ? "stock-positive" : "stock-negative"}>
                  {stock.change >= 0 ? "+" : ""}{number(stock.change)} ({stock.percentChange >= 0 ? "+" : ""}{number(stock.percentChange)}%)
                </span>
              </div>
            </section>

            <div className="detail-tabs">
              <span className="active">Overview</span>
              <span>Technical</span>
              <span>Fundamentals</span>
              <span>Financials</span>
              <span>News</span>
            </div>

            <section className="market-stats-grid detail-stats-grid">
              <div className="market-stat-card"><span>Open</span><strong>{currency(stock.open)}</strong></div>
              <div className="market-stat-card"><span>Previous Close</span><strong>{currency(stock.previousClose)}</strong></div>
              <div className="market-stat-card"><span>Day High</span><strong>{currency(stock.high)}</strong></div>
              <div className="market-stat-card"><span>Day Low</span><strong>{currency(stock.low)}</strong></div>
              <div className="market-stat-card"><span>52W High</span><strong>{currency(stock.week52High)}</strong></div>
              <div className="market-stat-card"><span>52W Low</span><strong>{currency(stock.week52Low)}</strong></div>
              <div className="market-stat-card"><span>Volume</span><strong>{number(stock.volume)}</strong></div>
              <div className="market-stat-card"><span>Market Status</span><strong>{stock.marketOpen ? "OPEN" : "CLOSED"}</strong></div>
            </section>

            <section className="dashboard-panel detail-chart-panel">
              <div className="panel-header">
                <div><p className="panel-eyebrow">PRICE TREND</p><h2>90 Day Price History</h2></div>
                <span className="chart-badge">{stock.symbol}</span>
              </div>
              {historyLoading ? (
                <div className="market-loading chart-loading"><div className="loading-spinner small"></div><p>Loading historical prices...</p></div>
              ) : history.length ? (
                <div className="chart-container">
                  <svg viewBox="0 0 1000 330" preserveAspectRatio="none" className="price-chart detail-price-chart">
                    <line x1="28" y1="82" x2="972" y2="82" className="chart-grid-line" />
                    <line x1="28" y1="165" x2="972" y2="165" className="chart-grid-line" />
                    <line x1="28" y1="248" x2="972" y2="248" className="chart-grid-line" />
                    <polygon points={areaPoints()} className="chart-area" />
                    <polyline points={points()} fill="none" className="chart-line" />
                  </svg>
                  <div className="chart-labels"><span>{history[history.length - 1]?.datetime}</span><span>{history[Math.floor(history.length / 2)]?.datetime}</span><span>{history[0]?.datetime}</span></div>
                </div>
              ) : (
                <div className="empty-panel"><p>Historical data is temporarily unavailable.</p></div>
              )}
            </section>

            <section className="dashboard-panel">
              <div className="panel-header">
                <div><p className="panel-eyebrow">HISTORICAL DATA</p><h2>Recent Prices</h2></div>
              </div>
              <div className="transactions-table-wrapper">
                <table className="transactions-table">
                  <thead><tr><th>Date</th><th>Open</th><th>High</th><th>Low</th><th>Close</th><th>Volume</th></tr></thead>
                  <tbody>
                    {history.slice(0, 20).map(item => (
                      <tr key={`${item.symbol}-${item.datetime}`}><td>{item.datetime}</td><td>{currency(item.open)}</td><td>{currency(item.high)}</td><td>{currency(item.low)}</td><td><strong>{currency(item.close)}</strong></td><td>{number(item.volume)}</td></tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </section>
          </>
        ) : null}
      </main>
    </div>
  );
}

export default StockDetail;
