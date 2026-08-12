package co.edu.poli.sw2.modelo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import co.edu.poli.sw2.modelo.Drone;

class DroneTest {

    @Test
    void deberiaCrearDroneCorrectamente() {

        Drone drone = new Drone(
                12,
                123,
                "Mavic 3",
                "DJI",
                895
        );

        assertEquals(12, drone.getId());
        assertEquals(123, drone.getSerial());
        assertEquals("Mavic 3", drone.getModelo());
        assertEquals("DJI", drone.getFabricante());
        assertEquals(895, drone.getPeso());
    }
}