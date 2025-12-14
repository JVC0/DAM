import { FiltroTarea, IdTarea, Tarea } from "../model/tarea";
import { RepositorioTareaRest } from "../repositorio/apiTareasRemota";
import { RespositorioTareaSqlite } from "../repositorio/repositorioTareasSqlite";
export type OrigenDatos = "local" | "remoto";

// cambiar nombre de typo
export class ServicioTareas {
  private repo: RespositorioTareaSqlite | RepositorioTareaRest;
  constructor(useRemote: OrigenDatos = "local", apiUrl?: string) {
    if (useRemote === "local") {
      this.repo = new RespositorioTareaSqlite();
    } else {
      this.repo = new RepositorioTareaRest(apiUrl);
    }
  }

  async listar(): Promise<Tarea[]> {
    const todas = await this.repo.obtenerTodas();
    return todas;
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

