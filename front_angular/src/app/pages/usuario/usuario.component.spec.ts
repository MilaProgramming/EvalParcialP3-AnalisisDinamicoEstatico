import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormsModule } from '@angular/forms';
import { UsuarioComponent } from './usuario.component';
import { By } from '@angular/platform-browser';

describe('UsuarioComponent', () => {
  let component: UsuarioComponent;
  let fixture: ComponentFixture<UsuarioComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, FormsModule, UsuarioComponent]
    }).compileComponents();

    fixture = TestBed.createComponent(UsuarioComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  // Test rendering of the component
  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should render the title and button correctly', () => {
    const titleElement = fixture.debugElement.query(By.css('h2')).nativeElement;
    expect(titleElement.textContent).toContain('Lista de Usuarios');

    const addButton = fixture.debugElement.query(By.css('button.agregar')).nativeElement;
    expect(addButton.textContent).toContain('Agregar');
  });

  // Test the modal visibility and content
  it('should show the modal when handleAgregarClick is called', () => {
    component.handleAgregarClick();
    fixture.detectChanges();
    const modal = fixture.debugElement.query(By.css('div.modal'));
    expect(modal).toBeTruthy();
    const heading = fixture.debugElement.query(By.css('h3')).nativeElement;
    expect(heading.textContent).toContain('Agregar Usuario');
  });

  it('should hide the modal when handleCloseModal is called', () => {
    component.handleAgregarClick(); // Open the modal
    fixture.detectChanges();
    component.handleCloseModal(); // Close the modal
    fixture.detectChanges();
    const modal = fixture.debugElement.query(By.css('div.modal'));
    expect(modal).toBeFalsy();
  });

  it('should show password mismatch error when passwords do not match', () => {
    // Open the modal
    component.showModal = true;
    fixture.detectChanges();

    // Query the password inputs
    const passwordInputs = fixture.debugElement.queryAll(By.css('input[type=password]'));
    console.log('Password inputs:', passwordInputs); // Debug: Check if inputs are found

    if (passwordInputs.length >= 2) {
      const passwordInput = passwordInputs[0].nativeElement;
      const confirmPasswordInput = passwordInputs[1].nativeElement;
      passwordInput.value = 'password123';
      confirmPasswordInput.value = 'password456';
      passwordInput.dispatchEvent(new Event('input'));
      confirmPasswordInput.dispatchEvent(new Event('input'));
      fixture.detectChanges();

      // Query the error message
      const errorElement = fixture.debugElement.query(By.css('p.error'));
      console.log('Error element:', errorElement); // Debug: Check if error element is found

      if (errorElement) {
        expect(errorElement.nativeElement.textContent).toContain('Las contraseñas no coinciden');
      } else {
        fail('Error message element not found');
      }
    } else {
      fail('Password input fields not found');
    }
  });

  // Test button click actions
  it('should call handleAgregarClick when the add button is clicked', () => {
    spyOn(component, 'handleAgregarClick');
    const addButton = fixture.debugElement.query(By.css('button.agregar')).nativeElement;
    addButton.click();
    expect(component.handleAgregarClick).toHaveBeenCalled();
  });

  it('should call handleDelete when the delete icon is clicked', () => {
    // Ensure there are some users to render in the table
    component.usuarios = [{ id: 1, nombre: 'Test User', email: 'test@example.com' }];
    fixture.detectChanges(); // Update the DOM with users

    spyOn(component, 'handleDelete');

    // Find the delete icon
    const deleteIcon = fixture.debugElement.query(By.css('i.fa-trash'));
    expect(deleteIcon).toBeTruthy(); // Ensure the delete icon exists

    if (deleteIcon) {
      deleteIcon.nativeElement.click();
      expect(component.handleDelete).toHaveBeenCalled();
    }
  });
});
