package com.crianto.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.crianto.cursomc.domain.Categoria;
import com.crianto.cursomc.dto.CategoriaDTO;
import com.crianto.cursomc.repositories.CategoriaRepository;
import com.crianto.cursomc.services.exceptions.DataIntegrityException;
import com.crianto.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class CategoriaService {

	/* declarar dependencia com notação Autowired... isso faz com que através da injeção de dependência
	ou inversão de controle, tal dependência é automaticamente instanciada pelo Spring */ 
	@Autowired
	private CategoriaRepository repo;
	
	public Categoria find(Integer id) {
		Optional<Categoria> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado. Id: " + id + ", Tipo: " + Categoria.class.getName()));		
	}
	
	public Categoria insert(Categoria obj) {
		obj.setId(null); // null: para o método save entender q ém insert... do contrário vai entender q é update
		return repo.save(obj);		
	}
	
	public Categoria update(Categoria obj) {
		// verificar se o id existe, antes de fazer o update
		Categoria newObj = find(obj.getId());
		
		// a função abaixo serve para buscar os dados do banco e atualizar somente aqueles passados no parâmetro obj
		updateData(newObj, obj);

		return repo.save(newObj);
	}	
	
	public void delete(Integer id) {
		find(id);
		try {
			repo.deleteById(id);			
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir categoria que possui produtos");
		}		
	}
	
	public List<Categoria> findAll() {
		return repo.findAll();
	}
	
	public Page<Categoria> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		
		return repo.findAll(pageRequest);		
	}
	
	public Categoria fromDTO(CategoriaDTO objDTO) {
		return new Categoria(objDTO.getId(), objDTO.getNome());
	}
	
	private void updateData(Categoria newObj, Categoria obj) {
		newObj.setNome(obj.getNome());
	}

}
