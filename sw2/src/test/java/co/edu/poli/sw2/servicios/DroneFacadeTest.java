package co.edu.poli.sw2.servicios;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import co.edu.poli.sw2.modelo.Agricultura;
import co.edu.poli.sw2.modelo.Drone;
import co.edu.poli.sw2.modelo.Vigilancia;


class DroneFacadeTest {
	
	private DroneFacade fachada;
	
	@BeforeEach
	
	void setUp() {
		fachada = new DroneFacade();
		}
	
	@Test
	
	void crearAgricultura_devuelveAgriculturaConSusDatos() {
		Drone drone = fachada.crearAgricultura("D1", "S1", "M1", "F1", 5.5, 12.5);
		Agricultura agricultura = assertInstanceOf(Agricultura.class, drone);
		assertEquals("D1", agricultura.getId());
		assertEquals("S1", agricultura.getSerial());
		assertEquals("M1", agricultura.getModelo());
		assertEquals("F1", agricultura.getFabricante());
		assertEquals(5.5, agricultura.getPeso());
		assertEquals(12.5, agricultura.getCapacidadTanque());
		}
	
	@Test
	
	void crearVigilancia_devuelveVigilanciaConSusDatos() {
		Drone drone = fachada.crearVigilancia("D2", "S2", "M2", "F2", 3.0, true);
		Vigilancia vigilancia = assertInstanceOf(Vigilancia.class, drone);
		assertEquals("D2", vigilancia.getId());
		assertEquals(3.0, vigilancia.getPeso());
		assertTrue(vigilancia.isDeteccionTermica());
		
	}
}