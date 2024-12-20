package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;
import java.util.stream.IntStream;

public class DB {

    private static Connection conn = null;

    public static Connection getConn () {
        if (conn== null) {
            try {
                Properties pros = loadProperties();
                String URL = pros.getProperty("dburl");
                String USER = pros.getProperty("user");
                String PASSWORD = pros.getProperty("password");
                conn = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (SQLException e) {
                throw new RuntimeException("NAO FOI POSSIVEL ESTABELECER CONEXAO COM O BANDO DE DADOS!" + e.getMessage());
            }
        }
        return conn;
    }

    public static Properties loadProperties () {
        try (FileInputStream fs = new FileInputStream("db.propeRties")) {
            Properties props = new Properties();
            props.load(fs);
            return props;
        } catch (IOException e) {
            throw new RuntimeException("NÃO FOI POSSIVEL LER O ARQUIVO! " + e.getMessage());
        }
    }

    public static void closeConnection () {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException("FALHA AO FECHAR CONEXAO COM O BANCO DE DADOS!");
            }
        }
        System.out.println("Conexao fechada com sucessp!");
    }
    public static void closeStatement (Statement st) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    public static void closeResultSet (ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }
}
