import React, { useEffect, useState } from 'react';
import { getToken, clearToken } from '../utils/auth';
import { useNavigate } from 'react-router-dom';
import profileBg from '../assets/profile.jpg';
import './ProfilePage.css';

const ProfilePage = () => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [message, setMessage] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    const token = getToken();
    if (!token) {
      navigate('/');
      return;
    }

    fetch('http://localhost:8080/api/user/me', {
      headers: { Authorization: `Bearer ${token}` },
    })
      .then(res => res.json())
      .then(data => {
        setUser(data.data);
        setLoading(false);
      })
      .catch(err => {
        console.error('Error loading profile:', err);
        setLoading(false);
        navigate('/');
      });
  }, [navigate]);

  const handleDeleteAccount = async () => {
    const confirm = window.confirm('Are you sure you want to delete your account? This action is irreversible.');
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
      console.error('Error deleting account:', err);
      setMessage('❌ Unexpected error');
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
        <button className="back-button" onClick={() => navigate('/dashboard')}>
          ← Back
        </button>
        <div className="profile-box">Loading profile...</div>
      </div>
    );
  }

  if (!user) {
    return (
      <div style={containerStyle}>
        <button className="back-button" onClick={() => navigate('/dashboard')}>
          ← Back
        </button>
        <div className="profile-box">Failed to load profile.</div>
      </div>
    );
  }

  return (
    <div style={containerStyle}>
      <button className="back-button" onClick={() => navigate('/dashboard')}>
        ← Back
      </button>

      <div className="profile-box">
        <h2>👤 Profile 👤</h2>
        <p><strong>Usuari:</strong> {user.username}</p>
        <p><strong>Email:</strong> {user.email}</p>
        <p><strong>Rol:</strong> {user.role}</p>

        <button className="delete-button" onClick={handleDeleteAccount}>
          🗑️ Delete account
        </button>

        {message && <p className="message">{message}</p>}
      </div>
    </div>
  );
};

export default ProfilePage;