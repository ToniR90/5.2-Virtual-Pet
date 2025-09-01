import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { getToken } from '../utils/auth';
import dashboardBg from '../assets/dashboard.jpg';
import './MyPets.css';

const getSpritePath = (variant, stage) => {
  try {
    return require(`../assets/sprites/${variant}-${stage}.png`);
  } catch {
    return null;
  }
};

const MyPets = () => {
  const [pets, setPets] = useState([]);
  const [loading, setLoading] = useState(true);
  const [message, setMessage] = useState('');
  const navigate = useNavigate();

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

  const handleDelete = async (petId) => {
    const confirm = window.confirm('Estàs segur que vols eliminar aquesta mascota?');
    if (!confirm) return;

    const token = getToken();
    try {
      const res = await fetch(`http://localhost:8080/api/pets/${petId}`, {
        method: 'DELETE',
        headers: { Authorization: `Bearer ${token}` },
      });

      if (res.ok) {
        setPets(prev => prev.filter(p => p.id !== petId));
      } else {
        console.error('❌ No s’ha pogut eliminar la mascota');
      }
    } catch (err) {
      console.error('❌ Error eliminant mascota:', err);
    }
  };

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
      <button className="back-button" onClick={() => navigate('/dashboard')}>
        ← Tornar
      </button>

      <div className="pets-box">
        <h2>🐾 Les meves mascotes</h2>
        {loading && <p>Carregant mascotes...</p>}
        {message && <p>{message}</p>}
        {!loading && pets.length === 0 && <p>Encara no tens cap mascota 🐾</p>}
        <div className="pets-grid">
          {pets.map(pet => {
            const spritePath = getSpritePath(pet.variant, pet.stage);
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
                <button
                  className="select-button"
                  onClick={() => navigate(`/pets/${pet.id}`)}
                >
                  Select
                </button>
                <button
                  className="delete-button"
                  onClick={() => handleDelete(pet.id)}
                >
                  🗑️ Eliminar
                </button>
              </div>
            );
          })}
        </div>
      </div>
    </div>
  );
};

export default MyPets;