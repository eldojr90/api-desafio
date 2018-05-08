package com.desafio.repository;

import org.springframework.data.repository.CrudRepository;

import com.desafio.model.Paciente;

public interface PacienteRepository extends CrudRepository<Paciente, String>{

}
