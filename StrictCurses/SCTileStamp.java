package StrictCurses;

import java.awt.*;
import java.awt.image.*;

public class SCTileStamp implements SCConstants
{
   private boolean[][] pixelArr;
	private int width;
	private int height;


	public int getWidth(){return width;}
	public int getHeight(){return height;}

   
   public SCTileStamp(int w, int h)
   {
      width = -1;
      height = -1;
   }
   
   public void set(BufferedImage img, int bgRGB)
   {
      width = img.getWidth();
      height = img.getHeight();
      pixelArr = new boolean[width][height];
      for(int x = 0; x < width; x++)
      for(int y = 0; y < height; y++)
      {
         if(img.getRGB(x, y) != bgRGB)
            pixelArr[x][y] = FOREGROUND;
      }
   }
   public void set(BufferedImage img){set(img, Color.BLACK.getRGB());}
}