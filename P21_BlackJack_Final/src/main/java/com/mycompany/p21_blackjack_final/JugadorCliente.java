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
    
    private static final String SERVER_ADREES= "127.0.0.1";    
    private static final int SERVER_PORT= 59001;   
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            Socket s = new Socket(SERVER_ADREES, SERVER_PORT);
            BufferedReader entradaSocket = new BufferedReader(new InputStreamReader( s.getInputStream()));
            PrintWriter salidaSocket = new PrintWriter(s.getOutputStream(), true);
            BufferedReader teclado = new BufferedReader(new InputStreamReader( System.in));
            
            while(true){
                String recibido = entradaSocket.readLine();
                if (recibido.startsWith("NAMEACCEPTED")) {
                    break;
                }else{
                    System.out.println("Introduce tu nombre: ");
                    String nombre = teclado.readLine();
                    salidaSocket.println(nombre);
                }
            }
            
            JuegoCartas juego = new JuegoCartas();
            List<Carta> mano = juego.repartirCartaJugador();
            for (Carta elemento : mano) {
                System.out.println(elemento);  
            }
            
            
            while(true){
                System.out.println("Valor de la mano: " + juego.valorMano(mano));
                System.out.println("\n1. Pedir otra carta");
                System.out.println("2. Doblar apuesta");
                System.out.println("3. Plantarse");
                System.out.println("Elija una opcion: ");
                
                String opcion = teclado.readLine();
                
                if ("1".equals(opcion)) {
                    juego.pedirCarta();
                } else if ("2".equals(opcion)){
                    System.out.println("Se ha doblado tu apuesta");
                } else if("3".equals(opcion)){
                    salidaSocket.println("/quit");
                    break;
                }else{
                    System.out.println("Opcion incorrecta");
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
