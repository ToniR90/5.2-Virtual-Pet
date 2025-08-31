import React, { useState } from 'react';
import '../pages/AuthPage.css';
import { useNavigate } from 'react-router-dom';

const RegisterForm = ({ onSwitch }) => {
  const [formData, setFormData] = useState({
    username: '',
    email: '',
    password: '',
    confirmPassword: '',
  });

  const navigate = useNavigate();

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (formData.password !== formData.confirmPassword) {
      alert("Passwords don't match");
      return;
    }

    const payload = {
      username: formData.username,
      email: formData.email,
      password: formData.password,
    };

    try {
      // 🔐 Registre
      const res = await fetch('http://localhost:8080/api/auth/register', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload),
      });

      const data = await res.json();

      if (res.ok) {
        console.log('✅ Usuari registrat correctament:', data.message);

        // 🔐 Login automàtic
        const loginPayload = {
          username: formData.username,
          password: formData.password,
        };

        const loginRes = await fetch('http://localhost:8080/api/auth/login', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(loginPayload),
        });

        const loginData = await loginRes.json();

        if (loginRes.ok && loginData.data && loginData.data.token) {
          localStorage.setItem('token', loginData.data.token);
          console.log('🔓 Sessió iniciada automàticament');
          navigate('/dashboard');
        } else {
          alert('⚠️ Registrat però no s’ha pogut iniciar sessió automàticament');
        }
      } else {
        alert('❌ Error: ' + (data.message || 'No s’ha pogut registrar'));
      }
    } catch (err) {
      console.error('Error inesperat:', err);
      alert('❌ Error inesperat');
    }
  };

  return (
    <div className="register-box">
      <h2>🐉Create an account🐉</h2>
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          name="username"
          placeholder="Username"
          value={formData.username}
          onChange={handleChange}
          required
        />
        <input
          type="email"
          name="email"
          placeholder="Email"
          value={formData.email}
          onChange={handleChange}
          required
        />
        <input
          type="password"
          name="password"
          placeholder="Password"
          value={formData.password}
          onChange={handleChange}
          required
        />
        <input
          type="password"
          name="confirmPassword"
          placeholder="Confirm password"
          value={formData.confirmPassword}
          onChange={handleChange}
          required
        />
        <button type="submit">Create</button>
      </form>
      <p className="register-text">
        Already registered?{' '}
        <button className="link-button" onClick={onSwitch}>
          Sign in
        </button>
      </p>
    </div>
  );
};

export default RegisterForm;