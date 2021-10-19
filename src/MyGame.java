import playground.Playground;
import playground.Level2;
public class MyGame extends GameLoop {



	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Hello World!");
		MyGame game1= new MyGame();
		game1.runGame(args);
	}

	@Override
	public Playground nextLevel(Playground currentLevel) {
		if(currentLevel==null) {
			 Level2 level2= new Level2();
			 return level2;
		}
		return null;
	}
	

}
