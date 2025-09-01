import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { getToken } from '../utils/auth';
import dashboardBg from '../assets/dashboard.jpg';
import './MyPets.css'; // reutilitzem els estils

const getSpritePath = (variant, stage) => {
  try {
    return require(`../assets/sprites/${variant}-${stage}.png`);
  } catch {
    return null;
  }
};

const PetDetail = () => {
  const { id } = useParams();
  const [pet, setPet] = useState(null);
  const [error, setError] = useState('');
  const [currentUser, setCurrentUser] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    const token = getToken();

    // Carrega mascota
    fetch(`http://localhost:8080/api/pets/${id}`, {
      headers: { Authorization: `Bearer ${token}` },
    })
      .then(res => {
        if (!res.ok) throw new Error('Mascota no trobada o accés denegat');
        return res.json();
      })
      .then(data => setPet(data.data))
      .catch(err => {
        console.error(err);
        setError('❌ No s’ha pogut carregar la mascota');
      });

    // Carrega usuari actual
    fetch('http://localhost:8080/api/user/me', {
      headers: { Authorization: `Bearer ${token}` },
    })
      .then(res => res.json())
      .then(data => setCurrentUser(data.data));
  }, [id]);

  const handleDelete = async () => {
    const confirm = window.confirm('Estàs segur que vols eliminar aquesta mascota?');
    if (!confirm) return;

    const token = getToken();
    try {
      const res = await fetch(`http://localhost:8080/api/pets/${id}`, {
        method: 'DELETE',
        headers: { Authorization: `Bearer ${token}` },
      });

      if (res.ok) {
        navigate('/dashboard');
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

  if (error) {
    return (
      <div style={containerStyle}>
        <button className="back-button" onClick={() => navigate('/dashboard')}>
          ← Tornar
        </button>
        <div className="pets-box">
          <p className="error-message">{error}</p>
        </div>
      </div>
    );
  }

  if (!pet) {
    return (
      <div style={containerStyle}>
        <button className="back-button" onClick={() => navigate('/dashboard')}>
          ← Tornar
        </button>
        <div className="pets-box">
          <p>Carregant mascota...</p>
        </div>
      </div>
    );
  }

  const spritePath = getSpritePath(pet.variant, pet.stage);
  const isOwner = currentUser?.username === pet.ownerUsername;
  const isAdmin = currentUser?.role === 'ROLE_ADMIN' || currentUser?.role === 'ROLE_SUPER_ADMIN';

  return (
    <div style={containerStyle}>
      <button className="back-button" onClick={() => navigate('/dashboard')}>
        ← Tornar
      </button>

      <div className="pets-box">
        <h2>🎮 Interacció amb {pet.name}</h2>
        <div className="pets-grid">
          <div className="pet-card">
            {spritePath && (
              <img src={spritePath} alt={`${pet.name} sprite`} className="pet-sprite" />
            )}
            <h3>{pet.name}</h3>
            <p><strong>Variant:</strong> {pet.variant}</p>
            <p><strong>Estat:</strong> {pet.stage}</p>
            {isAdmin && !isOwner && (
              <p><strong>Propietari:</strong> {pet.ownerUsername}</p>
            )}
            <button className="select-button" onClick={() => alert('Acció de selecció')}>
              Select
            </button>
            {(isOwner || isAdmin) && (
              <button className="delete-button" onClick={handleDelete}>
                🗑️ Eliminar
              </button>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default PetDetail;