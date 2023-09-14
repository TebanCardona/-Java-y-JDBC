package com.alura.jdbc.factory;

import java.sql.Connection;
import java.sql.SQLException;
import io.github.cdimascio.dotenv.Dotenv;
import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class ConnectionFactory {

  private DataSource dataSource;

  public ConnectionFactory() {
    Dotenv dotenv = Dotenv.load();
    var comboPooledDataSource = new ComboPooledDataSource();
    comboPooledDataSource.setJdbcUrl("jdbc:mysql://localhost/stock_products?useTimeZone=true&serverTimeZone=UTC");
    comboPooledDataSource.setUser(dotenv.get("USER"));
    comboPooledDataSource.setPassword(dotenv.get("PASSWORD"));
    comboPooledDataSource.setMaxPoolSize(10);

    this.dataSource = comboPooledDataSource;
  }

  public Connection recuperaConexion() {
    try {
      return this.dataSource.getConnection();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

}
