package com.example.p10.servicio;

import com.example.p10.contratos.PasarelaPago;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * An implementation of the PasarelaPago interface for PayPal.
 *
 * @Service: This crucial annotation tells Spring to create and manage this class as a bean.
 * @Primary: This annotation marks this bean as the default choice if multiple
 *           PasarelaPago implementations exist.
 */
@Service
@Primary
public class PasarelaPagoPayPal implements PasarelaPago {

    private static final Logger LOG = LoggerFactory.getLogger(PasarelaPagoPayPal.class);

    @Override
    public boolean procesarPago(double monto) {
        LOG.info("Processing payment of ${} through PayPal...", monto);
        // Real-world payment processing logic would go here.
        return true; // Simulate a successful payment.
    }

    @Override
    public String getNombrePasarela() {
        return "PayPal";
    }
}