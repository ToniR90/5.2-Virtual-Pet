import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import AuthPage from './pages/AuthPage';
import Dashboard from './pages/Dashboard';
import ProfilePage from './pages/ProfilePage'; // ✅ Importa la nova pàgina
import PrivateRoute from './components/PrivateRoute'; // ✅ Protegeix rutes privades
import EditProfile from './pages/EditProfile';
import AdminUsersPage from './pages/AdminUsersPage';
import GoodbyePage from './pages/GoodbyePage';




function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<AuthPage />} />
        <Route
          path="/dashboard"
          element={
            <PrivateRoute>
              <Dashboard />
            </PrivateRoute>
          }
        />
        <Route
          path="/profile"
          element={
            <PrivateRoute>
              <ProfilePage />
            </PrivateRoute>
          }
        />
        <Route
          path="/profile/edit"
          element={
            <PrivateRoute>
              <EditProfile />
            </PrivateRoute>
          }
        />
        <Route
          path="/admin/users"
          element={
            <PrivateRoute>
              <AdminUsersPage />
            </PrivateRoute>
          }
        />
        <Route path="/goodbye"
        element={
            <GoodbyePage />} />

      </Routes>
    </BrowserRouter>
  );
}

export default App;