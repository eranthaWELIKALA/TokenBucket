public class Main extends Thread{

	public TokenBucket tBucket;
	public static final int s2cSpeed       = 40;  // 40 bits per second
	public static final int trashCapacity  = 60; // 400 bits
	public static int currentTrashCapacity;   // starting value 0 bits

	public Main(TokenBucket tBucket){
		this.tBucket=tBucket;
		this.currentTrashCapacity=0;
	}

	public void run(){
		while(true){
			tBucket.increaseCurrentTokenCapacity();
			try{
				Thread.sleep(1000);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	public static void main(String [] args) throws Exception {
		//configure token bucket
		TokenBucket tBucket = new TokenBucket(100,10,2);
		//configure to send packets
		Packet packet = new Packet();

		Main trd = new Main(tBucket);

		System.out.println("Bucket Capacity             : " + tBucket.getTokenCapacity() + " bits");
        System.out.println("Token Generating Speed      : " + tBucket.getTokenGeneratingSpeed() + " bits per second");
        System.out.println("Token Bit Size              : " + tBucket.getTokenBitSize() + " bits");
        System.out.println("Starting Token Capacity     : " + tBucket.getCurrentTokenCapacity() + " bits");
        System.out.println("Trash Memory Capacity       : " + trashCapacity + " bits");
        System.out.println("  ----------------------------------------------------------------------------------------");
        System.out.println("  Tokens |  Data Packets  |  Sent Packets  |  Destroyed Packets  |  Speed  |  Trash Memory");
        System.out.println("  ----------------------------------------------------------------------------------------");

		trd.start();
		int packetCount,releasedToken = 0,tDestroyedPacket = 0,deletedPackets=0,tPacketCount,remainingToken,outputSize,speed;

		while(true){
			//creating packets
			packet.createPacket();
			packetCount=packet.getPacketNumbers();
			tPacketCount=packetCount+(int) (currentTrashCapacity/(tBucket.getTokenBitSize()+packet.getPacketSize()));
			currentTrashCapacity=0;
			remainingToken = tBucket.getCurrentTokenNumbers();
			int destroyedPacket = 0;
			if(tPacketCount>=remainingToken){
				destroyedPacket = tPacketCount-remainingToken;
				releasedToken = remainingToken;
				outputSize = releasedToken*tBucket.getTokenBitSize()+releasedToken*packet.getPacketSize();
				if(outputSize>s2cSpeed){
					currentTrashCapacity = outputSize-s2cSpeed+(destroyedPacket*(tBucket.getTokenBitSize()+packet.getPacketSize()));
					if(currentTrashCapacity>trashCapacity){
						deletedPackets += (currentTrashCapacity-trashCapacity)/(tBucket.getTokenBitSize()+packet.getPacketSize());
						currentTrashCapacity = trashCapacity;	
					}
					speed = s2cSpeed;
					destroyedPacket = (int) (currentTrashCapacity/(tBucket.getTokenBitSize()+packet.getPacketSize()));
					releasedToken = (int) (speed/(tBucket.getTokenBitSize()+packet.getPacketSize()));
				}
				else{
					currentTrashCapacity = (destroyedPacket*(tBucket.getTokenBitSize()+packet.getPacketSize()));
					if(currentTrashCapacity>trashCapacity){
						deletedPackets += (currentTrashCapacity-trashCapacity)/(tBucket.getTokenBitSize()+packet.getPacketSize());
						currentTrashCapacity = trashCapacity;	
					}
					speed =outputSize;
				}
				tBucket.decreaseToken(releasedToken);	
			}
			else{
				releasedToken = tPacketCount;
				outputSize = releasedToken*tBucket.getTokenBitSize()+releasedToken*packet.getPacketSize();
				if(outputSize>s2cSpeed){
					currentTrashCapacity = outputSize-s2cSpeed;
					if(currentTrashCapacity>trashCapacity){
						deletedPackets += (currentTrashCapacity-trashCapacity)/(tBucket.getTokenBitSize()+packet.getPacketSize());
						currentTrashCapacity = trashCapacity;	
					}
					speed = s2cSpeed;
					destroyedPacket = (int) (currentTrashCapacity/(tBucket.getTokenBitSize()+packet.getPacketSize()));
					releasedToken = (int) (speed/(tBucket.getTokenBitSize()+packet.getPacketSize()));
				}
				else{
					speed =outputSize;
				}
				tBucket.decreaseToken(releasedToken);
			}
			/*if(currentTrashCapacity>trashCapacity){
				deletedPackets = (currentTrashCapacity-trashCapacity)/(tBucket.getTokenBitSize()+packet.getPacketSize());
				currentTrashCapacity = trashCapacity;	
			}*/

			

			int rSpace=5,pSpace=12,reSpace=12,dSpace=17,sSpace=5;
			if(remainingToken<10){
				rSpace=6;
			}
			if(packetCount<10){
				pSpace=13;
			}
			if(releasedToken<10){
				reSpace=13;
			}
			if(destroyedPacket<10){
				dSpace=18;
			}
			if(speed<10){
				sSpace=6;
			}
			System.out.print("  "+remainingToken);
			for(int itr=0;itr<rSpace;itr++){
				System.out.print(" ");
			}
			System.out.print("|  "+packetCount);
			for(int itr=0;itr<pSpace;itr++){
				System.out.print(" ");
			}
			System.out.print("|  "+releasedToken);
			for(int itr=0;itr<reSpace;itr++){
				System.out.print(" ");
			}
			System.out.print("|  "+destroyedPacket);
			for(int itr=0;itr<dSpace;itr++){
				System.out.print(" ");
			}
			System.out.print("|  "+speed);
			for(int itr=0;itr<sSpace;itr++){
				System.out.print(" ");
			}
			System.out.println("|  "+currentTrashCapacity);
			System.out.println("Deleted Packets : "+deletedPackets);
			System.out.println();

			//System.out.println(remainingToken+"     |  "+packetCount+"            |  "+releasedToken+"            |  "+destroyedPacket+"                 |  "+tDestroyedPacket);
			Thread.sleep(1000);
		}
	}
}