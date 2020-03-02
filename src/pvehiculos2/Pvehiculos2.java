package pvehiculos2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Pvehiculos2 {

    //VARIABLES ORACLE
    private static String id;
    private static String dni;
    private static String nomec;
    private static String nomeveh;
    private static int pf;

    public static void ejercicio() throws SQLException {

        //CONEXION A ORACLE
        Connection conn;
        String driver = "jdbc:oracle:thin:";
        String host = "localhost.localdomain"; // tambien puede ser una ip como "192.168.1.14"
        String porto = "1521";
        String sid = "orcl";
        String usuario = "hr";
        String password = "hr";
        String url = driver + usuario + "/" + password + "@" + host + ":" + porto + ":" + sid;

        conn = DriverManager.getConnection(url);

        //OBTENEMOS LAS VARIABLES DE ORACLE
        PreparedStatement ps = conn.prepareStatement("select * from finalveh where id >= 5");

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            id = rs.getString("id");
            System.out.println("ID: " + id);

        }

        conn.close();

    }

    public static void main(String[] args) throws SQLException {

        ejercicio();

    }

}
