package com.alura.jdbc.modelo;

import java.util.ArrayList;
import java.util.List;

import com.alura.jdbc.controller.CategoriaController;

public class Categoria {

  private Integer id;
  private String nombre;

  /**
   * @return the nombre
   */
  public String getNombre() {
    return nombre;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  private List<Producto> productos = new ArrayList<>();

  public Categoria(Integer id, String nombre) {
    this.id = id;
    this.nombre = nombre;
  }

  public Categoria(String name) {
    this.nombre = name;
  }

  public Integer getId() {
    return this.id;
  }

  @Override
  public String toString() {
    return this.nombre;
  }

  public void findIdAndSet() {
    CategoriaController cC = new CategoriaController();
    var listaC = cC.listar();
    var cat = listaC.get(listaC.size() - 1);
    this.id = cat.id;
  }

  public void agregar(Producto producto) {
    this.productos.add(producto);
  }

  public List<Producto> getProductos() {
    return this.productos;
  }

}
