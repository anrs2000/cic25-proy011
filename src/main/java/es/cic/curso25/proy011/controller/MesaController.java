package es.cic.curso25.proy011.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.cic.curso25.proy011.model.Mesa;
import es.cic.curso25.proy011.service.MesaService;

@RestController
@RequestMapping("/mesa")
public class MesaController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MesaController.class);

    @Autowired MesaService mesaService;

    @GetMapping("/{id}")
    public Mesa getMesa(@PathVariable Long id){
        LOGGER.info(String.format("Método get a la ruta /mesa/%d (obtener mesa)", id));
        return mesaService.getMesa(id);
    }

    @PostMapping
    public Mesa postMesa(@RequestBody Mesa mesa){
        LOGGER.info("Método post a la ruta /mesa (crear mesa)");
        return mesaService.postMesa(mesa);
    }

    @PutMapping("/{id}")
    public Mesa updateMesa(@PathVariable Long id, @RequestBody Mesa mesaActualizada){
        return mesaService.updateMesa(id, mesaActualizada);
    }

    @DeleteMapping("/{id}")
    public void deleteMesa(@PathVariable Long id){
        mesaService.deleteMesa(id);
    }
}
