package com.espe.msvc_cursos.msvc_cursos;

import com.espe.msvc_cursos.msvc_cursos.clients.UsuarioClientRest;
import com.espe.msvc_cursos.msvc_cursos.models.entity.Curso;
import com.espe.msvc_cursos.msvc_cursos.models.entity.CursoUsuario;
import com.espe.msvc_cursos.msvc_cursos.models.entity.Usuario;
import com.espe.msvc_cursos.msvc_cursos.repositories.CursoRepository;
import com.espe.msvc_cursos.msvc_cursos.services.CursoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class CursoServiceImplTest {

    @Mock
    private CursoRepository cursoRepository;

    @Mock
    private UsuarioClientRest usuarioClientRest;

    @InjectMocks
    private CursoServiceImpl service;

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

        when(cursoRepository.findAll()).thenReturn(Arrays.asList(curso1, curso2));

        assertThat(service.listar()).hasSize(2);
        verify(cursoRepository, times(1)).findAll();
    }

    @Test
    void testGuardar() {
        Curso curso = new Curso();
        curso.setId(1L);

        when(cursoRepository.save(any(Curso.class))).thenReturn(curso);

        Curso savedCurso = service.guardar(curso);

        assertThat(savedCurso.getId()).isEqualTo(1L);
        verify(cursoRepository, times(1)).save(any(Curso.class));
    }

    @Test
    void testPorId() {
        Curso curso = new Curso();
        curso.setId(1L);

        when(cursoRepository.findById(anyLong())).thenReturn(Optional.of(curso));

        Optional<Curso> foundCurso = service.porId(1L);

        assertThat(foundCurso).isPresent();
        assertThat(foundCurso.get().getId()).isEqualTo(1L);
        verify(cursoRepository, times(1)).findById(anyLong());
    }

    @Test
    void testEliminar() {
        doNothing().when(cursoRepository).deleteById(anyLong());

        service.eliminar(1L);

        verify(cursoRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void testAgregarUsuario() {
        Curso curso = new Curso();
        curso.setId(1L);
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        when(cursoRepository.findById(anyLong())).thenReturn(Optional.of(curso));
        when(usuarioClientRest.detalle(anyLong())).thenReturn(usuario);

        Optional<Usuario> usuarioOptional = service.agregarUsuario(usuario, 1L);

        assertThat(usuarioOptional).isPresent();
        assertThat(usuarioOptional.get().getId()).isEqualTo(1L);
        verify(cursoRepository, times(1)).findById(anyLong());
        verify(usuarioClientRest, times(1)).detalle(anyLong());
    }

    @Test
    void testEliminarUsuario() {
        Curso curso = new Curso();
        curso.setId(1L);
        CursoUsuario cursoUsuario = new CursoUsuario();
        cursoUsuario.setId(1L);
        curso.addCursoUsuario(cursoUsuario);

        when(cursoRepository.findById(anyLong())).thenReturn(Optional.of(curso));

        boolean result = service.eliminarUsuario(1L, 1L);

        assertThat(result).isTrue();
        verify(cursoRepository, times(1)).findById(anyLong());
        verify(cursoRepository, times(1)).save(any(Curso.class));
    }


}
