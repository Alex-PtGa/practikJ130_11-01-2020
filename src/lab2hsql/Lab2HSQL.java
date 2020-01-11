package lab2hsql;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.*;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class Lab2HSQL {

    public static HashMap<String, PreparedStatement> hmp = new HashMap();
    private static Connection conn = null;

    public static void main(String[] args) {
        // TODO code application logic here
        loadDriver();
        getConnection();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT*FROM PRODUCT");
            while (rs.next()) {
                System.out.println( new Product(rs).toString());
            }
        } catch (SQLException ex) {
            System.out.println("Ошибка Statement ");
        }
        Product product = new Product(77, "Мишка плюшевый", 777.5);
        PreparedStatement pst = product.getSelectQuery(conn);

        System.out.println("pst = " + pst.toString());
        printAllProducts();
    }
private static void loadDriver() {
        try {
            Class.forName("org.hsqldb.jdbcDriver");
            System.out.println("Драйвер загружен");
        } catch (ClassNotFoundException ex) {
            System.out.println("Драйвер не загружен" + ex.getMessege());
            System.exit(1);
        }
    }

    private static void getConnection() {
        String patch = "localdb/";
        String db = "sample";
        String url = "jdbc:hsqldb:file:" + patch + db;
        String login = "aladdin";
        String passwd = "aladdin";
        try {
            conn = DriverManager.getConnection(url, login, passwd);
            System.out.println("Соединение получено conn=" + conn);
        } catch (SQLException ex) {
            System.out.println("Ошибка получения conn" + ex.getMessage());
        }
    }
}
