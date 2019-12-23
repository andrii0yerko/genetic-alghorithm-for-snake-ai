public enum Direction {
		LEFT,
		UP,
		RIGHT,
		DOWN;
		
	
	public static Direction valueOf(int i)
	{
		switch ((i+4)%4)
		{
			case 0: return LEFT;
			case 1: return UP;
			case 2: return RIGHT;
			case 3: return DOWN;
			
		}
		return UP;
	}
}