package br.pucpr;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class Barbeiro extends Thread{

    private final int minSleep;
    private final int maxSleep;

    private int contador = -1;

    Barbeiro(int minSleep, int maxSleep){
        this.minSleep = minSleep;
        this.maxSleep = maxSleep;
    }

    private void cortarCabelo() throws InterruptedException {
        Thread.sleep(new Random().nextInt(maxSleep-minSleep) +minSleep);
    }

    @Override
    public void run() {
        while (true)
        try {
            contador++;
            System.out.println("Barbeiro (" + contador + ") loop");

            Main.cliente.acquire();                 //cliente.espear();

            Main.mutex.acquire();

                Semaphore mem = Main.filaDeEspera[Main.primeiroFila];

            if (mem != null){
                Main.filaDeEspera[Main.primeiroFila] = null;
                Main.primeiroFila = Main.primeiroFila+1;
                if (Main.primeiroFila >= Main.maxFila) Main.primeiroFila = 0;
                Main.mutex.release();

                mem.release();                      //barbeiro.sinalizar();

                System.out.println("Barbeiro (" + contador + "): Inicia corte no proximo cliente");

                cortarCabelo();

                System.out.println("Barbeiro (" + contador + "): Finaliza corte no cliente atual");

                Main.clienteSatisfeito.acquire();   //clienteSatisfeito.esperar();
                Main.corteConcluido.release();      //corteCOncluido.sinalizar();
            }else {
                Main.mutex.release();
                System.out.println("DEU RUIM RUIM");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
