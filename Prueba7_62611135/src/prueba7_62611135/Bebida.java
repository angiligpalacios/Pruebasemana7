/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package prueba7_62611135;

/**
 *
 * @author ANGILI PALACIOS
 */
public class Bebida extends Producto {
    private static final long serialVersionUID = 1L;
    private int ml;

    public Bebida(String codigo, String nombre, double precio, int ml) {
        super(codigo, nombre, precio);
        this.ml = ml;
    }

    public int getMl() {
        return ml;
    }

    public void setMl(int ml) {
        this.ml = ml;
    }

    public Tipo getTipo() {
        return Tipo.BEBIDA;
    }

    public String getExtra() {
        return ml + " ml";
    }
}
