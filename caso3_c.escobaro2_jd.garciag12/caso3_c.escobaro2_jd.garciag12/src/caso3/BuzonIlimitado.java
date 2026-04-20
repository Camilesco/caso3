package caso3;

import java.util.LinkedList;
import java.util.Queue;

public class BuzonIlimitado<T> {
    private final Queue<T> cola;
    private int maximoObservado;

    public BuzonIlimitado() {
        this.cola = new LinkedList<T>();
        this.maximoObservado = 0;
    }

    public synchronized void depositar(T elemento) {
        cola.offer(elemento);
        if (cola.size() > maximoObservado) {
            maximoObservado = cola.size();
        }
        notifyAll();
    }

    public synchronized T retirar() throws InterruptedException {
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

    public synchronized int getMaximoObservado() {
        return maximoObservado;
    }
}
