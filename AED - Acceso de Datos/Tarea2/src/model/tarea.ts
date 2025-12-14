export interface Tarea{
    id:number;
    titulo:string;
    descripcion?:string;
    completada:boolean;
}
export type IdTarea = number;

export type FiltroTarea = "todas" | "pendientes" | "completadas";