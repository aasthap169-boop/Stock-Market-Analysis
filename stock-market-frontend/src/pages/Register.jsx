import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import Swal from "sweetalert2";
import { registerUser } from "../services/authService";

function Register() {
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    firstName: "",
    lastName: "",
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
      await registerUser(formData);

      await Swal.fire({
  icon: "success",
  title: "Registration Successful!",
  text: "Your account has been created successfully.",
  showConfirmButton: false,
  timer: 1000,
  timerProgressBar: true,
  background: "#111820",
  color: "#f1f5f9"
});

navigate("/login");
    } catch (error) {
      setError(
        error.response?.data?.message ||
          error.message ||
          "Registration failed"
      );
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
          <p className="eyebrow">START YOUR JOURNEY</p>
          <h1>Your market. Your portfolio. Your decisions.</h1>
          <p>
            Create your account and start exploring live market information
            and portfolio management.
          </p>
        </div>

        <div className="market-preview">
          <div>
            <span>MARKET STATUS</span>
            <strong>Live Market Data</strong>
          </div>
          <span className="positive">● LIVE</span>
        </div>
      </div>

      <div className="auth-right">
        <form className="auth-card" onSubmit={handleSubmit}>
          <div className="mobile-brand">
            <div className="brand-icon">₹</div>
            <span>StockVision</span>
          </div>

          <p className="eyebrow">GET STARTED</p>
          <h2>Create your account</h2>
          <p className="auth-subtitle">
            Register to start managing your investment portfolio.
          </p>

          {error && <div className="error-message">{error}</div>}

          <div className="input-row">
            <div>
              <label>First Name</label>
              <input
                type="text"
                name="firstName"
                value={formData.firstName}
                onChange={handleChange}
                placeholder="First name"
                required
              />
            </div>

            <div>
              <label>Last Name</label>
              <input
                type="text"
                name="lastName"
                value={formData.lastName}
                onChange={handleChange}
                placeholder="Last name"
                required
              />
            </div>
          </div>

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
            placeholder="Create a password"
            required
          />

          <button className="primary-button" type="submit" disabled={loading}>
            {loading ? "Creating account..." : "Create Account"}
          </button>

          <p className="auth-footer">
            Already have an account? <Link to="/login">Sign in</Link>
          </p>
        </form>
      </div>
    </div>
  );
}

export default Register;