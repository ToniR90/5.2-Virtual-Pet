import React, { useEffect, useState } from 'react';
import { getToken } from '../utils/auth';
import { useNavigate } from 'react-router-dom';
import profileBg from '../assets/profile.jpg';
import './ProfilePage.css';

const ProfilePage = () => {
  const [user, setUser] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    const token = getToken();
    if (!token) {
      navigate('/');
      return;
    }

    fetch('http://localhost:8080/api/user/me', {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    })
      .then(res => res.json())
      .then(data => setUser(data.data))
      .catch(err => {
        console.error('Error fetching profile:', err);
        navigate('/');
      });
  }, [navigate]);

  const containerStyle = {
    backgroundImage: `url(${profileBg})`,
    backgroundSize: 'cover',
    backgroundPosition: 'center',
    backgroundRepeat: 'no-repeat',
    height: '100vh',
    width: '100vw',
    overflow: 'hidden',
    margin: 0,
    padding: 0,
    position: 'relative',
  };

  const formatDate = (isoString) => {
    if (!isoString) return 'Encara no hi ha accions registrades';
    const date = new Date(isoString);
    return date.toLocaleString('ca-ES', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
    });
  };

  if (!user) {
    return (
      <div style={containerStyle}>
        <div className="profile-box">Carregant perfil...</div>
      </div>
    );
  }

  return (
    <div style={containerStyle}>
      <div className="profile-box">
        <h2>👤 Perfil</h2>
        <p><strong>Usuari:</strong> {user.username}</p>
        <p><strong>Email:</strong> {user.email}</p>
        <p><strong>Rol:</strong> {user.role}</p>
        <p><strong>Creat el:</strong> {formatDate(user.createdAt)}</p>
        <p><strong>Última acció amb mascota:</strong> {formatDate(user.lastPetAction)}</p>

        <button className="edit-button" onClick={() => navigate('/profile/edit')}>
          ✏️ Modificar perfil
        </button>
      </div>
    </div>
  );
};

export default ProfilePage;