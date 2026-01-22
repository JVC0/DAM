// Importa el hook useRouter para la navegación entre pantallas
import { useRouter } from "expo-router";
// Importa React y hooks necesarios para el componente
import React, { useContext, useState } from "react";
// Importa componentes básicos de React Native para la interfaz de usuario
import { Button, Text, TextInput, View } from "react-native";
// Importa el contexto de autenticación para acceder a la función de registro
import { AuthContext } from "../context/AuthContext";

/**
 * Register - Pantalla de registro de nuevos usuarios
 * 
 * Permite a los usuarios crear una nueva cuenta validando que la contraseña
 * cumpla con los requisitos de seguridad establecidos.
 */
export default function Register() {
  // Extrae la función register del contexto de autenticación
  const { register } = useContext(AuthContext);
  // Hook para navegar entre pantallas
  const router = useRouter();
  // Estado local para almacenar el nombre de usuario ingresado
  const [username, setUsername] = useState("");
  // Estado local para almacenar la contraseña ingresada
  const [password, setPassword] = useState("");
  // Estado local para almacenar mensajes de error
  const [error, setError] = useState("");

  /**
   * passwordValidator - Valida que la contraseña cumpla con los requisitos de seguridad
   * 
   * Requisitos:
   * - Mínimo 8 caracteres
   * - Al menos una letra mayúscula
   * - Al menos una letra minúscula
   * - Al menos un dígito
   * - Al menos un carácter especial (?, !, #, %, $, &)
   * 
   * @param password - La contraseña a validar
   * @returns true si la contraseña cumple todos los requisitos, false en caso contrario
   */
  const passwordValidator = (password: string): boolean => {
    return (password.length >= 8 && // Verifica que tenga al menos 8 caracteres
      /[A-Z]/.test(password) && // Verifica que contenga al menos una mayúscula
      /[a-z]/.test(password) && // Verifica que contenga al menos una minúscula
      /\d/.test(password) && // Verifica que contenga al menos un dígito
      /[?!#%$&]/.test(password) // Verifica que contenga al menos un carácter especial
    )

  }

  /**
   * handleRegister - Maneja el proceso de registro de usuario
   * 
   * Primero valida la contraseña. Si es válida, llama a la función register del contexto.
   * Si el registro es exitoso, redirige al usuario a la pantalla de login.
   * Si falla, muestra un mensaje de error.
   */
  const handleRegister = async () => {
    // Valida la contraseña antes de intentar registrar
    if (passwordValidator(password)) {
      // Llama a la función register del contexto con las credenciales
      const res = await register(username, password);
      // Si el registro fue exitoso
      if (res.ok) {
        // Redirige al usuario a la pantalla de login
        router.replace("/login");
      } else {
        // Si falló, muestra el mensaje de error
        setError(res.msg || "Error al registrar");
      }
    } else{
      // Si la contraseña no cumple los requisitos, muestra un mensaje de error
      setError("La contraseña no cumple todos los requisitos")
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
      {/* Botón para ejecutar el registro */}
      <Button title="Registrar" onPress={handleRegister} />
      {/* Botón para volver a la pantalla de login */}
      <Button title="Volver al login" onPress={() => router.push("/login")} />
    </View>
  );
}
