import React, { useEffect, useState } from 'react';
import { getToken } from '../utils/auth';
import { useNavigate } from 'react-router-dom';
import profileBg from '../assets/profile.jpg';
import './EditProfile.css';

const EditProfile = () => {
  const [user, setUser] = useState(null);
  const [formData, setFormData] = useState({
    username: '',
    email: '',
    currentPassword: '',
    newPassword: '',
    confirmPassword: '',
  });
  const [message, setMessage] = useState('');
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
      .then(data => {
        setUser(data.data);
        setFormData(prev => ({
          ...prev,
          username: data.data.username,
          email: data.data.email,
        }));
      })
      .catch(err => {
        console.error('Error fetching profile:', err);
        navigate('/');
      });
  }, [navigate]);

  const handleChange = (e) => {
    setFormData(prev => ({
      ...prev,
      [e.target.name]: e.target.value,
    }));
  };

  const handleSubmit = async () => {
    if (formData.newPassword && formData.newPassword !== formData.confirmPassword) {
      setMessage('❌ Les contrasenyes no coincideixen');
      return;
    }

    const token = getToken();
    const payload = {
      username: formData.username,
      email: formData.email,
    };

    if (formData.currentPassword && formData.newPassword) {
      payload.currentPassword = formData.currentPassword;
      payload.newPassword = formData.newPassword;
    }

    try {
      const response = await fetch('http://localhost:8080/api/user/update', {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(payload),
      });

      const result = await response.json();
      if (response.ok) {
        setMessage('✅ Perfil actualitzat correctament');
        navigate('/profile');
      } else {
        setMessage('❌ Error: ' + result.message);
      }
    } catch (err) {
      console.error('Error updating profile:', err);
      setMessage('❌ Error inesperat');
    }
  };

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

  if (!user) {
    return (
      <div style={containerStyle}>
        <div className="edit-box">Carregant perfil...</div>
      </div>
    );
  }

  return (
    <div style={containerStyle}>
      <div className="edit-box">
        <h2>✏️ Edita el teu perfil</h2>

        <label>Nom d'usuari:</label>
        <input
          type="text"
          name="username"
          value={formData.username}
          onChange={handleChange}
        />

        <label>Email:</label>
        <input
          type="email"
          name="email"
          value={formData.email}
          onChange={handleChange}
        />

        <hr />

        <h3>🔒 Canvi de contrasenya</h3>

        <label>Contrasenya actual:</label>
        <input
          type="password"
          name="currentPassword"
          value={formData.currentPassword}
          onChange={handleChange}
        />

        <label>Nova contrasenya:</label>
        <input
          type="password"
          name="newPassword"
          value={formData.newPassword}
          onChange={handleChange}
        />

        <label>Confirmació nova contrasenya:</label>
        <input
          type="password"
          name="confirmPassword"
          value={formData.confirmPassword}
          onChange={handleChange}
        />

        <button onClick={handleSubmit}>💾 Guardar canvis</button>
        {message && <p className="message">{message}</p>}
      </div>
    </div>
  );
};

export default EditProfile;