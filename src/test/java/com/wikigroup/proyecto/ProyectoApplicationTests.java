package com.wikigroup.proyecto;

import com.wikigroup.desarrolloweb.config.TestSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = com.wikigroup.desarrolloweb.DesarrolloWebApplication.class)
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
class ProyectoApplicationTests {

	@Test
	void contextLoads() {
	}

}
