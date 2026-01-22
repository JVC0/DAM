// Importa hooks de expo-router para navegación y obtención de parámetros de la URL
import { useRouter, useLocalSearchParams } from "expo-router";
// Importa React y hooks necesarios para el componente
import React, { useContext, useEffect, useState } from "react";
// Importa componentes básicos de React Native para la interfaz de usuario
import { Button, Text, TextInput, View, Alert, Modal, StyleSheet, ScrollView } from "react-native";
// Importa el contexto de autenticación para acceder al token
import { AuthContext } from "../../context/AuthContext";
// Importa Constants para acceder a la configuración de la aplicación
import Constants from "expo-constants";
// Importa SafeAreaView para evitar que el contenido se superponga con áreas del sistema (notch, barra de estado, etc.)
import { SafeAreaView } from "react-native-safe-area-context";

/**
 * Expense - Interfaz que define la estructura de un gasto
 */
interface Expense {
  id: number;          // ID único del gasto
  desc: string;        // Descripción del gasto
  amount: number;      // Monto del gasto
  paid_by: number;     // ID del usuario que pagó el gasto
}

/**
 * GroupDetail - Pantalla de detalles de un grupo
 * 
 * Muestra todos los gastos de un grupo específico y permite:
 * - Ver la lista de gastos
 * - Agregar nuevos gastos
 * - Editar gastos existentes
 * - Eliminar gastos
 */
