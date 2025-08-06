package es.cic.curso25.proy011.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.cic.curso25.proy011.exceptions.NotFoundException;
import es.cic.curso25.proy011.model.Mesa;
import es.cic.curso25.proy011.model.Silla;
import es.cic.curso25.proy011.repository.MesaRepository;
import jakarta.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MesaServiceIntegrationTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MesaService mesaService;

    @Autowired
    MesaRepository mesaRepository;

    Mesa mesa;
    List<Silla> sillas;
    Mesa mesaGuardada;

    @BeforeEach
    void preparacion() {
        mesa = new Mesa("azul", "redonda", 4, "madera");

        sillas = new ArrayList<>();

        Silla silla1 = new Silla(3, true, "azul");
        sillas.add(silla1);

        Silla silla2 = new Silla(4, false, "azul");
        sillas.add(silla2);

        Silla silla3 = new Silla(3, true, "blanco");
        sillas.add(silla3);

        Silla silla4 = new Silla(5, true, "verde");
        sillas.add(silla4);

        for (int i = 0; i < sillas.size(); i++) {
            mesa.addSilla(sillas.get(i));
        }

        mesaGuardada = mesaService.postMesa(mesa);
    }

    @Test
    void testDeleteMesa() {
        mesaService.deleteMesa(mesaGuardada.getId());

        Optional<Mesa> mesaBorrada = mesaRepository.findById(mesaGuardada.getId());

        assertTrue(mesaBorrada.isEmpty());

        assertThrows(NotFoundException.class, () -> {
            mesaService.getMesa(mesaGuardada.getId());
        });
    }

    // Con esto + el beforeEach, estaría probando también el método post
    @Test
    void testGetMesa() {
        Mesa mesaObtenida = mesaService.getMesa(mesaGuardada.getId());
        assertEquals(mesaObtenida.getColor(), mesaGuardada.getColor());
        assertEquals(mesaObtenida.getId(), mesaGuardada.getId());
        assertEquals(mesaObtenida.getMaterial(), mesaGuardada.getMaterial());
        assertEquals(mesaObtenida.getNumPatas(), mesaGuardada.getNumPatas());
        assertEquals(mesaObtenida.getForma(), mesaGuardada.getForma());
    }

    @Test
    void testGetAllMesas() {
        List<Mesa> todasLasMesas = mesaService.getAllMesas();
        assertTrue(todasLasMesas.size() >= 1);
    }

    @Test
    void testGetSillasDeMesa(){

    }

    @Test
    @Transactional
    void testAgregarSilla() {
        int numInicialSillas = mesaGuardada.getSillas().size();

        Silla sillaNueva = new Silla(3, true, "azul");

        Mesa mesaConSilla = mesaService.agregarSilla(sillaNueva, mesaGuardada.getId());

        assertTrue(mesaConSilla.getSillas().size() == numInicialSillas + 1);
    }

    @Test
    @Transactional
    void testUpdateMesa() {
        List<Silla> nuevasSillas = new ArrayList<>();
        Silla nuevSilla1 = new Silla(5, true, "marron");
        Silla nuevSilla2 = new Silla(6, false, "verde");
        Silla nuevSilla3 = new Silla(3, true, "blanco");

        nuevasSillas.add(nuevSilla1);
        nuevasSillas.add(nuevSilla2);
        nuevasSillas.add(nuevSilla3);

        Mesa mesaActualizada = new Mesa("morada", "cuadrada", 7, "granito");

        for (int i = 0; i < nuevasSillas.size(); i++) {
            mesaActualizada.addSilla(nuevasSillas.get(i));
        }

        Long id = mesaGuardada.getId();

        mesaService.updateMesa(id, mesaActualizada);

        Mesa mesaDesdeBD = mesaService.getMesa(mesaGuardada.getId());

        assertEquals(mesaActualizada.getColor(), mesaDesdeBD.getColor());
        assertEquals(mesaActualizada.getForma(), mesaDesdeBD.getForma());
        assertEquals(mesaGuardada.getId(), mesaDesdeBD.getId());
        assertEquals(mesaActualizada.getMaterial(), mesaDesdeBD.getMaterial());
        assertEquals(mesaActualizada.getNumPatas(), mesaDesdeBD.getNumPatas());
        assertEquals(mesaActualizada.getSillas().size(), mesaDesdeBD.getSillas().size());
    }

    @Test
    @Transactional
    void testUpdateSillaEnMesa() {
        Silla sillaActualizada = new Silla(12, true, "plateado");
        mesaService.updateSillaEnMesa(mesaGuardada.getSillas().get(0).getId(), mesaGuardada.getId(), sillaActualizada);
        
        Mesa mesaActualizada = mesaService.getMesa(mesaGuardada.getId());
        List<Silla> todasLasSillas = mesaActualizada.getSillas();

        Optional<Silla> sillaBuscada = todasLasSillas.stream()
                .filter(s -> s.getColor().equalsIgnoreCase("plateado"))
                .filter(Silla::isRespaldo)
                .filter(s -> s.getNumPatas() == 12)
                .findFirst();

        assertTrue(sillaBuscada.isPresent());
    }
}
