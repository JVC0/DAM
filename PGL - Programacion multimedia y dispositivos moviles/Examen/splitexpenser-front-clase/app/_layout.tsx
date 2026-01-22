// Importa el componente Slot de expo-router que actúa como placeholder para las rutas hijas
import { Slot } from "expo-router";
// Importa React para poder crear componentes
import React from "react";
// Importa el AuthProvider que proporciona el contexto de autenticación a toda la aplicación
import { AuthProvider } from "../context/AuthContext";

/**
 * RootLayout - Componente raíz de la aplicación
 * 
 * Este componente envuelve toda la aplicación con el AuthProvider,
 * permitiendo que todos los componentes hijos accedan al contexto de autenticación.
 * El componente Slot renderiza las rutas hijas dinámicamente según la navegación.
 */
export default function RootLayout() {
  return (
    // AuthProvider envuelve toda la app para proporcionar el contexto de autenticación
    <AuthProvider>
      {/* Slot renderiza el contenido de la ruta actual */}
      <Slot />
    </AuthProvider>
  );
}
