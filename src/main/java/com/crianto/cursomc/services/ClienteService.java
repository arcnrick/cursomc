package com.crianto.cursomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crianto.cursomc.domain.Cliente;
import com.crianto.cursomc.repositories.ClienteRepository;
import com.crianto.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {

	/* declarar dependencia com notação Autowired... isso faz com que através da injeção de dependência
	ou inversão de controle, tal dependência é automaticamente instanciada pelo Spring */ 
	@Autowired
	private ClienteRepository repo;
	
	public Cliente find(Integer id) {
		Optional<Cliente> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado. Id: " + id + ", Tipo: " + Cliente.class.getName()));		
	}
	
}
