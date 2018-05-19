public class Main extends Thread{
	public TokenBucket tBucket;
	public Main(TokenBucket tBucket){
		this.tBucket=tBucket;
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
        System.out.println("  -----------------------------------------------------------------------------------------");
        System.out.println("  Tokens |  Data Packets  |  Sent Packets  |  Destroyed Packets  |  Total Destroyed Packets");
        System.out.println("  -----------------------------------------------------------------------------------------");

		trd.start();
		int packetCount,releasedToken = 0,tDestroyedPacket = 0,remainingToken;

		while(true){
			//creating packets
			packet.createPacket();
			packetCount=packet.getPacketNumbers();
			remainingToken = tBucket.getCurrentTokenNumbers();
			int destroyedPacket = 0;
			if(packetCount>=remainingToken){
				tDestroyedPacket += packetCount-remainingToken;
				destroyedPacket = packetCount-remainingToken;
				tBucket.decreaseToken(remainingToken);
				releasedToken = remainingToken;
			}
			else{
				tBucket.decreaseToken(packetCount);
				releasedToken = packetCount;
			}
			int rSpace=5,pSpace=12,reSpace=12,dSpace=17;
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
			System.out.println("|  "+tDestroyedPacket);

			//System.out.println(remainingToken+"     |  "+packetCount+"            |  "+releasedToken+"            |  "+destroyedPacket+"                 |  "+tDestroyedPacket);
			Thread.sleep(1000);
		}
	}
}