package es.cic.curso25.proy011;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class Proy011ApplicationTests {

	@Test
	void contextLoads() {
	}

}
