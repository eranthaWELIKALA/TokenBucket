import java.util.Random;
public class Packet{
	private int packet;
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
}