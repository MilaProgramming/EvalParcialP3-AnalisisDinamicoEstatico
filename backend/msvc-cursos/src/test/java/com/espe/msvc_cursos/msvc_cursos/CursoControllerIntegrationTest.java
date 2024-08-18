package com.espe.msvc_cursos.msvc_cursos;

import com.espe.msvc_cursos.msvc_cursos.models.entity.Curso;
import com.espe.msvc_cursos.msvc_cursos.models.entity.Usuario;
import com.espe.msvc_cursos.msvc_cursos.services.CursoService;
import com.espe.msvc_cursos.msvc_cursos.controllers.CursoController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
public class CursoControllerIntegrationTest {

    private MockMvc mockMvc;

    @Mock
    private CursoService service;

    @InjectMocks
    private CursoController controller;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testListar() throws Exception {
        Curso curso = new Curso();
        curso.setId(1L);
        curso.setNombre("Curso de Prueba");

        when(service.listar()).thenReturn(List.of(curso));

        mockMvc.perform(MockMvcRequestBuilders.get("/cursos")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].nombre").value("Curso de Prueba"));
    }

    @Test
    public void testPorId() throws Exception {
        Curso curso = new Curso();
        curso.setId(1L);
        curso.setNombre("Curso de Prueba");

        when(service.porId(1L)).thenReturn(Optional.of(curso));

        mockMvc.perform(MockMvcRequestBuilders.get("/cursos/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nombre").value("Curso de Prueba"));
    }

    @Test
    public void testPorIdNotFound() throws Exception {
        when(service.porId(1L)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/cursos/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testGuardar() throws Exception {
        Curso curso = new Curso();
        curso.setId(1L);
        curso.setNombre("Curso Nuevo");

        when(service.guardar(any(Curso.class))).thenReturn(curso);

        mockMvc.perform(MockMvcRequestBuilders.post("/cursos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Curso Nuevo\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nombre").value("Curso Nuevo"));
    }

    @Test
    public void testActualizar() throws Exception {
        Curso curso = new Curso();
        curso.setId(1L);
        curso.setNombre("Curso Actualizado");

        when(service.porId(1L)).thenReturn(Optional.of(curso));
        when(service.guardar(any(Curso.class))).thenReturn(curso);

        mockMvc.perform(MockMvcRequestBuilders.put("/cursos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Curso Actualizado\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nombre").value("Curso Actualizado"));
    }

    @Test
    public void testActualizarNotFound() throws Exception {
        Curso curso = new Curso();
        curso.setId(1L);
        curso.setNombre("Curso Actualizado");

        when(service.porId(1L)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.put("/cursos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Curso Actualizado\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testEliminar() throws Exception {
        when(service.porId(1L)).thenReturn(Optional.of(new Curso()));
        doNothing().when(service).eliminar(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/cursos/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testEliminarNotFound() throws Exception {
        when(service.porId(1L)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.delete("/cursos/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testAgregarUsuario() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Usuario de Prueba");

        when(service.agregarUsuario(any(Usuario.class), eq(1L))).thenReturn(Optional.of(usuario));

        mockMvc.perform(MockMvcRequestBuilders.put("/cursos/1/agregar-usuario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"nombre\":\"Usuario de Prueba\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nombre").value("Usuario de Prueba"));
    }

    @Test
    public void testAgregarUsuarioNotFound() throws Exception {
        when(service.agregarUsuario(any(Usuario.class), eq(1L))).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.put("/cursos/1/agregar-usuario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"nombre\":\"Usuario de Prueba\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testEliminarUsuario() throws Exception {
        when(service.eliminarUsuario(1L, 1L)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.put("/cursos/1/eliminar-usuario/1"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testEliminarUsuarioNotFound() throws Exception {
        when(service.eliminarUsuario(1L, 1L)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.put("/cursos/1/eliminar-usuario/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testEliminarUsuarioInternalServerError() throws Exception {
        when(service.eliminarUsuario(1L, 1L)).thenThrow(new RuntimeException("Error"));

        mockMvc.perform(MockMvcRequestBuilders.put("/cursos/1/eliminar-usuario/1"))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.content().string("Error"));
    }
}
