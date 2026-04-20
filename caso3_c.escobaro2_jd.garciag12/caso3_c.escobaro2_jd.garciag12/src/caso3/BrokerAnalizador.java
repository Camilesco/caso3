package caso3;

import java.util.concurrent.CyclicBarrier;

public class BrokerAnalizador extends ActorBase {
    private final int totalEsperado;
    private final BuzonIlimitado<Evento> entrada;
    private final BuzonIlimitado<Evento> alertas;
    private final BuzonLimitado<Evento> clasificacion;
    private final EstadisticasSimulacion estadisticas;

    public BrokerAnalizador(int totalEsperado,
                            BuzonIlimitado<Evento> entrada,
                            BuzonIlimitado<Evento> alertas,
                            BuzonLimitado<Evento> clasificacion,
                            EstadisticasSimulacion estadisticas,
                            CyclicBarrier barreraInicio) {
        super("Broker-Analizador", barreraInicio);
        this.totalEsperado = totalEsperado;
        this.entrada = entrada;
        this.alertas = alertas;
        this.clasificacion = clasificacion;
        this.estadisticas = estadisticas;
    }

    @Override
    protected void ejecutarActor() throws InterruptedException {
        for (int i = 0; i < totalEsperado; i++) {
            Evento evento = entrada.retirar();
            estadisticas.registrarEventoProcesadoBroker();
            int numero = random.nextInt(201);
            if (numero % 8 == 0) {
                alertas.depositar(evento);
                estadisticas.registrarEventoAlerta();
            } else {
                clasificacion.depositarSemiActivo(evento);
                estadisticas.registrarEventoClasificacionBroker();
            }
        }
        alertas.depositar(Evento.fin("BROKER"));
    }
}
