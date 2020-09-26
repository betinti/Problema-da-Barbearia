package br.pucpr;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class Cliente extends Thread{

    private final int minSleep;
    private final int maxSleep;
    private int cont;

    public Cliente(int cont, int minSleep, int maxSleep) {
        this.minSleep = minSleep;
        this.maxSleep = maxSleep;
        this.cont = cont;
    }

    private void terCabeloCortado() throws InterruptedException {
        Thread.sleep(new Random().nextInt(maxSleep-minSleep) +minSleep);
    }

    private void desistir(){
        System.out.println("Cliente (" + this.cont +  "): Desistir");
    }

    @Override
    public void run() {
        try {
            Thread.sleep(new Random().nextInt(maxSleep -minSleep) +minSleep);
            System.out.println("Cliente (" + this.cont +  "): chega");
            Semaphore mem = new Semaphore(0);

            Main.mutex.acquire();
                if (Main.contFila < Main.maxFila)
                {
                    Main.contFila = Main.contFila+1;
                    Main.ultimoFila = Main.ultimoFila +1;
                    if (Main.ultimoFila >= Main.maxFila) Main.ultimoFila = 0;
                    Main.filaDeEspera[Main.ultimoFila] = mem;
                    System.out.println("Cliente (" + this.cont +  "): Tenta entrar na fila");
                    Main.mutex.release();
                } else
                    {
                    desistir();
                    Main.mutex.release();
                    //desistir
                }

            Main.cliente.release();             //cliente.sinalizar();
            mem.acquire();                      //barbeiro.esperar();

            System.out.println("Cliente (" + this.cont +  "): Inicia o corte de cabelo");

            terCabeloCortado();

            System.out.println("Cliente (" + this.cont +  "): Finaliza o corte de cabelo");

            Main.clienteSatisfeito.release();   //clienteSatisfeito.sinalizar();
            Main.corteConcluido.acquire();      //corteConcluido.esperar();

            Main.mutex.acquire();

                System.out.println("Cliente (" + this.cont +  "): Finaliza o corte");
                Main.contFila = Main.contFila-1;

            Main.mutex.release();

            System.out.println("Cliente (" + this.cont +  "): Vai em bora");
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
