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
        try (ServerSocket listener = new ServerSocket(59002)) {
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

                // Pedimos al cliente que introduzca la apuesta:
                out.println("Introduzca la cantidad que quieres apostar: ");
                int apuesta = in.nextInt();
                System.out.println("Cantidad apostada por " + name + " es de " + apuesta);

                // Voy a meter la mano del crupier y la voy a mostrar en los usuario, no en el servidor:
                JuegoCartas juego = new JuegoCartas();
                List<Carta> manoCrupier = juego.repartirCartaJugador();
                out.println("El valor de la mano del crupier es " + juego.valorMano(manoCrupier));

                List<Carta> manoJugador = juego.repartirCartaJugador();
                int valorMano = juego.valorMano(manoJugador);
                // Enviar las cartas al jugador
                StringBuilder sb = new StringBuilder();
                sb.append("Tus cartas son: ");
                sb.append("\n");
                for (Carta carta : manoJugador) {
                    sb.append(carta).append("\n");
                }
                out.println(sb.toString());
                out.println(); //para que sepa que ha acabado en el cliente

                //enviamos el valor de la mano
                sb = new StringBuilder();
                out.println(sb.append(valorMano).toString());

//                -------------------------------------------------------------------- Hasta aquí bien
                // El servidor se encarga de enviar el menú al cliente
                String opcion = in.next();
                boolean[] resultados = new boolean[2];
                boolean salirMesa = false;
                boolean plantarse = false;

                //leemos las opciones del jugador y las mostramos en el servidor
                System.out.println("Opcion recibida del jugador " + name + ": " + opcion);
                resultados = menu1(opcion, out, juego, manoJugador, valorMano);
                plantarse = resultados[0];
                salirMesa = resultados[1];

                while (!salirMesa) { // El primer menú se ejecuta sí o sí, el segundo depende de si se ha plantado o salido de la mesas
                    if (plantarse) {
                        // Aquí el código por si se planta
                    } else if (!salirMesa) {
                        opcion = in.next();
                        System.out.println("Opcion recibida del jugador " + name + ": " + opcion);
                        resultados = menu2(opcion, out, juego, manoJugador, valorMano);
                        plantarse = resultados[0];
                        salirMesa = resultados[1];
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

    // ESTO EN NINGÚN MOMENTO SE EJECUTA, SE EJECUTAN LOS MENUS QUE ESTÁN DENTRO DEL CLIENTE
    private static boolean[] menu1(String opcion, PrintWriter out, JuegoCartas juego, List<Carta> manoJugador, int valorManoActual) {
        StringBuilder sb = new StringBuilder();
        boolean plantarse = false;
        boolean salir = false;

        if ("1".equals(opcion)) {
            Carta cartaNueva = juego.pedirCarta(manoJugador);
            valorManoActual = juego.valorMano(manoJugador);
            sb.append("Tu carta es ");
            sb.append(cartaNueva).append("\n");
            sb.append("Ahora el valor de tu mano es " + Integer.toString(valorManoActual));

        } else if ("2".equals(opcion)) {
            sb.append("Se ha doblado tu apuesta");
        } else if ("3".equals(opcion)) {
            sb.append("Te has plantado con " + valorManoActual);
            plantarse = true;
        } else if ("4".equals(opcion)) { //que meter aqui
            salir = true;
        } else {
            sb.append("Opcion incorrecta");
        }

        if (valorManoActual > 21) {
            sb.append("\n Te has pasado de 21, has perdido tu apuesta!");
            salir = true;
        }

        out.println(sb.toString());
        out.println();

        boolean[] resultados = new boolean[2];
        resultados[0] = plantarse;
        resultados[1] = salir;
        return resultados;
    }

    private static boolean[] menu2(String opcion, PrintWriter out, JuegoCartas juego, List<Carta> manoJugador, int valorManoActual) {
        StringBuilder sb = new StringBuilder();
        boolean plantarse = false;
        boolean salir = false;

        if ("1".equals(opcion)) {
            Carta cartaNueva = juego.pedirCarta(manoJugador);
            valorManoActual = juego.valorMano(manoJugador);
            sb.append("Tu carta es ");
            sb.append(cartaNueva).append("\n");
            sb.append("Ahora el valor de tu mano es " + Integer.toString(valorManoActual));

        } else if ("2".equals(opcion)) {
            sb.append("Te has plantado con " + valorManoActual);
            plantarse = true;
        } else if ("3".equals(opcion)) {
            salir = true;
        } else {
            sb.append("Opcion incorrecta");
        }

        if (valorManoActual > 21) {
            sb.append("\n Te has pasado de 21, has perdido tu apuesta!");
            salir = true;
        }

        out.println(sb.toString());
        out.println();

        boolean[] resultados = new boolean[2];
        resultados[0] = plantarse;
        resultados[1] = salir;
        return resultados;
    }

//    private static StringBuilder menu2(String opcion, JuegoCartas juego, List<Carta> manoJugador, int valorManoActual) {
//        StringBuilder sb = new StringBuilder();
//        boolean salir = false;
//        boolean plantarse = false;
//
//        if ("1".equals(opcion)) {
//            Carta cartaNueva = juego.pedirCarta(manoJugador);
//            valorManoActual = juego.valorMano(manoJugador);
//            sb.append("Tu carta es ");
//            sb.append(cartaNueva).append("\n");
//            sb.append("Ahora el valor de tu mano es " + Integer.toString(valorManoActual));
//        } else if ("2".equals(opcion)) {
//            sb.append("Te has plantado con " + valorManoActual);
////            salir = true;
//        } else if ("3".equals(opcion)) {
////            return;
//        } else {
//            sb.append("Opcion incorrecta");
//        }
//
//        if (valorManoActual > 21) {
//            sb.append("\n Te has pasado de 21, has perdido tu apuesta!");
//
//        }
//        return sb;
//    }
}
