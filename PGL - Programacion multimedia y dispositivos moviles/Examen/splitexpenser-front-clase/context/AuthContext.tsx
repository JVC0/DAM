// Importa SecureStore de Expo para almacenar datos de forma segura (como tokens)
import * as SecureStore from "expo-secure-store";
// Importa Constants para acceder a la configuración de la aplicación
import Constants from "expo-constants";
// Importa React y hooks necesarios para crear el contexto
import React, { createContext, useEffect, useMemo, useState } from "react";

// Obtiene la URL de la API desde la configuración de la aplicación
const API_URL = Constants.expoConfig?.extra?.apiUrl ?? "";
// Obtiene la clave para almacenar el token desde la configuración
const TOKEN_KEY = Constants.expoConfig?.extra?.tokenKey ?? "";

/**
 * AuthContextType - Define el tipo del contexto de autenticación
 * 
 * Especifica todas las propiedades y funciones disponibles en el contexto
 */
type AuthContextType = {
  token: string | null;                                                    // Token de autenticación actual (null si no está autenticado)
  loading: boolean;                                                        // Indica si se está cargando el token inicial
  login: (username: string, password: string) => Promise<any>;            // Función para iniciar sesión
  register: (username: string, password: string) => Promise<any>;         // Función para registrar un nuevo usuario
  logout: () => Promise<void>;                                            // Función para cerrar sesión
  unregister: (username: string ,password: string ) => Promise<any>       // Función para eliminar la cuenta
};

/**
 * AuthContext - Contexto de React para la autenticación
 * 
 * Proporciona el estado de autenticación y funciones relacionadas
 * a todos los componentes de la aplicación.
 * Se inicializa con valores por defecto (funciones vacías).
 */
export const AuthContext = createContext<AuthContextType>({
  token: null,                    // Sin token inicialmente
  loading: true,                  // Cargando por defecto
  login: async () => ({}),        // Función login vacía
  register: async () => ({}),     // Función register vacía
  logout: async () => { },        // Función logout vacía
  unregister: async () => {}      // Función unregister vacía
});

/**
 * AuthProvider - Componente proveedor del contexto de autenticación
 * 
 * Envuelve la aplicación y proporciona el contexto de autenticación
 * a todos los componentes hijos.
 * 
 * @param children - Componentes hijos que tendrán acceso al contexto
 */
export const AuthProvider = ({ children }: { children: React.ReactNode }) => {
  // Estado para almacenar el token de autenticación
  const [token, setToken] = useState<string | null>(null);
  // Estado para indicar si se está cargando el token inicial
  const [loading, setLoading] = useState(true);

  /**
   * useEffect - Carga el token almacenado al iniciar la aplicación
   * 
   * Se ejecuta una sola vez cuando el componente se monta.
   * Intenta recuperar el token guardado en SecureStore.
   */
  useEffect(() => {
    /**
     * loadToken - Función asíncrona para cargar el token guardado
     */
    const loadToken = async () => {
      // Intenta obtener el token guardado en SecureStore
      const saved = await SecureStore.getItemAsync(TOKEN_KEY);
      // Si existe un token guardado, lo establece en el estado
      if (saved) setToken(saved);
      // Marca que terminó de cargar
      setLoading(false);
    };
    // Ejecuta la función de carga
    loadToken();
  }, []); // Array vacío = se ejecuta solo una vez al montar

  /**
   * register - Registra un nuevo usuario en la aplicación
   * 
   * Realiza una petición POST al endpoint /auth/register con las credenciales.
   * No guarda el token automáticamente, el usuario debe hacer login después.
   * 
   * @param username - Nombre de usuario
   * @param password - Contraseña
   * @returns Objeto con la respuesta del servidor o un error
   */
  const register = async (username: string, password: string) => {
    try {
      // Realiza la petición POST a la API
      const res = await fetch(`${API_URL}/auth/register`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, password }), // Envía las credenciales
      });
      // Devuelve la respuesta convertida a JSON
      return await res.json();
    } catch (err) {
      // Si hay un error de red, devuelve un objeto de error
      return { ok: false, msg: "Network error" };
    }
  };

  /**
   * login - Inicia sesión en la aplicación
   * 
   * Realiza una petición POST al endpoint /auth/login con las credenciales.
   * Si es exitoso, guarda el token en SecureStore y actualiza el estado.
   * 
   * @param username - Nombre de usuario
   * @param password - Contraseña
   * @returns Objeto con la respuesta del servidor (incluye access_token si es exitoso)
   */
  const login = async (username: string, password: string) => {
    try {
      // Realiza la petición POST a la API
      const res = await fetch(`${API_URL}/auth/login`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, password }), // Envía las credenciales
      });
      // Convierte la respuesta a JSON
      const data = await res.json();
      // Si la respuesta es exitosa y contiene un token
      if (res.ok && data.access_token) {
        // Guarda el token de forma segura en SecureStore
        await SecureStore.setItemAsync(TOKEN_KEY, data.access_token);
        // Actualiza el estado con el nuevo token
        setToken(data.access_token);
      }
      // Devuelve los datos de la respuesta
      return data;
    } catch (err) {
      // Si hay un error de red, devuelve un objeto de error
      return { ok: false, msg: "Network error" };
    }
  };




  /**
   * unregister - Elimina la cuenta del usuario
   * 
   * Realiza una petición POST al endpoint /auth/unregister con las credenciales.
   * Requiere autenticación (token en el header).
   * Después de la petición, elimina el token local independientemente del resultado.
   * 
   * @param username - Nombre de usuario
   * @param password - Contraseña
   * @returns Objeto con la respuesta del servidor o un error
   */
  const unregister = async (username: string, password: string) => {
    try {
      // Realiza la petición POST a la API
      const res = await fetch(`${API_URL}/auth/unregister`, {
        method: "POST",
        headers: { 
          "Content-Type": "application/json", 
          "Authorization": `Bearer ${token}` // Incluye el token en el header
        },
        body: JSON.stringify({ username, password }), // Envía las credenciales
      });
      // Elimina el token guardado en SecureStore
      await SecureStore.deleteItemAsync(TOKEN_KEY);
      // Limpia el token del estado
      setToken(null);
      // Devuelve la respuesta convertida a JSON
      return await res.json();
    
  } catch (err) {
    // Si hay un error de red, devuelve un objeto de error
    return { ok: false, msg: "Network error" };
  }
};



/**
 * logout - Cierra la sesión del usuario
 * 
 * Elimina el token guardado en SecureStore y limpia el estado.
 * No hace ninguna petición al servidor.
 */
const logout = async () => {
  // Elimina el token guardado en SecureStore
  await SecureStore.deleteItemAsync(TOKEN_KEY);
  // Limpia el token del estado
  setToken(null);
};

/**
 * useMemo - Optimiza el valor del contexto
 * 
 * Memoriza el objeto value para evitar re-renderizados innecesarios.
 * Solo se recalcula cuando token o loading cambian.
 */
const value = useMemo(
  () => ({ token, loading, login, register, logout, unregister }),
  [token, loading], // Dependencias: solo recalcula si estos valores cambian
);

// Retorna el Provider del contexto con el valor calculado
// Todos los componentes hijos tendrán acceso a este contexto
return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};
