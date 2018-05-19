/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Welikala EY
 */
public class TokenBucket {
    
    private final int capacity; //100bits
    private final int tokenRate; // 10bits per second 
    private final int tokenSize; //2bits
    private int currentSize; //0bits
    
    public TokenBucket(int capacity,int tokenRate,int tokenSize,int currentSize){
        this.capacity = capacity;
        this.tokenRate = tokenRate;
        this.tokenSize = tokenSize;
        this.currentSize = currentSize;
    }
    
    public int getCapacity (){
        return this.capacity;
    }
    
    public int getTokenRate(){
        return this.tokenRate;
    }
    
    public int getTokenSize(){
        return this.tokenSize;
    }
    
    public int getCurrentSize(){
        return this.currentSize;
    }
    
    public void setCurrentSize(int size){
        this.currentSize = size;
    }
    
    public int getTokenCount(){
        return this.getCurrentSize()/this.getTokenSize();
    }
}
