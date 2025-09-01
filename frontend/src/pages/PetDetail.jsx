import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { getToken } from '../utils/auth';
import './PetDetail.css';

const getSpritePath = (variant, stage) => {
  try {
    return require(`../assets/sprites/${variant}-${stage}.png`);
  } catch {
    return null;
  }
};

const getBackgroundForVariant = (variant) => {
  try {
    return require(`../assets/${variant.toLowerCase()}.jpg`);
  } catch {
    return null;
  }
};

const PetDetail = () => {
  const { id } = useParams();
  const [pet, setPet] = useState(null);
  const [error, setError] = useState('');
  const [message, setMessage] = useState('');
  const [currentUser, setCurrentUser] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    const token = getToken();

    fetch(`http://localhost:8080/api/pets/${id}`, {
      headers: { Authorization: `Bearer ${token}` },
    })
      .then(res => {
        if (!res.ok) throw new Error('Pet not found or access denied');
        return res.json();
      })
      .then(data => setPet(data.data))
      .catch(err => {
        console.error(err);
        setError('❌ Failed to load pet');
      });

    fetch('http://localhost:8080/api/user/me', {
      headers: { Authorization: `Bearer ${token}` },
    })
      .then(res => res.json())
      .then(data => setCurrentUser(data.data));
  }, [id]);

  const handleAction = async (action) => {
    const token = getToken();
    try {
      const res = await fetch(`http://localhost:8080/api/pets/${id}/${action}`, {
        method: 'POST',
        headers: { Authorization: `Bearer ${token}` },
      });
      const result = await res.json();
      if (res.ok) {
        setPet(result.data);
        setMessage(`✅ ${action} completed`);
      } else {
        setMessage(`❌ ${result.message || 'Action failed'}`);
      }
    } catch (err) {
      console.error('❌ Error performing action:', err);
      setMessage('❌ Unexpected error');
    }
  };

  const isOwner = currentUser?.username === pet?.ownerUsername;
  const spritePath = pet ? getSpritePath(pet.variant, pet.stage) : null;
  const backgroundImage = pet ? getBackgroundForVariant(pet.variant) : null;

  if (error) {
    return (
      <div className="pet-detail-container" style={{ backgroundImage: `url(${backgroundImage})` }}>
        <button className="back-button" onClick={() => navigate('/dashboard')}>← Back</button>
        <div className="pet-stats-box">
          <p className="error-message">{error}</p>
        </div>
      </div>
    );
  }

  if (!pet || !currentUser) {
    return (
      <div className="pet-detail-container" style={{ backgroundImage: `url(${backgroundImage})` }}>
        <button className="back-button" onClick={() => navigate('/dashboard')}>← Back</button>
        <div className="pet-stats-box">
          <p>Loading pet...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="pet-detail-container" style={{ backgroundImage: `url(${backgroundImage})` }}>
      <button className="back-button" onClick={() => navigate('/dashboard')}>← Back</button>

      {spritePath && (
        <img src={spritePath} alt={`${pet.name} sprite`} className="pet-detail-sprite" />
      )}

      <div className="pet-stats-box">
        <h3>{pet.name}</h3>
        <p><strong>Variant:</strong> {pet.variant}</p>
        <p><strong>Stage:</strong> {pet.stage}</p>
        <hr />
        <p><strong>Experience:</strong> {pet.experience}</p>
        <p><strong>Energy:</strong> {pet.energy}</p>
        <p><strong>Happiness:</strong> {pet.happiness}</p>
        <p><strong>Hunger:</strong> {pet.hunger}</p>
      </div>

      {isOwner && (
        <div className="pet-actions-box">
          <button onClick={() => handleAction('play')}>🎾 Play</button>
          <button onClick={() => handleAction('feed')}>🍖 Feed</button>
          <button onClick={() => handleAction('rest')}>💤 Rest</button>
          <button onClick={() => handleAction('ignore')}>🙈 Ignore</button>
        </div>
      )}

      {message && <p className="message">{message}</p>}
    </div>
  );
};

export default PetDetail;