import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { loginUser } from "../services/authService";

function Login() {
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    email: "",
    password: ""
  });

  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const handleChange = (event) => {
    setFormData({
      ...formData,
      [event.target.name]: event.target.value
    });
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    setError("");
    setLoading(true);

    try {
      const response = await loginUser(formData);

      const token =
        response?.data?.token ||
        response?.token ||
        response?.data?.jwt ||
        response?.jwt ||
        (typeof response?.data === "string" ? response.data : null);

      if (!token) {
        throw new Error("JWT token was not received from server");
      }

      localStorage.setItem("token", token);
      navigate("/dashboard");
    } catch (error) {
      setError(error.response?.data?.message || error.message || "Login failed");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-page">
      <div className="auth-left">
        <div className="brand">
          <div className="brand-icon">₹</div>
          <span>StockVision</span>
        </div>

        <div className="auth-intro">
          <p className="eyebrow">SMART MARKET ANALYSIS</p>
          <h1>Invest with better market insights.</h1>
          <p>
            Track live stock prices, analyze market trends and manage your
            portfolio from one powerful platform.
          </p>
        </div>

        <div className="market-preview">
          <div>
            <span>NIFTY 50</span>
            <strong>24,768.35</strong>
          </div>
          <span className="positive">+0.82%</span>
        </div>
      </div>

      <div className="auth-right">
        <form className="auth-card" onSubmit={handleSubmit}>
          <div className="mobile-brand">
            <div className="brand-icon">₹</div>
            <span>StockVision</span>
          </div>

          <p className="eyebrow">WELCOME BACK</p>
          <h2>Sign in to your account</h2>
          <p className="auth-subtitle">
            Enter your credentials to access your portfolio.
          </p>

          {error && <div className="error-message">{error}</div>}

          <label>Email Address</label>
          <input
            type="email"
            name="email"
            value={formData.email}
            onChange={handleChange}
            placeholder="Enter your email"
            required
          />

          <label>Password</label>
          <input
            type="password"
            name="password"
            value={formData.password}
            onChange={handleChange}
            placeholder="Enter your password"
            required
          />

          <button className="primary-button" type="submit" disabled={loading}>
            {loading ? "Signing in..." : "Sign In"}
          </button>

          <p className="auth-footer">
            Don't have an account? <Link to="/register">Create account</Link>
          </p>
        </form>
      </div>
    </div>
  );
}

export default Login;