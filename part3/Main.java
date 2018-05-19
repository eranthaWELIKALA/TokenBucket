
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author shehan shaman
 */
public class Main extends Thread {
    
    public TokenBucket bucket;
    public static final int packetSize = 2; //2bits /packet
    public static final int outSpeed = 40;//40kbps
    
    public Main(TokenBucket bucket){
        this.bucket = bucket; 
    }
    
    @Override
    public void run(){
        int newSize;
        while(true){
            newSize = bucket.getCurrentSize() + bucket.getTokenRate();
            if(newSize>bucket.getCapacity()) newSize = bucket.getCapacity();
            bucket.setCurrentSize(newSize);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public static int createPacket(){
        Random rand = new Random();
        int tmp = rand.nextInt(30);
        return tmp;
    }
    
    public static void main(String [] args) throws InterruptedException{
        //while(true) System.out.println(createPacket());
        TokenBucket tb = new TokenBucket(200,20,2,100);
        Queue qOut = new Queue(200,0);
        Queue qIn = new Queue(200,0);
        Main t = new Main(tb);
        t.start();
        
        int packetCount,sentPacket = 0,destroyPacket = 0 ,tmp,disTmp,disIn =0;
        double speed = 0;
        
        System.out.println("Bucket Capacity : " + tb.getCapacity() + " kbits");
        System.out.println("Token Rate      : " + tb.getTokenRate() + " kb/s");
        System.out.println("Token Size      : " + tb.getTokenSize() + " kbits");
        System.out.println("Output Buffer   : " + qOut.getCapacity() + " kbits");
        System.out.println("Input Buffer    : " + qIn.getCapacity() + " kbits");
        System.out.println("----------------------------------------------------------------------------------------------------------");
        System.out.println("Token   Data Packts     Sent Packets    Destroy Packets     Speed       Output Buffer       Input Buffer");
        System.out.println("----------------------------------------------------------------------------------------------------------");
        
        while(true) {
            //System.out.println(tb.getCurrentSize());
            packetCount = createPacket(); 
            tmp = tb.getTokenCount();
            System.out.println(tmp + "      |       " + packetCount + "     |       " 
                    + sentPacket + "    |        " + destroyPacket + "      |       " 
                    + speed + "     |       " + qOut.getCurrentSize() + "       |        " +
                    qIn.getCurrentSize());
            
            if(packetCount >= tmp){
                destroyPacket += (packetCount - tmp);
                disIn = packetCount - tmp;
                sentPacket += tmp;
                tb.setCurrentSize(0);
                speed = (packetCount - tmp) * (tb.getTokenSize() + packetSize);
            }else{
                sentPacket += packetCount;
                tmp = tmp - packetCount;
                if(qIn.getCurrentSize() > 0){
                    int inBP = qIn.getCurrentSize() / packetSize;
                    if(inBP - tmp > 0){
                        qIn.setCurrentSize(qIn.getCurrentSize() - tmp * packetSize);
                        tmp =0;
                    }else{
                        qIn.setCurrentSize(0);
                        tmp = tmp - inBP;
                    }
                }
                tb.setCurrentSize(tmp * tb.getTokenSize());
                speed = packetCount * (tb.getTokenSize() + packetSize);
            }
            
            if((qOut.getCurrentSize() - outSpeed + speed) > qOut.getCapacity()){
                disTmp = (int) (qOut.getCurrentSize() - outSpeed + speed - qOut.getCapacity());
                disTmp = disTmp / (tb.getTokenSize() + packetSize);
                destroyPacket += disTmp;
                sentPacket -= disTmp;
                speed = outSpeed;
            }else{
                //can have add data
                //qOut.setCurrentSize(speed);
                if(speed>outSpeed){
                    disTmp = (int) (speed - outSpeed);
                    int bufferPacket = disTmp / (tb.getTokenSize() + packetSize);//packet
                    sentPacket -= bufferPacket;
                    qOut.setCurrentSize(qOut.getCurrentSize() + disTmp);
                    speed = outSpeed;
                }else{
                    int rel = (int) (outSpeed - speed);
                    if(qOut.getCurrentSize() >= rel){
                        sentPacket += rel / (tb.getTokenSize() + packetSize);
                        qOut.setCurrentSize(qOut.getCurrentSize() - rel);
                        speed = outSpeed;
                    }else{
                        sentPacket += rel / (tb.getTokenSize() + packetSize);
                        speed += qOut.getCurrentSize();
                        qOut.setCurrentSize(0);
                    }
                    
                }
            }
            
            if(qIn.getCurrentSize() < qIn.getCapacity() ){
                int add = 0;
                if(qIn.getCapacity() - qIn.getCurrentSize() - disIn * packetSize >= 0){
                    //add = qIn.getCapacity() - qIn.getCurrentSize() - disIn * packetSize;
                    qIn.setCurrentSize(qIn.getCurrentSize() + disIn * packetSize);
                    destroyPacket -= disIn;
                }else{
                    qIn.setCurrentSize(qIn.getCapacity());
                    add = (qIn.getCapacity() - qIn.getCurrentSize()) / packetSize;
                    destroyPacket -= add;
                }
            }
            disIn = 0;
            Thread.sleep(1000);
        }
        
    }
    
}
