package es.cic.curso25.proy011;

import org.springframework.boot.SpringApplication;

public class TestProy011Application {

	public static void main(String[] args) {
		SpringApplication.from(Proy011Application::main).with(TestcontainersConfiguration.class).run(args);
	}

}
