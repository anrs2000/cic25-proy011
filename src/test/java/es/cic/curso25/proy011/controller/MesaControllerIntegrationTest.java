package es.cic.curso25.proy011.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.cic.curso25.proy011.model.Mesa;
import es.cic.curso25.proy011.model.Silla;
import es.cic.curso25.proy011.repository.MesaRepository;
import es.cic.curso25.proy011.service.MesaService;
import jakarta.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MesaControllerIntegrationTest {

    @Autowired
    MesaRepository mesaRepository;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MesaService mesaService;

    Mesa mesa;
    List<Silla> sillas;
    Mesa mesaGuardada;

    @BeforeEach
    void previa() {
        mesaRepository.deleteAll();
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

        mesaGuardada = mesaRepository.save(mesa);
    }

    @Test
    @Transactional
    void testAgregarSilla() throws Exception {
        Silla nuevaSilla = new Silla(23, true, "granate");

        int numInicialSillas = mesaGuardada.getSillas().size();

        mockMvc.perform(post("/mesa/" + mesaGuardada.getId())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(nuevaSilla)))
                .andExpect(status().isOk()).andExpect(result -> {
                    Mesa mesaObtenida = objectMapper.readValue(result.getResponse().getContentAsString(), Mesa.class);
                    assertEquals((numInicialSillas + 1), mesaObtenida.getSillas().size());
                });
    }

    @Test
    void testDeleteMesa() throws Exception {
        mockMvc.perform(delete("/mesa/" + mesaGuardada.getId())
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    Optional<Mesa> mesaBorrada = mesaRepository.findById(mesaGuardada.getId());
                    assertTrue(mesaBorrada.isEmpty());
                });

        mockMvc.perform(get("/mesa/" + mesaGuardada.getId())
                .contentType("application/json"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllMesas() throws Exception {
        mockMvc.perform(get("/mesa"))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    List<Mesa> mesasObtenidas = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<List<Mesa>>() {
                            });

                    assertNotNull(mesasObtenidas);
                    assertFalse(mesasObtenidas.isEmpty(), "La lista de mesas no debería estar vacía");
                });
    }

    @Test
    void testGetMesa() throws Exception {
        mockMvc.perform(get("/mesa/" + mesa.getId())
                .contentType("application/json"))
                .andExpect(result -> {
                    Mesa mesaObtenida = objectMapper.readValue(result.getResponse().getContentAsString(), Mesa.class);
                    assertEquals(mesa.getColor(), mesaObtenida.getColor());
                    assertEquals(mesa.getForma(), mesaObtenida.getForma());
                    assertEquals(mesa.getNumPatas(), mesaObtenida.getNumPatas());
                    assertEquals(mesa.getMaterial(), mesaObtenida.getMaterial());
                })
                .andDo(print())
                .andDo(print());
    }

    @Test
    void testPostMesa() throws Exception {
        Mesa mesaACrear = new Mesa("verde", "pentágono", 5, "madera");

        mockMvc.perform(post("/mesa")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(mesaACrear)))
                .andExpect(result -> {
                    Mesa mesaObtenida = objectMapper.readValue(result.getResponse().getContentAsString(), Mesa.class);
                    assertEquals(mesaACrear.getColor(), mesaObtenida.getColor());
                    assertEquals(mesaACrear.getForma(), mesaObtenida.getForma());
                    assertEquals(mesaACrear.getNumPatas(), mesaObtenida.getNumPatas());
                    assertEquals(mesaACrear.getMaterial(), mesaObtenida.getMaterial());
                });
    }

    @Test
    void testUpdateMesa() throws Exception {
        Mesa mesaActualizada = new Mesa("verde", "pentágono", 5, "madera");

        mockMvc.perform(put("/mesa/" + mesaGuardada.getId())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(mesaActualizada)))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    Mesa mesaObtenida = objectMapper.readValue(result.getResponse().getContentAsString(), Mesa.class);
                    assertEquals(mesaActualizada.getColor(), mesaObtenida.getColor());
                    assertEquals(mesaActualizada.getForma(), mesaObtenida.getForma());
                    assertEquals(mesaActualizada.getNumPatas(), mesaObtenida.getNumPatas());
                    assertEquals(mesaActualizada.getMaterial(), mesaObtenida.getMaterial());
                });
    }

    @Test
    void testUpdateSillaEnMesa() throws Exception{
        Silla sillaOriginal = mesaGuardada.getSillas().get(0); 
        Long idSilla = sillaOriginal.getId();

        // Creamos una nueva versión de esa silla
        Silla sillaActualizada = new Silla(10, false, "negro");

        mockMvc.perform(put("/mesa/" + mesaGuardada.getId() + "/" + idSilla)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(sillaActualizada)))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    Mesa mesaObtenida = objectMapper.readValue(result.getResponse().getContentAsString(), Mesa.class);
                    assertNotNull(mesaObtenida);
                    assertEquals(mesaGuardada.getSillas().size(), mesaObtenida.getSillas().size());

                    // Buscamos la silla actualizada por su ID
                    Silla sillaModificada = mesaObtenida.getSillas().stream()
                            .filter(s -> s.getId().equals(idSilla))
                            .findFirst()
                            .orElseThrow(() -> new AssertionError("No se encontró la silla actualizada"));

                    assertEquals(sillaActualizada.getMesa(), sillaModificada.getMesa());
                    assertEquals(sillaActualizada.getNumPatas(), sillaModificada.getNumPatas());
                    assertEquals(sillaActualizada.getColor(), sillaModificada.getColor());
                });
    }
}
