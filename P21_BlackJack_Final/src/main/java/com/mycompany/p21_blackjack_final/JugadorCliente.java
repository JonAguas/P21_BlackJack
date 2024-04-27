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

            //mostrar mano jugador
            String recibido;
            while (!(recibido = entradaSocket.readLine()).isEmpty()) {
                System.out.println(recibido);
            }

            //mostrar valo mano
            int valorMano = Integer.parseInt(entradaSocket.readLine());
            System.out.println("El valor de tus cartas es de " + valorMano);

            boolean salir = menu1(teclado, entradaSocket, salidaSocket, valorMano);

            if (!salir) {
                menu2(teclado, entradaSocket, salidaSocket, valorMano);
            }
            s.close();

        } catch (IOException e) {
            System.out.println("Exception: " + e);
            System.out.println("Message: " + e.getMessage());
            e.printStackTrace(System.err);
        }
    }

    private static boolean menu1(BufferedReader teclado, BufferedReader entradaSocket, PrintWriter salidaSocket, int valorMano) {
        boolean salir = false;
        try {
            System.out.println("\n1. Pedir otra carta");
            System.out.println("2. Doblar apuesta"); // Solo se puede doblar al principio
            System.out.println("3. Plantarse"); // Que no vuelva a salir el menú cuando me planto --> comparo con el crupier
            System.out.println("4. Salir de la mesa");
            System.out.println("Elija una opcion: ");

            int opcion_int = Integer.parseInt(teclado.readLine());
            salidaSocket.println(opcion_int);
            String opcion = Integer.toString(opcion_int);

            if ("1".equals(opcion)) {
                String recibido;
                while (!(recibido = entradaSocket.readLine()).isEmpty()) {
                    System.out.println(recibido);
                }
            } else if ("2".equals(opcion)) {
                recibido = entradaSocket.readLine();
                System.out.println(recibido);
            } else if ("3".equals(opcion)) {
                recibido = entradaSocket.readLine();
                System.out.println(recibido);
                salir = true;
            } else if ("4".equals(opcion)) {
                salir = true;
            } else {
                recibido = entradaSocket.readLine();
                System.out.println(recibido);
            }

        } catch (IOException e) {
            System.out.println("Exception: " + e);
            System.out.println("Message: " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return salir;
    }

    private static void menu2(BufferedReader teclado, BufferedReader entradaSocket, PrintWriter salidaSocket, int valorMano) {
        try {
            boolean plantarse = false;
            do {
                System.out.println("\n1. Pedir otra carta");
                System.out.println("2. Plantarse"); // Que no vuelva a salir el menú cuando me planto --> comparo con el crupier
                System.out.println("3. Salir de la mesa");
                System.out.println("Elija una opcion: ");

                int opcion_int = Integer.parseInt(teclado.readLine());
                salidaSocket.println(opcion_int);
                String opcion = Integer.toString(opcion_int);

                if ("1".equals(opcion)) {
                    String recibido;
                    while (!(recibido = entradaSocket.readLine()).isEmpty()) {
                        System.out.println(recibido);
                    }
                } else if ("2".equals(opcion)) {
                    recibido = entradaSocket.readLine();
                    System.out.println(recibido);
                    plantarse = true;
                } else if ("3".equals(opcion)) { //hay q ver que ponemos aqui
                } else {
                    recibido = entradaSocket.readLine();
                    System.out.println(recibido);
                }
                
                recibido = entradaSocket.readLine();
                if (recibido.startsWith("Te has pasado de 21")){
                    recibido = entradaSocket.readLine();
                    System.out.println(recibido);
                    break;
                }

            } while (valorMano <= 21 && !plantarse);
            

        } catch (IOException e) {
            System.out.println("Exception: " + e);
            System.out.println("Message: " + e.getMessage());
            e.printStackTrace(System.err);

        }
    }
}
