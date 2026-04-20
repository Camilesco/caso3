package caso3;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

public class Simulacion {
    private final ConfiguracionSimulacion config;

    public Simulacion(ConfiguracionSimulacion config) {
        this.config = config;
    }

    public ResultadoSimulacion ejecutar() throws InterruptedException {
        BuzonIlimitado<Evento> entrada = new BuzonIlimitado<Evento>();
        BuzonIlimitado<Evento> alertas = new BuzonIlimitado<Evento>();
        BuzonLimitado<Evento> clasificacion = new BuzonLimitado<Evento>(config.getTam1());
        List<BuzonLimitado<Evento>> buzonesServidores = new ArrayList<BuzonLimitado<Evento>>();
        for (int i = 0; i < config.getNs(); i++) {
            buzonesServidores.add(new BuzonLimitado<Evento>(config.getTam2()));
        }

        EstadisticasSimulacion estadisticas = new EstadisticasSimulacion();
        CoordinadorFinClasificadores coordinador = new CoordinadorFinClasificadores(config.getNc());

        int totalActores = config.getNi() + 1 + 1 + config.getNc() + config.getNs();
        CyclicBarrier barreraInicio = new CyclicBarrier(totalActores);
        List<Thread> hilos = new ArrayList<Thread>();

        for (int i = 1; i <= config.getNi(); i++) {
            int eventos = config.getBaseEventos() * i;
            hilos.add(new Sensor(i, eventos, config.getNs(), entrada, estadisticas, barreraInicio));
        }

        hilos.add(new BrokerAnalizador(config.getTotalEventosEsperados(), entrada, alertas, clasificacion, estadisticas, barreraInicio));
        hilos.add(new Administrador(config.getNc(), alertas, clasificacion, estadisticas, barreraInicio));

        for (int i = 1; i <= config.getNc(); i++) {
            hilos.add(new Clasificador(i, clasificacion, buzonesServidores, coordinador, estadisticas, barreraInicio));
        }

        for (int i = 1; i <= config.getNs(); i++) {
            hilos.add(new ServidorConsolidacion(i, buzonesServidores.get(i - 1), estadisticas, barreraInicio));
        }

        for (Thread hilo : hilos) {
            hilo.start();
        }
        for (Thread hilo : hilos) {
            hilo.join();
        }

        boolean buzonesVacios = entrada.estaVacio() && alertas.estaVacio() && clasificacion.estaVacio();
        int[] maxServidores = new int[config.getNs()];
        for (int i = 0; i < config.getNs(); i++) {
            buzonesVacios = buzonesVacios && buzonesServidores.get(i).estaVacio();
            maxServidores[i] = buzonesServidores.get(i).getMaximoObservado();
        }

        return new ResultadoSimulacion(
            config,
            estadisticas,
            buzonesVacios,
            entrada.getMaximoObservado(),
            alertas.getMaximoObservado(),
            clasificacion.getMaximoObservado(),
            maxServidores
        );
    }
}
