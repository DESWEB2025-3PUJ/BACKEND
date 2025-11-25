package com.wikigroup.desarrolloweb.performance;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wikigroup.desarrolloweb.config.TestSecurityConfig;
import com.wikigroup.desarrolloweb.dtos.EmpresaDto;
import com.wikigroup.desarrolloweb.dtos.UsuarioDto;
import com.wikigroup.desarrolloweb.model.Empresa;
import com.wikigroup.desarrolloweb.repository.EmpresaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
class UsuarioControllerLoadTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Long empresaId;

    @BeforeEach
    void setUpEmpresa() {
        // Dejamos una empresa real en la BD de prueba para poder crear usuarios
        empresaRepository.deleteAll();

        Empresa empresa = new Empresa();
        empresa.setNombre("Empresa Load Test");
        empresaId = empresaRepository.save(empresa).getId();
    }

    /**
     * Prueba de carga media sobre el GET de usuarios:
     *  - 20 hilos
     *  - cada hilo hace 10 peticiones GET /api/usuarios
     *  - verificamos que todas devuelvan 200 OK.
     */
    @Test
    void carga_media_sobre_getAllUsuarios() throws Exception {
        int hilos = 20;
        int peticionesPorHilo = 10;

        ExecutorService pool = Executors.newFixedThreadPool(hilos);
        CountDownLatch latch = new CountDownLatch(hilos * peticionesPorHilo);

        for (int i = 0; i < hilos; i++) {
            pool.submit(() -> {
                try {
                    for (int j = 0; j < peticionesPorHilo; j++) {
                        try {
                            mockMvc.perform(get("/api/usuarios"))
                                   .andExpect(status().isOk());
                        } finally {
                            // Siempre descontamos, falle o no la petición
                            latch.countDown();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            });
        }

        latch.await();
        pool.shutdown();
    }

    /**
     * Prueba de carga sobre creación de usuarios:
     *  - 10 hilos
     *  - cada hilo hace 10 POST a /api/usuarios/empresa/{empresaId}
     *    (100 usuarios en total).
     */
    @Test
    void carga_sobre_creacion_de_usuarios() throws Exception {
        int hilos = 10;
        int peticionesPorHilo = 10; // 100 usuarios en total
        AtomicInteger contador = new AtomicInteger(0);

        ExecutorService pool = Executors.newFixedThreadPool(hilos);
        CountDownLatch latch = new CountDownLatch(hilos * peticionesPorHilo);

        for (int i = 0; i < hilos; i++) {
            pool.submit(() -> {
                try {
                    for (int j = 0; j < peticionesPorHilo; j++) {
                        int n = contador.incrementAndGet();

                        UsuarioDto body = new UsuarioDto();
                        body.setNombre("User " + n);
                        body.setEmail("user" + n + "@example.com");
                        body.setPassword("Password123!");
                        body.setRol("ADMINISTRADOR");
                        body.setEmpresaId(empresaId);

                        try {
                            mockMvc.perform(
                                    post("/api/usuarios/empresa/" + empresaId)
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content(objectMapper.writeValueAsString(body))
                            ).andExpect(status().isCreated());
                        } finally {
                            latch.countDown();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            });
        }

        latch.await();
        pool.shutdown();
    }
}
