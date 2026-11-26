/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package prueba7_62611135;

/**
 *
 * @author ANGILI PALACIOS
 */
public enum Modo {
    LOCAL("En el local"),
    DOMICILIO("A domicilio");

    private String texto;

    Modo(String texto) {
        this.texto = texto;
    }

    public String toString() {
        return texto;
    }
}