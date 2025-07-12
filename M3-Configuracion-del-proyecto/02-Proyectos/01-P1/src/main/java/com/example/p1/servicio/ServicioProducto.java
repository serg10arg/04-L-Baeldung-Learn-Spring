package com.example.p1.servicio;

import com.example.p1.modelo.Producto;
import jakarta.annotation.PostConstruct; // Para ejecutar lógica después de la construcción del bean
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment; // Para la Interfaz Environment
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class ServicioProducto {

    private static final Logger REGISTRO = LoggerFactory.getLogger(ServicioProducto.class);

    // Inyección de propiedades usando la anotación @Value
    @Value("${producto.inventario.prefijo.categoria}")
    private String prefijoCategoria; // Inyecta "CAT-"

    @Value("${producto.inventario.sufijo.activo}")
    private String sufijoActivo; // Inyecta "EN_STOCK"

    @Value("${producto.inventario.sufijo.bajo_stock}")
    private String sufijoBajoStock; // Inyecta "STOCK_BAJO"

    @Value("${producto.inventario.umbral_bajo_stock}")
    private int umbralBajoStock; // Inyecta 10 como Integer

    // Inyección de la interfaz Environment para acceso programático a propiedades
    @Autowired
    private Environment entorno;

    private Long contadorIdProductos = 0L; // Simula un generador de IDs

    /**
     * Simula el guardado de un producto y genera su código de inventario.
     * @param producto El producto a guardar.
     * @return El producto con el código de inventario generado.
     */
    public Producto guardarProducto(Producto producto) {
        REGISTRO.info("Preparando para guardar producto: {}", producto.getNombre());

        // Asignar un ID simulado para el producto
        producto.setId(++contadorIdProductos);

        // Determinar el sufijo basado en el stock y el umbral inyectado
        String sufijoFinal;
        if (producto.getStock() <= umbralBajoStock) {
            sufijoFinal = sufijoBajoStock;
            REGISTRO.warn("¡Producto {} con stock bajo! Cantidad: {}", producto.getNombre(), producto.getStock());
        } else {
            sufijoFinal = sufijoActivo;
        }

        // Generar el código de inventario combinando prefijo, ID y sufijo
        String codigoGenerado = prefijoCategoria + producto.getId() + "-" + sufijoFinal;
        producto.setCodigoInventario(codigoGenerado);

        REGISTRO.info("Producto {} guardado con ID: {} y Código de Inventario: {}",
                producto.getNombre(), producto.getId(), producto.getCodigoInventario());
        return producto;
    }

    /**
     * Método de inicialización que demuestra el acceso programático a propiedades
     * a través de la interfaz Environment.
     */
    @PostConstruct // Se ejecuta una vez después de que el bean ha sido construido y sus dependencias inyectadas
    public void verificarConfiguracionDesdeEnvironment() {
        REGISTRO.info("--- Verificación de Configuración de Propiedades (vía Environment) ---");
        // Acceder a las propiedades utilizando entorno.getProperty()
        REGISTRO.info("Prefijo de categoría recuperado: {}", entorno.getProperty("producto.inventario.prefijo.categoria"));
        REGISTRO.info("Umbral de bajo stock recuperado: {}", entorno.getProperty("producto.inventario.umbral_bajo_stock", Integer.class));
        // También podemos verificar si una propiedad existe o intentar acceder a una que no (para fines de depuración)
        REGISTRO.info("Sufijo 'PRODUCTO_RETIRADO' (no existente): {}", entorno.getProperty("producto.inventario.sufijo.retirado", "NO_DEFINIDO"));
        REGISTRO.info("--- Fin Verificación de Configuración ---");
    }
}