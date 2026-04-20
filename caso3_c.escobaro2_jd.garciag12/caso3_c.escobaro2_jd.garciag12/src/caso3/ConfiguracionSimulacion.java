package caso3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ConfiguracionSimulacion {
    private final int ni;
    private final int baseEventos;
    private final int nc;
    private final int ns;
    private final int tam1;
    private final int tam2;

    public ConfiguracionSimulacion(int ni, int baseEventos, int nc, int ns, int tam1, int tam2) {
        validarPositivo("ni", ni);
        validarPositivo("baseEventos", baseEventos);
        validarPositivo("nc", nc);
        validarPositivo("ns", ns);
        validarPositivo("tam1", tam1);
        validarPositivo("tam2", tam2);
        this.ni = ni;
        this.baseEventos = baseEventos;
        this.nc = nc;
        this.ns = ns;
        this.tam1 = tam1;
        this.tam2 = tam2;
    }

    public static ConfiguracionSimulacion cargar(String ruta) throws IOException {
        Map<String, Integer> valores = new HashMap<String, Integer>();
        BufferedReader br = new BufferedReader(new FileReader(ruta));
        try {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty() || linea.startsWith("#")) {
                    continue;
                }
                String[] partes = linea.split("=");
                if (partes.length != 2) {
                    throw new IllegalArgumentException("Linea invalida en configuracion: " + linea);
                }
                String clave = partes[0].trim();
                int valor = Integer.parseInt(partes[1].trim());
                valores.put(clave, valor);
            }
        } finally {
            br.close();
        }

        return new ConfiguracionSimulacion(
            getRequerido(valores, "ni"),
            getRequerido(valores, "baseEventos"),
            getRequerido(valores, "nc"),
            getRequerido(valores, "ns"),
            getRequerido(valores, "tam1"),
            getRequerido(valores, "tam2")
        );
    }

    private static int getRequerido(Map<String, Integer> valores, String clave) {
        Integer valor = valores.get(clave);
        if (valor == null) {
            throw new IllegalArgumentException("Falta el parametro: " + clave);
        }
        return valor.intValue();
    }

    private static void validarPositivo(String nombre, int valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException("El parametro " + nombre + " debe ser positivo");
        }
    }

    public int getNi() {
        return ni;
    }

    public int getBaseEventos() {
        return baseEventos;
    }

    public int getNc() {
        return nc;
    }

    public int getNs() {
        return ns;
    }

    public int getTam1() {
        return tam1;
    }

    public int getTam2() {
        return tam2;
    }

    public int getTotalEventosEsperados() {
        int total = 0;
        for (int i = 1; i <= ni; i++) {
            total += baseEventos * i;
        }
        return total;
    }

    @Override
    public String toString() {
        return "Configuracion{" +
            "ni=" + ni +
            ", baseEventos=" + baseEventos +
            ", nc=" + nc +
            ", ns=" + ns +
            ", tam1=" + tam1 +
            ", tam2=" + tam2 +
            ", totalEsperado=" + getTotalEventosEsperados() +
            '}';
    }
}
