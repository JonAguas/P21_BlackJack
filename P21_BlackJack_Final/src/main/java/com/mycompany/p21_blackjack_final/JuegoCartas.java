/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.p21_blackjack_final;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 *
 * @author jonag
 */
public class JuegoCartas {
    private List<Carta> baraja;
    private List<Carta> mano;
    
    
    public JuegoCartas(){ //jugamos con 2 barajas
        baraja = new ArrayList<>();
        for(PaloCarta palo: PaloCarta.values()){
            for(int i = 1; i <= 13; i++){
                baraja.add(new Carta(i, palo));
            }
        }
        
        for(PaloCarta palo: PaloCarta.values()){
            for(int i = 1; i <= 13; i++){
                baraja.add(new Carta(i, palo));
            }
        }
        
        Collections.shuffle(baraja);
    }
    
    public List<Carta> repartirCartaJugador(){ //no comprobamos si la baraja esta vacia ya que se juega con 2 barjas y no se van a acabar(cada partida se renuevan las barajas)
        mano = new ArrayList<>();
        mano.add(baraja.remove(0));
        mano.add(baraja.remove(1));
        return mano;
    }
    
    public void pedirCarta(){
        
    }
    
    //ahora lo hago con semaforos
//    private List<Carta> baraja;
//    
//    private Semaphore semaforoJugador1 =  new Semaphore(1);
//    private Semaphore semaforoJugador2 =  new Semaphore(0);  
//    
//    public JuegoCartas(){
//        baraja = new ArrayList<>();
//        for(PaloCarta palo: PaloCarta.values()){
//            for(int i = 1; i <= 12; i++){
//                baraja.add(new Carta(i, palo));
//            }
//        }
//        
//        Collections.shuffle(baraja);
//    }
//    
//    public boolean barajaVacia(){
//        return baraja.isEmpty();
//    }
//    
//    public synchronized Carta robarCartaJugador1(){
//        while(barajaVacia()){
//            try{
//                wait();
//            }catch(InterruptedException e){
//                System.err.println("Capturada excepcion: " + e.getMessage());
//                e.printStackTrace(System.err);
//            }
//        }
//        
//        Carta carta = new Carta(0, PaloCarta.OROS);
//        try{
//            semaforoJugador2.acquire();
//            carta = baraja.remove(0);
//        }catch(InterruptedException e){
//            System.err.println("Capturada excepcion: " + e.getMessage());
//            e.printStackTrace(System.err);
//        }finally{
//            semaforoJugador2.release();
//        }
//        
//        return carta;
//    }
//    
//    public synchronized Carta robarCartaJugador2(){
//        while(barajaVacia()){
//            try{
//                wait();
//            }catch(InterruptedException e){
//                System.err.println("Capturada excepcion: " + e.getMessage());
//                e.printStackTrace(System.err);
//            }
//        }
//        
//        Carta carta = new Carta(0, PaloCarta.OROS);
//        try{
//            semaforoJugador1.acquire();
//            carta = baraja.remove(0);
//        }catch(InterruptedException e){
//            System.err.println("Capturada excepcion: " + e.getMessage());
//            e.printStackTrace(System.err);
//        }finally{
//            semaforoJugador1.release();
//        }
//        
//        return carta;
//    }
//    
//    public synchronized void reponerBaraja(){
//        while(!barajaVacia()){
//            try{
//                wait();
//            }catch(InterruptedException e){
//                System.err.println("Capturada excepcion: " + e.getMessage());
//                e.printStackTrace(System.err);
//                System.exit(0);
//            }
//        }
//        System.out.println("Crupier: reponiendo baraja...");
//        baraja = new ArrayList<>();
//        for(PaloCarta palo: PaloCarta.values()){
//            for(int i = 1; i <= 12; i++){
//                baraja.add(new Carta(i, palo));
//            }
//        }
//        
//        Collections.shuffle(baraja);
//        System.out.println("Crupier acaba de de reponer baraja");
//    }
}
