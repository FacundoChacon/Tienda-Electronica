/** @type {import('tailwindcss').Config} */
export default {
  darkMode: 'class',
  content: ['./index.html', './src/**/*.{js,jsx}'],
  theme: {
    extend: {
      colors: {
        // Sistema de diseño "ficha tecnica": pensado para electronica,
        // no para un e-commerce generico. Cada color tiene un proposito fijo.
        //
        // IMPORTANTE: este color se llama "canvas" y NO "base" a proposito.
        // Tailwind ya tiene una utilidad nativa "text-base" que significa
        // "font-size: 1rem" (tamano de texto base). Si el color se llamara
        // "base", Tailwind generaria TAMBIEN una utilidad "text-base" para
        // el COLOR, que choca por nombre con la de tamano de fuente y termina
        // pisando cualquier color de texto explicito puesto al lado, como
        // text-[#1A2332]. "canvas" no colisiona con ninguna utilidad nativa.
        canvas: {
          DEFAULT: '#F5F6F7',   // fondo modo claro
          dark: '#0B0E14',      // fondo modo oscuro (pantalla apagada)
        },
        surface: {
          DEFAULT: '#FFFFFF',   // tarjetas modo claro
          dark: '#1A2332',      // tarjetas modo oscuro
        },
        accent: {
          DEFAULT: '#FF5A1F',   // naranja electrico: CTAs, precios, focus
          hover: '#E64A12',
        },
        success: '#3DDC97',     // stock disponible, confirmaciones
        warning: '#FFC857',     // stock bajo
        danger: '#FF4040',      // sin stock, errores
        muted: {
          DEFAULT: '#5B6472',   // texto secundario modo claro
          dark: '#7C8B9C',      // texto secundario modo oscuro
        },
      },
      fontFamily: {
        display: ['"Space Grotesk"', 'sans-serif'],
        body: ['Inter', 'sans-serif'],
        mono: ['"JetBrains Mono"', 'monospace'],
      },
    },
  },
  plugins: [],
}
