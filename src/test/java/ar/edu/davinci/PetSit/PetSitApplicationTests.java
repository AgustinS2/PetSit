package ar.edu.davinci.PetSit;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles; // PARA QUE LOS TESTS DEJEN DE DEPENDER DE DATA.SQL,SCHEMA.SQL Y SECURITY

@SpringBootTest
@ActiveProfiles("test") // PARA QUE LOS TESTS DEJEN DE DEPENDER DE DATA.SQL,SCHEMA.SQL Y SECURITY
class PetSitApplicationTests {

	@Test
	void contextLoads() {
	}

}
