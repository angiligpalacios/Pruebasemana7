/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package prueba7_62611135;

/**
 *
 * @author ANGILI PALACIOS
 */
import java.io.Serializable;
import java.util.ArrayList;
public class Pedido implements Serializable{
    private static final long serialVersionUID = 1L;
    private int id;
    private String fecha;
    private ArrayList<Linea> lineas;
    private Modo modo;
    private Estado estado;

    public Pedido(int id, String fecha, Modo modo) {
        this.id = id;
        this.fecha = fecha;
        this.lineas = new ArrayList<Linea>();
        this.modo = modo;
        this.estado = Estado.PENDIENTE;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public ArrayList<Linea> getLineas() {
        return lineas;
    }

    public Modo getModo() {
        return modo;
    }

    public void setModo(Modo modo) {
        this.modo = modo;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public void agregar(Linea linea) {
        lineas.add(linea);
    }

    public void quitar(int i) {
        if (i >= 0 && i < lineas.size()) {
            lineas.remove(i);
        }
    }

    public double total() {
        return totalRecursivo(0);
    }

    private double totalRecursivo(int i) {
        if (i >= lineas.size()) {
            return 0;
        }
        Linea linea = lineas.get(i);
        double parcial = linea.getProducto().getPrecio() * linea.getCantidad();
        return parcial + totalRecursivo(i + 1);
    }  
}
