package com.alura.jdbc.view;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.alura.jdbc.controller.CategoriaController;
import com.alura.jdbc.controller.ProductoController;
import com.alura.jdbc.modelo.Categoria;
import com.alura.jdbc.modelo.Producto;

public class ControlDeStockFrame extends JFrame {

  private static final long serialVersionUID = 1L;

  private JLabel labelNombre, labelDescripcion, labelCantidad, labelCategoria, labelCName;
  private JTextField textoNombre, textoDescripcion, textoCantidad, textoCName;
  private JComboBox<Categoria> comboCategoria;
  private JButton botonGuardar, botonModificar, botonLimpiar, botonEliminar, botonReporte, botonCrearCategoria;
  private JTable tabla;
  private DefaultTableModel modelo;
  private ProductoController productoController;
  private CategoriaController categoriaController;

  public ControlDeStockFrame() {
    super("Productos");

    this.categoriaController = new CategoriaController();
    this.productoController = new ProductoController();

    Container container = getContentPane();
    setLayout(null);

    configurarCamposDelFormulario(container);

    configurarTablaDeContenido(container);

    configurarAccionesDelFormulario();
  }

  private void configurarTablaDeContenido(Container container) {
    tabla = new JTable();

    modelo = (DefaultTableModel) tabla.getModel();
    modelo.addColumn("Identificador del Producto");
    modelo.addColumn("Nombre del Producto");
    modelo.addColumn("Descripción del Producto");
    modelo.addColumn("Cantidad");
    modelo.addColumn("Categoria Name");

    cargarTabla();

    tabla.setBounds(10, 205, 860, 280);
    botonEliminar = new JButton("Eliminar");
    botonModificar = new JButton("Modificar");
    botonReporte = new JButton("Ver Reporte");
    botonEliminar.setBounds(10, 500, 100, 20);
    botonModificar.setBounds(120, 500, 100, 20);
    botonReporte.setBounds(230, 500, 100, 20);
    container.add(tabla);
    container.add(botonEliminar);
    container.add(botonModificar);
    container.add(botonReporte);

    setSize(900, 600);
    setVisible(true);
    setLocationRelativeTo(null);
  }

  private void configurarCamposDelFormulario(Container container) {
    labelNombre = new JLabel("Nombre del Producto");
    labelDescripcion = new JLabel("Descripción del Producto");
    labelCantidad = new JLabel("Cantidad");
    labelCategoria = new JLabel("Categoría del Producto");
    labelCName = new JLabel("Nombre de la Categoria");

    labelNombre.setBounds(10, 10, 240, 15);
    labelDescripcion.setBounds(10, 50, 240, 15);
    labelCantidad.setBounds(10, 90, 240, 15);
    labelCategoria.setBounds(10, 130, 240, 15);
    labelCName.setBounds(610, 10, 240, 15);

    labelNombre.setForeground(Color.BLACK);
    labelDescripcion.setForeground(Color.BLACK);
    labelCategoria.setForeground(Color.BLACK);
    labelCName.setForeground(Color.BLACK);

    textoNombre = new JTextField();
    textoDescripcion = new JTextField();
    textoCantidad = new JTextField();
    textoCName = new JTextField();
    comboCategoria = new JComboBox<>();
    comboCategoria.addItem(new Categoria(0, "Elige una Categoría"));

    var categorias = this.categoriaController.listar();
    categorias.forEach(categoria -> comboCategoria.addItem(categoria));

    textoNombre.setBounds(10, 25, 265, 20);
    textoDescripcion.setBounds(10, 65, 265, 20);
    textoCantidad.setBounds(10, 105, 265, 20);
    textoCName.setBounds(540, 25, 265, 20);
    comboCategoria.setBounds(10, 145, 265, 20);

    botonGuardar = new JButton("Guardar");
    botonLimpiar = new JButton("Limpiar");
    botonCrearCategoria = new JButton("Crear Categoria");
    botonCrearCategoria.setBounds(550, 50, 245, 20);
    botonGuardar.setBounds(10, 175, 80, 20);
    botonLimpiar.setBounds(100, 175, 80, 20);

    container.add(labelNombre);
    container.add(labelDescripcion);
    container.add(labelCantidad);
    container.add(labelCategoria);
    container.add(textoNombre);
    container.add(textoDescripcion);
    container.add(textoCantidad);
    container.add(comboCategoria);
    container.add(botonGuardar);
    container.add(botonLimpiar);
    container.add(botonCrearCategoria);
    container.add(textoCName);
    container.add(labelCName);

  }

