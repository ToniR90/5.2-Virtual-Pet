import React from 'react';
import background from '../assets/background.jpg';
import './AuthPage.css';

const AuthPage = () => {
  const containerStyle = {
    backgroundImage: `url(${background})`,
    backgroundSize: 'contain',           // mostra la imatge sencera
    backgroundRepeat: 'no-repeat',       // evita repeticions
    backgroundPosition: 'center',        // centra la imatge
    backgroundColor: 'black',            // fons negre per espais buits
    height: '100vh',
    width: '100vw',
    overflow: 'hidden',                  // evita scroll
    margin: 0,
    padding: 0,
    position: 'relative',
  };

  return (
    <div style={containerStyle}>
      <div className="auth-box">
        <h2>Welcome to your Virtual Pet Manager 🐉</h2>
        <button>Login</button>
        <button>Register</button>
      </div>
    </div>
  );
};

export default AuthPage;