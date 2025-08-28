const RegisterForm = ({ onSwitch }) => {
  return (
    <div className="auth-box">
      <h2>Registra't</h2>
      <form>
        <input type="text" placeholder="Nom complet" />
        <input type="text" placeholder="Nom d'usuari" />
        <input type="email" placeholder="Correu electrònic" />
        <input type="password" placeholder="Contrasenya" />
        <input type="password" placeholder="Confirma la contrasenya" />
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