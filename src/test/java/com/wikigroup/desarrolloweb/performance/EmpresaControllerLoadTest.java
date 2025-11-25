package com.wikigroup.desarrolloweb.performance;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wikigroup.desarrolloweb.config.TestSecurityConfig;
import com.wikigroup.desarrolloweb.dtos.EmpresaDto;
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
class EmpresaControllerLoadTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 1) Carga de lectura sobre GET /api/empresas:
     *    muchas peticiones concurrentes solo leyendo.
     */
    @Test
    void carga_sobre_listado_empresas() throws Exception {
        int hilos = 15;
        int peticionesPorHilo = 15;

        ExecutorService pool = Executors.newFixedThreadPool(hilos);
        CountDownLatch latch = new CountDownLatch(hilos * peticionesPorHilo);

        for (int i = 0; i < hilos; i++) {
            pool.submit(() -> {
                try {
                    for (int j = 0; j < peticionesPorHilo; j++) {
                        try {
                            mockMvc.perform(get("/api/empresas"))
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
     * 2) Carga sobre creación de empresas:
     *    muchos POST concurrentes a /api/empresas.
     */
@Test
void carga_sobre_creacion_empresas() throws Exception {
    int hilos = 10;
    int peticionesPorHilo = 10;
    AtomicInteger contador = new AtomicInteger(0);

    ExecutorService pool = Executors.newFixedThreadPool(hilos);
    CountDownLatch latch = new CountDownLatch(hilos * peticionesPorHilo);

    for (int i = 0; i < hilos; i++) {
        pool.submit(() -> {
            try {
                for (int j = 0; j < peticionesPorHilo; j++) {
                    int n = contador.incrementAndGet();

                    EmpresaDto dto = new EmpresaDto();
                    dto.setNombre("Empresa Load " + n);
                    dto.setNit("NIT-" + n);
                    dto.setCorreoContacto("empresa" + n + "@example.com");
                    dto.setDescripcion("Empresa de prueba para test de carga #" + n);

                    try {
                        mockMvc.perform(
                                post("/api/empresas")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(dto))
                        )
                        .andExpect(status().is2xxSuccessful());
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
