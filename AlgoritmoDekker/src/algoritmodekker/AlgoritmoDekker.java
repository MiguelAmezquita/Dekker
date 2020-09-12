/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algoritmodekker;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MigueL
 */
public class AlgoritmoDekker {
    // Las Variables son representacion del hilo que desea entrar ala seccion critica
    static volatile boolean proceso1 = false, proceso2 = false;
    // Se inicializo el turno en 1 para que el primero en iniciar la seccion critica fuera el Proceso 1
    static volatile int turno = 1;
    // se utilizo para inicializar los procesos
    static volatile boolean InicioProceso1 = true, InicioProceso2 = true;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new AlgoritmoDekker();
    }
    
    AlgoritmoDekker(){
        Thread proceso1 = new Proceso1();
        Thread proceso2 = new Proceso2();
        proceso1.start();
        proceso2.start();
            try {
                proceso1.join();
                proceso2.join();
            } catch (InterruptedException e) {
        }
    }
        
    // Clase que representa el Proceso Numero 1
    public class Proceso1 extends Thread {
        public void run() {
            Bloqueo();
        }
        public void Bloqueo(){
            while (InicioProceso1) {
                proceso1 = true;
                while (proceso2) {
                    if (turno == 2) {
                        proceso1 = false;
                        while (turno == 2) {
                            Thread.yield();
                        }
                        proceso1 = true;
                    }
                }
                System.out.println("Proceso: #1  / Estado: Inicio sección critica");
                pausa(2000);
                Desbloqueo(false,2);
            }
        }
        public void Desbloqueo(boolean flag, int Nturno) {
            System.out.println("Proceso: #1  / Estado: Fin sección Critica");
            turno = Nturno;
            proceso1 = flag;
        }
    }
    
    // Clase que representa el Proceso Numero 2
    public class Proceso2 extends Thread {
        public void run() {
            Bloqueo();
        }
        public void Bloqueo(){
            while (InicioProceso2) {
                proceso2 = true;
                while (proceso1) {
                    if (turno == 1) {
                        proceso2 = false;
                        while (turno == 1) {
                            Thread.yield();
                        }
                        proceso2 = true;
                    }
                }
                System.out.println("Proceso: #2  / Estado: Inicio sección critica");
                pausa(2000);
                Desbloqueo(false, 1);
            }
        }
        public void Desbloqueo(boolean flag, int Nturno) {
            System.out.println("Proceso: #2  / Estado: Fin sección Critica");
            turno = Nturno;
            proceso2 = flag;
        }
    }
    
    // Utilizado para pausar momentaneamente los mensajes en pantalla
    static void pausa(int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException ex) {
            Logger.getLogger(AlgoritmoDekker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
