/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author shehan shaman
 */
public class Queue {
    
    private final int capacity; //100kbits
    private int currentSize; //Kbits
    
    public Queue(int capacity,int currentSize){
        this.capacity = capacity;
        this.currentSize = currentSize;
    }
    
    public int getCapacity(){
        return this.capacity;
    }
    
    public int getCurrentSize(){
        return this.currentSize;
    }
    
    public void setCurrentSize(int size){
        this.currentSize = size;
    }
}
