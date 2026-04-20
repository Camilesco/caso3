package caso3;

import java.util.LinkedList;
import java.util.Queue;

public class BuzonLimitado<T> {
    private final Queue<T> cola;
    private final int capacidad;
    private int maximoObservado;

    public BuzonLimitado(int capacidad) {
        if (capacidad <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser positiva");
        }
        this.capacidad = capacidad;
        this.cola = new LinkedList<T>();
        this.maximoObservado = 0;
    }

    public void depositarSemiActivo(T elemento) {
        boolean agregado = false;
        while (!agregado) {
            synchronized (this) {
                if (cola.size() < capacidad) {
                    cola.offer(elemento);
                    if (cola.size() > maximoObservado) {
                        maximoObservado = cola.size();
                    }
                    agregado = true;
                    notifyAll();
                }
            }
            if (!agregado) {
                Thread.yield();
            }
        }
    }

    public synchronized T retirarPasivo() throws InterruptedException {
        while (cola.isEmpty()) {
            wait();
        }
        T elemento = cola.poll();
        notifyAll();
        return elemento;
    }

    public synchronized boolean estaVacio() {
        return cola.isEmpty();
    }

    public synchronized int getCapacidad() {
        return capacidad;
    }

    public synchronized int getMaximoObservado() {
        return maximoObservado;
    }
}
