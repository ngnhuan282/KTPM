import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "../styles/Login.css";
import api from "../services/api";

const LoginForm = () => {
    const [username, setUsername] = useState("");   // 🔁 từ email -> username
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError("");

        try {
            // Gửi username + password lên backend
            const res = await api.post("/api/auth/login", {
                username,
                password,
            });

            const data = res.data;
            console.log("Login response:", data);

            if (!data.success) {
                // backend trả success=false
                setError(data.message || "Đăng nhập thất bại!");
                return;
            }

            // ✅ Lưu token + username vào localStorage
            localStorage.setItem("token", data.token);
            localStorage.setItem("username", data.username);

            alert("Đăng nhập thành công!");
            navigate("/products");
        } catch (err) {
            console.error("Login error:", err);
            setError("Đăng nhập thất bại! Vui lòng kiểm tra lại thông tin.");
        }
    };

    return (
        <div className="page-container">
            <form onSubmit={handleSubmit} className="loginForm">
                <h1 className="title">LOGIN</h1>
                {error && <p className="error">{error}</p>}

                <div>
                    <label htmlFor="username">Username:</label>
                    <input
                        type="text"
                        id="username"
                        placeholder="Nhập username..."
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        required
                    />
                </div>

                <div>
                    <label htmlFor="password">Mật khẩu:</label>
                    <input
                        type="password"
                        id="password"
                        placeholder="Nhập mật khẩu..."
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                </div>

                <button type="submit">Đăng nhập</button>

                <p>
                    Chưa có tài khoản?{" "}
                    <a href="/register" className="link">
                        Đăng ký ngay
                    </a>
                </p>
            </form>
        </div>
    );
};

export default LoginForm;
