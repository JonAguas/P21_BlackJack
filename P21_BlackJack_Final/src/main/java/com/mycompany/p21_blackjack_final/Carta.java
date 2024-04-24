/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.p21_blackjack_final;

import java.io.Serializable;

/**
 *
 * @author jonag
 */
public class Carta implements Serializable{
    private int valor;
    private PaloCarta palo;

    public Carta(int valor, PaloCarta palo) {
        this.valor = valor;
        this.palo = palo;
    }

    public int getValor() {
        if (valor <= 10) {
            return valor;
        }
        switch(valor){
            case 11 -> valor = 10;
            case 12 -> valor = 10;
            case 13 -> valor = 10;
        }
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public PaloCarta getPalo() {
        return palo;
    }

    public void setPalo(PaloCarta palo) {
        this.palo = palo;
    }

    @Override
    public String toString() {
        String nombreValor = "";
        switch(valor){
            case 1:
                nombreValor = "As";
                break;
            case 11:
                nombreValor = "J";
                break;
            case 12: 
                nombreValor = "Q";
                break;
            case 13:
                nombreValor = "K";
                break;
            default:
                nombreValor = String.valueOf(valor);
        }
        return nombreValor + " de " + palo;
    }
    
    
}
