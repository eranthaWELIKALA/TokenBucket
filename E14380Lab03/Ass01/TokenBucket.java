public class TokenBucket{
	private final int tokenCapacity;           //maximum bits
	private final int tokenGeneratingSpeed;    //bits per second
	private final int tokenBitSize;            //bit size of a token
	private int currentTokenCapacity;          //used bits for tokens

	public TokenBucket(int tokenCapacity,int tokenGeneratingSpeed,int tokenBitSize){
		this.tokenCapacity = tokenCapacity;
		this.tokenGeneratingSpeed = tokenGeneratingSpeed;
		this.tokenBitSize = tokenBitSize;
		this.currentTokenCapacity = 0;
	}

	//Getters
	public int getTokenCapacity(){
		return this.tokenCapacity;
	}
	public int getTokenGeneratingSpeed(){
		return this.tokenGeneratingSpeed;
	}
	public int getTokenBitSize(){
		return this.tokenBitSize;
	}
	public int getCurrentTokenCapacity(){
		return this.currentTokenCapacity;
	}
	public int getCurrentTokenNumbers(){
		return this.currentTokenCapacity/this.tokenBitSize;
	}

	//Setters
	public void increaseCurrentTokenCapacity(){
		int tempCurrentTokenCapacity;
		tempCurrentTokenCapacity = this.currentTokenCapacity+this.tokenGeneratingSpeed;
		if(tempCurrentTokenCapacity>=this.tokenCapacity){
			this.currentTokenCapacity=tokenCapacity;
		}
		else{
			this.currentTokenCapacity =tempCurrentTokenCapacity;
		}
	}
	public void decreaseToken(int decrement){
		this.currentTokenCapacity=this.currentTokenCapacity-decrement;
	}
}