// Importa el hook useRouter para la navegación entre pantallas
import { useRouter } from "expo-router";
// Importa React y hooks necesarios para el componente
import React, { useContext, useEffect, useState } from "react";
// Importa componentes básicos de React Native para la interfaz de usuario
import { Button, Text, TextInput, View, StyleSheet, Alert, TouchableOpacity } from "react-native";
// Importa el contexto de autenticación para acceder al token y función de logout
import { AuthContext } from "../context/AuthContext";
// Importa Constants para acceder a la configuración de la aplicación
import Constants from "expo-constants";

/**
 * Home - Pantalla principal de la aplicación
 * 
 * Muestra la lista de grupos del usuario y permite:
 * - Ver todos los grupos
 * - Crear nuevos grupos
 * - Navegar a los detalles de cada grupo
 * - Cerrar sesión
 * - Eliminar la cuenta
 */
export default function Home() {
  // Extrae el token y la función logout del contexto de autenticación
  const { token, logout } = useContext(AuthContext);
  // Hook para navegar entre pantallas
  const router = useRouter();
  // Estado local para almacenar la lista de grupos (tipo any[] - podría mejorarse con una interfaz)
  const [groups, setGroups] = useState<any[]>([]);
  // Estado local para almacenar el nombre del nuevo grupo a crear
  const [groupName, setGroupName] = useState("");
  // Obtiene la URL de la API desde la configuración de la aplicación
  const API_URL = Constants.expoConfig?.extra?.apiUrl ?? "";

  /**
   * handleGroupList - Obtiene la lista de grupos del usuario desde la API
   * 
   * Realiza una petición GET al endpoint /groups con el token de autenticación.
   * Si es exitosa, actualiza el estado con los grupos recibidos.
   * Si falla, muestra un mensaje de error.
   */
  const handleGroupList = async () => {
    try {
      // Realiza la petición GET a la API
      const res = await fetch(`${API_URL}/groups`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          "Authorization": `Bearer ${token}` // Incluye el token en el header
        },
      });
      // Si la respuesta no es exitosa, lanza un error
      if (!res.ok) throw new Error("Error al cargar grupos");
      // Convierte la respuesta a JSON
      const data = await res.json();
      // Actualiza el estado con los grupos recibidos
      setGroups(data);
    } catch (err) {
      // Registra el error en la consola
      console.error(err);
      // Muestra una alerta al usuario
      Alert.alert("Error", "No se pudieron cargar los grupos");
    }
  };

  /**
   * useEffect - Verifica autenticación y carga grupos
   * 
   * Si el usuario está autenticado (tiene token), carga la lista de grupos.
   * Si no está autenticado, redirige al login.
   * Se ejecuta cada vez que el token cambia.
   */
  useEffect(() => {
    if (token) {
      // Si hay token, carga los grupos
      handleGroupList();
    } else {
      // Si no hay token, redirige al login después de 0ms
      // (setTimeout evita problemas de navegación durante el renderizado)
      setTimeout(() => {
        router.replace("/login");
      }, 0);
    }
  }, [token]); // Se ejecuta cuando el token cambia

  /**
   * handleAddGroup - Crea un nuevo grupo
   * 
   * Realiza una petición POST al endpoint /groups con el nombre del grupo.
   * Si es exitosa, añade el nuevo grupo a la lista sin recargar todos los grupos.
   * Si falla, muestra un mensaje de error.
   * 
   * @param name - Nombre del grupo a crear
   */
  const handleAddGroup = async (name: string) => {
    try {
      // Realiza la petición POST a la API
      const res = await fetch(`${API_URL}/groups`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "Authorization": `Bearer ${token}` // Incluye el token en el header
        }, 
        body: JSON.stringify({ name }) // Envía el nombre del grupo en el body
      });
      // Si la respuesta no es exitosa, lanza un error
      if (!res.ok) throw new Error("Error al cargar grupos");
      // Convierte la respuesta a JSON (contiene el grupo recién creado)
      const data = await res.json()
      // Añade el nuevo grupo a la lista existente
      setGroups([...groups, data])
    } catch (err) {
      // Registra el error en la consola
      console.error(err);
      // Muestra una alerta al usuario
      Alert.alert("Error", "No se pudo agregar el grupo");
    }
  };



  return (
    // Contenedor principal usando estilos definidos en StyleSheet
    <View style={styles.container}>
      {/* Título de la pantalla */}
      <Text style={styles.title}>Sus Grupos</Text>

      {/* Campo de texto para ingresar el nombre del nuevo grupo */}
      <TextInput
        placeholder="Nombre del grupo"
        value={groupName}
        onChangeText={setGroupName} // Actualiza el estado cuando el usuario escribe
        style={{ borderWidth: 1, marginBottom: 10, padding: 8 }}
      />

      {/* Botón para crear un nuevo grupo */}
      <Button
        onPress={() => { handleAddGroup(groupName) }} // Llama a handleAddGroup con el nombre ingresado
        title="Presionar"
      />
      {/* Contenedor para la lista de grupos */}
      <View style={styles.listContainer}>
        {/* Mapea cada grupo a un componente TouchableOpacity clickeable */}
        {groups.map((item, index) => (
          <View key={index} style={styles.listContainer}>
            {/* Componente clickeable que navega a los detalles del grupo */}
            <TouchableOpacity
              style={styles.groupItem}
              onPress={() => router.push({
                pathname: "/groupdetail/[groupId]", // Ruta dinámica
                params: { groupId: item.id } // Pasa el ID del grupo como parámetro
              })}
            >
              {/* Muestra el nombre y el ID del grupo */}
              <Text style={styles.groupText}>{item.name} - {item.id}</Text>
            </TouchableOpacity>
          </View>
        ))}
      </View>

      {/* Sección de pie de página con botones de acción */}
      <View style={styles.footer}>
        {/* Botón para cerrar sesión (void para evitar warnings de promesas no manejadas) */}
        <Button title="Cerrar sesión" onPress={() => void logout()} />
        {/* Espaciador entre botones */}
        <View style={{ marginTop: 10 }}>
          {/* Botón rojo para eliminar la cuenta (acción destructiva) */}
          <Button color={"red"} title="Eliminar Cuenta" onPress={() => router.push("/unregister")} />
        </View>
      </View>
    </View>
  );

}

