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

    public Builder conId(String id) {
        this.id = id;
        return this;
    }
    
    public Builder conSerial(String serial) {
    	this.serial = serial;
    	return this;
    }
    
    public Builder conModelo(String modelo) {
    	this.modelo = modelo;
    	return this;
    }
    
    public Builder conFabricante(String fabricante) {
    	this.fabricante = fabricante;
    	return this;
    }
    
    public Builder conPeso (double peso) {
    	this.peso = peso;
    	return this;
    }

    public Builder conTipo(String tipo) {
    	this.tipo = tipo;
    	return this;
    }
    
    public Builder conAtributoEspecifico(String atributoEspecifico) {
    	this.atributoEspecifico = atributoEspecifico;
    	return this;
    }

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