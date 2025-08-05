package es.cic.curso25.proy011;

import org.springframework.boot.SpringApplication;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("testcontainer")
public class TestProy011Application {

	public static void main(String[] args) {
		SpringApplication.from(Proy011Application::main).with(TestcontainersConfiguration.class).run(args);
	}

}
