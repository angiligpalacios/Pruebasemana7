/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package prueba7_62611135;

/**
 *
 * @author ANGILI PALACIOS
 */
public enum Tipo {
    PLATO("Plato"),
    BEBIDA("Bebida"),
    POSTRE("Postre");

    private String texto;

    Tipo(String texto) {
        this.texto = texto;
    }

    public String toString() {
        return texto;
    }
}
