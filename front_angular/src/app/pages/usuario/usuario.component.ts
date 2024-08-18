import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';

// Define the interface for a Usuario
interface Usuario {
  id: number;
  nombre: string;
  email: string;
  password?: string; // Optional if not used for fetching
}

@Component({
  selector: 'app-usuario',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule],
  templateUrl: './usuario.component.html',
  styleUrls: ['./usuario.component.css']
})
export class UsuarioComponent {
  usuarios: Usuario[] = [];
  showModal: boolean = false;
  showModalWarning: boolean = false;
  id: number | null = null;
  nombre: string = '';
  email: string = '';
  password: string = '';
  confirmPassword: string = '';
  isAddOrUpdate: boolean | null = null;
  passwordMatch: boolean = false;

  private http = inject(HttpClient);

  constructor() {
    this.fetchUsuarios();
  }

  fetchUsuarios(): void {
    this.http.get<Usuario[]>('http://localhost:8001/api/usuarios/listar').subscribe({
      next: (data) => {
        this.usuarios = data;
      },
      error: (err) => {
        console.error('Error fetching Usuarios:', err);
      }
    });
  }

  handleAgregarClick(): void {
    this.showModal = true;
    this.isAddOrUpdate = true;
  }

  handleCloseModal(): void {
    this.showModal = false;
    this.showModalWarning = false;
    this.passwordMatch = false;
    this.nombre = '';
    this.email = '';
    this.password = '';
    this.confirmPassword = '';
  }

  handleNombreChange(event: Event): void {
    this.nombre = (event.target as HTMLInputElement).value;
  }

  handleEmailChange(event: Event): void {
    this.email = (event.target as HTMLInputElement).value;
  }

  handlePasswordChange(event: Event): void {
    this.password = (event.target as HTMLInputElement).value;
    this.checkPasswordMatch();
  }

  handleConfirmPasswordChange(event: Event): void {
    this.confirmPassword = (event.target as HTMLInputElement).value;
    this.checkPasswordMatch();
  }

  checkPasswordMatch(): void {
    this.passwordMatch = this.password === this.confirmPassword;
  }

  handleAceptarClick(): void {
    if (!this.passwordMatch) return;

    const user: Omit<Usuario, 'id'> = {
      nombre: this.nombre,
      email: this.email,
      password: this.password
    };

    if (this.isAddOrUpdate) {
      this.http.post<Usuario>('http://localhost:8001/api/usuarios/crear', user).subscribe({
        next: () => {
          this.fetchUsuarios();
          this.handleCloseModal();
        },
        error: (err) => {
          console.error('Error adding Usuario:', err);
        }
      });
    } else if (this.id) {
      this.http.put<Usuario>(`http://localhost:8001/api/usuarios/editar/${this.id}`, user).subscribe({
        next: () => {
          this.fetchUsuarios();
          this.handleCloseModal();
        },
        error: (err) => {
          console.error('Error updating Usuario:', err);
        }
      });
    }
  }

  handleUpdate(id: number, nombre: string, email: string): void {
    this.id = id;
    this.nombre = nombre;
    this.email = email;
    this.showModal = true;
    this.isAddOrUpdate = false;
  }

  handleDelete(idUser: number): void {
    console.log(`Deleting user with ID ${idUser}`);
    this.http.delete(`http://localhost:8001/api/usuarios/eliminar/${idUser}`).subscribe({
      next: () => {
        this.fetchUsuarios();
      },
      error: (err) => {
        console.error('Error deleting Usuario:', err);
        this.showModalWarning = true;
      }
    });
  }
}
