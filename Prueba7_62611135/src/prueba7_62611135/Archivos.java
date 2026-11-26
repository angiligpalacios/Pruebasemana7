/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package prueba7_62611135;

/**
 *
 * @author ANGILI PALACIOS
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Archivos {
    private static File carpeta() {
        File f = new File("datos");
        if (!f.exists()) {
            f.mkdirs();
        }
        return f;
    }

    public static File archivoProductos() {
        return new File(carpeta(), "productos.dat");
    }

    public static File archivoPedidos() {
        return new File(carpeta(), "pedidos.dat");
    }

    public static void guardarProductos(Datos datos) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(archivoProductos()));
        try {
            out.writeObject(datos.getProductos());
        } finally {
            out.close();
        }
    }

    @SuppressWarnings("unchecked")
    public static void cargarProductos(Datos datos) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(archivoProductos()));
        try {
            ArrayList<Producto> lista = (ArrayList<Producto>) in.readObject();
            datos.setProductos(lista);
        } finally {
            in.close();
        }
    }

    public static void guardarPedidos(Datos datos) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(archivoPedidos()));
        try {
            out.writeObject(datos.getPedidos());
            out.writeInt(datos.getSiguiente());
        } finally {
            out.close();
        }
    }

    @SuppressWarnings("unchecked")
    public static void cargarPedidos(Datos datos) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(archivoPedidos()));
        try {
            ArrayList<Pedido> lista = (ArrayList<Pedido>) in.readObject();
            int sig = in.readInt();
            datos.setPedidos(lista);
            datos.setSiguiente(sig);
        } finally {
            in.close();
        }
    }
}