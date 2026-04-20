package caso3;

public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Uso: java caso3.Main <ruta-config>");
            System.exit(1);
        }

        try {
            ConfiguracionSimulacion config = ConfiguracionSimulacion.cargar(args[0]);
            Simulacion simulacion = new Simulacion(config);
            ResultadoSimulacion resultado = simulacion.ejecutar();
            System.out.println(resultado.generarSalidaConsola());
        } catch (Exception e) {
            System.err.println("Error ejecutando la simulacion: " + e.getMessage());
            e.printStackTrace();
            System.exit(2);
        }
    }
}
