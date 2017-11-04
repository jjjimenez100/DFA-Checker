package com.jjjimenez.amata;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author JJJimenez
 */
public class State {
    public State(){
        finalState = false;
        destinationOne = 0;
        destinationZero = 0;
    }

    public boolean isFinalState() {
        return finalState;
    }

    public void setFinalState(boolean finalState) {
        this.finalState = finalState;
    }

    public int getDestinationOne() {
        return destinationOne;
    }

    public void setDestinationOne(int destinationOne) {
        this.destinationOne = destinationOne;
    }

    public int getDestinationZero() {
        return destinationZero;
    }

    public void setDestinationZero(int destinationZero) {
        this.destinationZero = destinationZero;
    }
   
    private boolean finalState;
    private int destinationOne;
    private int destinationZero;
}
