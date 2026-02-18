export interface LoginRequest {
  email: string;
  senha: string;
}

export interface LoginResponse {
  token: string;
  tipo: string;
  usuario: Usuario;
}

export interface Usuario {
  id: number;
  nome: string;
  email: string;
  roles: string[];
}

export interface MenuItemModel {
  id: number;
  label: string;
  icone: string;
  rota: string | null;
  ordem: number;
  children: MenuItemModel[];
}
