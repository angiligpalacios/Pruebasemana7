/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package prueba7_62611135;

/**
 *
 * @author ANGILI PALACIOS
 */
import java.util.ArrayList;

public class Datos {
    private ArrayList<Producto> productos;
    private ArrayList<Pedido> pedidos;
    private int siguiente;

    public Datos() {
        productos = new ArrayList<Producto>();
        pedidos = new ArrayList<Pedido>();
        siguiente = 1;
    }

    public ArrayList<Producto> getProductos() {
        return productos;
    }

    public void setProductos(ArrayList<Producto> productos) {
        this.productos = productos;
    }

    public ArrayList<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(ArrayList<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    public int getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(int siguiente) {
        this.siguiente = siguiente;
    }

    public void agregar(Producto producto) {
        productos.add(producto);
    }

    public Producto buscar(String codigo) {
        for (int i = 0; i < productos.size(); i++) {
            Producto p = productos.get(i);
            if (p.getCodigo().equalsIgnoreCase(codigo)) {
                return p;
            }
        }
        return null;
    }

    public int nuevoId() {
        int id = siguiente;
        siguiente = siguiente + 1;
        return id;
    }
}