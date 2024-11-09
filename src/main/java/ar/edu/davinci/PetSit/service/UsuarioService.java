package ar.edu.davinci.PetSit.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ar.edu.davinci.PetSit.domain.Usuario;
import ar.edu.davinci.PetSit.domain.TipoUsuario;
import ar.edu.davinci.PetSit.exceptions.BusinessException;

public interface UsuarioService {
	
	Usuario save(final Usuario usuario) throws BusinessException;
	Usuario update(final Usuario usuario) throws BusinessException;
	void delete(final Usuario usuario);
	void delete(final Long id);
	
	Usuario findById(final Long id) throws BusinessException;
	
	List<Usuario> list();
	Page<Usuario> list(Pageable pageable);
	
	long count();
	
	List<TipoUsuario> getTipoUsuarios();
}