package caso3;

import java.util.List;

public class CoordinadorFinClasificadores {
    private int clasificadoresActivos;

    public CoordinadorFinClasificadores(int clasificadoresActivosIniciales) {
        this.clasificadoresActivos = clasificadoresActivosIniciales;
    }

    public synchronized boolean registrarFinalizacionYVerificarSiEsElUltimo() {
        clasificadoresActivos--;
        return clasificadoresActivos == 0;
    }

    public void enviarEventosFinAServidores(List<BuzonLimitado<Evento>> buzonesServidores,
                                            EstadisticasSimulacion estadisticas) {
        for (int i = 0; i < buzonesServidores.size(); i++) {
            buzonesServidores.get(i).depositarSemiActivo(Evento.fin("CLASIFICADOR-FINAL-" + (i + 1)));
            estadisticas.registrarFinServidor();
        }
    }
}
