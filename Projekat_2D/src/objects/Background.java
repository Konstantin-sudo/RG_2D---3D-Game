package objects;


import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import models.GameModel;

import java.util.Random;

import static models.GameModel.GAME_SCENE_MARGIN_X;
import static models.GameModel.GAME_SCENE_MARGIN_Y;

public class Background extends GameObject {

	public Background ( ) {
		//BACKGROUND 1
		Random rand = new Random();
		int n = 10;
		Rectangle background1[] = new Rectangle[n];
		double oneRectangleHeight = GameModel.getInstance().getSceneHeight() / n;
		double posY=0;
		for(int i = 0; i < n; i++){
			Color color1;
			Color color2;
			int mod = i%3;
			switch (mod){
				case 0:
					color1 = Color.rgb(143, 143, 143); //light gray
					//color2 = Color.rgb(133, 130, 130); //medium gray
					color2 = Color.rgb(110, 107, 105); 	//dark grey
					break;
				case 1:
					color2 = Color.rgb(143, 143, 143); //light gray
					color1 = Color.rgb(133, 130, 130); //medium gray
					//color2 = Color.rgb(110, 107, 105); 	//dark grey
					break;
				default:
					color2 = Color.rgb(143, 143, 143); //light gray
					//color1 = Color.rgb(133, 130, 130); //medium gray
					color1 = Color.rgb(110, 107, 105); 	//dark grey
			}
			Stop[] stops = new Stop[] {
					new Stop(0, color1),
					new Stop(1, color2)
			};
			LinearGradient backgroundColor = new LinearGradient(
					0,0,
					rand.nextInt(70)/100+0.3,0,
					true,
					CycleMethod.REFLECT,
					stops
			);
			double height = oneRectangleHeight * (((double)rand.nextInt(40))/((double)100) + 1);
			background1[i] = new Rectangle (
					0,
					posY + 0,
					GameModel.getInstance ( ).getSceneWidth ( ) + 0,
					height + 0
			);
			posY+=height;
			//System.out.println(background1[i].getHeight());
			background1[i].setFill(backgroundColor);
			background1[i].setStrokeWidth(5);
			background1[i].setStroke(Color.rgb(150, 150, 150));
			this.getChildren ( ).add (  background1[i]  );
		}

		//BACKGROUND 2
		Rectangle background2 = new Rectangle (
				GAME_SCENE_MARGIN_X,
				GAME_SCENE_MARGIN_Y,
				GameModel.getInstance ( ).getSceneWidth ( ) - GAME_SCENE_MARGIN_X * 2,
				GameModel.getInstance ( ).getSceneHeight ( ) - GAME_SCENE_MARGIN_Y * 2
		);
		Stop[] stops2 = new Stop[] {
				new Stop(0,Color.YELLOW),
				new Stop(1,Color.BLACK)
		};
		LinearGradient background2Color = new LinearGradient(0,0,0,1,true, CycleMethod.NO_CYCLE, stops2);
		background2.setFill ( background2Color );

		this.getChildren ( ).add (  background2  );
	}
}
