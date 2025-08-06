package es.cic.curso25.proy011.controller;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.cic.curso25.proy011.model.Mesa;
import es.cic.curso25.proy011.model.Silla;
import es.cic.curso25.proy011.repository.MesaRepository;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
public class MesaControllerIntegrationTest {

    @Autowired
    MesaRepository mesaRepository;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    Mesa mesa;
    List<Silla> sillas;
    Mesa mesaGuardada;

    @BeforeEach
    void previa() {
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
    void testAgregarSilla() throws Exception{
        Silla nuevaSilla = new Silla(23, true, "granate");

        int numInicialSillas = mesaGuardada.getSillas().size();

        mockMvc.perform(post("/mesa/" + mesaGuardada.getId())
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(nuevaSilla))
        )
        .andExpect(result -> {
            Mesa mesaObtenida = objectMapper.readValue(result.getResponse().getContentAsString(), Mesa.class);
            assertTrue(numInicialSillas + 1 == mesaObtenida.getSillas().size());
        });
    }

    @Test
    void testDeleteMesa() {

    }

    @Test
    void testGetAllMesas() {

    }

    @Test
    void testGetMesa() {

    }

    @Test
    void testPostMesa() {

    }

    @Test
    void testUpdateMesa() {

    }

    @Test
    void testUpdateSillaEnMesa() {

    }
}
