import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import AuthPage from './pages/AuthPage';
import Dashboard from './pages/Dashboard';
import ProfilePage from './pages/ProfilePage';
import EditProfile from './pages/EditProfile';
import AdminUsersPage from './pages/AdminUsersPage';
import AdminPetsPage from './pages/AdminPetsPage'; // ✅ Nova vista afegida
import GoodbyePage from './pages/GoodbyePage';
import MyPets from './pages/MyPets';
import CreatePet from './pages/CreatePet';
import PetDetail from './pages/PetDetail';
import PrivateRoute from './components/PrivateRoute';

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
          path="/admin/pets"
          element={
            <PrivateRoute>
              <AdminPetsPage />
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
          path="/pets/create"
          element={
            <PrivateRoute>
              <CreatePet />
            </PrivateRoute>
          }
        />
        <Route
          path="/pets/:id"
          element={
            <PrivateRoute>
              <PetDetail />
            </PrivateRoute>
          }
        />
        <Route path="/goodbye" element={<GoodbyePage />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;