package es.cic.curso25.proy011.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.cic.curso25.proy011.exceptions.NotFoundException;
import es.cic.curso25.proy011.model.Mesa;
import es.cic.curso25.proy011.repository.MesaRepository;

import java.util.Optional;

import org.slf4j.Logger; 
import org.slf4j.LoggerFactory; 

@Service
public class MesaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MesaService.class);

    @Autowired
    MesaRepository mesaRepository;

    public Mesa getMesa(Long id){
        LOGGER.info(String.format("Buscando la mesa con id %d", id));
        Optional<Mesa> mesa = mesaRepository.findById(id);
        if(mesa.isEmpty()){
            LOGGER.error(String.format("No se ha encontrado ninguna mesa con id %d", id, NotFoundException.class));
            throw new NotFoundException(String.format("No se ha encontrado ninguna mesa con id %d", id));
        }
        return mesa.get();
    }
}
