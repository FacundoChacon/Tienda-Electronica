import { createContext, useContext, useState, useEffect, useCallback } from 'react';

const TemaContext = createContext(null);

export function TemaProvider({ children }) {
  const [esOscuro, setEsOscuro] = useState(() => {
    const guardado = localStorage.getItem('tema');
    if (guardado) return guardado === 'oscuro';
    // Si el usuario no elegio nada todavia, respetamos la preferencia del sistema operativo.
    return window.matchMedia('(prefers-color-scheme: dark)').matches;
  });

  useEffect(() => {
    document.documentElement.classList.toggle('dark', esOscuro);
    localStorage.setItem('tema', esOscuro ? 'oscuro' : 'claro');
  }, [esOscuro]);

  const alternarTema = useCallback(() => setEsOscuro((prev) => !prev), []);

  return (
    <TemaContext.Provider value={{ esOscuro, alternarTema }}>
      {children}
    </TemaContext.Provider>
  );
}

export function useTema() {
  const context = useContext(TemaContext);
  if (!context) throw new Error('useTema debe usarse dentro de un TemaProvider');
  return context;
}
