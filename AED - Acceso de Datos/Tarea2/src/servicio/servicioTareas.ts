import { FiltroTarea, IdTarea, Tarea } from "../model/tarea";
import { RespositorioTareaSqlite } from "../repositorio/repositorioTareasSqlite";


export class ServicioTareas {
  constructor(private repo: RespositorioTareaSqlite) {}

  listar(filtro: FiltroTarea): Tarea[] {
    const todas = this.repo.obtenerTodas();
    switch (filtro) {
      case "pendientes":
        return todas.filter((t) => !t.completada);
      case "completadas":
        return todas.filter((t) => t.completada);
      case "todas":
      default:
        return todas;
    }
  }

  crear(titulo: string, descripcion?: string): Tarea {
    if (!titulo || titulo.trim().length === 0) {
      throw new Error("El título no puede estar vacío");
    }
    return this.repo.crear(titulo, descripcion);
  }
  obtenerPorId(id: IdTarea): Tarea | undefined {
    if (id < 0) {
      throw new Error("EL id no puede ser menor que 0");
    }
    return this.repo.obtenerPorId(id);
  }
  actualizar(tarea: Tarea): Tarea | undefined {
    if (tarea == null) {
      throw new Error("EL id no puede ser menor que 0");
    }

    return this.repo.actualizar(tarea);
  }
  borrar(id: IdTarea): boolean {
    if (id < 0) {
      throw new Error("EL id no puede ser menor que 0");
    }
    return this.repo.borrar(id);
  }
}
