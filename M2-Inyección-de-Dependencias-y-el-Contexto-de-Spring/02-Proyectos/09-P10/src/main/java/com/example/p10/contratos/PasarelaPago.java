package com.example.p10.contratos;

public interface PasarelaPago {
    boolean procesarPago(double monto);
    String getNombrePasarela();
}
