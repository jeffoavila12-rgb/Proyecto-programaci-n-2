/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;

public class PruebaConexion {

    public static void main(String[] args) {

        Connection conexion = Conexion.conectar();

        if (conexion != null) {
            System.out.println("================================");
            System.out.println("CONEXION EXITOSA CON MYSQL");
            System.out.println("================================");

            try {
                conexion.close();
            } catch (Exception e) {
                System.out.println("Error al cerrar la conexión: " + e.getMessage());
            }

        } else {
            System.out.println("================================");
            System.out.println("NO SE PUDO CONECTAR A MYSQL");
            System.out.println("================================");
        }
    }
}
