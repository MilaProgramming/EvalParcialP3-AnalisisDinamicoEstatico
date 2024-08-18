package com.espe.msvc_cursos.msvc_cursos.controllers;

import com.espe.msvc_cursos.msvc_cursos.models.entity.CursoUsuario;
import com.espe.msvc_cursos.msvc_cursos.models.entity.Usuario;
import feign.FeignException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.espe.msvc_cursos.msvc_cursos.models.entity.Curso;
import com.espe.msvc_cursos.msvc_cursos.services.CursoService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:4200", "http://localhost:9876"})
public class CursoController {

    @Autowired
    private CursoService service;

    private static ResponseEntity<Map<String, String>> validar(BindingResult result) {
        Map<String, String> errores = new HashMap<>();
        result.getFieldErrors().forEach(err ->{
            errores.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errores);
    }

    @GetMapping("/cursos")
    public List<Curso> listar() {
        return service.listar();
    }

    @GetMapping("/cursos/{id}")
    public ResponseEntity<Curso> porId(@PathVariable Long id) {
        Optional<Curso> curso = service.porId(id);
        if (curso.isPresent()) {
            return ResponseEntity.ok(curso.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/cursos")
    public ResponseEntity<?> guardar(@Valid  @RequestBody Curso curso, BindingResult result) {

        if (result.hasErrors()) {
            return validar(result);
        }

        return ResponseEntity.ok(service.guardar(curso));
    }

    @PutMapping("/cursos/{id}")
    public ResponseEntity<?> actualizar(@Valid @RequestBody Curso curso, @PathVariable Long id, BindingResult result) {

        if (result.hasErrors()) {
            return validar(result);
        }

        Optional<Curso> cursoDb = service.porId(id);
        if (cursoDb.isPresent()) {
            Curso cursoActual = cursoDb.get();
            cursoActual.setNombre(curso.getNombre());
            return ResponseEntity.ok(service.guardar(cursoActual));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/cursos/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        Optional<Curso> curso = service.porId(id);
        if (curso.isPresent()) {
            service.eliminar(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/cursos/{id}/agregar-usuario")
    public ResponseEntity<?> agregarUsuario(@RequestBody Usuario usuario, @PathVariable Long id) {
        System.out.println("Received request to add user to course with ID: " + id);
        Optional<Usuario> usuarioDb;

        try {
            usuarioDb = service.agregarUsuario(usuario, id);
        } catch (FeignException e) {
            return ResponseEntity.status(e.status()).body(e.contentUTF8());
        }

        if (usuarioDb.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioDb.get());
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/cursos/{id}/eliminar-usuario/{cursoUsuarioId}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id, @PathVariable Long cursoUsuarioId) {

        try {
            boolean removed = service.eliminarUsuario(cursoUsuarioId, id);
            if (removed) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }


}
