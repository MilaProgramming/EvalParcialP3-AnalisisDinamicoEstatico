package com.espe.msvc_cursos.msvc_cursos.services;

import com.espe.msvc_cursos.msvc_cursos.clients.UsuarioClientRest;
import com.espe.msvc_cursos.msvc_cursos.controllers.CursoController;
import com.espe.msvc_cursos.msvc_cursos.models.entity.CursoUsuario;
import com.espe.msvc_cursos.msvc_cursos.models.entity.Usuario;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.espe.msvc_cursos.msvc_cursos.models.entity.Curso;
import com.espe.msvc_cursos.msvc_cursos.repositories.CursoRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CursoServiceImpl implements CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    UsuarioClientRest usuarioClientRest;

    @Mock
    private CursoService cursoService;

    @InjectMocks
    private CursoController cursoController;

    @Override
    @Transactional(readOnly = true)
    public List<Curso> listar() {
        return (List<Curso>) cursoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Curso> porId(Long id) {
        return cursoRepository.findById(id);
    }

    @Override
    @Transactional
    public Curso guardar(Curso curso) {
        return cursoRepository.save(curso);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        cursoRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Optional<Usuario> agregarUsuario(Usuario usuario, Long idCurso) {
        System.out.println("PRRROOCESING request with user: " + usuario + " and course ID: " + idCurso);
        Optional<Curso> optionalCurso = cursoRepository.findById(idCurso);

        if (optionalCurso.isPresent()) {
            Usuario usuarioMicro = usuarioClientRest.detalle(usuario.getId());

            Curso curso = optionalCurso.get();
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioMicro.getId());

            curso.addCursoUsuario(cursoUsuario);
            cursoRepository.save(curso);
            return Optional.of(usuarioMicro);
        }

        return Optional.empty();
    }


    @Override
    @Transactional
    public boolean eliminarUsuario(Long cursoUsuarioId, Long cursoId) {
        Optional<Curso> cursoOptional = cursoRepository.findById(cursoId);
        if (cursoOptional.isPresent()) {
            Curso curso = cursoOptional.get();
            CursoUsuario cursoUsuarioToRemove = null;
            for (CursoUsuario cursoUsuario : curso.getCursoUsuarios()) {
                if (cursoUsuario.getId().equals(cursoUsuarioId)) {
                    cursoUsuarioToRemove = cursoUsuario;
                    break;
                }
            }
            if (cursoUsuarioToRemove != null) {
                curso.removeCursoUsuario(cursoUsuarioToRemove);
                cursoRepository.save(curso);
                return true;
            }
        }
        return false;
    }

}
