package caso3;

import java.util.concurrent.CyclicBarrier;

public class Administrador extends ActorBase {
    private final int cantidadClasificadores;
    private final BuzonIlimitado<Evento> alertas;
    private final BuzonLimitado<Evento> clasificacion;
    private final EstadisticasSimulacion estadisticas;

    public Administrador(int cantidadClasificadores,
                         BuzonIlimitado<Evento> alertas,
                         BuzonLimitado<Evento> clasificacion,
                         EstadisticasSimulacion estadisticas,
                         CyclicBarrier barreraInicio) {
        super("Administrador", barreraInicio);
        this.cantidadClasificadores = cantidadClasificadores;
        this.alertas = alertas;
        this.clasificacion = clasificacion;
        this.estadisticas = estadisticas;
    }

    @Override
    protected void ejecutarActor() throws InterruptedException {
        while (true) {
            Evento evento = alertas.retirar();
            if (evento.esFin()) {
                for (int i = 0; i < cantidadClasificadores; i++) {
                    clasificacion.depositarSemiActivo(Evento.fin("ADMIN-CLASIF-" + (i + 1)));
                    estadisticas.registrarFinClasificador();
                }
                return;
            }

            int numero = random.nextInt(21);
            if (numero % 4 == 0) {
                clasificacion.depositarSemiActivo(evento);
                estadisticas.registrarAlertaAprobada();
            } else {
                estadisticas.registrarAlertaDescartada();
            }
        }
    }
}
