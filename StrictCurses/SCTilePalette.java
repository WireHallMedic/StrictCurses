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

public class SCTilePalette implements SCConstants
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
   
   public SCTilePalette(String fileName, int tileWidthPx, int tileHeightPx)
   {
      this(fileName, tileWidthPx, tileHeightPx, DEFAULT_BG_COLOR);
   }
   
   public int getTileWidth()
   {
      return tileWidth;
   }
   
   public int getTileHeight()
   {
      return tileHeight;
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
      boolean isJar = false;
      if(SCTilePalette.class.getResource("SCTilePalette.class").toString().contains(".jar"))
         isJar = true;
      BufferedImage img = null;
      // try and load resource if we're in a jar
      if(isJar)
      {
         try
         {
            img = ImageIO.read(new File(fileName));
         }
         catch(Exception ex)
         {
            img = null;
         }
      }
      // if no image yet, load local file
      if(img == null)
      {
         try
         {
            img = ImageIO.read(new File("./" + fileName));
         }
         catch(Exception ex)
         {
            img = null;
            System.out.println("Unable to load file %s: %s".format(fileName, ex.toString()));
         }
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
   
   // check location is in bounds
   public boolean isInBounds(int x, int y)
   {
      return x >= 0 && x < tilesWide &&
         y >= 0 && y < tilesTall;
   }
   
   // check location is in bounds
   public boolean isInBounds(int index)
   {
      return index >= 0 && index < tilesWide * tilesTall;
   }
   
   public int xYToIndex(int x, int y)
   {
      return x + (y * tilesWide);
   }
   
   // main for testing
   public static void main(String[] args)
   {
      SCTilePalette p = new SCTilePalette("WidlerTiles_16x16.png", 16, 16, DEFAULT_BG_COLOR);
   }
}