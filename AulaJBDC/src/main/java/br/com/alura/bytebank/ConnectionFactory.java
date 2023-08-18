package br.com.alura.bytebank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class ConnectionFactory {
    public static void main(String[] a){
        getConnectionn();
    }

    public static Connection getConnectionn(){
        try{
            return createDataSource().getConnection();
        }catch(SQLException e){
            System.out.println(e);
        }
        return null;
    }

    private static HikariDataSource createDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/byte_bank");
        config.setUsername("root");
        config.setPassword("@vittzxTheMan33@");
        config.setMaximumPoolSize(10);

        return new HikariDataSource(config);
    }
}
