// Importa el hook useRouter para la navegación entre pantallas
import { useRouter } from "expo-router";
// Importa React y hooks necesarios para el componente
import React, { useContext, useEffect, useState } from "react";
// Importa componentes básicos de React Native para la interfaz de usuario
import { Button, Text, TextInput, View } from "react-native";
// Importa el contexto de autenticación para acceder a las funciones de eliminación de cuenta
import { AuthContext } from "../context/AuthContext";

/**
 * logout - Pantalla para eliminar la cuenta del usuario
 * 
 * NOTA: El nombre de la función debería ser "Unregister" en lugar de "logout"
 * ya que su propósito es eliminar la cuenta, no cerrar sesión.
 * 
 * Permite a los usuarios autenticados eliminar permanentemente su cuenta
 * ingresando sus credenciales para confirmar la acción.
 */
export default function logout() {
  // Hook para navegar entre pantallas
  const router = useRouter();
  // Extrae el token y la función unregister del contexto de autenticación
  const { token, unregister } = useContext(AuthContext);
  // Estado local para almacenar el nombre de usuario ingresado
  const [username, setUsername] = useState("");
  // Estado local para almacenar la contraseña ingresada
  const [password, setPassword] = useState("");
  // Estado local para almacenar mensajes de error
  const [error, setError] = useState("");

  /**
   * useEffect - Verifica que el usuario esté autenticado
   * 
   * Si no hay token (usuario no autenticado), redirige al login.
   * Se ejecuta cada vez que el token cambia.
   */
  useEffect(() => {
    if (!token) {
      // Usa setTimeout con 0ms para evitar problemas de navegación durante el renderizado
      setTimeout(() => router.replace("/login"), 0);
    }
  }, [token]); // Se ejecuta cuando el token cambia

  // Si no hay token, no renderiza nada (el usuario será redirigido)
  if (!token) return null;

  /**
   * handleUnregister - Maneja el proceso de eliminación de cuenta
   * 
   * Llama a la función unregister del contexto con las credenciales ingresadas.
   * NOTA: Hay un error lógico aquí - si la eliminación es exitosa, no debería
   * devolver un access_token ni redirigir a "/", sino al login.
   */
  const handleUnregister = async () => {
    // Llama a la función unregister del contexto con las credenciales
    const res = await unregister(username, password);
    // NOTA: Esta lógica parece incorrecta - unregister no debería devolver access_token
    if (res.access_token) {
      // Redirige al usuario a la página principal (debería ser "/login")
      router.replace("/");
    } else {
      // Si falló, muestra el mensaje de error
      setError(res.msg || "Login fallido");
    }
  };

  return (
    // Contenedor principal con estilos inline
    <View style={{ flex: 1, justifyContent: "center", padding: 20 }}>
      {/* Campo de texto para ingresar el nombre de usuario */}
      <TextInput
        placeholder="Usuario"
        value={username}
        onChangeText={setUsername} // Actualiza el estado cuando el usuario escribe
        style={{ borderWidth: 1, marginBottom: 10, padding: 8 }}
      />
      {/* Campo de texto para ingresar la contraseña */}
      <TextInput
        placeholder="Contraseña"
        value={password}
        onChangeText={setPassword} // Actualiza el estado cuando el usuario escribe
        secureTextEntry // Oculta el texto de la contraseña
        style={{ borderWidth: 1, marginBottom: 10, padding: 8 }}
      />
      
      {/* Contenedor para el botón de eliminar cuenta */}
      <View style={{ marginBottom: 10 }}>
        {/* Botón para ejecutar la eliminación de cuenta (color rojo para indicar acción destructiva) */}
        <Button color={"red"} title="Eliminar cuenta" onPress={handleUnregister} />
      </View>
      
      {/* Botón para volver a la página principal sin eliminar la cuenta */}
      <Button title="Volver a inicio" onPress={() => router.push("/")} />
    </View>
  );
}