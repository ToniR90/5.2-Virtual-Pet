import React, { useEffect, useState } from 'react';
import { getToken } from '../utils/auth';
import dashboardBg from '../assets/dashboard.jpg'; // Imatge de fons
import './MyPets.css';

// Obté el sprite segons tipus i etapa
const getSpritePath = (type, stage) => {
  try {
    return require(`../assets/sprites/${type}-${stage}.png`);
  } catch {
    return null;
  }
};

const MyPets = () => {
  const [pets, setPets] = useState([]);
  const [loading, setLoading] = useState(true);
  const [message, setMessage] = useState('');

  useEffect(() => {
    const token = getToken();
    fetch('http://localhost:8080/api/pets', {
      headers: { Authorization: `Bearer ${token}` },
    })
      .then(res => res.json())
      .then(data => {
        setPets(data.data || []);
        setLoading(false);
      })
      .catch(err => {
        console.error('Error carregant mascotes:', err);
        setMessage('❌ No s’han pogut carregar les mascotes');
        setLoading(false);
      });
  }, []);

  const containerStyle = {
    backgroundImage: `url(${dashboardBg})`,
    backgroundSize: 'cover',
    backgroundPosition: 'center',
    backgroundRepeat: 'no-repeat',
    minHeight: '100vh',
    paddingTop: '2rem',
  };

  return (
    <div style={containerStyle}>
      <div className="pets-box">
        <h2>🐾 Les meves mascotes</h2>
        {loading && <p>Carregant mascotes...</p>}
        {message && <p>{message}</p>}
        {!loading && pets.length === 0 && <p>Encara no tens cap mascota 🐾</p>}
        <div className="pets-grid">
          {pets.map(pet => {
            const spritePath = getSpritePath(pet.type, pet.stage);
            return (
              <div key={pet.id} className="pet-card">
                {spritePath && (
                  <img
                    src={spritePath}
                    alt={`${pet.name} sprite`}
                    className="pet-sprite"
                  />
                )}
                <h3>{pet.name}</h3>
                <button className="select-button">Select</button>
              </div>
            );
          })}
        </div>
      </div>
    </div>
  );
};

export default MyPets;