/**
 * styles - Estilos del componente usando StyleSheet
 * 
 * Define todos los estilos visuales de la pantalla principal
 */
const styles = StyleSheet.create({
  // Contenedor principal de la pantalla
  container: {
    flex: 1, // Ocupa todo el espacio disponible
    justifyContent: "center", // Centra el contenido verticalmente
    padding: 20, // Espaciado interno de 20px
    gap: 15, // Espacio entre elementos hijos
    backgroundColor: '#fff', // Fondo blanco
  },
  // Estilo del título
  title: {
    fontSize: 24, // Tamaño de fuente grande
    fontWeight: 'bold', // Texto en negrita
    textAlign: 'center', // Centrado horizontalmente
    marginBottom: 10, // Margen inferior
  },
  // Estilo de los campos de entrada (no se usa actualmente)
  input: {
    borderWidth: 1, // Borde de 1px
    borderColor: '#ccc', // Color del borde gris claro
    padding: 10, // Espaciado interno
    borderRadius: 5, // Bordes redondeados
    width: '100%', // Ancho completo
  },
  // Contenedor de la lista de grupos
  listContainer: {
    width: '100%', // Ancho completo
    marginVertical: 10, // Margen vertical
  },
  // Estilo de cada elemento de grupo
  groupItem: {
    padding: 15, // Espaciado interno
    backgroundColor: '#f0f0f0', // Fondo gris claro
    marginBottom: 10, // Margen inferior entre elementos
    borderRadius: 8, // Bordes redondeados
  },
  // Estilo del texto dentro de cada grupo
  groupText: {
    fontSize: 16, // Tamaño de fuente mediano
    textAlign: 'center', // Centrado horizontalmente
    color: '#007AFF', // Color azul (estilo iOS)
  },
  // Estilo del pie de página
  footer: {
    marginTop: 20, // Margen superior
    width: '100%', // Ancho completo
  }
})