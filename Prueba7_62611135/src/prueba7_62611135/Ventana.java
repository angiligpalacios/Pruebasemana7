/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package prueba7_62611135;

/**
 *
 * @author ANGILI PALACIOS
 */
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Ventana extends JFrame {
    private Datos datos = new Datos();
    private Pedido actual = new Pedido(0, "", Modo.LOCAL);
    private JTabbedPane tabs;
    private JLabel statP, statE, statS, statI, extraLbl, msgP, total, msgC;
    private JTextField codigo, nombre, precio, extra;
    private JComboBox<Tipo> tipo;
    private JComboBox<String> filtro;
    private JComboBox<Producto> combo;
    private JComboBox<Modo> modo;
    private JComboBox<Estado> estado;
    private JSpinner cant;
    private DefaultTableModel modProd, modLin, modPed, modDet;
    private JTable tProd, tLin, tPed;
    private JTextArea log;

    public Ventana() {
        setTitle("Sabor Casero - Gestión de pedidos");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                guardarAlSalir();
            }
        });
        setSize(980, 640);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(247, 241, 234));
        tabs = new JTabbedPane();
        tabs.setFont(new Font("SansSerif", Font.BOLD, 13));
        tabs.add("Inicio", inicio());
        tabs.add("Productos", productos());
        tabs.add("Pedido", pedido());
        tabs.add("Consulta", consulta());
        tabs.add("Archivos", archivos());
        tabs.addChangeListener(e -> alCambiar());
        add(tabs);
        cargarAlInicio();
        refrescarInicio();
    }

    private void cargarAlInicio() {
        try {
            Archivos.cargarProductos(datos);
        } catch (Exception e) {
        }
        try {
            Archivos.cargarPedidos(datos);
        } catch (Exception e) {
        }
    }

    private void guardarAlSalir() {
        try {
            Archivos.guardarProductos(datos);
            Archivos.guardarPedidos(datos);
        } catch (Exception e) {
        }
        dispose();
        System.exit(0);
    }

    private void alCambiar() {
        int i = tabs.getSelectedIndex();
        if (i == 0) refrescarInicio();
        if (i == 1) llenarProd();
        if (i == 2) { llenarCombo(); llenarLin(); }
        if (i == 3) llenarPed();
    }

    private JPanel inicio() {
        JPanel p = panel();
        p.add(titulo("Sabor Casero", "Sistema de gestión de pedidos"), BorderLayout.NORTH);
        statP = num(); statE = num(); statS = num(); statI = num();
        JPanel s = new JPanel(new GridLayout(1, 4, 8, 8));
        s.setOpaque(false);
        s.add(caja("Productos", statP));
        s.add(caja("Pedidos", statE));
        s.add(caja("Sin entregar", statS));
        s.add(caja("Importe", statI));
        JPanel b = new JPanel(new GridLayout(2, 2, 8, 8));
        b.setOpaque(false);
        b.add(ir("Gestionar productos", 1));
        b.add(ir("Crear pedido", 2));
        b.add(ir("Ver pedidos", 3));
        b.add(ir("Guardar datos", 4));
        JPanel c = new JPanel(new BorderLayout(0, 16));
        c.setOpaque(false);
        c.add(s, BorderLayout.NORTH);
        c.add(b, BorderLayout.CENTER);
        p.add(c, BorderLayout.CENTER);
        return p;
    }

    private void refrescarInicio() {
        int pend = 0;
        double suma = 0;
        for (int i = 0; i < datos.getPedidos().size(); i++) {
            Pedido ped = datos.getPedidos().get(i);
            suma += ped.total();
            if (ped.getEstado() != Estado.ENTREGADO) pend++;
        }
        statP.setText(String.valueOf(datos.getProductos().size()));
        statE.setText(String.valueOf(datos.getPedidos().size()));
        statS.setText(String.valueOf(pend));
        statI.setText("$" + dinero(suma));
    }

    private JPanel productos() {
        JPanel p = panel();
        p.add(titulo("Productos", "Registre platos, bebidas y postres"), BorderLayout.NORTH);
        codigo = campo(); nombre = campo(); precio = campo(); extra = campo();
        tipo = new JComboBox<Tipo>(Tipo.values());
        extraLbl = new JLabel("Porción");
        msgP = new JLabel(" ");
        tipo.addActionListener(e -> cambiarExtra());
        JPanel f = new JPanel(new GridLayout(0, 1, 4, 4));
        f.setPreferredSize(new Dimension(260, 0));
        f.setOpaque(false);
        f.add(new JLabel("Código")); f.add(codigo);
        f.add(new JLabel("Nombre")); f.add(nombre);
        f.add(new JLabel("Precio")); f.add(precio);
        f.add(new JLabel("Categoría")); f.add(tipo);
        f.add(extraLbl); f.add(extra);
        JButton reg = new JButton("Registrar");
        JButton lim = new JButton("Limpiar");
        reg.addActionListener(e -> registrar());
        lim.addActionListener(e -> limpiar());
        JPanel bt = new JPanel(new GridLayout(1, 2, 6, 0));
        bt.add(reg); bt.add(lim);
        f.add(bt); f.add(msgP);
        modProd = modelo("Código", "Nombre", "Categoria", "Precio", "Detalle");
        tProd = tabla(modProd);
        filtro = new JComboBox<String>(new String[]{"Todos", "Platos", "Bebidas", "Postres"});
        filtro.addActionListener(e -> llenarProd());
        JPanel der = new JPanel(new BorderLayout(0, 6));
        der.setOpaque(false);
        der.add(filtro, BorderLayout.NORTH);
        der.add(new JScrollPane(tProd), BorderLayout.CENTER);
        p.add(f, BorderLayout.WEST);
        p.add(der, BorderLayout.CENTER);
        return p;
    }

    private void cambiarExtra() {
        Tipo t = (Tipo) tipo.getSelectedItem();
        if (t == Tipo.PLATO) extraLbl.setText("Porción");
        else if (t == Tipo.BEBIDA) extraLbl.setText("Mililitros");
        else extraLbl.setText("Sabor");
    }

    private void registrar() {
        String cod = codigo.getText().trim();
        String nom = nombre.getText().trim();
        String ext = extra.getText().trim();
        if (cod.isEmpty() || nom.isEmpty() || ext.isEmpty()) {
            decir(msgP, "Complete todos los campos", false);
            return;
        }
        double pre = leerPrecio();
        if (pre <= 0) {
            decir(msgP, "El precio debe ser mayor a cero", false);
            return;
        }
        if (datos.buscar(cod) != null) {
            decir(msgP, "Ya existe un producto con ese codigo", false);
            return;
        }
        Tipo t = (Tipo) tipo.getSelectedItem();
        Producto nuevo;
        if (t == Tipo.PLATO) nuevo = new Plato(cod, nom, pre, ext);
        else if (t == Tipo.BEBIDA) {
            int ml;
            try { ml = Integer.parseInt(ext); }
            catch (NumberFormatException e) {
                decir(msgP, "Los ml deben ser un numero entero", false);
                return;
            }
            if (ml <= 0) {
                decir(msgP, "Los ml deben ser mayores a cero", false);
                return;
            }
            nuevo = new Bebida(cod, nom, pre, ml);
        } else nuevo = new Postre(cod, nom, pre, ext);
        datos.agregar(nuevo);
        decir(msgP, "Producto registrado", true);
        limpiar();
        llenarProd();
    }

    private void limpiar() {
        codigo.setText(""); nombre.setText(""); precio.setText(""); extra.setText("");
        tipo.setSelectedIndex(0);
        cambiarExtra();
    }

    private double leerPrecio() {
        try { return Double.parseDouble(precio.getText().trim().replace(",", ".")); }
        catch (NumberFormatException e) { return -1; }
    }

    private void llenarProd() {
        modProd.setRowCount(0);
        String f = filtro.getSelectedItem().toString();
        for (int i = 0; i < datos.getProductos().size(); i++) {
            Producto pr = datos.getProductos().get(i);
            if (f.equals("Platos") && pr.getTipo() != Tipo.PLATO) continue;
            if (f.equals("Bebidas") && pr.getTipo() != Tipo.BEBIDA) continue;
            if (f.equals("Postres") && pr.getTipo() != Tipo.POSTRE) continue;
            modProd.addRow(new Object[]{pr.getCodigo(), pr.getNombre(), pr.getTipo().toString(), "$" + dinero(pr.getPrecio()), pr.getExtra()});
        }
    }

    private JPanel pedido() {
        JPanel p = panel();
        p.add(titulo("Nuevo pedido", "Elija productos cantidades y la modalidad"), BorderLayout.NORTH);
        combo = new JComboBox<Producto>();
        cant = new JSpinner(new SpinnerNumberModel(1, 1, 500, 1));
        modo = new JComboBox<Modo>(Modo.values());
        JButton ag = new JButton("Agregar");
        JButton qu = new JButton("Quitar");
        JButton cr = new JButton("Crear pedido");
        ag.addActionListener(e -> agregarLinea());
        qu.addActionListener(e -> quitarLinea());
        cr.addActionListener(e -> crearPedido());
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bar.setOpaque(false);
        bar.add(new JLabel("Producto")); bar.add(combo);
        bar.add(new JLabel("Cantidad")); bar.add(cant);
        bar.add(ag); bar.add(qu);
        modLin = modelo("Código", "Nombre", "Precio", "Cantidad", "Subtotal");
        tLin = tabla(modLin);
        total = new JLabel("Total  0.00");
        total.setFont(new Font("Serif", Font.BOLD, 22));
        JPanel sur = new JPanel(new BorderLayout());
        sur.setOpaque(false);
        JPanel iz = new JPanel(new FlowLayout(FlowLayout.LEFT));
        iz.setOpaque(false);
        iz.add(new JLabel("Modalidad")); iz.add(modo); iz.add(cr);
        sur.add(iz, BorderLayout.WEST);
        sur.add(total, BorderLayout.EAST);
        p.add(bar, BorderLayout.NORTH);
        JPanel centro = new JPanel(new BorderLayout(0, 8));
        centro.setOpaque(false);
        centro.add(new JScrollPane(tLin), BorderLayout.CENTER);
        centro.add(sur, BorderLayout.SOUTH);
        p.add(centro, BorderLayout.CENTER);
        return p;
    }

    private void llenarCombo() {
        Producto sel = (Producto) combo.getSelectedItem();
        combo.removeAllItems();
        for (int i = 0; i < datos.getProductos().size(); i++) combo.addItem(datos.getProductos().get(i));
        if (sel != null) combo.setSelectedItem(sel);
    }

    private void llenarLin() {
        modLin.setRowCount(0);
        ArrayList<Linea> lista = actual.getLineas();
        for (int i = 0; i < lista.size(); i++) {
            Linea l = lista.get(i);
            Producto pr = l.getProducto();
            modLin.addRow(new Object[]{pr.getCodigo(), pr.getNombre(), "$" + dinero(pr.getPrecio()), l.getCantidad(), "$" + dinero(pr.getPrecio() * l.getCantidad())});
        }
        total.setText("Total  $" + dinero(actual.total()));
    }

    private void agregarLinea() {
        Producto pr = (Producto) combo.getSelectedItem();
        if (pr == null) { JOptionPane.showMessageDialog(this, "Primero registre productos"); return; }
        int n;
        try { cant.commitEdit(); n = ((Number) cant.getValue()).intValue(); }
        catch (Exception e) { JOptionPane.showMessageDialog(this, "La cantidad no es válida"); return; }
        if (n <= 0) { JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a cero"); return; }
        for (int i = 0; i < actual.getLineas().size(); i++) {
            Linea l = actual.getLineas().get(i);
            if (l.getProducto().getCodigo().equalsIgnoreCase(pr.getCodigo())) {
                l.setCantidad(l.getCantidad() + n);
                llenarLin();
                return;
            }
        }
        actual.agregar(new Linea(pr, n));
        llenarLin();
    }

    private void quitarLinea() {
        int fila = tLin.getSelectedRow();
        if (fila < 0) { JOptionPane.showMessageDialog(this, "Seleccione un producto del pedido"); return; }
        actual.quitar(fila);
        llenarLin();
    }

    private void crearPedido() {
        if (actual.getLineas().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se puede crear un pedido sin productos");
            return;
        }
        actual.setModo((Modo) modo.getSelectedItem());
        actual.setId(datos.nuevoId());
        actual.setFecha(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()));
        datos.getPedidos().add(actual);
        JOptionPane.showMessageDialog(this, "Pedido " + actual.getId() + " creado. Total: $" + dinero(actual.total()));
        actual = new Pedido(0, "", (Modo) modo.getSelectedItem());
        cant.setValue(1);
        llenarLin();
    }

    private JPanel consulta() {
        JPanel p = panel();
        p.add(titulo("Consulta", "Revise el importe y cambie el estado"), BorderLayout.NORTH);
        modPed = modelo("Id", "Fecha", "Modalidad", "Estado", "Total", "Items");
        tPed = tabla(modPed);
        tPed.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) llenarDet(tPed.getSelectedRow());
        });
        modDet = modelo("Codigo", "Nombre", "Precio", "Cantidad", "Subtotal");
        JTable tDet = tabla(modDet);
        estado = new JComboBox<Estado>(Estado.values());
        JButton act = new JButton("Actualizar estado");
        act.addActionListener(e -> cambiarEstado());
        msgC = new JLabel("Seleccione un pedido");
        JPanel acc = new JPanel(new FlowLayout(FlowLayout.LEFT));
        acc.setOpaque(false);
        acc.add(new JLabel("Nuevo estado")); acc.add(estado); acc.add(act); acc.add(msgC);
        JPanel tablas = new JPanel(new GridLayout(2, 1, 0, 8));
        tablas.setOpaque(false);
        tablas.add(new JScrollPane(tPed));
        tablas.add(new JScrollPane(tDet));
        p.add(tablas, BorderLayout.CENTER);
        p.add(acc, BorderLayout.SOUTH);
        return p;
    }

    private void llenarPed() {
        int sel = tPed.getSelectedRow();
        modPed.setRowCount(0);
        for (int i = 0; i < datos.getPedidos().size(); i++) {
            Pedido ped = datos.getPedidos().get(i);
            modPed.addRow(new Object[]{ped.getId(), ped.getFecha(), ped.getModo().toString(), ped.getEstado().toString(), "" + dinero(ped.total()), ped.getLineas().size()});
        }
        if (sel >= 0 && sel < tPed.getRowCount()) tPed.setRowSelectionInterval(sel, sel);
        else { modDet.setRowCount(0); msgC.setText("Seleccione un pedido"); }
    }

    private void llenarDet(int fila) {
        modDet.setRowCount(0);
        if (fila < 0 || fila >= datos.getPedidos().size()) return;
        Pedido ped = datos.getPedidos().get(fila);
        estado.setSelectedItem(ped.getEstado());
        for (int i = 0; i < ped.getLineas().size(); i++) {
            Linea l = ped.getLineas().get(i);
            Producto pr = l.getProducto();
            modDet.addRow(new Object[]{pr.getCodigo(), pr.getNombre(), "$" + dinero(pr.getPrecio()), l.getCantidad(), "$" + dinero(pr.getPrecio() * l.getCantidad())});
        }
        msgC.setText("Pedido " + ped.getId() + "  " + ped.getModo() + "  $" + dinero(ped.total()));
    }

    private void cambiarEstado() {
        int fila = tPed.getSelectedRow();
        if (fila < 0) { JOptionPane.showMessageDialog(this, "Seleccione un pedido"); return; }
        Pedido ped = datos.getPedidos().get(fila);
        ped.setEstado((Estado) estado.getSelectedItem());
        llenarPed();
        if (fila < tPed.getRowCount()) { tPed.setRowSelectionInterval(fila, fila); llenarDet(fila); }
        msgC.setText("Pedido " + ped.getId() + " actualizado a " + ped.getEstado());
    }

    private JPanel archivos() {
        JPanel p = panel();
        p.add(titulo("Almacenamiento", "Guarde y recupere los archivos binarios"), BorderLayout.NORTH);
        JPanel b = new JPanel(new GridLayout(2, 3, 8, 8));
        b.setOpaque(false);
        b.add(acc("Guardar productos", e -> guardarProductos()));
        b.add(acc("Recuperar productos", e -> cargarProductos(true)));
        b.add(acc("Guardar todo", e -> { guardarProductos(); guardarPedidos(); }));
        b.add(acc("Guardar pedidos", e -> guardarPedidos()));
        b.add(acc("Recuperar pedidos", e -> cargarPedidos(true)));
        b.add(acc("Recuperar todo", e -> cargarTodo()));
        log = new JTextArea("Resultado de cada operacion Los archivos quedan en la carpeta datos.\n");
        log.setEditable(false);
        log.setLineWrap(true);
        JPanel c = new JPanel(new BorderLayout(0, 8));
        c.setOpaque(false);
        c.add(b, BorderLayout.NORTH);
        c.add(new JScrollPane(log), BorderLayout.CENTER);
        p.add(c, BorderLayout.CENTER);
        return p;
    }

    private void guardarProductos() {
        try {
            Archivos.guardarProductos(datos);
            nota("productos guardados en " + Archivos.archivoProductos().getAbsolutePath());
        } catch (Exception e) { nota("al guardar productos: " + e.getMessage()); }
    }

    private void guardarPedidos() {
        try {
            Archivos.guardarPedidos(datos);
            nota("pedidos guardados en " + Archivos.archivoPedidos().getAbsolutePath());
        } catch (Exception e) { nota("al guardar pedidos: " + e.getMessage()); }
    }

    private void cargarProductos(boolean preguntar) {
        if (preguntar && !confirmar("Se reemplazarán los productos desea seguir")) return;
        try {
            Archivos.cargarProductos(datos);
            nota(" se recuperaron " + datos.getProductos().size() + " productos");
        } catch (FileNotFoundException e) { nota("no existe el archivo de productos"); }
        catch (Exception e) { nota("al recuperar productos: " + e.getMessage()); }
    }

    private void cargarPedidos(boolean preguntar) {
        if (preguntar && !confirmar("Se reemplazaran los pedidos desea seguir si o no")) return;
        try {
            Archivos.cargarPedidos(datos);
            nota("se recuperaron " + datos.getPedidos().size() + " pedidos");
        } catch (FileNotFoundException e) { nota("no existe el archivo de pedidos"); }
        catch (Exception e) { nota("al recuperar pedidos: " + e.getMessage()); }
    }

    private void cargarTodo() {
        if (!confirmar("Se reemplazarán los productos y los pedidos desea continuar si o no")) return;
        cargarProductos(false);
        cargarPedidos(false);
    }

    private void nota(String s) {
        log.append(new SimpleDateFormat("HH:mm:ss").format(new Date()) + "   " + s + "\n");
        log.setCaretPosition(log.getDocument().getLength());
    }

    private boolean confirmar(String t) {
        return JOptionPane.showConfirmDialog(this, t, "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    private void decir(JLabel l, String t, boolean ok) {
        l.setText(t);
        l.setForeground(ok ? new Color(40, 110, 70) : new Color(150, 40, 40));
    }

    private String dinero(double n) { return String.format("%.2f", n); }

    private JPanel panel() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        p.setBackground(new Color(247, 241, 234));
        return p;
    }

    private JPanel titulo(String a, String b) {
        JPanel p = new JPanel(new GridLayout(2, 1));
        p.setOpaque(false);
        JLabel t = new JLabel(a);
        t.setFont(new Font("Serif", Font.BOLD, 26));
        JLabel s = new JLabel(b);
        p.add(t); p.add(s);
        return p;
    }

    private JLabel num() {
        JLabel l = new JLabel("0");
        l.setFont(new Font("Serif", Font.BOLD, 26));
        return l;
    }

    private JPanel caja(String t, JLabel v) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createTitledBorder(t));
        p.add(v);
        return p;
    }

    private JButton ir(String t, int tab) {
        JButton b = new JButton(t);
        b.addActionListener(e -> tabs.setSelectedIndex(tab));
        return b;
    }

    private JButton acc(String t, java.awt.event.ActionListener al) {
        JButton b = new JButton(t);
        b.addActionListener(al);
        return b;
    }

    private JTextField campo() { return new JTextField(); }

    private DefaultTableModel modelo(String... cols) {
        return new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int f, int c) { return false; }
        };
    }

    private JTable tabla(DefaultTableModel m) {
        JTable t = new JTable(m);
        t.setRowHeight(26);
        t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        return t;
    }
}