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
    private static final int SERVER_PORT = 59001;
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
            System.out.println("\n" + recibido);

            JuegoCartas juego = new JuegoCartas();
            List<Carta> mano = juego.repartirCartaJugador();
            System.out.println("\nTus cartas son: ");
            for (Carta elemento : mano) {
                System.out.println(elemento);
            }

            int valorManoActual = juego.valorMano(mano);

            boolean salir = menu1(juego, mano, teclado, valorManoActual, salidaSocket);

            if (!salir){
                menu2(juego, mano, teclado, valorManoActual, salidaSocket);
            }

            s.close();

        } catch (IOException e) {
            System.out.println("Exception: " + e);
            System.out.println("Message: " + e.getMessage());
            e.printStackTrace(System.err);
        }
    }

    private static boolean menu1(JuegoCartas juego, List<Carta> mano, BufferedReader teclado, int valorManoActual, PrintWriter salidaSocket) {
        boolean salir = false;
        try {
            System.out.println("Valor de la mano: " + juego.valorMano(mano));
            System.out.println("\n1. Pedir otra carta");
            System.out.println("2. Doblar apuesta"); // Solo se puede doblar al principio
            System.out.println("3. Plantarse"); // Que no vuelva a salir el menú cuando me planto --> comparo con el crupier
            System.out.println("4. Salir de la mesa");
            System.out.println("Elija una opcion: ");

            String opcion = teclado.readLine();

            if ("1".equals(opcion)) {
                juego.pedirCarta();
                valorManoActual = juego.valorMano(mano);
            } else if ("2".equals(opcion)) {
                System.out.println("Se ha doblado tu apuesta");
            } else if ("3".equals(opcion)) {
                System.out.println("Te has plantado con " + juego.valorMano(mano));
                salir = true;
            } else if ("4".equals(opcion)) {
                salir = true;
            } else {
                System.out.println("Opcion incorrecta");
            }
        } catch (IOException e) {
            System.out.println("Exception: " + e);
            System.out.println("Message: " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return salir;
    }

    private static void menu2(JuegoCartas juego, List<Carta> mano, BufferedReader teclado, int valorManoActual, PrintWriter salidaSocket) {
        try {
            boolean plantarse = false;
            do {
                System.out.println("Valor de la mano: " + juego.valorMano(mano));
                System.out.println("\n1. Pedir otra carta");
                System.out.println("2. Plantarse"); // Que no vuelva a salir el menú cuando me planto --> comparo con el crupier
                System.out.println("3. Salir de la mesa");
                System.out.println("Elija una opcion: ");

                String opcion = teclado.readLine();

                if ("1".equals(opcion)) {
                    juego.pedirCarta();
                    valorManoActual = juego.valorMano(mano);
                } else if ("2".equals(opcion)) {
                    System.out.println("Te has plantado con " + juego.valorMano(mano));
                    plantarse = true;
                } else if ("3".equals(opcion)) {
                    salidaSocket.println("/quit");
                    break;
                } else {
                    System.out.println("Opcion incorrecta");
                }

            } while (valorManoActual <= 21 && !plantarse);
            if (valorManoActual > 21) {
                System.out.println("Te has pasado de 21, has perdido tu apuesta!");
            }
        } catch (IOException e) {
            System.out.println("Exception: " + e);
            System.out.println("Message: " + e.getMessage());
            e.printStackTrace(System.err);

        }
    }
}
