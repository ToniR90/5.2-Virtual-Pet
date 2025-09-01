import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import AuthPage from './pages/AuthPage';
import Dashboard from './pages/Dashboard';
import ProfilePage from './pages/ProfilePage';
import EditProfile from './pages/EditProfile';
import AdminUsersPage from './pages/AdminUsersPage';
import GoodbyePage from './pages/GoodbyePage';
import MyPets from './pages/MyPets'; // ✅ Importa la nova vista
import CreatePet from './pages/CreatePet';
import PrivateRoute from './components/PrivateRoute'; // ✅ Protegeix rutes privades

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
        <Route
          path="/pets"
          element={
            <PrivateRoute>
              <MyPets />
            </PrivateRoute>
          }
        />
        <Route
          path="/goodbye"
          element={<GoodbyePage />}
        />
        <Route
          path="/pets/create"
          element={
            <PrivateRoute>
              <CreatePet />
            </PrivateRoute>
          }
        />
      </Routes>
    </BrowserRouter>
  );
}

export default App;