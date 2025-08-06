package es.cic.curso25.proy011.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.cic.curso25.proy011.exceptions.NotFoundException;
import es.cic.curso25.proy011.model.Mesa;
import es.cic.curso25.proy011.model.Silla;
import es.cic.curso25.proy011.repository.MesaRepository;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class MesaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MesaService.class);

    @Autowired
    MesaRepository mesaRepository;

    @Transactional(readOnly = true)
    public Mesa getMesa(Long id) {
        LOGGER.info(String.format("Buscando la mesa con id %d", id));
        Optional<Mesa> mesa = mesaRepository.findById(id);
        if (mesa.isEmpty()) {
            LOGGER.error(String.format("No se ha encontrado ninguna mesa con id %d", id, NotFoundException.class));
            throw new NotFoundException(String.format("No se ha encontrado ninguna mesa con id %d", id));
        }
        return mesa.get();
    }

    @Transactional(readOnly = true)
    public List<Mesa> getAllMesas() {
        LOGGER.info("Obteniendo todas las mesas");
        return mesaRepository.findAll();
    }

    public Mesa postMesa(Mesa mesa) {
        LOGGER.info("Creando una nueva mesa");
        return mesaRepository.save(mesa);
    }

    public Mesa agregarSilla(Silla silla, Long idMesa){
        LOGGER.info(String.format("Agregando una nueva silla a la mesa con id %d", idMesa));
        Mesa mesa = this.getMesa(idMesa);
        // Mesa mesa = mesaRepository.findById(idMesa).get();
        mesa.addSilla(silla);
        return mesaRepository.save(mesa);
    }

    public Mesa updateMesa(Long id, Mesa mesa) {
        LOGGER.info(String.format("Actualizando la mesa con id %d", id));
        // Comprobamos que exista una mesa con ese id
        Mesa mesaEnBD = this.getMesa(id);

        mesaEnBD.setForma(mesa.getForma());
        mesaEnBD.setColor(mesa.getColor());
        mesaEnBD.setMaterial(mesa.getMaterial());
        mesaEnBD.setNumPatas(mesa.getNumPatas());
        mesaEnBD.setSillas(mesa.getSillas());

        return mesaRepository.save(mesaEnBD);
    }

    public Mesa updateSillaEnMesa(Long idSilla, Long idMesa, Silla sillaActualizada){
        LOGGER.info(String.format("Actualizando la silla con id %d", idSilla));
        Mesa mesaEnBd = this.getMesa(idMesa);
        
        List<Silla> sillasEnBd = mesaEnBd.getSillas();
        boolean encontrado = false;

        Iterator<Silla> iterator = sillasEnBd.iterator();
        while(iterator.hasNext() && encontrado == false){
            Silla sillaActual = iterator.next();
            if(sillaActual.getId().equals(idSilla)){
                 sillaActual.setColor(sillaActualizada.getColor());
                 sillaActual.setNumPatas(sillaActualizada.getNumPatas());
                 sillaActual.setRespaldo(sillaActualizada.isRespaldo());

                 encontrado = true;
            }
        }

        mesaEnBd.setSillas(sillasEnBd);
        return mesaRepository.save(mesaEnBd);
    }

    public void deleteMesa(Long id){
        LOGGER.info(String.format("Eliminando la mesa con id %d", id));
        mesaRepository.deleteById(id);
    }
}
