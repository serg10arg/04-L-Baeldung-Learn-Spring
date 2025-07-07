package com.example.pedidosonline.modelo;

import java.util.List;

public class Pedido {
    private String id;
    private String idCliente;
    private List<Producto> productos;
    private Double total;

    public Pedido (String id, String idCliente, List<Producto> productos, Double total) {
        this.id = id;
        this.idCliente = idCliente;
        this.productos = productos;
        this.total = total;
    }

    //Gettters y Setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

}
