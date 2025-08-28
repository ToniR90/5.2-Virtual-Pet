// src/utils/auth.js

// ✅ Guarda el token al localStorage
export const saveToken = (token) => {
  localStorage.setItem('token', token);
};

// ✅ Recupera el token
export const getToken = () => {
  return localStorage.getItem('token');
};

// ✅ Elimina el token (logout)
export const clearToken = () => {
  localStorage.removeItem('token');
};

// ✅ Comprova si l’usuari està autenticat
export const isAuthenticated = () => {
  return !!localStorage.getItem('token');
};