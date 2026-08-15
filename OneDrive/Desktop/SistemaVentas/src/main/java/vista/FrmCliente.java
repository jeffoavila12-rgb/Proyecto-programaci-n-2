/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

import modelo.Cliente;
import dao.ClienteDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class FrmCliente extends JInternalFrame {

    private JTextField txtId, txtNombre, txtNit, txtTelefono, txtDireccion;
    private DefaultTableModel modeloTabla;
    private ClienteDAO clienteDAO;

    public FrmCliente() {

        super("Gestión de Clientes", true, true, true, true);

        clienteDAO = new ClienteDAO();

        setSize(600, 400);
        setLayout(new BorderLayout());

        JPanel panelForm = new JPanel(new GridLayout(5, 2, 5, 5));
        panelForm.setBorder(BorderFactory.createTitledBorder("Datos del Cliente"));

        txtId = new JTextField();
        txtNombre = new JTextField();
        txtNit = new JTextField();
        txtTelefono = new JTextField();
        txtDireccion = new JTextField();

        panelForm.add(new JLabel("ID:")); panelForm.add(txtId);
        panelForm.add(new JLabel("Nombre:")); panelForm.add(txtNombre);
        panelForm.add(new JLabel("NIT:")); panelForm.add(txtNit);
        panelForm.add(new JLabel("Teléfono:")); panelForm.add(txtTelefono);
        panelForm.add(new JLabel("Dirección:")); panelForm.add(txtDireccion);

        JButton btnGuardar = new JButton("Guardar Cliente");
        btnGuardar.addActionListener(e -> guardarCliente());

        modeloTabla = new DefaultTableModel(new String[]{"ID", "Nombre", "NIT", "Teléfono", "Dirección"}, 0);
        JTable tabla = new JTable(modeloTabla);

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(panelForm, BorderLayout.CENTER);
        panelSuperior.add(btnGuardar, BorderLayout.SOUTH);

        add(panelSuperior, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
    }

    private void guardarCliente() {

    try {

        int id = Integer.parseInt(txtId.getText());

        String nombre = txtNombre.getText().trim();
        String nit = txtNit.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String direccion = txtDireccion.getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Ingrese el nombre del cliente.");
            return;
        }

        Cliente c = new Cliente(
                id,
                nombre,
                nit,
                telefono,
                direccion
        );

        if (clienteDAO.guardar(c)) {

            modeloTabla.addRow(new Object[]{
                c.getId(),
                c.getNombre(),
                c.getNit(),
                c.getTelefono(),
                c.getDireccion()
            });

            JOptionPane.showMessageDialog(this,
                    "Cliente guardado correctamente en MySQL.");

            limpiar();

        } else {

            JOptionPane.showMessageDialog(this,
                    "No se pudo guardar el cliente.");
        }

    } catch (NumberFormatException ex) {

        JOptionPane.showMessageDialog(this,
                "El ID debe ser un número entero.");
    }
}

    private void limpiar() {
        txtId.setText(""); txtNombre.setText(""); txtNit.setText(""); txtTelefono.setText(""); txtDireccion.setText("");
    }
}

