package com.espe.msvc_cursos.msvc_cursos;

import com.espe.msvc_cursos.msvc_cursos.models.entity.Curso;
import com.espe.msvc_cursos.msvc_cursos.models.entity.Usuario;
import com.espe.msvc_cursos.msvc_cursos.services.CursoService;
import com.espe.msvc_cursos.msvc_cursos.controllers.CursoController;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class CursoControllerTest {

    @Mock
    private CursoService service;

    @InjectMocks
    private CursoController controller;

    @Mock
    private BindingResult bindingResult;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListar() {
        Curso curso1 = new Curso();
        curso1.setId(1L);
        Curso curso2 = new Curso();
        curso2.setId(2L);

        when(service.listar()).thenReturn(Arrays.asList(curso1, curso2));

        List<Curso> cursos = controller.listar();

        assertThat(cursos).hasSize(2);
        verify(service, times(1)).listar();
    }

    @Test
    void testPorId() {
        Curso curso = new Curso();
        curso.setId(1L);

        when(service.porId(anyLong())).thenReturn(Optional.of(curso));

        ResponseEntity<Curso> response = controller.porId(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(curso);
        verify(service, times(1)).porId(anyLong());
    }

    @Test
    void testGuardar() {
        Curso curso = new Curso();
        curso.setId(1L);

        when(service.guardar(any(Curso.class))).thenReturn(curso);

        ResponseEntity<?> response = controller.guardar(curso, bindingResult);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(curso);
        verify(service, times(1)).guardar(any(Curso.class));
    }

    @Test
    void testActualizar() {
        Curso curso = new Curso();
        curso.setId(1L);
        curso.setNombre("Math");

        when(service.porId(anyLong())).thenReturn(Optional.of(curso));
        when(service.guardar(any(Curso.class))).thenReturn(curso);

        ResponseEntity<?> response = controller.actualizar(curso, 1L, bindingResult);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(curso);
        verify(service, times(1)).porId(anyLong());
        verify(service, times(1)).guardar(any(Curso.class));
    }

    @Test
    void testEliminar() {
        Curso curso = new Curso();
        curso.setId(1L);

        when(service.porId(anyLong())).thenReturn(Optional.of(curso));
        doNothing().when(service).eliminar(anyLong());

        ResponseEntity<Void> response = controller.eliminar(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(service, times(1)).porId(anyLong());
        verify(service, times(1)).eliminar(anyLong());
    }

    @Test
    void testAgregarUsuario() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        when(service.agregarUsuario(any(Usuario.class), anyLong())).thenReturn(Optional.of(usuario));

        ResponseEntity<?> response = controller.agregarUsuario(usuario, 1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(usuario);
        verify(service, times(1)).agregarUsuario(any(Usuario.class), anyLong());
    }

    @Test
    void testEliminarUsuario() {
        when(service.eliminarUsuario(anyLong(), anyLong())).thenReturn(true);

        ResponseEntity<?> response = controller.eliminarUsuario(1L, 1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(service, times(1)).eliminarUsuario(anyLong(), anyLong());
    }
}
