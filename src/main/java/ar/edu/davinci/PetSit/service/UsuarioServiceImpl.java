package ar.edu.davinci.PetSit.service;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ar.edu.davinci.PetSit.domain.Usuario;
import ar.edu.davinci.PetSit.domain.TipoUsuario;
import ar.edu.davinci.PetSit.exceptions.BusinessException;
import ar.edu.davinci.PetSit.repository.UsuarioRepository;

@Service
public class UsuarioServiceImpl implements UsuarioService {
private final Logger LOGGER = LoggerFactory.getLogger(UsuarioServiceImpl.class);
private final UsuarioRepository repository;

@Autowired
public UsuarioServiceImpl(final UsuarioRepository repository) {
this.repository = repository;
}

@Override
public Usuario save(final Usuario usuario) throws BusinessException {
LOGGER.debug("Grabamos el usuario: " + usuario.toString());
if (usuario.getId() == null) {
return repository.save(usuario);
}

throw new BusinessException("No se puede crear el usuario con un id específico.");
}

@Override
public Usuario update(final Usuario usuario) throws BusinessException {
LOGGER.debug("Modificamos la prenda: " + usuario.toString());
if (usuario.getId() != null) {
return repository.save(usuario);
}
throw new BusinessException("No se puede modificar un usuario que aún no fue creada.");
}

@Override
public void delete(final Usuario usuario) {
LOGGER.debug("Borramos la prenda: " + usuario.toString());
repository.delete(usuario);
}
public void delete(final Long id) {
LOGGER.debug("Borramos el usuario con id: " + id);
repository.deleteById(id);
}

@Override
public Usuario findById(final Long id) throws BusinessException {
LOGGER.debug("Buscamos al usuario por id: " + id);
Optional<Usuario> usuarioOptional = repository.findById(id);
if (usuarioOptional.isPresent()) {
return usuarioOptional.get();
}
throw new BusinessException("No se encontró al usuario con el id: " + id);
}

@Override
public List<Usuario> list() {
LOGGER.debug("Listado de todos los usuarios");
return repository.findAll();
}

@Override
public Page<Usuario> list(Pageable pageable) {
LOGGER.debug("Listado de todos los usuarios por páginas");
LOGGER.debug("Pageable: offset: " + pageable.getOffset() + ", pageSize: " +

pageable.getPageSize() + " and pageNumber: " + pageable.getPageNumber());

return repository.findAll(pageable);
}

@Override
public long count() {
return repository.count();
}

@Override
public List<TipoUsuario> getTipoUsuarios() {
return TipoUsuario.getTipoUsuarios();
}
}
