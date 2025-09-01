import React, { useEffect, useState } from 'react';
import { getToken } from '../utils/auth';
import { useNavigate } from 'react-router-dom';
import './AdminPetsPage.css';
import adminBg from '../assets/pets.jpg';

const getSpritePath = (variant, stage) => {
  try {
    return require(`../assets/sprites/${variant}-${stage}.png`);
  } catch {
    return null;
  }
};

const AdminPetsPage = () => {
  const [pets, setPets] = useState([]);
  const [message, setMessage] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    const token = getToken();
    fetch('http://localhost:8080/api/admin/pets', {
      headers: { Authorization: `Bearer ${token}` },
    })
      .then(res => res.json())
      .then(data => setPets(data.data))
      .catch(err => {
        console.error('Cannot load the pets:', err);
        navigate('/');
      });
  }, [navigate]);

  const handleDelete = async (petId) => {
    const token = getToken();
    try {
      const res = await fetch(`http://localhost:8080/api/admin/pets/${petId}`, {
        method: 'DELETE',
        headers: { Authorization: `Bearer ${token}` },
      });
      if (res.ok) {
        setPets(prev => prev.filter(p => p.id !== petId));
        setMessage('✅ Pet deleted successfully');
      } else {
        const result = await res.json();
        setMessage('❌ Error: ' + (result.message || 'Unexpected error'));
      }
    } catch (err) {
      console.error('Error deleting pet:', err);
      setMessage('❌ Unexpected error');
    }
  };

  const containerStyle = {
    backgroundImage: `url(${adminBg})`,
    backgroundSize: 'cover',
    backgroundPosition: 'center',
    backgroundRepeat: 'no-repeat',
    position: 'fixed',
    top: 0,
    left: 0,
    width: '100vw',
    height: '100vh',
    overflow: 'hidden',
    paddingTop: '2rem',
    color: 'white',
    zIndex: -1,
  };

  return (
    <div style={containerStyle}>
      <div style={{ position: 'relative', zIndex: 1, height: '100vh', overflowY: 'auto' }}>
        <button className="back-button" onClick={() => navigate('/dashboard')}>
          ← Back
        </button>

        <div className="admin-pets-container">
          <h2>🐾 Pet management 🐾</h2>
          {message && <p className="message">{message}</p>}

          <div className="pets-grid">
            {pets.map(pet => {
              const spritePath = getSpritePath(pet.variant, pet.stage);
              return (
                <div key={pet.id} className="pet-card">
                  {spritePath && (
                    <img
                      src={spritePath}
                      alt={`Sprite de ${pet.name}`}
                      className="pet-sprite"
                    />
                  )}
                  <h3>{pet.name}</h3>
                  <p><strong>Propietari:</strong> {pet.ownerUsername}</p>
                  <p><strong>Variant:</strong> {pet.variant}</p>
                  <p><strong>Estat:</strong> {pet.stage}</p>

                  <div className="actions">
                    <button onClick={() => handleDelete(pet.id)}>🗑️ Delete</button>
                  </div>
                </div>
              );
            })}
          </div>
        </div>
      </div>
    </div>
  );
};

export default AdminPetsPage;