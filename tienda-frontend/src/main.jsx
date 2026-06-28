import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import App from './App.jsx';
import { AuthProvider } from './context/AuthContext.jsx';
import { CarritoProvider } from './context/CarritoContext.jsx';
import { TemaProvider } from './context/TemaContext.jsx';
import './index.css';

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <TemaProvider>
      <AuthProvider>
        <CarritoProvider>
          <App />
        </CarritoProvider>
      </AuthProvider>
    </TemaProvider>
  </StrictMode>,
);
