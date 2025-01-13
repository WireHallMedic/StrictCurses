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

   
   public SCTileStamp(BufferedImage img)
   {
      width = -1;
      height = -1;
      if(img != null)
         set(img);
   }
   
   public SCTileStamp(BufferedImage img, int bgRGB)
   {
      width = -1;
      height = -1;
      if(img != null)
         set(img, bgRGB);
   }
   
   // set based on passed image
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
   
   // apply the stamp, starting at the passed location, to the passed image, in the passed colors
   public void stampImage(int startX, int startY, BufferedImage image, int fgRGB, int bgRGB)
   {
      for(int x = 0; x < width; x++)
      for(int y = 0; y < height; y++)
      {
         if(pixelArr[x][y])
            image.setRGB(x + startX, y + startY, fgRGB);
         else
            image.setRGB(x + startX, y + startY, bgRGB);
      }
   }
}