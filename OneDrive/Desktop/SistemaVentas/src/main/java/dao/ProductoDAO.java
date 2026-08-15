/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import modelo.Producto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class ProductoDAO {

    public boolean guardar(Producto producto) {

        String sql = "INSERT INTO productos "
                + "(nombre, precio, existencia) "
                + "VALUES (?, ?, ?)";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, producto.getNombre());
            ps.setDouble(2, producto.getPrecio());
            ps.setInt(3, producto.getExistencia());

            ps.executeUpdate();

            return true;

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    null,
                    "ERROR MYSQL:\n" + e.getMessage()
            );

            System.out.println(
                    "Error al guardar producto: " + e.getMessage()
            );

            return false;
        }
    }

    public List<Producto> listar() {

        List<Producto> productos = new ArrayList<>();

        String sql = "SELECT * FROM productos";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Producto producto = new Producto(
                        rs.getInt("id_producto"),
                        rs.getString("nombre"),
                        rs.getDouble("precio"),
                        rs.getInt("existencia")
                );

                productos.add(producto);
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    null,
                    "ERROR AL LISTAR PRODUCTOS:\n" + e.getMessage()
            );

            System.out.println(
                    "Error al listar productos: " + e.getMessage()
            );
        }

        return productos;
    }
}