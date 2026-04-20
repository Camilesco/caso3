package caso3;

import java.util.concurrent.CyclicBarrier;

public class Sensor extends ActorBase {
    private final int idSensor;
    private final int eventosAProducir;
    private final int cantidadServidores;
    private final BuzonIlimitado<Evento> buzonEntrada;
    private final EstadisticasSimulacion estadisticas;

    public Sensor(int idSensor,
                  int eventosAProducir,
                  int cantidadServidores,
                  BuzonIlimitado<Evento> buzonEntrada,
                  EstadisticasSimulacion estadisticas,
                  CyclicBarrier barreraInicio) {
        super("Sensor-" + idSensor, barreraInicio);
        this.idSensor = idSensor;
        this.eventosAProducir = eventosAProducir;
        this.cantidadServidores = cantidadServidores;
        this.buzonEntrada = buzonEntrada;
        this.estadisticas = estadisticas;
    }

    @Override
    protected void ejecutarActor() {
        for (int i = 1; i <= eventosAProducir; i++) {
            int destino = random.nextInt(cantidadServidores) + 1;
            Evento evento = Evento.normal(idSensor, i, destino);
            buzonEntrada.depositar(evento);
            estadisticas.registrarEventoGenerado();
            Thread.yield();
        }
    }
}
