package dao;

import modelo.Factura;
import modelo.DetalleFactura;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;

public class FacturaDAO {

    public boolean guardar(Factura factura) {

        String sqlFactura = "INSERT INTO facturas "
                + "(id_factura, id_cliente, fecha) "
                + "VALUES (?, ?, ?)";

        String sqlDetalle = "INSERT INTO detalle_factura "
                + "(id_factura, id_producto, cantidad, precio) "
                + "VALUES (?, ?, ?, ?)";

        Connection conexion = null;

        try {

            conexion = Conexion.conectar();
            conexion.setAutoCommit(false);

            // ==========================
            // GUARDAR FACTURA
            // ==========================
            try (PreparedStatement psFactura =
                    conexion.prepareStatement(sqlFactura)) {

                psFactura.setInt(1, factura.getIdFactura());
                psFactura.setInt(2, factura.getCliente().getId());
                psFactura.setDate(
                        3,
                        java.sql.Date.valueOf(factura.getFecha())
                );

                psFactura.executeUpdate();
            }

            // ==========================
            // GUARDAR DETALLES
            // ==========================
            try (PreparedStatement psDetalle =
                    conexion.prepareStatement(sqlDetalle)) {

                List<DetalleFactura> detalles = factura.getDetalles();

                for (DetalleFactura detalle : detalles) {

                    psDetalle.setInt(
                            1,
                            factura.getIdFactura()
                    );

                    psDetalle.setInt(
                            2,
                            detalle.getProducto().getIdProducto()
                    );

                    psDetalle.setInt(
                            3,
                            detalle.getCantidad()
                    );

                    psDetalle.setDouble(
                            4,
                            detalle.getPrecio()
                    );

                    psDetalle.executeUpdate();
                }
            }

            // Confirmar
            conexion.commit();

            return true;

        } catch (SQLException e) {

            try {
                if (conexion != null) {
                    conexion.rollback();
                }
            } catch (SQLException ex) {
                System.out.println(
                        "Error en rollback: " + ex.getMessage()
                );
            }

            JOptionPane.showMessageDialog(
                    null,
                    "ERROR MYSQL AL GUARDAR FACTURA:\n"
                    + e.getMessage()
            );

            System.out.println(
                    "Error al guardar factura: "
                    + e.getMessage()
            );

            return false;

        } finally {

            try {
                if (conexion != null) {
                    conexion.setAutoCommit(true);
                    conexion.close();
                }
            } catch (SQLException e) {
                System.out.println(
                        "Error al cerrar conexión: "
                        + e.getMessage()
                );
            }
        }
    }
}