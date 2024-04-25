/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.mycompany.p21_blackjack_final;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author alumno
 */
public class CrupierServidor {

    private static Set<String> names = new HashSet<>();
    // El conjunto de todos los print writers de los clientes,
    // empleados para hacer la difusión/broadcast.
    private static Set<PrintWriter> writers = new HashSet<>();

    public static void main(String[] args) throws Exception {
        System.out.println("The blackjack's server is running...");
        ExecutorService pool = Executors.newFixedThreadPool(500);
        try (ServerSocket listener = new ServerSocket(59001)) {
            while (true) {
                pool.execute(new Handler(listener.accept()));
            }
        }
        /**
         * El gestor de la interacción con el cliente. Definido como una clase
         * privada
         */
    }

    private static class Handler implements Runnable {

        private String name;
        private Socket socket;
        private Scanner in;
        private PrintWriter out;

        /**
         * Construye el hilo gestor, le pasa el socket de comunicación con el
         * cliente. Todo el trabajo se realiza en el método run. Recuerda que se
         * llama desde el método main servidor, así que debe ser lo más simple
         * posible.
         */
        public Handler(Socket socket) {
            this.socket = socket;
        }

        /**
         * Atiende al cliente de este hilo solicitando repetidamente un nombre
         * de usuario hasta que sea único, luego reconoce el nombre y lo
         * registra, así como el flujo de salida (PrintWriter) en un conjunto
         * global. Luego recibe texto de los clientes y lo difunde por estos
         * flujos de salida
         */
        public void run() {
            try {
                in = new Scanner(socket.getInputStream());
                out = new PrintWriter(socket.getOutputStream(), true);
                while (true) {
                    out.println("SUBMITNAME");
                    name = in.nextLine();
                    if (name == null) {
                        return;
                    }
                    synchronized (names) {
                        if (!name.isBlank() && !names.contains(name)) {
                            names.add(name);
                            break;
                        }
                    }
                }
                // Ya se ha incorporado un nuevo usuario, lo añadimos al conjunto de
                // nombre. Pero antes de añadirlo mandamos un mensaje a todos los usuarios
                // que un nuevo usuario se ha añadido al sistema
                out.println("NAMEACCEPTED " + name);
                System.out.println(name + " se ha unido a la mesa"); // --> Indico quién se ha unido a la mesa

                // Voy a meter la mano del crupier y la voy a mostrar en los usuario, no en el servidor:
                JuegoCartas juegoCrupier = new JuegoCartas();
                List<Carta> manoCrupier = juegoCrupier.repartirCartaJugador();
                out.println("\nEl valor de la mano del crupier es " + juegoCrupier.valorMano(manoCrupier));

                writers.add(out);
                // Acepta todos los mensajes de este cliente y los difunde.

                //cambiar
                while (true) {
                    String input = in.nextLine();
                    if (input.toLowerCase().startsWith("/quit")) {
                        return;
                    }
                }
            } catch (Exception e) {
                System.out.println("Exception: " + e);
                System.out.println("Message: " + e.getMessage());
                e.printStackTrace(System.err);
            } finally {
                if (out != null) {
                    writers.remove(out);
                }
                if (name != null) {
                    System.out.println(name + " se ha ido de la mesa");
                    names.remove(name);
                    for (PrintWriter writer : writers) {
                        writer.println("MESSAGE " + name + " has left");
                    }
                }
                try {
                    socket.close();
                } catch (IOException e) {
                    System.out.println("Exception: " + e);
                    System.out.println("Message: " + e.getMessage());
                    e.printStackTrace(System.err);
                }
            }
        }
    }

}
