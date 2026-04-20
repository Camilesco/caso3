package caso3;

import java.util.List;
import java.util.concurrent.CyclicBarrier;

public class Clasificador extends ActorBase {
    private final BuzonLimitado<Evento> clasificacion;
    private final List<BuzonLimitado<Evento>> buzonesServidores;
    private final CoordinadorFinClasificadores coordinador;
    private final EstadisticasSimulacion estadisticas;

    public Clasificador(int id,
                        BuzonLimitado<Evento> clasificacion,
                        List<BuzonLimitado<Evento>> buzonesServidores,
                        CoordinadorFinClasificadores coordinador,
                        EstadisticasSimulacion estadisticas,
                        CyclicBarrier barreraInicio) {
        super("Clasificador-" + id, barreraInicio);
        this.clasificacion = clasificacion;
        this.buzonesServidores = buzonesServidores;
        this.coordinador = coordinador;
        this.estadisticas = estadisticas;
    }

    @Override
    protected void ejecutarActor() throws InterruptedException {
        while (true) {
            Evento evento = clasificacion.retirarPasivo();
            if (evento.esFin()) {
                boolean ultimo = coordinador.registrarFinalizacionYVerificarSiEsElUltimo();
                if (ultimo) {
                    coordinador.enviarEventosFinAServidores(buzonesServidores, estadisticas);
                }
                return;
            }
            int indiceServidor = evento.getDestinoServidor() - 1;
            buzonesServidores.get(indiceServidor).depositarSemiActivo(evento);
            estadisticas.registrarEventoClasificado();
        }
    }
}
