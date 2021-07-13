package com.devsuperior.desafiodsclientmodulo1.service;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.desafiodsclientmodulo1.dto.ClientDTO;
import com.devsuperior.desafiodsclientmodulo1.entities.Client;
import com.devsuperior.desafiodsclientmodulo1.repositories.ClientRepository;
import com.devsuperior.desafiodsclientmodulo1.service.exceptions.DatabaseException;
import com.devsuperior.desafiodsclientmodulo1.service.exceptions.ResourceNotFoundException;

@Service
public class ClientService {

	@Autowired
	private ClientRepository repository;
	
	@Transactional(readOnly = true)
	public Page<ClientDTO> findAll(PageRequest pageRequest){
		Page<Client> list = repository.findAll(pageRequest);
		return list.map(ClientDTO::new);
	}
	

	@Transactional
	public ClientDTO save(ClientDTO dto) {
		Client entity = toEntity(dto);
		repository.save(entity);
		return new ClientDTO(entity);
	}

	@Transactional
	public ClientDTO update(Long id, ClientDTO dto) {
		try {
			Client entity = toUpdate(id, dto);
			repository.save(entity);
			return new ClientDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("ID not found : " + id);
		}
	}

	@Transactional(readOnly = true)
	public ClientDTO findById(Long id) {
		return repository.findById(id).map(ClientDTO::new)
				.orElseThrow(() -> new ResourceNotFoundException("ID not found : " + id));
	}

	public void delete(Long id) {
		try {
			repository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("ID not found : " + id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("ID not found : " + id);
		}
	}

	// Auxiliary methods

	public Client toEntity(ClientDTO dto) {
		Client entity = new Client();
		entity.setName(dto.getName());
		entity.setCpf(dto.getCpf());
		entity.setIncome(dto.getIncome());
		entity.setChildren(dto.getChildren());
		entity.setBirthDate(dto.getBirthDate());
		return entity;
	}

	public Client toUpdate(Long id, ClientDTO dto) {
		Client entity = repository.getOne(id);
		entity.setName(dto.getName());
		entity.setCpf(dto.getCpf());
		entity.setIncome(dto.getIncome());
		entity.setChildren(dto.getChildren());
		entity.setBirthDate(dto.getBirthDate());
		return entity;
	}
}
