public class State {
	
	int posX;
	int posY;
	
	int velX;
	int velY;
	
	public State(int posY, int posX, int velY, int velX) {
		this.posY = posY;
		this.posX = posX;
		this.velY = velY;
		this.velX = velX;
	}
	
	public State act(Action a) {
		//clone
		State s = clone();
		
		//adjust velocity
		s.velY += a.accY;
		s.velX += a.accX;
		
		//adjust position
		s.posY += s.velY;
		s.posX += s.velX;
		
		return s;
	}
	
	public State actWithStochastic(Action a) {
		State s = act(a);
		
		if(Math.random() < 0.5) {
			if(Math.random() < 0.5) {
				s.posX += 1;
			}
			else {
				s.posY += -1;
			}
		}
		
		return s;
	}
	
	public State clone() {
		return new State(posY, posX, velY, velX);
	}
	
	public boolean equals(State s) {
		return s.posY == posY && s.posX == posX && s.velY == velY && s.velX == velX;
	}
}
