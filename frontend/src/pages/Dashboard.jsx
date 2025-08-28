import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import dashboardBg from '../assets/dashboard-bg.jpg';

const Dashboard = () => {
  const [user, setUser] = useState(null);
  const navigate = useNavigate();

const containerStyle = {
  backgroundImage: `url(${dashboardBg})`,
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

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (!token) {
      navigate('/'); // 🔐 Si no hi ha token, redirigeix al login
      return;
    }

    fetch('http://localhost:3001/api/user/me', {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    })
      .then(res => res.json())
      .then(data => setUser(data))
      .catch(err => {
        console.error('Error fetching user:', err);
        navigate('/');
      });
  }, [navigate]);

  if (!user) return <div className="auth-box">Loading...</div>;

  return (
    <div style={containerStyle}> {/* ✅ Imatge de fons aplicada aquí */}
      <div className="auth-box">
        <h2>Welcome back, {user.username} 🐾</h2>
        <p>Email: {user.email}</p>
        <p>Role: {user.role}</p>

        {/* Aquí podràs afegir mascotes, editar perfil, etc. */}
        <button onClick={() => {
          localStorage.removeItem('token');
          navigate('/');
        }}>
          Log out
        </button>
      </div>
    </div>
  );

export default Dashboard;