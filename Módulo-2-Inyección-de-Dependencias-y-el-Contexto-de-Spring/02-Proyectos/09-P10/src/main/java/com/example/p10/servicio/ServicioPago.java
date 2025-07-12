package com.example.p10.servicio;

import com.example.p10.contratos.PasarelaPago;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ServicioPago {

    private static final Logger LOG = LoggerFactory.getLogger(ServicioPago.class);
    private final PasarelaPago pasarelaPago;

    // Usamos @Primary por defecto. No se necesita @Qualifier aquí para la demo.
    public ServicioPago(PasarelaPago pasarelaPago) {
        this.pasarelaPago = pasarelaPago;
        LOG.info("ServicioPago inicializado con la pasarela por defecto: {}", pasarelaPago.getNombrePasarela());
    }

    public boolean procesar(double monto) {
        return pasarelaPago.procesarPago(monto);
    }
}