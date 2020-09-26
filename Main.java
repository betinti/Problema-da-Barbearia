package br.pucpr;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class Main {

    //TODO: Fila de Espera
    public static int maxFila = 4;
    public static Semaphore[] filaDeEspera = new Semaphore[maxFila];
    public static int contFila = -1;
    public static int primeiroFila = 0;
    public static int ultimoFila = 0;


    public static Semaphore mutex = new Semaphore(1);

    public static Semaphore corteConcluido = new Semaphore(0);
    public static Semaphore clienteSatisfeito = new Semaphore(0);

    public static Semaphore cliente = new Semaphore(0);

    public static void main(String[] args) {

        Random r = new Random();
        int cont = 0;
        int minSleep = 1000;
        int maxSleep = 2000;

        Barbeiro b = new Barbeiro(minSleep, maxSleep);

        try {

            b.start();

            while(true){
                new Cliente(cont++, minSleep, maxSleep).start();
                Thread.sleep(r.nextInt(1500));
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
