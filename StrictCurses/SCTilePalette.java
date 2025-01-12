/*
A class that holds an array of tileStamps. The array is based on an image file at instantiation.

Stamps can be accessed by x, y location on sheet, or single index (such as char values)
*/

package StrictCurses;

import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import javax.imageio.*;
import java.io.*;

public class SCTilePalette
{
   private SCTileStamp[][] stampArr;
   private int tilesWide;
   private int tilesTall;
   private int tileWidth;
   private int tileHeight;
   
   public SCTilePalette(String fileName, int tileWidthPx, int tileHeightPx, int bgRGB)
   {
      set(fileName, tileWidthPx, tileHeightPx, bgRGB);
   }
   
   // set from image file
   public void set(String fileName, int tileWidthPx, int tileHeightPx, int bgRGB)
   {
      BufferedImage sheetImage = loadImage(fileName);
      if(sheetImage != null)
      {
         tileWidth = tileWidthPx;
         tileHeight = tileHeightPx;
         tilesWide = sheetImage.getWidth() / tileWidth;
         tilesTall = sheetImage.getHeight() / tileHeight;
         stampArr = new SCTileStamp[tilesWide][tilesTall];
         for(int x = 0; x < tilesWide; x++)
         for(int y = 0; y < tilesTall; y++)
         {
            stampArr[x][y] = new SCTileStamp(sheetImage.getSubimage(x * tileWidth, y * tileHeight, tileWidth, tileHeight), bgRGB);
         }
      }
   }
   
   // load an image from file
   public BufferedImage loadImage(String fileName)
   {
      String fileLoc = "";
      if(SCTilePalette.class.getResource("SCTilePalette.class").toString().contains(".jar"))
         fileLoc = fileName;
      else
         fileLoc = "./StrictCurses/" + fileName;
      BufferedImage img = null;
      try
      {
         img = ImageIO.read(new File(fileLoc));
      }
      catch(Exception ex)
      {
         System.out.println(ex.toString());
         img = null;
      }
      return img;
   }
   
   // get a stamp by x, y location on palette
   public SCTileStamp getStamp(int x, int y)
   {
      if(isInBounds(x, y))
         return stampArr[x][y];
      return null;
   }
   
   // get a stamp by single index
   public SCTileStamp getStamp(int index)
   {
      if(isInBounds(index))
         return getStamp(index % tilesWide, index / tilesWide);
      return null;
   }
   
   public boolean isInBounds(int x, int y)
   {
      return x >= 0 && x < tilesWide &&
         y >= 0 && y < tilesTall;
   }
   
   public boolean isInBounds(int index)
   {
      return index >= 0 && index < tilesWide * tilesTall;
   }
}