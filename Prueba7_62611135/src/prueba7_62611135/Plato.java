/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package prueba7_62611135;

/**
 *
 * @author ANGILI PALACIOS
 */
public class Plato extends Producto{
  private static final long serialVersionUID = 1L;
    private String porcion;

    public Plato(String codigo, String nombre, double precio, String porcion) {
        super(codigo, nombre, precio);
        this.porcion = porcion;
    }

    public String getPorcion() {
        return porcion;
    }

    public void setPorcion(String porcion) {
        this.porcion = porcion;
    }

    public Tipo getTipo() {
        return Tipo.PLATO;
    }

    public String getExtra() {
        return "Porción: " + porcion;
    }
}  
