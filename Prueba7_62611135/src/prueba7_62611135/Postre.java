/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package prueba7_62611135;

/**
 *
 * @author ANGILI PALACIOS
 */
public class Postre extends Producto{
  private static final long serialVersionUID = 1L;
    private String sabor;

    public Postre(String codigo, String nombre, double precio, String sabor) {
        super(codigo, nombre, precio);
        this.sabor = sabor;
    }

    public String getSabor() {
        return sabor;
    }

    public void setSabor(String sabor) {
        this.sabor = sabor;
    }

    public Tipo getTipo() {
        return Tipo.POSTRE;
    }

    public String getExtra() {
        return "Sabor: " + sabor;
    }  
}
