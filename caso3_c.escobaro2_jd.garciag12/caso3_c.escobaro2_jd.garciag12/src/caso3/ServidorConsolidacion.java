package caso3;

import java.util.concurrent.CyclicBarrier;

public class ServidorConsolidacion extends ActorBase {
    private final BuzonLimitado<Evento> buzon;
    private final EstadisticasSimulacion estadisticas;

    public ServidorConsolidacion(int id,
                                 BuzonLimitado<Evento> buzon,
                                 EstadisticasSimulacion estadisticas,
                                 CyclicBarrier barreraInicio) {
        super("Servidor-" + id, barreraInicio);
        this.buzon = buzon;
        this.estadisticas = estadisticas;
    }

    @Override
    protected void ejecutarActor() throws InterruptedException {
        while (true) {
            Evento evento = buzon.retirarPasivo();
            if (evento.esFin()) {
                return;
            }
            int pausa = 100 + random.nextInt(901);
            Thread.sleep(pausa);
            estadisticas.registrarEventoProcesadoServidor();
        }
    }
}
