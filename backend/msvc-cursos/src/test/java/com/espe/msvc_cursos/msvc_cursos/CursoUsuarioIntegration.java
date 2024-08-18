package com.espe.msvc_cursos.msvc_cursos;

import com.espe.msvc_cursos.msvc_cursos.clients.UsuarioClientRest;
import com.espe.msvc_cursos.msvc_cursos.models.entity.Curso;
import com.espe.msvc_cursos.msvc_cursos.models.entity.Usuario;
import com.espe.msvc_cursos.msvc_cursos.services.CursoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
public class CursoUsuarioIntegration {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioClientRest usuarioClientRest;

    @MockBean
    private CursoService cursoService;

    @Test
    public void testAgregarUsuarioSuccess() throws Exception {
        // Create and set up Usuario
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Usuario de Prueba");
        usuario.setEmail("usuario@prueba.com");
        usuario.setPassword("password123");

        // Create and set up Curso
        Curso curso = new Curso();
        curso.setId(1L);
        curso.setNombre("Curso de Prueba");

        // Mock the service behavior
        when(usuarioClientRest.detalle(1L)).thenReturn(usuario);
        when(cursoService.porId(1L)).thenReturn(Optional.of(curso));
        when(cursoService.agregarUsuario(any(Usuario.class), anyLong())).thenReturn(Optional.of(usuario));

        // Perform the request and verify the response
        mockMvc.perform(MockMvcRequestBuilders.put("/cursos/1/agregar-usuario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"nombre\":\"Usuario de Prueba\", \"email\":\"usuario@prueba.com\", \"password\":\"password123\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nombre").value("Usuario de Prueba"));
    }

    @Test
    public void testAgregarUsuarioNotFound() throws Exception {
        when(usuarioClientRest.detalle(1L)).thenReturn(null); // Simulate not found

        mockMvc.perform(MockMvcRequestBuilders.put("/cursos/1/agregar-usuario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"nombre\":\"Usuario de Prueba\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testEliminarUsuarioSuccess() throws Exception {
        // Mock the service behavior to return true when attempting to remove a user
        when(cursoService.eliminarUsuario(1L, 1L)).thenReturn(true);

        // Perform the request and verify the response
        mockMvc.perform(MockMvcRequestBuilders.put("/cursos/1/eliminar-usuario/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testEliminarUsuarioNotFound() throws Exception {
        // Mock the service behavior to return false when attempting to remove a user
        when(cursoService.eliminarUsuario(1L, 1L)).thenReturn(false);

        // Perform the request and verify the response
        mockMvc.perform(MockMvcRequestBuilders.put("/cursos/1/eliminar-usuario/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }



}


