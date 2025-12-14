import { getDb } from "../bbdd/db";
import { IdTarea, Tarea } from "../model/tarea";

export class RespositorioTareaSqlite {
  private db = getDb();

  obtenerTodas(): Tarea[] {
    const rows = this.db
      .prepare("SELECT id, titulo, descripcion, completada FROM tareas")
      .all() as Tarea[];
    return rows;
  }

  obtenerPorId(id: IdTarea): Tarea | undefined {
    const stmt = this.db.prepare(
      "SELECT id, titulo, descripcion, completada FROM tareas WHERE id = ?"
    );

    return stmt.get(id) as Tarea | undefined;
  }

  crear(titulo: string, descripcion?: string): Tarea {
    const stmt = this.db.prepare(
      "INSERT INTO tareas (titulo, descripcion, completada) VALUES (?, ?, 0)"
    );
    const result = stmt.run(titulo, descripcion);
    const id = Number(result.lastInsertRowid);

    return {
      id,
      titulo,
      descripcion,
      completada: false,
    };
  }

  actualizar(tarea: Tarea): Tarea | undefined {
    const stmt = this.db.prepare(
      "UPDATE tareas SET titulo = ?, descripcion = ?, completada = ? WHERE id = ?"
    );

    const result = stmt.run(
      tarea.titulo,
      tarea.descripcion || null,
      tarea.completada ? 1 : 0,
      tarea.id
    );

    if (result.changes === 0) {
      return undefined;
    }

    return this.obtenerPorId(tarea.id);
  }

  borrar(id: IdTarea): boolean {
    const stmt = this.db.prepare("DELETE FROM tareas WHERE id = ?");
    const result = stmt.run(id);
    return result.changes === 1;
  }
}
