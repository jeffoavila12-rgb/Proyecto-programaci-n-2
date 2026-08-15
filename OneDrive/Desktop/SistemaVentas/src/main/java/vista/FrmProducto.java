package vista;

import dao.ProductoDAO;
import modelo.Producto;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class FrmProducto extends JInternalFrame {

    private JTextField txtNombre, txtPrecio, txtExistencia;
    private DefaultTableModel modeloTabla;
    private JTable tabla;
    private ProductoDAO productoDAO;

    public FrmProducto() {

        super("Gestión de Productos", true, true, true, true);

        setSize(600, 400);
        setLayout(new BorderLayout());

        productoDAO = new ProductoDAO();

        JPanel panelForm = new JPanel(new GridLayout(3, 2, 5, 5));
        panelForm.setBorder(
                BorderFactory.createTitledBorder("Datos del Producto")
        );

        txtNombre = new JTextField();
        txtPrecio = new JTextField();
        txtExistencia = new JTextField();

        panelForm.add(new JLabel("Nombre:"));
        panelForm.add(txtNombre);

        panelForm.add(new JLabel("Precio:"));
        panelForm.add(txtPrecio);

        panelForm.add(new JLabel("Existencia (Stock):"));
        panelForm.add(txtExistencia);

        JButton btnGuardar = new JButton("Guardar Producto");

        btnGuardar.addActionListener(e -> guardarProducto());

        modeloTabla = new DefaultTableModel(
                new String[]{"ID", "Nombre", "Precio", "Stock"}, 0
        );

        tabla = new JTable(modeloTabla);

        JPanel panelSuperior = new JPanel(new BorderLayout());

        panelSuperior.add(panelForm, BorderLayout.CENTER);
        panelSuperior.add(btnGuardar, BorderLayout.SOUTH);

        add(panelSuperior, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        cargarProductos();
    }

    private void guardarProducto() {

        try {

            String nombre = txtNombre.getText().trim();

            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Ingrese el nombre del producto."
                );
                return;
            }

            double precio = Double.parseDouble(
                    txtPrecio.getText().trim()
            );

            int stock = Integer.parseInt(
                    txtExistencia.getText().trim()
            );

            if (precio < 0 || stock < 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "El precio y el stock no pueden ser negativos."
                );
                return;
            }

            Producto producto = new Producto(
                    0,
                    nombre,
                    precio,
                    stock
            );

            if (productoDAO.guardar(producto)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Producto guardado correctamente en MySQL."
                );

                limpiar();
                cargarProductos();
            }

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Precio y existencia deben ser valores numéricos."
            );
        }
    }

    private void cargarProductos() {

        modeloTabla.setRowCount(0);

        for (Producto p : productoDAO.listar()) {

            modeloTabla.addRow(
                    new Object[]{
                        p.getIdProducto(),
                        p.getNombre(),
                        p.getPrecio(),
                        p.getExistencia()
                    }
            );
        }
    }

    private void limpiar() {

        txtNombre.setText("");
        txtPrecio.setText("");
        txtExistencia.setText("");

        txtNombre.requestFocus();
    }
}