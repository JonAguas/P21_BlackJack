/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.mycompany.p21_blackjack_final;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

/**
 *
 * @author alumno
 */
public class JugadorCliente {

    private static final String SERVER_ADREES = "127.0.0.1";
    private static final int SERVER_PORT = 59002;
    private static String recibido;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            Socket s = new Socket(SERVER_ADREES, SERVER_PORT);
            BufferedReader entradaSocket = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter salidaSocket = new PrintWriter(s.getOutputStream(), true);
            BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                recibido = entradaSocket.readLine();
                if (recibido.startsWith("NAMEACCEPTED")) {
                    break;
                } else {
                    System.out.println("Introduce tu nombre: ");
                    String nombre = teclado.readLine();
                    salidaSocket.println(nombre);
                }
            }

            // Introducimos la apuesta: 
            recibido = entradaSocket.readLine();
            System.out.println(recibido);
            try {
                int apuestaInicial = Integer.parseInt(teclado.readLine());
                salidaSocket.println(apuestaInicial);
            } catch (NumberFormatException e) {
                System.out.println("VALOR INCORRECTO");
                System.out.println("Vuelva a introducir un valor correcto para la apuesta: ");
                int apuestaInicial = Integer.parseInt(teclado.readLine());
                salidaSocket.println(apuestaInicial);
            }

            // Mostramos la mano del crupier:
            recibido = entradaSocket.readLine();
            System.out.println("\n" + recibido + "\n");

            //mostrar la mano del jugador
            while (!(recibido = entradaSocket.readLine()).isEmpty()) {
                System.out.println(recibido);
            }

            //mostrar valor de la mano
            entradaSocket.read(); // --> Sino el valor de la mano se me imprimia tarde
            String valorMano = entradaSocket.readLine();
            System.out.println("El valor de tus cartas es de " + valorMano);

            // Mostrar el menu1 desde el servidor:
            System.out.println("\n1. Pedir otra carta");
            System.out.println("2. Doblar apuesta"); // Solo se puede doblar al principio
            System.out.println("3. Plantarse"); // Que no vuelva a salir el menú cuando me planto --> comparo con el crupier
            System.out.println("4. Salir de la mesa");
            System.out.println("Elija una opcion: ");

            String opcion1 = teclado.readLine();
            salidaSocket.println(opcion1);

            boolean salir = false;
            boolean plantarse = false;
            recibido = entradaSocket.readLine();
            if (recibido.startsWith("Has salido de la mesa")) {
                System.out.println(recibido);
                salir = true;
            } else if (recibido.startsWith("Te has plantado")) {
                System.out.println(recibido);
                plantarse = true;
            } else {
                System.out.println(recibido);
                while (!(recibido = entradaSocket.readLine()).isEmpty()) {
                    if (recibido.startsWith("Te has pasado de 21")) {
                        salir = true;
                    }
                    System.out.println(recibido);
                }
            }

            // Mostrar el menu2 desde el servidor:
            while (!salir && !plantarse) {
                System.out.println("\n1. Pedir otra carta");
                System.out.println("2. Plantarse"); // Que no vuelva a salir el menú cuando me planto --> comparo con el crupier
                System.out.println("3. Salir de la mesa");
                System.out.println("Elija una opcion: ");

                String opcion2 = teclado.readLine();
                salidaSocket.println(opcion2);

                recibido = entradaSocket.readLine();
                if (recibido.startsWith("Has salido de la mesa")) {
                    System.out.println(recibido);
                    salir = true;
                } else if (recibido.startsWith("Te has plantado")) {
                    System.out.println(recibido);
                    plantarse = true;
                } else {
                    System.out.println(recibido);
                    while (!(recibido = entradaSocket.readLine()).isEmpty()) {
                        if (recibido.startsWith("Te has pasado de 21")) {
                            salir = true;
                        }
                        System.out.println(recibido);
                    }
                }
            }

            //compara con el crupier si ha ganado o ha perdido
            if (plantarse) {
                recibido = entradaSocket.readLine();
                System.out.println(recibido);
                while (!(recibido = entradaSocket.readLine()).isEmpty()) {
                    System.out.println(recibido);
                }
            }
            s.close();

        } catch (IOException e) {
            System.out.println("Exception: " + e);
            System.out.println("Message: " + e.getMessage());
            e.printStackTrace(System.err);
        }
    }
}
