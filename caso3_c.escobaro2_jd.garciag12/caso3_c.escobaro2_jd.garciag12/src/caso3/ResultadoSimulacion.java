package caso3;

import java.util.ArrayList;
import java.util.List;

public class ResultadoSimulacion {
    private final ConfiguracionSimulacion configuracion;
    private final EstadisticasSimulacion estadisticas;
    private final boolean buzonesVacios;
    private final int maxEntrada;
    private final int maxAlertas;
    private final int maxClasificacion;
    private final int[] maxBuzonesServidores;
    private final List<String> validaciones;

    public ResultadoSimulacion(ConfiguracionSimulacion configuracion,
                               EstadisticasSimulacion estadisticas,
                               boolean buzonesVacios,
                               int maxEntrada,
                               int maxAlertas,
                               int maxClasificacion,
                               int[] maxBuzonesServidores) {
        this.configuracion = configuracion;
        this.estadisticas = estadisticas;
        this.buzonesVacios = buzonesVacios;
        this.maxEntrada = maxEntrada;
        this.maxAlertas = maxAlertas;
        this.maxClasificacion = maxClasificacion;
        this.maxBuzonesServidores = maxBuzonesServidores;
        this.validaciones = construirValidaciones();
    }

    private List<String> construirValidaciones() {
        List<String> lista = new ArrayList<String>();
        int total = configuracion.getTotalEventosEsperados();
        lista.add(check("Todos los sensores generaron el total esperado", estadisticas.getEventosGenerados() == total,
                estadisticas.getEventosGenerados() + " / " + total));
        lista.add(check("El broker proceso todos los eventos esperados", estadisticas.getEventosProcesadosBroker() == total,
                estadisticas.getEventosProcesadosBroker() + " / " + total));
        lista.add(check("Conservacion en broker", estadisticas.getEventosEnviadosAlertas() + estadisticas.getEventosEnviadosClasificacionPorBroker() == total,
                "alertas + clasificacion = " + (estadisticas.getEventosEnviadosAlertas() + estadisticas.getEventosEnviadosClasificacionPorBroker())));
        lista.add(check("Conservacion en administrador", estadisticas.getAlertasAprobadasPorAdministrador() + estadisticas.getAlertasDescartadasPorAdministrador() == estadisticas.getEventosEnviadosAlertas(),
                "aprobadas + descartadas = " + (estadisticas.getAlertasAprobadasPorAdministrador() + estadisticas.getAlertasDescartadasPorAdministrador())));
        int normalesAServidores = estadisticas.getEventosEnviadosClasificacionPorBroker() + estadisticas.getAlertasAprobadasPorAdministrador();
        lista.add(check("Los clasificadores reenviaron todos los normales", estadisticas.getEventosClasificados() == normalesAServidores,
                estadisticas.getEventosClasificados() + " / " + normalesAServidores));
        lista.add(check("Los servidores procesaron todos los eventos normales", estadisticas.getEventosProcesadosServidores() == normalesAServidores,
                estadisticas.getEventosProcesadosServidores() + " / " + normalesAServidores));
        lista.add(check("Se enviaron nc eventos de fin a clasificadores", estadisticas.getEventosFinClasificadores() == configuracion.getNc(),
                estadisticas.getEventosFinClasificadores() + " / " + configuracion.getNc()));
        lista.add(check("Se enviaron ns eventos de fin a servidores", estadisticas.getEventosFinServidores() == configuracion.getNs(),
                estadisticas.getEventosFinServidores() + " / " + configuracion.getNs()));
        lista.add(check("Todos los buzones terminaron vacios", buzonesVacios, buzonesVacios ? "ok" : "hay elementos remanentes"));
        lista.add(check("La ocupacion maxima del buzon de clasificacion respeto tam1", maxClasificacion <= configuracion.getTam1(),
                maxClasificacion + " / cap " + configuracion.getTam1()));
        boolean servidoresOk = true;
        String detalle = "";
        for (int i = 0; i < maxBuzonesServidores.length; i++) {
            if (maxBuzonesServidores[i] > configuracion.getTam2()) {
                servidoresOk = false;
            }
            if (i > 0) {
                detalle += ", ";
            }
            detalle += "S" + (i + 1) + "=" + maxBuzonesServidores[i] + "/" + configuracion.getTam2();
        }
        lista.add(check("La ocupacion maxima de buzones de servidores respeto tam2", servidoresOk, detalle));
        return lista;
    }

    private String check(String mensaje, boolean ok, String detalle) {
        return (ok ? "[OK] " : "[ERROR] ") + mensaje + " -> " + detalle;
    }

    public String generarSalidaConsola() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== RESULTADO SIMULACION CASO 3 ===\n");
        sb.append(configuracion).append("\n\n");
        sb.append("--- Resumen cuantitativo ---\n");
        sb.append("Eventos generados: ").append(estadisticas.getEventosGenerados()).append("\n");
        sb.append("Broker procesados: ").append(estadisticas.getEventosProcesadosBroker()).append("\n");
        sb.append("Enviados a alertas: ").append(estadisticas.getEventosEnviadosAlertas()).append("\n");
        sb.append("Enviados a clasificacion por broker: ").append(estadisticas.getEventosEnviadosClasificacionPorBroker()).append("\n");
        sb.append("Alertas aprobadas por administrador: ").append(estadisticas.getAlertasAprobadasPorAdministrador()).append("\n");
        sb.append("Alertas descartadas por administrador: ").append(estadisticas.getAlertasDescartadasPorAdministrador()).append("\n");
        sb.append("Eventos clasificados: ").append(estadisticas.getEventosClasificados()).append("\n");
        sb.append("Eventos procesados por servidores: ").append(estadisticas.getEventosProcesadosServidores()).append("\n");
        sb.append("Fines a clasificadores: ").append(estadisticas.getEventosFinClasificadores()).append("\n");
        sb.append("Fines a servidores: ").append(estadisticas.getEventosFinServidores()).append("\n");
        sb.append("Max entrada: ").append(maxEntrada).append("\n");
        sb.append("Max alertas: ").append(maxAlertas).append("\n");
        sb.append("Max clasificacion: ").append(maxClasificacion).append("\n");
        for (int i = 0; i < maxBuzonesServidores.length; i++) {
            sb.append("Max buzon servidor ").append(i + 1).append(": ").append(maxBuzonesServidores[i]).append("\n");
        }
        sb.append("\n--- Validaciones ---\n");
        for (String validacion : validaciones) {
            sb.append(validacion).append("\n");
        }
        return sb.toString();
    }

    public List<String> getValidaciones() {
        return validaciones;
    }
}
