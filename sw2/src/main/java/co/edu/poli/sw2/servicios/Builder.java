package co.edu.poli.sw2.servicios;

import co.edu.poli.sw2.modelo.Drone;

/**
 * Builder (patrón Builder).
 * Arma un Drone paso a paso mediante métodos encadenables, y delega
 * la construcción final al Factory Method (DroneCreator).
 */
public class Builder {

    private String id;
    private String serial;
    private String modelo;
    private String fabricante;
    private double peso;
    private String tipo;                // "AGRICULTURA" o "VIGILANCIA"
    private String atributoEspecifico;   // capacidadTanque o deteccionTermica, como texto

    /**
     * Establece el id del drone a construir.
     *
     * @param id identificador del drone
     * @return este mismo Builder, para encadenar llamadas
     */
    public Builder conId(String id) {
        this.id = id;
        return this;
    }

    /**
     * Establece el serial del drone a construir.
     *
     * @param serial serial del drone
     * @return este mismo Builder, para encadenar llamadas
     */
    public Builder conSerial(String serial) {
    	this.serial = serial;
    	return this;
    }

    /**
     * Establece el modelo del drone a construir.
     *
     * @param modelo modelo del drone
     * @return este mismo Builder, para encadenar llamadas
     */
    public Builder conModelo(String modelo) {
    	this.modelo = modelo;
    	return this;
    }

    /**
     * Establece el fabricante del drone a construir.
     *
     * @param fabricante fabricante del drone
     * @return este mismo Builder, para encadenar llamadas
     */
    public Builder conFabricante(String fabricante) {
    	this.fabricante = fabricante;
    	return this;
    }

    /**
     * Establece el peso del drone a construir.
     *
     * @param peso peso del drone
     * @return este mismo Builder, para encadenar llamadas
     */
    public Builder conPeso (double peso) {
    	this.peso = peso;
    	return this;
    }

    /**
     * Establece el tipo del drone a construir ("AGRICULTURA" o
     * "VIGILANCIA"), usado luego para elegir el
     * {@link DroneCreator} correcto en {@link #build()}.
     *
     * @param tipo tipo de drone
     * @return este mismo Builder, para encadenar llamadas
     */
    public Builder conTipo(String tipo) {
    	this.tipo = tipo;
    	return this;
    }

    /**
     * Establece el atributo específico del tipo (capacidadTanque para
     * Agricultura, deteccionTermica para Vigilancia), como texto.
     *
     * @param atributoEspecifico atributo específico del tipo, en texto
     * @return este mismo Builder, para encadenar llamadas
     */
    public Builder conAtributoEspecifico(String atributoEspecifico) {
    	this.atributoEspecifico = atributoEspecifico;
    	return this;
    }

    /**
     * Construye el {@link Drone} final con todos los valores
     * acumulados, delegando en {@link DroneCreator#paraTipo(String)}
     * según el tipo indicado con {@link #conTipo(String)}.
     *
     * <p>Si {@code atributoEspecifico} no fue indicado (o quedó en
     * blanco), se usa {@code "0"} como valor por defecto.</p>
     *
     * @return el {@link Drone} construido (Agricultura o Vigilancia)
     * @throws IllegalStateException si {@code id} o {@code tipo} no
     *                                fueron indicados
     */
    public Drone build() {

    	if(id == null || id.trim().isEmpty()) {
    		throw new IllegalStateException("La id del drone es obligatoria para construirlo");
    	}

        if (tipo == null || tipo.trim().isEmpty()) {
            throw new IllegalStateException("El tipo de drone es obligatorio para construirlo");
        }

        if (atributoEspecifico == null || atributoEspecifico.trim().isEmpty()) {
            atributoEspecifico = "0";
        }

        return DroneCreator.paraTipo(tipo)
                .crearDrone(id, serial, modelo, fabricante, peso, atributoEspecifico);
    }
}