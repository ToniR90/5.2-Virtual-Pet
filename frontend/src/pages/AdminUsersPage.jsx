import React, { useEffect, useState } from 'react';
import { getToken } from '../utils/auth';
import { useNavigate } from 'react-router-dom';
import './AdminUsersPage.css';
import adminBg from '../assets/users.jpg';

const AdminUsersPage = () => {
  const [users, setUsers] = useState([]);
  const [currentUser, setCurrentUser] = useState(null);
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
      .then(data => setCurrentUser(data.data));

    fetch('http://localhost:8080/api/admin/users', {
      headers: { Authorization: `Bearer ${token}` },
    })
      .then(res => res.json())
      .then(data => {
        const loadedUsers = data.data;
        setUsers(loadedUsers);

        loadedUsers.forEach(user => {
          fetch(`http://localhost:8080/api/admin/users/${user.id}/pet-count`, {
            headers: { Authorization: `Bearer ${getToken()}` },
          })
            .then(res => res.json())
            .then(countData => {
              setUsers(prev =>
                prev.map(u =>
                  u.id === user.id ? { ...u, petCount: countData.data } : u
                )
              );
            });
        });
      })
      .catch(err => {
        console.error('Error loading users:', err);
        navigate('/');
      });
  }, [navigate]);

  const canDelete = (targetRole) => {
    if (!currentUser) return false;
    if (currentUser.role === 'ROLE_ADMIN') return targetRole === 'ROLE_USER';
    if (currentUser.role === 'ROLE_SUPER_ADMIN') return targetRole !== 'ROLE_SUPER_ADMIN';
    return false;
  };

  const handleDelete = async (userId) => {
    const token = getToken();
    try {
      const res = await fetch(`http://localhost:8080/api/admin/users/${userId}`, {
        method: 'DELETE',
        headers: { Authorization: `Bearer ${token}` },
      });
      if (res.ok) {
        setUsers(prev => prev.filter(u => u.id !== userId));
        setMessage('✅ User deleted successfully');
      } else {
        const result = await res.json();
        setMessage('❌ Error: ' + (result.message || 'Unexpected error'));
      }
    } catch (err) {
      console.error('Error deleting user:', err);
      setMessage('❌ Unexpected error');
    }
  };

  const containerStyle = {
    backgroundImage: `url(${adminBg})`,
    backgroundSize: 'cover',
    backgroundPosition: 'center',
    backgroundRepeat: 'no-repeat',
    minHeight: '100vh',
    paddingTop: '2rem',
    color: 'white',
  };

  return (
    <div style={containerStyle}>
      <button className="back-button" onClick={() => navigate('/dashboard')}>
        ← Back
      </button>

      <div className="admin-users-container">
        <h2>👥 Users management 👥</h2>
        {message && <p className="message">{message}</p>}

        <div className="users-grid">
          {users.map(user => (
            <div key={user.id} className="user-card">
              <h3>{user.username}</h3>
              <p><strong>Rol:</strong> {user.role.replace('ROLE_', '')}</p>
              <p><strong>Email:</strong> {user.email}</p>
              <p><strong>Mascotes:</strong> {user.petCount ?? 'Carregant...'}</p>

              <div className="actions">
                {canDelete(user.role) && (
                  <button onClick={() => handleDelete(user.id)}>🗑️ Delete</button>
                )}
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default AdminUsersPage;