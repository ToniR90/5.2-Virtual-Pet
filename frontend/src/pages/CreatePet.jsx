import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { getToken } from '../utils/auth';
import './CreatePet.css';
import createBg from '../assets/creation.jpg';
import eggMountain from '../assets/sprites/MOUNTAIN-EGG.png';
import eggSwamp from '../assets/sprites/SWAMP-EGG.png';
import eggForest from '../assets/sprites/FOREST-EGG.png';

const eggOptions = [
  { variant: 'MOUNTAIN', label: 'Drac de Muntanya', sprite: eggMountain },
  { variant: 'SWAMP', label: 'Drac de Pantà', sprite: eggSwamp },
  { variant: 'FOREST', label: 'Drac de Bosc', sprite: eggForest },
];

const CreatePet = () => {
  const [selectedVariant, setSelectedVariant] = useState(null);
  const [name, setName] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleCreate = () => {
    if (!selectedVariant || !name.trim()) {
      setError('Has de seleccionar un ou i escriure un nom!');
      return;
    }

    const token = getToken();
    console.log('🔧 Creant mascota:', { name, variant: selectedVariant });

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
        console.log('✅ Mascota creada:', data);
        navigate('/pets');
      })
      .catch(err => {
        console.error('❌ Error creant mascota:', err);
        setError('No s’ha pogut crear la mascota');
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
      <div className="create-pet-container">
        <h2>🐣 Crea la teva mascota</h2>
        <p>Selecciona un ou i escriu el nom del teu drac</p>

        <div className="egg-selection">
          {eggOptions.map(egg => (
            <div
              key={egg.variant}
              className={`egg-card ${selectedVariant === egg.variant ? 'selected' : ''}`}
              onClick={() => {
                console.log('✅ Ou seleccionat:', egg.variant);
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
          placeholder="Nom del drac..."
          value={name}
          onChange={e => setName(e.target.value)}
          className="pet-name-input"
        />

        {error && <p className="error-msg">{error}</p>}

        <button className="create-button" onClick={handleCreate}>
          Crear mascota
        </button>
      </div>
    </div>
  );
};

export default CreatePet;