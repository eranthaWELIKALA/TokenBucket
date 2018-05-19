import java.util.Random;
public class Packet{
	private int packet;
	private int packetSize=2;// 2 bits
	public void Packet(){
		this.packet=0;
	}

	//Setters

	public void createPacket(){
		Random random = new Random();
		this.packet = random.nextInt(20);
	}

	//Getters
	public int getPacketNumbers(){
		return this.packet;
	}
	public int getPacketSize(){
		return packetSize;
	}
}