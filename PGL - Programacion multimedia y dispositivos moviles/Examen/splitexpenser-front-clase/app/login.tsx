// Importa el hook useRouter para la navegación entre pantallas
import { useRouter } from "expo-router";
// Importa React y el hook useContext para acceder al contexto, y useState para manejar el estado local
import React, { useContext, useState } from "react";
// Importa componentes básicos de React Native para la interfaz de usuario
import { Button, Text, TextInput, View } from "react-native";
// Importa el contexto de autenticación para acceder a las funciones de login
import { AuthContext } from "../context/AuthContext";

/**
 * Login - Pantalla de inicio de sesión
 * 
 * Permite a los usuarios autenticarse en la aplicación ingresando
 * su nombre de usuario y contraseña.
 */
export default function Login() {
  // Extrae la función login y el token del contexto de autenticación
  const { login, token } = useContext(AuthContext);
  // Hook para navegar entre pantallas
  const router = useRouter();
  // Estado local para almacenar el nombre de usuario ingresado
  const [username, setUsername] = useState("");
  // Estado local para almacenar la contraseña ingresada
  const [password, setPassword] = useState("");
  // Estado local para almacenar mensajes de error
  const [error, setError] = useState("");

  /**
   * handleLogin - Maneja el proceso de inicio de sesión
   * 
   * Llama a la función login del contexto con las credenciales ingresadas.
   * Si el login es exitoso (devuelve un access_token), redirige al usuario a la página principal.
   * Si falla, muestra un mensaje de error.
   */
  const handleLogin = async () => {
    // Llama a la función login del contexto con las credenciales
    const res = await login(username, password);
    // Si la respuesta contiene un token de acceso, el login fue exitoso
    if (res.access_token) {
      // Redirige al usuario a la página principal
      router.replace("/");
    } else {
      // Si no hay token, muestra el mensaje de error
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
      {/* Muestra el mensaje de error solo si existe */}
      {error ? <Text style={{ color: "red" }}>{error}</Text> : null}
      
      {/* Contenedor para el botón de login */}
      <View style={{ marginBottom: 10 }}>
        {/* Botón para ejecutar el login */}
        <Button title="Login" onPress={handleLogin} />
      </View>

      {/* Botón para navegar a la pantalla de registro */}
      <Button title="Registrarse" onPress={() => router.push("/register")} />
    </View>
  );
}