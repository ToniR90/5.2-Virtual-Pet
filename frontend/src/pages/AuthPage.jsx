import React from 'react';
import background from '../assets/background.jpg';
import './AuthPage.css';

const AuthPage = () => {
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
      <div className="auth-box">
        <h2>Welcome to your Virtual Pet Manager 🐉</h2>
        <form>
          <input type="text" placeholder="User" />
          <input type="password" placeholder="Password" />
          <button type="submit">Sign In</button>
        </form>
        <p className="register-text">
          Not registered? <button className="link-button">Click here!</button>
        </p>
      </div>
    </div>
  );
};

export default AuthPage;