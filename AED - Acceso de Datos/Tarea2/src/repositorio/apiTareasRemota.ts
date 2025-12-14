import { IdTarea, Tarea } from "../model/tarea";

export type NuevaTarea = Omit<Tarea, "id">;

export class RepositorioTareaRest {
  private API_URL: string;

  constructor(apiUrl: string = "http://localhost:3000/tareas") {
    this.API_URL = apiUrl;
  }

  async obtenerTodas(): Promise<Tarea[]> {
    const respuesta = await fetch(this.API_URL);

    if (!respuesta.ok) {
      throw new Error(
        `Error al cargar tareas: ${respuesta.status} ${respuesta.statusText}`
      );
    }

    const datos: unknown = await respuesta.json();
    return datos as Tarea[];
  }

  async obtenerPorId(id: IdTarea): Promise<Tarea | undefined> {
    const respuesta = await fetch(`${this.API_URL}/${id}`);

    if (!respuesta.ok) {
      if (respuesta.status === 404) {
        return undefined;
      }
      throw new Error(
        `Error al cargar tarea ${id}: ${respuesta.status} ${respuesta.statusText}`
      );
    }

    const datos: unknown = await respuesta.json();
    return datos as Tarea;
  }

  async crear(nueva: NuevaTarea): Promise<Tarea> {
    const respuesta = await fetch(this.API_URL, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(nueva),
    });

    if (!respuesta.ok) {
      throw new Error(
        `Error al crear tarea: ${respuesta.status} ${respuesta.statusText}`
      );
    }

    const datos: unknown = await respuesta.json();
    return datos as Tarea;
  }

  async actualizar(tarea: Tarea): Promise<Tarea | undefined> {
    const respuesta = await fetch(`${this.API_URL}/${tarea.id}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(tarea),
    });

    if (!respuesta.ok) {
      if (respuesta.status === 404) {
        return undefined;
      }
      throw new Error(
        `Error al actualizar tarea ${tarea.id}: ${respuesta.status} ${respuesta.statusText}`
      );
    }

    const datos: unknown = await respuesta.json();
    return datos as Tarea;
  }

  async borrar(id: IdTarea): Promise<boolean> {
    const respuesta = await fetch(`${this.API_URL}/${id}`, {
      method: "DELETE",
    });

    if (!respuesta.ok) {
      if (respuesta.status === 404) {
        return false;
      }
      throw new Error(
        `Error al borrar tarea ${id}: ${respuesta.status} ${respuesta.statusText}`
      );
    }

    return true;
  }
}