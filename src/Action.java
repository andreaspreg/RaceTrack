
public class Action {
	int accX;
	int accY;
	
	public Action(int accY, int accX) {
		this.accY = accY;
		this.accX = accX;
	}
	
	@Override
	public String toString() {
		return "Action(" + accY + "," + accX + ")"; 
	}
}
