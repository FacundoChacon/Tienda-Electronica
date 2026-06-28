import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    // Redirige las llamadas a /api hacia el backend de Spring Boot,
    // asi en desarrollo evitamos problemas de CORS sin tener que
    // configurar nada extra en el navegador.
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
      },
    },
  },
})
