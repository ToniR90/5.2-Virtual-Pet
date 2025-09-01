import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { getToken } from '../utils/auth';
import './CreatePet.css';
import createBg from '../assets/creation.jpg';
import eggMountain from '../assets/sprites/MOUNTAIN-EGG.png';
import eggSwamp from '../assets/sprites/SWAMP-EGG.png';
import eggForest from '../assets/sprites/FOREST-EGG.png';

const eggOptions = [
  { variant: 'MOUNTAIN', label: '🔥 Strong mountain dragon 🔥', sprite: eggMountain },
  { variant: 'SWAMP', label: '💀 Scary swamp dragon 💀', sprite: eggSwamp },
  { variant: 'FOREST', label: '🍃 Harmonic forest dragon 🍃', sprite: eggForest },
];

const CreatePet = () => {
  const [selectedVariant, setSelectedVariant] = useState(null);
  const [name, setName] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleCreate = () => {
    if (!selectedVariant || !name.trim()) {
      setError('Select an egg and name it!');
      return;
    }

    const token = getToken();
    console.log('🔧 Creating pet:', { name, variant: selectedVariant });

    fetch('http://localhost:8080/api/pets', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({ name, variant: selectedVariant }),
    })
      .then(res => res.json())
      .then(data => {
        console.log('✅ Pet successfully created:', data);
        navigate('/pets');
      })
      .catch(err => {
        console.error('❌ Error creating pet:', err);
        setError('Pet could not be created');
      });
  };

  const containerStyle = {
    backgroundImage: `url(${createBg})`,
    backgroundSize: 'cover',
    backgroundPosition: 'center',
    backgroundRepeat: 'no-repeat',
    minHeight: '100vh',
    paddingTop: '2rem',
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'flex-start',
  };

  return (
    <div style={containerStyle}>
      <button className="back-button" onClick={() => navigate('/dashboard')}>
        ← Back
      </button>

      <div className="create-pet-container">
        <h2>🦎 Create your pet! 🦎</h2>
        <p>Select an egg and name it!</p>

        <div className="egg-selection">
          {eggOptions.map(egg => (
            <div
              key={egg.variant}
              className={`egg-card ${selectedVariant === egg.variant ? 'selected' : ''}`}
              onClick={() => {
                console.log('✅ Egg selected:', egg.variant);
                setSelectedVariant(egg.variant);
              }}
            >
              <img src={egg.sprite} alt={egg.label} className="egg-sprite" />
              <p>{egg.label}</p>
            </div>
          ))}
        </div>

        <input
          type="text"
          placeholder="Dragon name..."
          value={name}
          onChange={e => setName(e.target.value)}
          className="pet-name-input"
        />

        {error && <p className="error-msg">{error}</p>}

        <button className="create-button" onClick={handleCreate}>
          Create pet
        </button>
      </div>
    </div>
  );
};

export default CreatePet;