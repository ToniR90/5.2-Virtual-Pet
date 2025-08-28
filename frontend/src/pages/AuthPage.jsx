import React, { useState } from 'react';
import background from '../assets/background.jpg';
import './AuthPage.css';
import RegisterForm from '../components/RegisterForm';

const AuthPage = () => {
  const [isRegistering, setIsRegistering] = useState(false);

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

  return (
    <div style={containerStyle}>
      {isRegistering ? (
        <RegisterForm onSwitch={() => setIsRegistering(false)} />
      ) : (
        <div className="auth-box">
          <h2>Inicia sessió</h2>
          <form>
            <input type="text" placeholder="Usuari" />
            <input type="password" placeholder="Contrasenya" />
            <button type="submit">Entrar</button>
          </form>
          <p className="register-text">
            No estàs registrat?{' '}
            <button className="link-button" onClick={() => setIsRegistering(true)}>
              Registra't aquí!
            </button>
          </p>
        </div>
      )}
    </div>
  );
};

export default AuthPage;