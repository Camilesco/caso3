package caso3;

public class Evento {
    private final String id;
    private final int idSensor;
    private final int consecutivoSensor;
    private final int destinoServidor;
    private final boolean fin;

    private Evento(String id, int idSensor, int consecutivoSensor, int destinoServidor, boolean fin) {
        this.id = id;
        this.idSensor = idSensor;
        this.consecutivoSensor = consecutivoSensor;
        this.destinoServidor = destinoServidor;
        this.fin = fin;
    }

    public static Evento normal(int idSensor, int consecutivoSensor, int destinoServidor) {
        String id = "S" + idSensor + "-E" + consecutivoSensor;
        return new Evento(id, idSensor, consecutivoSensor, destinoServidor, false);
    }

    public static Evento fin(String origen) {
        return new Evento("FIN-" + origen, -1, -1, -1, true);
    }

    public String getId() {
        return id;
    }

    public int getIdSensor() {
        return idSensor;
    }

    public int getConsecutivoSensor() {
        return consecutivoSensor;
    }

    public int getDestinoServidor() {
        return destinoServidor;
    }

    public boolean esFin() {
        return fin;
    }

    @Override
    public String toString() {
        if (fin) {
            return id;
        }
        return id + "(sensor=" + idSensor + ", tipo/servidor=" + destinoServidor + ")";
    }
}
