package caso3;

public class EstadisticasSimulacion {
    private int eventosGenerados;
    private int eventosProcesadosBroker;
    private int eventosEnviadosAlertas;
    private int eventosEnviadosClasificacionPorBroker;
    private int alertasAprobadasPorAdministrador;
    private int alertasDescartadasPorAdministrador;
    private int eventosClasificados;
    private int eventosProcesadosServidores;
    private int eventosFinClasificadores;
    private int eventosFinServidores;

    public synchronized void registrarEventoGenerado() { eventosGenerados++; }
    public synchronized void registrarEventoProcesadoBroker() { eventosProcesadosBroker++; }
    public synchronized void registrarEventoAlerta() { eventosEnviadosAlertas++; }
    public synchronized void registrarEventoClasificacionBroker() { eventosEnviadosClasificacionPorBroker++; }
    public synchronized void registrarAlertaAprobada() { alertasAprobadasPorAdministrador++; }
    public synchronized void registrarAlertaDescartada() { alertasDescartadasPorAdministrador++; }
    public synchronized void registrarEventoClasificado() { eventosClasificados++; }
    public synchronized void registrarEventoProcesadoServidor() { eventosProcesadosServidores++; }
    public synchronized void registrarFinClasificador() { eventosFinClasificadores++; }
    public synchronized void registrarFinServidor() { eventosFinServidores++; }

    public synchronized int getEventosGenerados() { return eventosGenerados; }
    public synchronized int getEventosProcesadosBroker() { return eventosProcesadosBroker; }
    public synchronized int getEventosEnviadosAlertas() { return eventosEnviadosAlertas; }
    public synchronized int getEventosEnviadosClasificacionPorBroker() { return eventosEnviadosClasificacionPorBroker; }
    public synchronized int getAlertasAprobadasPorAdministrador() { return alertasAprobadasPorAdministrador; }
    public synchronized int getAlertasDescartadasPorAdministrador() { return alertasDescartadasPorAdministrador; }
    public synchronized int getEventosClasificados() { return eventosClasificados; }
    public synchronized int getEventosProcesadosServidores() { return eventosProcesadosServidores; }
    public synchronized int getEventosFinClasificadores() { return eventosFinClasificadores; }
    public synchronized int getEventosFinServidores() { return eventosFinServidores; }
}
