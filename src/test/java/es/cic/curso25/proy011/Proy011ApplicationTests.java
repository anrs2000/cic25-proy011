package es.cic.curso25.proy011;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;


@Import(TestcontainersConfiguration.class)
@SpringBootTest
@ActiveProfiles("testcontainer")
class Proy011ApplicationTests {

	@Test
	void contextLoads() {
	}

}
