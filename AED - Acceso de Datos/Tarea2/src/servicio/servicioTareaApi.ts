import { FiltroTarea, IdTarea, Tarea } from "../model/tarea";
import { RepositorioTareaRest, NuevaTarea } from '../repositorio/apiTareasRemota';



export class ServicioTareas {
  constructor(private api: RepositorioTareaRest) {}

  async listar(): Promise<Tarea[]> {
    const todas = await this.api.obtenerTodas();
    return todas;
  }

  async crear(titulo: string, descripcion?: string): Promise<Tarea[]> {
    if (!titulo || titulo.trim().length === 0) {
      throw new Error("El título no puede estar vacío");
    }
    const nuevaTarea: NuevaTarea = {
      titulo: titulo.trim(),
      descripcion: descripcion?.trim(),
      completada: false,
    };
    return this.api.crear(nuevaTarea);
  }
  async obtenerPorId(id: IdTarea): Tarea | undefined {
    if (id < 0) {
      throw new Error("EL id no puede ser menor que 0");
    }
    return this.repo.obtenerPorId(id);
  }
  async actualizar(tarea: Tarea): Tarea | undefined {
    if (tarea == null) {
      throw new Error("EL id no puede ser menor que 0");
    }

    return this.repo.actualizar(tarea);
  }
  async borrar(id: IdTarea): boolean {
    if (id < 0) {
      throw new Error("EL id no puede ser menor que 0");
    }
    return this.repo.borrar(id);
  }
}