  private void configurarAccionesDelFormulario() {
    botonGuardar.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        guardar();
        limpiarTabla();
        cargarTabla();
      }
    });
    botonCrearCategoria.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        guardarCategoria();
        limpiarTabla();
        cargarTabla();

      }
    });
    botonLimpiar.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        limpiarFormulario();
      }
    });

    botonEliminar.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        eliminar();
        limpiarTabla();
        cargarTabla();
      }
    });

    botonModificar.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        modificar();
        limpiarTabla();
        cargarTabla();
      }
    });

    botonReporte.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        abrirReporte();
      }
    });
  }

  private void abrirReporte() {
    new ReporteFrame(this);
  }

  private void guardarCategoria() {
    if (textoCName.getText().isBlank()) {
      JOptionPane.showMessageDialog(this, "Los campos Nombre de Categoria son requeridos.");
      return;
    }

    Categoria categoria = new Categoria(textoCName.getText());
    this.categoriaController.agregar(categoria);
    JOptionPane.showMessageDialog(this, "Registrado con éxito!");
    categoria.findIdAndSet();
    comboCategoria.addItem(categoria);
    this.limpiarFormulario();
  }

  private void limpiarTabla() {
    modelo.getDataVector().clear();
  }

  private boolean tieneFilaElegida() {
    return tabla.getSelectedRowCount() == 0 || tabla.getSelectedColumnCount() == 0;
  }

  private void modificar() {
    if (tieneFilaElegida()) {
      JOptionPane.showMessageDialog(this, "Por favor, elije un item");
      return;
    }

    Optional.ofNullable(modelo.getValueAt(tabla.getSelectedRow(), tabla.getSelectedColumn()))
        .ifPresentOrElse(fila -> {
          Integer id = Integer.valueOf(modelo.getValueAt(tabla.getSelectedRow(), 0).toString());
          String nombre = (String) modelo.getValueAt(tabla.getSelectedRow(), 1);
          String descripcion = (String) modelo.getValueAt(tabla.getSelectedRow(), 2);
          Integer cantidad = Integer.valueOf(modelo.getValueAt(tabla.getSelectedRow(), 3).toString());

          var filasModificadas = this.productoController.modificar(nombre, descripcion, cantidad, id);

          JOptionPane.showMessageDialog(this, String.format("%d item modificado con éxito!", filasModificadas));
        }, () -> JOptionPane.showMessageDialog(this, "Por favor, elije un item"));
  }

  private void eliminar() {
    if (tieneFilaElegida()) {
      JOptionPane.showMessageDialog(this, "Por favor, elije un item");
      return;
    }

    Optional.ofNullable(modelo.getValueAt(tabla.getSelectedRow(), tabla.getSelectedColumn()))
        .ifPresentOrElse(fila -> {
          Integer id = Integer.valueOf(modelo.getValueAt(tabla.getSelectedRow(), 0).toString());

          var filasModificadas = this.productoController.eliminar(id);

          modelo.removeRow(tabla.getSelectedRow());

          JOptionPane.showMessageDialog(this,
              String.format("%d item eliminado con éxito!", filasModificadas));
        }, () -> JOptionPane.showMessageDialog(this, "Por favor, elije un item"));
  }

  private void cargarTabla() {
    var productos = this.productoController.listar();

    productos.forEach(producto -> modelo.addRow(
        new Object[] {
            producto.getId(),
            producto.getNombre(),
            producto.getDescripcion(),
            producto.getCantidad(),
            producto.getCategoriaName() }));
  }

  private void guardar() {
    if (textoNombre.getText().isBlank() || textoDescripcion.getText().isBlank()) {
      JOptionPane.showMessageDialog(this, "Los campos Nombre y Descripción son requeridos.");
      return;
    }

    Integer cantidadInt;

    try {
      cantidadInt = Integer.parseInt(textoCantidad.getText());
    } catch (NumberFormatException e) {
      JOptionPane.showMessageDialog(this, String
          .format("El campo cantidad debe ser numérico dentro del rango %d y %d.", 0, Integer.MAX_VALUE));
      return;
    }

    var producto = new Producto(
        textoNombre.getText(),
        textoDescripcion.getText(),
        cantidadInt);

    var categoria = (Categoria) comboCategoria.getSelectedItem();

    this.productoController.guardar(producto, categoria.getId());

    JOptionPane.showMessageDialog(this, "Registrado con éxito!");

    this.limpiarFormulario();
  }

  private void limpiarFormulario() {
    this.textoNombre.setText("");
    this.textoDescripcion.setText("");
    this.textoCantidad.setText("");
    this.comboCategoria.setSelectedIndex(0);
    this.textoCName.setText("");
  }

}
