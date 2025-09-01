import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import dashboardBg from '../assets/dashboard.jpg';
import { getToken, clearToken } from '../utils/auth';
import './Dashboard.css';

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
        console.error('Error fetching user:', err);
        navigate('/');
      });
  }, [navigate]);

  if (!user) return <div className="dashboard-box">Loading...</div>;

  const isAdmin = user.role === 'ROLE_ADMIN' || user.role === 'ROLE_SUPER_ADMIN';
  const isSuperAdmin = user.role === 'ROLE_SUPER_ADMIN';

  return (
    <div style={containerStyle}>
      <div className="dashboard-box">
        <h2>👋 Welcome back, {user.username}</h2>

        <hr />
        <button onClick={() => navigate('/profile')}>👤 View Profile</button>
        <button onClick={() => navigate('/pets')}>🐾 View My Pets</button>
        <button onClick={() => navigate('/pets/create')}>➕ Create Pet</button>

        {isAdmin && (
          <>
            <hr />
            <button onClick={() => navigate('/admin/users')}>👥 View All Users</button>
            <button onClick={() => navigate('/admin/pets')}>🐾 View All Pets</button>
          </>
        )}

        {isSuperAdmin && (
          <>
            <hr />
            <button onClick={() => navigate('/admin/roles')}>🔄 Manage User Roles</button>
          </>
        )}

        <hr />

        <button onClick={() => {
          clearToken();
          navigate('/');
        }}>
          🚪 Log out
        </button>
      </div>
    </div>
  );
};

export default Dashboard;