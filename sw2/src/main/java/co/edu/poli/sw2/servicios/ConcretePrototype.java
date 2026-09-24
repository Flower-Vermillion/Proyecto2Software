package co.edu.poli.sw2.servicios;

import co.edu.poli.sw2.modelo.Agricultura;
import co.edu.poli.sw2.modelo.Drone;
import co.edu.poli.sw2.modelo.Vigilancia;

/**
 * ConcretePrototype (patrón Prototype).
 *
 * Envuelve un {@link Drone} existente (el "original") y sabe producir
 * una copia independiente del mismo, con los mismos valores pero una
 * instancia nueva en el heap.
 *
 * Como Drone es una clase abstracta con subtipos concretos que agregan
 * su propio atributo (Agricultura -> capacidadTanque, Vigilancia ->
 * deteccionTermica), el clonado se hace reconstruyendo el subtipo
 * correcto en vez de depender de Object.clone()/Cloneable, evitando
 * así el problema clásico de la clonación superficial con jerarquías.
 *
 * Java no expone la dirección de memoria real de un objeto (el
 * recolector de basura puede reubicarlo), así que se usa
 * System.identityHashCode(obj) como identificador de la instancia:
 * es el sustituto estándar para fines didácticos/de depuración, y es
 * distinto entre el original y el clon aunque los datos sean iguales.
 */
public class ConcretePrototype {

    private final Drone original;

    public ConcretePrototype(Drone original) {
        if (original == null) {
            throw new IllegalArgumentException("El drone original no puede ser nulo");
        }
        this.original = original;
    }

    public Drone getOriginal() {
        return original;
    }

    /**
     * Produce el clon del drone original. No modifica ni guarda nada
     * en base de datos: solo crea la copia en memoria.
     */
    public Drone clonar() {

        if (original instanceof Agricultura agricultura) {
            return new Agricultura(
                    agricultura.getId(), agricultura.getSerial(), agricultura.getModelo(),
                    agricultura.getFabricante(), agricultura.getPeso(),
                    agricultura.getCapacidadTanque()
            );
        }

        if (original instanceof Vigilancia vigilancia) {
            return new Vigilancia(
                    vigilancia.getId(), vigilancia.getSerial(), vigilancia.getModelo(),
                    vigilancia.getFabricante(), vigilancia.getPeso(),
                    vigilancia.isDeteccionTermica()
            );
        }

        throw new IllegalArgumentException(
                "Tipo de drone no soportado para clonar: " + original.getTipo());
    }

    /**
     * Arma el mensaje que se le muestra al usuario tras clonar:
     * el id del drone copiado junto con la dirección de memoria del
     * original y la del clon recién creado.
     */
    public String mensajeClonado(Drone clon) {

        return "Se clonó el drone \"" + original.getId() + "\" (" + original.getTipo() + ").\n\n"
                + "Original:\n"
                + "  Dirección de memoria: " + direccionMemoria(original) + "\n\n"
                + "Clon:\n"
                + "  Dirección de memoria: " + direccionMemoria(clon);
    }

    private String direccionMemoria(Object obj) {
        return "0x" + Integer.toHexString(System.identityHashCode(obj));
    }
}