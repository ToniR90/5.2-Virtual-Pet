import React, { useState } from 'react';
import '../pages/AuthPage.css'; // Reutilitzem els estils

const RegisterForm = ({ onSwitch }) => {
  const [formData, setFormData] = useState({
    username: '',
    email: '',
    password: '',
    confirmPassword: '',
  });

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    if (formData.password !== formData.confirmPassword) {
      alert("Les contrasenyes no coincideixen");
      return;
    }

    const payload = {
      username: formData.username,
      email: formData.email,
      password: formData.password
    };

    fetch('http://localhost:3001/api/createUser', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload),
    })
      .then((res) => res.json())
      .then((data) => {
        console.log('Usuari creat:', data);
        // Aquí pots redirigir o mostrar missatge
      })
      .catch((err) => {
        console.error('Error en el registre:', err);
      });
  };

  return (
    <div className="auth-box">
      <h2>Registra't</h2>
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          name="username"
          placeholder="Nom d'usuari"
          value={formData.username}
          onChange={handleChange}
          required
        />
        <input
          type="email"
          name="email"
          placeholder="Correu electrònic"
          value={formData.email}
          onChange={handleChange}
          required
        />
        <input
          type="password"
          name="password"
          placeholder="Contrasenya"
          value={formData.password}
          onChange={handleChange}
          required
        />
        <input
          type="password"
          name="confirmPassword"
          placeholder="Confirma la contrasenya"
          value={formData.confirmPassword}
          onChange={handleChange}
          required
        />
        <button type="submit">Crear compte</button>
      </form>
      <p className="register-text">
        Ja tens compte?{' '}
        <button className="link-button" onClick={onSwitch}>
          Inicia sessió
        </button>
      </p>
    </div>
  );
};

export default RegisterForm;