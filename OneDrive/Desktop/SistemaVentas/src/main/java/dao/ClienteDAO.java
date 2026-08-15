/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import modelo.Cliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class ClienteDAO {

    public boolean guardar(Cliente cliente) {

        String sql = "INSERT INTO clientes "
                + "(id_cliente, nombre, nit, telefono, direccion) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, cliente.getId());
            ps.setString(2, cliente.getNombre());
            ps.setString(3, cliente.getNit());
            ps.setString(4, cliente.getTelefono());
            ps.setString(5, cliente.getDireccion());

            ps.executeUpdate();

            return true;

} catch (SQLException e) {
    JOptionPane.showMessageDialog(
        null,
        "ERROR MYSQL:\n" + e.getMessage()
    );

    System.out.println("Error al guardar cliente: " + e.getMessage());

    return false;
}
    }

    public List<Cliente> listar() {

        List<Cliente> clientes = new ArrayList<>();

        String sql = "SELECT * FROM clientes";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Cliente cliente = new Cliente(
                        rs.getInt("id_cliente"),
                        rs.getString("nombre"),
                        rs.getString("nit"),
                        rs.getString("telefono"),
                        rs.getString("direccion")
                );

                clientes.add(cliente);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar clientes: " + e.getMessage());
        }

        return clientes;
    }
}