export default function GroupDetail() {
  // Extrae el token del contexto de autenticación
  const { token } = useContext(AuthContext);
  // Obtiene el parámetro groupId de la URL (ruta dinámica)
  const { groupId } = useLocalSearchParams();
  // Estado para almacenar el ID del gasto que se está editando (null si se está agregando uno nuevo)
  const [currentExpenseId, setCurrentExpenseId] = useState<number | null>(null);
  // Estado para almacenar el monto del gasto en el formulario
  const [newAmount, setNewAmount] = useState("");
  // Estado para almacenar la descripción del gasto en el formulario
  const [newDescription, setNewDescription] = useState("");
  // Estado para controlar la visibilidad del modal de agregar/editar
  const [visibleModal, setVisibleModal] = useState(false);
  // Estado para almacenar la lista de gastos del grupo
  const [expenses, setExpenses] = useState<Expense[]>([]);
  // Hook para navegar entre pantallas
  const router = useRouter();
  // Obtiene la URL de la API desde la configuración de la aplicación
  const API_URL = Constants.expoConfig?.extra?.apiUrl ?? "";

  /**
   * handleExpensesList - Obtiene la lista de gastos del grupo desde la API
   * 
   * Realiza una petición GET al endpoint /groups/{groupId}/expenses
   * Si es exitosa, actualiza el estado con los gastos recibidos.
   * Si falla, muestra un mensaje de error.
   */
  const handleExpensesList = async () => {
    try {
      // Realiza la petición GET a la API
      const res = await fetch(`${API_URL}/groups/${groupId}/expenses`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          "Authorization": `Bearer ${token}` // Incluye el token en el header
        },
      });
      // Si la respuesta no es exitosa, lanza un error
      if (!res.ok) throw new Error("Error al cargar gastos");
      // Convierte la respuesta a JSON
      const data = await res.json();
      // Actualiza el estado con los gastos recibidos
      setExpenses(data);
    } catch (err) {
      // Registra el error en la consola
      console.error(err);
      // Muestra una alerta al usuario
      Alert.alert("Error", "No se pudieron cargar los gastos");
    }
  };

  /**
   * useEffect - Verifica autenticación y carga gastos
   * 
   * Si el usuario está autenticado y hay un groupId, carga la lista de gastos.
   * Si no está autenticado, redirige al login.
   * Se ejecuta cada vez que el token o groupId cambian.
   */
  useEffect(() => {
    if (token && groupId) {
      // Si hay token y groupId, carga los gastos
      handleExpensesList();
      // Log para debugging
      console.log(groupId);
    } else {
      // Si no hay token, redirige al login después de 0ms
      setTimeout(() => {
        router.replace("/login");
      }, 0);
    }
  }, [token, groupId]); // Se ejecuta cuando el token o groupId cambian

  /**
   * handleAddExpense - Crea un nuevo gasto en el grupo
   * 
   * Valida que los campos estén completos y realiza una petición POST
   * al endpoint /groups/{groupId}/expenses.
   * Si es exitosa, recarga la lista de gastos y cierra el modal.
   * Si falla, muestra un mensaje de error.
   */
  const handleAddExpense = async () => {
    // Valida que los campos no estén vacíos
    if (!newAmount || !newDescription) {
      Alert.alert("Error", "Completa todos los campos");
      return;
    }
    try {
      // Realiza la petición POST a la API
      const res = await fetch(`${API_URL}/groups/${groupId}/expenses`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "Authorization": `Bearer ${token}` // Incluye el token en el header
        }, 
        body: JSON.stringify({ 
          description: newDescription,
          amount: parseFloat(newAmount) // Convierte el string a número
        })
      });
      // Si la respuesta no es exitosa, lanza un error
      if (!res.ok) throw new Error("Error al agregar gasto");
      // Obtiene el gasto recién creado (no se usa actualmente)
      const data = await res.json();
      // Recarga la lista completa de gastos desde el servidor
      handleExpensesList(); 
      // Cierra el modal
      setVisibleModal(false);
      // Limpia los campos del formulario
      setNewAmount("");
      setNewDescription("");
    } catch (err) {
      // Registra el error en la consola
      console.error(err);
      // Muestra una alerta al usuario
      Alert.alert("Error", "No se pudo agregar el gasto");
    }
  };

  /**
   * handleEditExpense - Edita un gasto existente
   * 
   * Valida que los campos estén completos y realiza una petición PUT
   * al endpoint /groups/{groupId}/expenses/{expenseId}.
   * Si es exitosa, actualiza el gasto en el estado local sin recargar todo.
   * Si falla, muestra un mensaje de error.
   */
  const handleEditExpense = async () => {
    // Valida que los campos no estén vacíos y que haya un ID de gasto
    if (!newAmount || !newDescription || !currentExpenseId) {
      Alert.alert("Error", "Completa todos los campos");
      return;
    }
    try {
      // Realiza la petición PUT a la API
      const res = await fetch(`${API_URL}/groups/${groupId}/expenses/${currentExpenseId}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          "Authorization": `Bearer ${token}` // Incluye el token en el header
        }, 
        body: JSON.stringify({ 
          description: newDescription,
          amount: parseFloat(newAmount) // Convierte el string a número
        })
      });
      // Si la respuesta no es exitosa, lanza un error
      if (!res.ok) throw new Error("Error al editar gasto");
      
      // Actualiza el gasto en el estado local sin recargar desde el servidor
      // Mapea todos los gastos y reemplaza el que coincide con el ID editado
      setExpenses(expenses.map(e => 
        e.id === currentExpenseId 
          ? { ...e, desc: newDescription, amount: parseFloat(newAmount) }
          : e
      ));
      
      // Cierra el modal
      setVisibleModal(false);
      // Limpia los campos del formulario
      setNewAmount("");
      setNewDescription("");
      // Limpia el ID del gasto actual
      setCurrentExpenseId(null);
    } catch (err) {
      // Registra el error en la consola
      console.error(err);
      // Muestra una alerta al usuario
      Alert.alert("Error", "No se pudo editar el gasto");
    }
  };

  /**
   * handleDeleteExpense - Elimina un gasto
   * 
   * Realiza una petición DELETE al endpoint /groups/{groupId}/expenses/{id}.
   * Si es exitosa, elimina el gasto del estado local sin recargar todo.
   * Si falla, muestra un mensaje de error.
   * 
   * @param id - ID del gasto a eliminar
   */
  const handleDeleteExpense = async (id: number) => {
    try {
      // Realiza la petición DELETE a la API
      const res = await fetch(`${API_URL}/groups/${groupId}/expenses/${id}`, {
        method: "DELETE",
        headers: {
          "Content-Type": "application/json",
          "Authorization": `Bearer ${token}` // Incluye el token en el header
        },
      });
      // Si la respuesta no es exitosa, lanza un error
      if (!res.ok) throw new Error("Error al borrar gasto");
      
      // Filtra el gasto eliminado del estado local
      setExpenses(expenses.filter(e => e.id !== id));
      // Muestra un mensaje de éxito
      Alert.alert("Éxito", "Gasto eliminado");
    } catch (err) {
      // Registra el error en la consola
      console.error(err);
      // Muestra una alerta al usuario
      Alert.alert("Error", "No se pudo eliminar el gasto");
    }
  };

  /**
   * openModalForAdd - Abre el modal para agregar un nuevo gasto
   * 
   * Limpia todos los campos del formulario y establece currentExpenseId a null
   * para indicar que se está creando un gasto nuevo.
   */
  const openModalForAdd = () => {
    setCurrentExpenseId(null); // Indica que es un nuevo gasto
    setNewAmount(""); // Limpia el campo de monto
    setNewDescription(""); // Limpia el campo de descripción
    setVisibleModal(true); // Muestra el modal
  };

  /**
   * openModalForEdit - Abre el modal para editar un gasto existente
   * 
   * Carga los datos del gasto en el formulario y establece currentExpenseId
   * para indicar que se está editando un gasto existente.
   * 
   * @param expense - El gasto a editar
   */
  const openModalForEdit = (expense: Expense) => {
    setCurrentExpenseId(expense.id); // Guarda el ID del gasto a editar
    setNewAmount(expense.amount.toString()); // Carga el monto en el formulario
    setNewDescription(expense.desc); // Carga la descripción en el formulario
    setVisibleModal(true); // Muestra el modal
  };

  /**
   * closeModal - Cierra el modal y limpia el formulario
   * 
   * Resetea todos los estados relacionados con el formulario del modal.
   */
  const closeModal = () => {
    setVisibleModal(false); // Oculta el modal
    setNewAmount(""); // Limpia el campo de monto
    setNewDescription(""); // Limpia el campo de descripción
    setCurrentExpenseId(null); // Limpia el ID del gasto actual
  };

  return (
    // SafeAreaView evita que el contenido se superponga con áreas del sistema
    <SafeAreaView style={styles.container}>
      {/* Título mostrando el ID del grupo */}
      <Text style={styles.title}>Mi grupo #{groupId}</Text>

      {/* Contenedor del botón para agregar gastos */}
      <View style={styles.addButtonContainer}>
        <Button title="Agregar Gasto" onPress={openModalForAdd} />
      </View>

      {/* ScrollView permite desplazamiento si hay muchos gastos */}
      <ScrollView style={styles.scrollView}>
        {/* Mapea cada gasto a un componente visual */}
        {expenses.map((e, index) => (
          <View key={index} style={styles.expenseItem}>
            {/* Muestra el monto del gasto */}
            <Text style={styles.expenseText}>Monto: ${e.amount}</Text>
            {/* Muestra la descripción del gasto */}
            <Text style={styles.expenseText}>Descripción: {e.desc}</Text>
            {/* Muestra quién pagó el gasto */}
            <Text style={styles.expenseText}>Pagado por: {e.paid_by}</Text>
            {/* Contenedor de botones de acción */}
            <View style={styles.buttonContainer}>
              {/* Botón para editar el gasto */}
              <Button title="Editar" onPress={() => openModalForEdit(e)} />
              {/* Botón rojo para borrar el gasto */}
              <Button title="Borrar" onPress={() => handleDeleteExpense(e.id)} color="#ff3b30" />
            </View>
          </View>
        ))}
      </ScrollView>

      {/* Modal para agregar o editar gastos */}
      <Modal visible={visibleModal} animationType="slide" transparent={true}>
        {/* Overlay semitransparente de fondo */}
        <View style={styles.modalOverlay}>
          {/* Contenedor del contenido del modal */}
          <View style={styles.modalContent}>
            {/* Título del modal (cambia según si se edita o agrega) */}
            <Text style={styles.modalTitle}>
              {currentExpenseId ? `Editar gasto #${currentExpenseId}` : "Agregar nuevo gasto"}
            </Text>
            
            {/* Label para el campo de monto */}
            <Text style={styles.label}>Monto:</Text>
            {/* Campo de texto para ingresar el monto */}
            <TextInput
              style={styles.input}
              value={newAmount}
              onChangeText={setNewAmount} // Actualiza el estado cuando el usuario escribe
              placeholder="Ej: 25.50"
              keyboardType="numeric" // Muestra teclado numérico
            />
            
            {/* Label para el campo de descripción */}
            <Text style={styles.label}>Descripción:</Text>
            {/* Campo de texto para ingresar la descripción */}
            <TextInput
              style={styles.input}
              value={newDescription}
              onChangeText={setNewDescription} // Actualiza el estado cuando el usuario escribe
              placeholder="Ej: Cena restaurante"
            />
            
            {/* Contenedor de botones del modal */}
            <View style={styles.modalButtons}>
              {/* Botón que cambia según si se edita o agrega */}
              <Button 
                title={currentExpenseId ? "Guardar" : "Agregar"} 
                onPress={currentExpenseId ? handleEditExpense : handleAddExpense} 
              />
              {/* Espaciador entre botones */}
              <View style={styles.buttonSpacer} />
              {/* Botón rojo para cancelar */}
              <Button title="Cancelar" onPress={closeModal} color="#ff3b30" />
            </View>
          </View>
        </View>
      </Modal>

      {/* Pie de página con botón para volver */}
      <View style={styles.footer}>
        <Button title="Volver" onPress={() => router.replace("/")} />
      </View>
    </SafeAreaView>
  );
}

/**
 * styles - Estilos del componente usando StyleSheet
 * 
 * Define todos los estilos visuales de la pantalla de detalles del grupo
 */
const styles = StyleSheet.create({
  // Contenedor principal de la pantalla
  container: {
    flex: 1, // Ocupa todo el espacio disponible
    padding: 20, // Espaciado interno
    backgroundColor: '#fff', // Fondo blanco
  },
  // Estilo del título
  title: {
    fontSize: 24, // Tamaño de fuente grande
    fontWeight: 'bold', // Texto en negrita
    marginBottom: 20, // Margen inferior
  },
  // Contenedor del botón de agregar
  addButtonContainer: {
    marginBottom: 15, // Margen inferior
  },
  // Contenedor con scroll para la lista de gastos
  scrollView: {
    flex: 1, // Ocupa el espacio disponible
  },
  // Estilo de cada elemento de gasto
  expenseItem: {
    padding: 15, // Espaciado interno
    marginBottom: 10, // Margen inferior entre elementos
    borderWidth: 1, // Borde de 1px
    borderColor: '#ddd', // Color del borde gris claro
    borderRadius: 5, // Bordes redondeados
    backgroundColor: '#f9f9f9', // Fondo gris muy claro
  },
  // Estilo del texto dentro de cada gasto
  expenseText: {
    fontSize: 16, // Tamaño de fuente mediano
    marginBottom: 5, // Margen inferior entre líneas
  },
  // Contenedor de botones dentro de cada gasto
  buttonContainer: {
    flexDirection: 'row', // Botones en fila horizontal
    gap: 10, // Espacio entre botones
    marginTop: 10, // Margen superior
  },
  // Overlay semitransparente del modal
  modalOverlay: {
    flex: 1, // Ocupa toda la pantalla
    justifyContent: 'center', // Centra el contenido verticalmente
    padding: 20, // Espaciado interno
    backgroundColor: 'rgba(0,0,0,0.5)', // Fondo negro semitransparente
  },
  // Contenedor del contenido del modal
  modalContent: {
    backgroundColor: '#fff', // Fondo blanco
    padding: 20, // Espaciado interno
    borderRadius: 10, // Bordes redondeados
  },
  // Título del modal
  modalTitle: {
    fontSize: 20, // Tamaño de fuente grande
    fontWeight: 'bold', // Texto en negrita
    marginBottom: 20, // Margen inferior
  },
  // Labels de los campos del formulario
  label: {
    fontSize: 16, // Tamaño de fuente mediano
    fontWeight: 'bold', // Texto en negrita
    marginTop: 10, // Margen superior
    marginBottom: 5, // Margen inferior
  },
  // Campos de entrada del formulario
  input: {
    borderWidth: 2, // Borde de 2px
    borderColor: '#007AFF', // Color azul (estilo iOS)
    padding: 12, // Espaciado interno
    marginBottom: 15, // Margen inferior
    borderRadius: 5, // Bordes redondeados
    fontSize: 16, // Tamaño de fuente mediano
    backgroundColor: '#fff', // Fondo blanco
  },
  // Contenedor de botones del modal
  modalButtons: {
    marginTop: 10, // Margen superior
  },
  // Espaciador entre botones del modal
  buttonSpacer: {
    height: 10, // Altura de 10px
  },
  // Pie de página
  footer: {
    marginTop: 20, // Margen superior
  },
});
