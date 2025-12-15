import { FiltroTarea, IdTarea, Tarea } from "../model/tarea";
import { RepositorioTareaRest } from "../repositorio/apiTareasRemota";
import { RespositorioTareaSqlite } from "../repositorio/repositorioTareasSqlite";

export type OrigenDatos = "local" | "remoto";

export class ServicioTareas {
  private repoLocal: RespositorioTareaSqlite;
  private repoRemoto: RepositorioTareaRest;
  private origenActual: OrigenDatos;

  constructor(
    repoLocal: RespositorioTareaSqlite,
    repoRemoto: RepositorioTareaRest,
    origenInicial: OrigenDatos = "local"
  ) {
    this.repoLocal = repoLocal;
    this.repoRemoto = repoRemoto;
    this.origenActual = origenInicial;
  }


  async listar(filtro: FiltroTarea): Promise<Tarea[]> {
    const todas =
      this.origenActual === "local"
        ? this.repoLocal.obtenerTodas()
        : await this.repoRemoto.obtenerTodas();

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

  async crear(titulo: string, descripcion?: string): Promise<Tarea> {
    if (!titulo || titulo.trim().length === 0) {
      throw new Error("El título no puede estar vacío");
    }

    if (this.origenActual === "local") {
      return this.repoLocal.crear(titulo, descripcion);
    } else {
      return await this.repoRemoto.crear({
        titulo,
        descripcion,
        completada: false,
      });
    }
  }

  async obtenerPorId(id: IdTarea): Promise<Tarea | undefined> {
    if (id < 0) {
      throw new Error("El id no puede ser menor que 0");
    }

    return this.origenActual === "local"
      ? this.repoLocal.obtenerPorId(id)
      : await this.repoRemoto.obtenerPorId(id);
  }

  async actualizar(tarea: Tarea): Promise<Tarea | undefined> {
    if (tarea == null) {
      throw new Error("La tarea no puede ser null");
    }

    return this.origenActual === "local"
      ? this.repoLocal.actualizar(tarea)
      : await this.repoRemoto.actualizar(tarea);
  }

  async borrar(id: IdTarea): Promise<boolean> {
    if (id < 0) {
      throw new Error("El id no puede ser menor que 0");
    }

    return this.origenActual === "local"
? this.repoLocal.borrar(id)
      : await this.repoRemoto.borrar(id);
  }
   async sincronizarRemotoALocal(): Promise<void> {
    const tareasRemotas = await this.repoRemoto.obtenerTodas();
    const tareasLocales = this.repoLocal.obtenerTodas();

    for (const tareaLocal of tareasLocales) {
      this.repoLocal.borrar(tareaLocal.id);
    }

    for (const tareaRemota of tareasRemotas) {
      this.repoLocal.crear(tareaRemota.titulo, tareaRemota.descripcion);
    }
  }

}
