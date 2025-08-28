import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom'; // ✅ Necessari per redirigir
import background from '../assets/background.jpg';
import './AuthPage.css';
import RegisterForm from '../components/RegisterForm';

const AuthPage = () => {
  const [isRegistering, setIsRegistering] = useState(false);
  const [formData, setFormData] = useState({ username: '', password: '' }); // ✅ Captura inputs
  const navigate = useNavigate(); // ✅ Per redirigir després del login

  const containerStyle = {
    backgroundImage: `url(${background})`,
    backgroundSize: 'contain',
    backgroundRepeat: 'no-repeat',
    backgroundPosition: 'center',
    backgroundColor: 'black',
    height: '100vh',
    width: '100vw',
    overflow: 'hidden',
    margin: 0,
    padding: 0,
    position: 'relative',
  };

  // ✅ Aquesta funció s'executa quan l'usuari fa "Sign In"
  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await fetch('http://localhost:3001/api/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(formData),
      });

      const data = await response.json();

      if (response.ok) {
        localStorage.setItem('token', data.token); // ✅ Guarda el token
        navigate('/dashboard'); // ✅ Redirigeix a la vista protegida
      } else {
        alert(data.message || 'Login failed');
      }
    } catch (error) {
      console.error('Error during login:', error);
    }
  };

  // ✅ Actualitza el valor dels inputs
  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  return (
    <div style={containerStyle}>
      {isRegistering ? (
        <RegisterForm onSwitch={() => setIsRegistering(false)} />
      ) : (
        <div className="auth-box">
          <h2>🐉Welcome to your Virtual Pet Manager!!🐉</h2>
          <form onSubmit={handleSubmit}> {/* ✅ Afegim la funció de submit */}
            <input
              type="text"
              name="username"
              placeholder="User"
              value={formData.username}
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
            <button type="submit">Sign In</button>
          </form>
          <p className="register-text">
            Don't have an account?{' '}
            <button className="link-button" onClick={() => setIsRegistering(true)}>
              Click here
            </button>
          </p>
        </div>
      )}
    </div>
  );
};

export default AuthPage;