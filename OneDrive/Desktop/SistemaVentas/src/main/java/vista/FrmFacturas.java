package vista;

import dao.FacturaDAO;
import modelo.Cliente;
import modelo.DetalleFactura;
import modelo.Factura;
import modelo.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;

public class FrmFacturas extends JInternalFrame {

    private JTextField txtNumero;
    private JTextField txtCliente;
    private JTextField txtFecha;

    private JTextField txtProducto;
    private JTextField txtPrecio;
    private JTextField txtCantidad;

    private JLabel lblTotal;

    private JTable tabla;
    private DefaultTableModel modeloTabla;

    private FacturaDAO facturaDAO;

    public FrmFacturas() {

        super(
                "Gestión de Facturas",
                true,
                true,
                true,
                true
        );

        setSize(750, 550);

        setLayout(
                new BorderLayout(10, 10)
        );

        facturaDAO = new FacturaDAO();

        // ==========================================
        // DATOS DE LA FACTURA
        // ==========================================

        JPanel panelFactura =
                new JPanel(
                        new GridLayout(3, 2, 5, 5)
                );

        panelFactura.setBorder(
                BorderFactory.createTitledBorder(
                        "Datos de la Factura"
                )
        );

        txtNumero = new JTextField();
        txtCliente = new JTextField();
        txtFecha = new JTextField(
                LocalDate.now().toString()
        );

        panelFactura.add(
                new JLabel("ID Factura:")
        );

        panelFactura.add(txtNumero);

        panelFactura.add(
                new JLabel("ID Cliente:")
        );

        panelFactura.add(txtCliente);

        panelFactura.add(
                new JLabel("Fecha:")
        );

        panelFactura.add(txtFecha);

        add(
                panelFactura,
                BorderLayout.NORTH
        );

        // ==========================================
        // PRODUCTOS
        // ==========================================

        JPanel panelProducto =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT
                        )
                );

        panelProducto.setBorder(
                BorderFactory.createTitledBorder(
                        "Agregar Producto"
                )
        );

        txtProducto =
                new JTextField(8);

        txtPrecio =
                new JTextField(7);

        txtCantidad =
                new JTextField(5);

        JButton btnAgregar =
                new JButton("Agregar");

        panelProducto.add(
                new JLabel("ID Producto:")
        );

        panelProducto.add(txtProducto);

        panelProducto.add(
                new JLabel("Precio:")
        );

        panelProducto.add(txtPrecio);

        panelProducto.add(
                new JLabel("Cantidad:")
        );

        panelProducto.add(txtCantidad);

        panelProducto.add(btnAgregar);

        // ==========================================
        // TABLA
        // ==========================================

        modeloTabla =
                new DefaultTableModel(
                        new String[]{
                            "ID Producto",
                            "Precio",
                            "Cantidad",
                            "Subtotal"
                        },
                        0
                );

        tabla =
                new JTable(modeloTabla);

        JScrollPane scroll =
                new JScrollPane(tabla);

        JPanel centro =
                new JPanel(
                        new BorderLayout()
                );

        centro.add(
                panelProducto,
                BorderLayout.NORTH
        );

        centro.add(
                scroll,
                BorderLayout.CENTER
        );

        add(
                centro,
                BorderLayout.CENTER
        );

        // ==========================================
        // PARTE INFERIOR
        // ==========================================

        JPanel panelInferior =
                new JPanel(
                        new BorderLayout()
                );

        lblTotal =
                new JLabel("TOTAL: Q0.00");

        lblTotal.setFont(
                new Font(
                        "Tahoma",
                        Font.BOLD,
                        16
                )
        );

        JButton btnEliminar =
                new JButton(
                        "Eliminar seleccionado"
                );

        JButton btnGuardar =
                new JButton(
                        "Guardar Factura"
                );

        JPanel botones =
                new JPanel();

        botones.add(btnEliminar);
        botones.add(btnGuardar);

        panelInferior.add(
                lblTotal,
                BorderLayout.WEST
        );

        panelInferior.add(
                botones,
                BorderLayout.EAST
        );

        add(
                panelInferior,
                BorderLayout.SOUTH
        );

        // ==========================================
        // EVENTOS
        // ==========================================

        btnAgregar.addActionListener(
                e -> agregarProducto()
        );

        btnEliminar.addActionListener(
                e -> eliminarProducto()
        );

        btnGuardar.addActionListener(
                e -> guardarFactura()
        );
    }

    // ==========================================
    // AGREGAR PRODUCTO
    // ==========================================

    private void agregarProducto() {

        try {

            int idProducto =
                    Integer.parseInt(
                            txtProducto
                                    .getText()
                                    .trim()
                    );

            double precio =
                    Double.parseDouble(
                            txtPrecio
                                    .getText()
                                    .trim()
                    );

            int cantidad =
                    Integer.parseInt(
                            txtCantidad
                                    .getText()
                                    .trim()
                    );

            if (precio <= 0 ||
                    cantidad <= 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Precio y cantidad deben ser mayores que 0."
                );

                return;
            }

            double subtotal =
                    precio * cantidad;

            modeloTabla.addRow(
                    new Object[]{
                        idProducto,
                        precio,
                        cantidad,
                        subtotal
                    }
            );

            actualizarTotal();

            txtProducto.setText("");
            txtPrecio.setText("");
            txtCantidad.setText("");

            txtProducto.requestFocus();

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Ingrese valores numéricos válidos."
            );
        }
    }

    // ==========================================
    // ELIMINAR
    // ==========================================

    private void eliminarProducto() {

        int fila =
                tabla.getSelectedRow();

        if (fila >= 0) {

            modeloTabla.removeRow(fila);

            actualizarTotal();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un producto."
            );
        }
    }

    // ==========================================
    // TOTAL
    // ==========================================

    private void actualizarTotal() {

        double total = 0;

        for (int i = 0;
                i < modeloTabla.getRowCount();
                i++) {

            total +=
                    ((Number)
                            modeloTabla
                                    .getValueAt(i, 3))
                            .doubleValue();
        }

        lblTotal.setText(
                String.format(
                        "TOTAL: Q%.2f",
                        total
                )
        );
    }

    // ==========================================
    // GUARDAR
    // ==========================================

    private void guardarFactura() {

        try {

            int idFactura =
                    Integer.parseInt(
                            txtNumero
                                    .getText()
                                    .trim()
                    );

            int idCliente =
                    Integer.parseInt(
                            txtCliente
                                    .getText()
                                    .trim()
                    );

            LocalDate fecha =
                    LocalDate.parse(
                            txtFecha
                                    .getText()
                                    .trim()
                    );

            if (modeloTabla.getRowCount() == 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Agregue al menos un producto."
                );

                return;
            }

            // ======================================
            // CLIENTE
            // ======================================

            Cliente cliente =
                    new Cliente(
                            idCliente,
                            "Cliente",
                            "CF",
                            "00000000",
                            "Ciudad"
                    );

            // ======================================
            // FACTURA
            // ======================================

            Factura factura =
                    new Factura(
                            idFactura,
                            fecha,
                            cliente
                    );

            // ======================================
            // DETALLES
            // ======================================

            for (int i = 0;
                    i < modeloTabla.getRowCount();
                    i++) {

                int idProducto =
                        ((Number)
                                modeloTabla
                                        .getValueAt(i, 0))
                                .intValue();

                double precio =
                        ((Number)
                                modeloTabla
                                        .getValueAt(i, 1))
                                .doubleValue();

                int cantidad =
                        ((Number)
                                modeloTabla
                                        .getValueAt(i, 2))
                                .intValue();

                Producto producto =
                        new Producto(
                                idProducto,
                                "Producto",
                                precio,
                                100
                        );

                DetalleFactura detalle =
                        new DetalleFactura(
                                producto,
                                cantidad,
                                precio
                        );

                factura.agregarDetalle(
                        detalle
                );
            }

            // ======================================
            // GUARDAR MYSQL
            // ======================================

            if (facturaDAO.guardar(factura)) {

                JOptionPane.showMessageDialog(
                        this,
                        "¡Factura guardada correctamente!"
                );

                limpiar();
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "ID de factura, ID de cliente, "
                    + "ID de producto, precio y cantidad "
                    + "deben ser números válidos."
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error:\n"
                    + e.getMessage()
            );
        }
    }

    // ==========================================
    // LIMPIAR
    // ==========================================

    private void limpiar() {

        txtNumero.setText("");
        txtCliente.setText("");

        txtFecha.setText(
                LocalDate.now().toString()
        );

        modeloTabla.setRowCount(0);

        lblTotal.setText(
                "TOTAL: Q0.00"
        );
    }
}