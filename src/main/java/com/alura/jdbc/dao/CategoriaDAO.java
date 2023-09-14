package com.alura.jdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.alura.jdbc.modelo.Categoria;
import com.alura.jdbc.modelo.Producto;

public class CategoriaDAO {

  private Connection con;

  public CategoriaDAO(Connection con) {
    this.con = con;
  }

  public void guardar(Categoria categoria) {
    try {
      PreparedStatement statement;
      statement = con.prepareStatement("Insert INTO category"
          + "(name)"
          + "VALUES (?)",
          Statement.RETURN_GENERATED_KEYS);
      try (statement) {
        statement.setString(1, categoria.toString());
        statement.execute();

        final ResultSet resultSet = statement.getGeneratedKeys();
        try (resultSet) {
          while (resultSet.next()) {
            System.out.println(String.format("Fue insertado el producto: %s", categoria));
          }
        }
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return;
  }

  public List<Categoria> listar() {
    List<Categoria> resultado = new ArrayList<>();

    try {
      String sql = "SELECT id_category, name FROM category";

      System.out.println(sql);

      final PreparedStatement statement = con
          .prepareStatement(sql);

      try (statement) {
        final ResultSet resultSet = statement.executeQuery();

        try (resultSet) {
          while (resultSet.next()) {
            resultado.add(new Categoria(
                resultSet.getInt("id_category"),
                resultSet.getString("name")));
          }
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    return resultado;
  }

  public List<Categoria> listarConProductos() {
    List<Categoria> resultado = new ArrayList<>();

    try {
      String sql = "SELECT C.id_category, C.name, P.id_product, P.name, P.quantity "
          + " FROM category C INNER JOIN product P "
          + " ON C.id_category = P.id_category";

      System.out.println(sql);

      final PreparedStatement statement = con
          .prepareStatement(sql);

      try (statement) {
        final ResultSet resultSet = statement.executeQuery();

        try (resultSet) {
          while (resultSet.next()) {
            int categoriaId = resultSet.getInt("C.id_category");
            String categoriaNombre = resultSet.getString("C.name");

            Categoria categoria = resultado
                .stream()
                .filter(cat -> cat.getId().equals(categoriaId))
                .findAny().orElseGet(() -> {
                  Categoria cat = new Categoria(
                      categoriaId, categoriaNombre);
                  resultado.add(cat);

                  return cat;
                });

            Producto producto = new Producto(
                resultSet.getInt("P.id_product"),
                resultSet.getString("P.name"),
                resultSet.getInt("P.quantity"));

            categoria.agregar(producto);
          }
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    return resultado;
  }

}
