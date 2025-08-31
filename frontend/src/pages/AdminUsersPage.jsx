import React, { useEffect, useState } from 'react';
import { getToken } from '../utils/auth';
import { useNavigate } from 'react-router-dom';
import './AdminUsersPage.css';

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

    // Load current user
    fetch('http://localhost:8080/api/user/me', {
      headers: { Authorization: `Bearer ${token}` },
    })
      .then(res => res.json())
      .then(data => setCurrentUser(data.data));

    // Load all users
    fetch('http://localhost:8080/api/admin/users', {
      headers: { Authorization: `Bearer ${token}` },
    })
      .then(res => res.json())
      .then(data => setUsers(data.data))
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

  const canChangeRole = (targetRole) => {
    return currentUser?.role === 'ROLE_SUPER_ADMIN' && targetRole !== 'ROLE_SUPER_ADMIN';
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
        setMessage('✅ Usuari eliminat correctament');
      } else {
        const result = await res.json();
        setMessage('❌ Error: ' + result.message);
      }
    } catch (err) {
      console.error('Error deleting user:', err);
      setMessage('❌ Error inesperat');
    }
  };

  const handleRoleChange = async (userId) => {
    const token = getToken();
    try {
      const res = await fetch(`http://localhost:8080/api/admin/roles/${userId}/toggle`, {
        method: 'PUT',
        headers: { Authorization: `Bearer ${token}` },
      });
      const result = await res.json();
      if (res.ok) {
        setUsers(prev =>
          prev.map(u => (u.id === userId ? { ...u, role: result.data.role } : u))
        );
        setMessage('🔄 Rol modificat correctament');
      } else {
        setMessage('❌ Error: ' + result.message);
      }
    } catch (err) {
      console.error('Error changing role:', err);
      setMessage('❌ Error inesperat');
    }
  };

  return (
    <div className="admin-users-container">
      <h2>👥 Gestió d'usuaris</h2>
      {message && <p className="message">{message}</p>}

      <div className="user-list">
        {users.map(user => (
          <div key={user.id} className="user-card">
            <p><strong>{user.username}</strong></p>
            <p>{user.email}</p>
            <p>Rol: {user.role.replace('ROLE_', '')}</p>

            <div className="actions">
              {canDelete(user.role) && (
                <button onClick={() => handleDelete(user.id)}>🗑️ Eliminar</button>
              )}
              {canChangeRole(user.role) && (
                <button onClick={() => handleRoleChange(user.id)}>🔄 Canviar rol</button>
              )}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default AdminUsersPage;