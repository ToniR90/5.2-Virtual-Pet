import React from 'react';
import { useNavigate } from 'react-router-dom';
import goodbyeBg from '../assets/goodbye.jpg'; // substitueix pel nom real de la imatge
import './GoodbyePage.css';

const GoodbyePage = () => {
  const navigate = useNavigate();

  const containerStyle = {
    backgroundImage: `url(${goodbyeBg})`,
    backgroundSize: 'cover',
    backgroundPosition: 'center',
    backgroundRepeat: 'no-repeat',
    height: '100vh',
    width: '100vw',
    margin: 0,
    padding: 0,
    position: 'relative',
  };

  return (
    <div style={containerStyle}>
      <div className="goodbye-message">
        <h1>Destruction has arrived…</h1>
      </div>

      <button className="return-button" onClick={() => navigate('/')}>
        🔁 Back to login page
      </button>
    </div>
  );
};

export default GoodbyePage;