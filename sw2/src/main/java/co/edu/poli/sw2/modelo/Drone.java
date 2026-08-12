package co.edu.poli.sw2.modelo;

import java.util.ArrayList;
import java.util.List;

public class Drone {

    private int id;
    private int serial;
    private String modelo;
    private String fabricante;
    private int peso;

    private Piloto piloto;
    private List<Sensor> sensores;
    private List<Mision> misiones;

    public Drone() {
        sensores = new ArrayList<>();
        misiones = new ArrayList<>();
    }

    public Drone(int id, int serial, String modelo,
                 String fabricante, int peso) {

        this.id = id;
        this.serial = serial;
        this.modelo = modelo;
        this.fabricante = fabricante;
        this.peso = peso;

        this.sensores = new ArrayList<>();
        this.misiones = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSerial() {
        return serial;
    }

    public void setSerial(int serial) {
        this.serial = serial;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getFabricante() {
        return fabricante;
    }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }

    public Piloto getPiloto() {
        return piloto;
    }

    public void setPiloto(Piloto piloto) {
        this.piloto = piloto;
    }

    public List<Sensor> getSensores() {
        return sensores;
    }

    public void setSensores(List<Sensor> sensores) {
        this.sensores = sensores;
    }

    public List<Mision> getMisiones() {
        return misiones;
    }

    public void setMisiones(List<Mision> misiones) {
        this.misiones = misiones;
    }

    @Override
    public String toString() {
        return "Drone{" +
                "id=" + id +
                ", serial=" + serial +
                ", modelo='" + modelo + '\'' +
                ", fabricante='" + fabricante + '\'' +
                ", peso=" + peso +
                '}';
    }
}