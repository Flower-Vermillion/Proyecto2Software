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

    /**
     * Crea el prototipo a partir del drone original a clonar.
     *
     * @param original el drone que se va a clonar; no puede ser nulo
     * @throws IllegalArgumentException si {@code original} es nulo
     */
    public ConcretePrototype(Drone original) {
        if (original == null) {
            throw new IllegalArgumentException("El drone original no puede ser nulo");
        }
        this.original = original;
    }

    /**
     * Devuelve el drone original que este prototipo envuelve.
     *
     * @return el {@link Drone} original
     */
    public Drone getOriginal() {
        return original;
    }

    /**
     * Produce el clon del drone original. No modifica ni guarda nada
     * en base de datos: solo crea la copia en memoria.
     *
     * @return una nueva instancia (Agricultura o Vigilancia) con los
     *         mismos valores que el original
     * @throws IllegalArgumentException si el tipo concreto del drone
     *                                  original no está soportado
     *                                  para clonar
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
     *
     * @param clon el drone clonado, obtenido de {@link #clonar()}
     * @return el mensaje de confirmación, listo para mostrarse en la
     *         interfaz
     */
    public String mensajeClonado(Drone clon) {

        return "Se clonó el drone \"" + original.getId() + "\" (" + original.getTipo() + ").\n\n"
                + "Original:\n"
                + "  Dirección de memoria: " + direccionMemoria(original) + "\n\n"
                + "Clon:\n"
                + "  Dirección de memoria: " + direccionMemoria(clon);
    }

    /**
     * Calcula un identificador de instancia legible para el objeto
     * dado, a partir de {@link System#identityHashCode(Object)}
     * (sustituto estándar de la dirección de memoria real, ya que
     * Java no la expone directamente).
     *
     * @param obj el objeto del que se quiere el identificador
     * @return el identificador de instancia en formato hexadecimal
     *         (por ejemplo "0x1a2b3c")
     */
    private String direccionMemoria(Object obj) {
        return "0x" + Integer.toHexString(System.identityHashCode(obj));
    }
}