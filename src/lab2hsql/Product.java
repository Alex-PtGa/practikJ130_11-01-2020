/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab2hsql;

import java.io.*;
import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

/**
 *
 * @author alekra
 */
public class Product {

    int id;
    String name;
    double price;

    public Product(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Product(ResultSet rs) {
        try {
            this.id = rs.getInt(1);
            this.name = rs.getString(2);
            this.price = rs.getString(3);
        } catch (SQLException ex) {
            System.out.println("Ошибка создания Product из Resultset");
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Product{" + "id=" + id + ", name=" + name + ", price=" + price + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + this.id;
        hash = 29 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Product other = (Product) obj;
        return true;
    }

    public static PreparedStatement getSelectQuery(Connection conn) throws IOException {
        String sqlName = "getAllProductInfo";
        PreparedStatement pst = Lab2HSQL.hmp.get(sqlName);
        if (pst != null) {
            return pst;
        }
        String q = readQuery(sqlName);
        System.out.println("read sql" + q);
        try {
            pst = conn.prepareStatement(q);
        } catch (SQLException ex) {
            System.out.println("Ошибка получения PreparedStatement...." + ex.getMessage());
        }
        Lab2HSQL.hmp.put(sqlName, pst);
        return null;
    }

    private static String readQuery(String sqlName) throws IOException {
        String sql;
        StringBuilder sb = new StringBuilder();
        try (InputStream is
                = ClassLoader.getSystemResourceAsStream("sql/" + sqlName + ".sql");
                Reader r = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(r)) {
            String s;
            while ((s = br.readLine()) != null) {
                sb.append(s).append(" ");
            }
            return sb.toString().trim();
        } catch (SQLException ex) {
            System.out.println("Ошибка чтения sql : " + sqlName);
            return null;
        }
    }

    private static void printAllProducts() {
        PreparedStatement pst = Product.getSelectQuery(conn);
        try {
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                (new Product(rs)).toString();
            }
        } catch (SQLException ex) {
            System.out.println("Ошибка получения продуктов из базы. ");
        }
    }
}
