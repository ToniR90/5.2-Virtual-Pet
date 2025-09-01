import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { getToken } from '../utils/auth';
import './PetDetail.css';
import bgMountain from '../assets/mountain.jpg';
import bgSwamp from '../assets/swamp.jpg';
import bgForest from '../assets/forest.jpg';

const backgrounds = {
  MOUNTAIN: bgMountain,
  SWAMP: bgSwamp,
  FOREST: bgForest,
};

const PetDetail = () => {
  const { id } = useParams();
  const [pet, setPet] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    const token = getToken();
    fetch(`http://localhost:8080/api/pets/${id}`, {
      headers: { Authorization: `Bearer ${token}` },
    })
      .then(res => res.json())
      .then(data => setPet(data.data))
      .catch(err => {
        console.error('Error carregant mascota:', err);
        navigate('/pets');
      });
  }, [id, navigate]);

  if (!pet) return null;

  const bgStyle = {
    backgroundImage: `url(${backgrounds[pet.variant]})`,
    backgroundSize: 'cover',
    backgroundPosition: 'center',
    minHeight: '100vh',
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    paddingTop: '3rem',
    color: 'white',
  };

  const spritePath = require(`../assets/sprites/${pet.variant}-${pet.stage}.png`);

  return (
    <div style={bgStyle}>
      <button className="back-button" onClick={() => navigate('/pets')}>← Tornar</button>

      <img src={spritePath} alt={pet.name} className="pet-detail-sprite" />
      <h2>{pet.name}</h2>

      <div className="pet-stats">
        <p><strong>Variant:</strong> {pet.variant}</p>
        <p><strong>Etapa:</strong> {pet.stage}</p>
        <p><strong>Experiencia:</strong> {pet.experience}</p>
        <p><strong>Energia:</strong> {pet.energy}</p>
        <p><strong>Felicitat:</strong> {pet.happiness}</p>
        <p><strong>Saciedad:</strong> {pet.hunger}</p>
      </div>

      <div className="pet-actions">
        <button onClick={() => handleAction('play')}>🎮 Jugar</button>
        <button onClick={() => handleAction('feed')}>🍗 Alimentar</button>
        <button onClick={() => handleAction('rest')}>🛌 Descansar</button>
        <button onClick={() => handleAction('ignore')}>🙈 Ignorar</button>
      </div>
    </div>
  );

  function handleAction(action) {
    const token = getToken();
    fetch(`http://localhost:8080/api/pets/${id}/${action}`, {
      method: 'POST',
      headers: { Authorization: `Bearer ${token}` },
    })
      .then(res => res.json())
      .then(data => {
        console.log(`✅ Acció ${action} feta:`, data);
        setPet(data.data); // actualitza els stats
      })
      .catch(err => console.error(`❌ Error amb acció ${action}:`, err));
  }
};

export default PetDetail;