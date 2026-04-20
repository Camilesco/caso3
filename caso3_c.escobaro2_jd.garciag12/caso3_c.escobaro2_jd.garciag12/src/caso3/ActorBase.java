package caso3;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public abstract class ActorBase extends Thread {
    protected final Random random;
    private final CyclicBarrier barreraInicio;

    protected ActorBase(String nombre, CyclicBarrier barreraInicio) {
        super(nombre);
        this.barreraInicio = barreraInicio;
        this.random = new Random();
    }

    @Override
    public final void run() {
        try {
            barreraInicio.await();
            ejecutarActor();
        } catch (InterruptedException e) {
            interrupt();
            System.err.println(getName() + " fue interrumpido");
        } catch (BrokenBarrierException e) {
            System.err.println("La barrera de inicio se rompio para " + getName());
        }
    }

    protected abstract void ejecutarActor() throws InterruptedException;
}
