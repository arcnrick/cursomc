package com.crianto.cursomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crianto.cursomc.domain.Categoria;
import com.crianto.cursomc.repositories.CategoriaRepository;
import com.crianto.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class CategoriaService {

	/* declarar dependencia com notação Autowired... isso faz com que através da injeção de dependência
	ou inversão de controle, tal dependência é automaticamente instanciada pelo Spring */ 
	@Autowired
	private CategoriaRepository repo;
	
	public Categoria buscar(Integer id) {
		Optional<Categoria> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado. Id: " + id + ", Tipo: " + Categoria.class.getName()));		
	}
	
}
