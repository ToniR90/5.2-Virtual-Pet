/*import React, { useEffect, useState, useCallback } from 'react';
import { getToken, clearToken } from '../utils/auth';
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
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  const loadUser = useCallback(async () => {
    const token = getToken();
    try {
      const res = await fetch('http://localhost:8080/api/user/me', {
        headers: { Authorization: `Bearer ${token}` },
      });
      const data = await res.json();
      console.log('🔍 Resposta de /api/user/me:', data);

      if (res.ok && data && data.data) {
        setUser(data.data);
        setFormData(prev => ({
          ...prev,
          username: data.data.username || '',
          email: data.data.email || '',
        }));
        setMessage('');
      } else {
        setMessage('❌ ' + (data.message || 'No s’ha pogut carregar el perfil'));
      }
    } catch (err) {
      console.error('Error carregant perfil:', err);
      setMessage('❌ Error inesperat en carregar el perfil');
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    loadUser();
  }, [loadUser]);

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

        // 🔐 Guarda el nou token si el backend el retorna
        if (result.data && result.data.token) {
          localStorage.setItem('token', result.data.token);
        }

        loadUser(); // ✅ Recarrega el perfil amb el token actualitzat
      } else {
        setMessage('❌ Error: ' + result.message);
      }
    } catch (err) {
      console.error('Error actualitzant perfil:', err);
      setMessage('❌ Error inesperat');
    }
  };

  const handleDeleteAccount = async () => {
    const confirm = window.confirm('Estàs segur que vols eliminar el teu compte? Aquesta acció és irreversible.');
    if (!confirm) return;

    const token = getToken();
    try {
      const res = await fetch('http://localhost:8080/api/user/delete', {
        method: 'DELETE',
        headers: { Authorization: `Bearer ${token}` },
      });

      if (res.ok) {
        clearToken();
        navigate('/goodbye');
      } else {
        const result = await res.json();
        setMessage('❌ Error: ' + result.message);
      }
    } catch (err) {
      console.error('Error eliminant compte:', err);
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

  if (loading) {
    return (
      <div style={containerStyle}>
        <div className="edit-box">Carregant perfil...</div>
      </div>
    );
  }

  if (!user) {
    return (
      <div style={containerStyle}>
        <div className="edit-box">{message || 'No s’ha pogut carregar el perfil.'}</div>
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

        <hr />

        <button className="delete-button" onClick={handleDeleteAccount}>
          🗑️ Eliminar compte
        </button>

        {message && <p className="message">{message}</p>}
      </div>
    </div>
  );
};

export default EditProfile;*/