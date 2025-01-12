/*
A JPanel that displays an image made of tiles.

Dynamically resizes the image to fill the screen. You can choose to keep the original ratio, or not.

If the ratio is retained, you can choose to either center the image (default), or place it in the upper-right corner.

If the ratio is not retained, the image will fill the panel
*/

package StrictCurses;

import javax.swing.*;
import java.awt.image.*;
import java.awt.*;

public class SCPanel extends JPanel implements SCConstants
{
   private SCTileStruct[][] structArr;
   private SCTilePalette palette;
   private BufferedImage baseImage;
   private double sizeRatio;
   private int tilesWide;
   private int tilesTall;
   private boolean maintainRatio;
   private boolean centerImage;
   
   public int getTilesWide(){return tilesWide;}
   public int getTilesTall(){return tilesTall;}
   public boolean getMaintainRatio(){return maintainRatio;}
   public boolean getCenterImage(){return centerImage;}
   
   public void setMaintainRatio(boolean mr){maintainRatio = mr;}
   public void setCenterImage(boolean ci){centerImage = ci;}
   
   public SCPanel(SCTilePalette tilePalette, int widthInTiles, int heightInTiles)
   {
      super();
      maintainRatio = true;
      centerImage = true;
      palette = tilePalette;
      tilesWide = widthInTiles;
      tilesTall = heightInTiles;
      sizeRatio = (double)(tilesWide * getTileWidth()) / (double)(tilesTall * getTileHeight());
      structArr = new SCTileStruct[tilesWide][tilesTall];
      for(int x = 0; x < tilesWide; x++)
      for(int y = 0; y < tilesTall; y++)
         structArr[x][y] = new SCTileStruct();
      createBaseImage();
      setBackground(new Color(DEFAULT_BG_COLOR));
   }
   
   public int getTileWidth()
   {
      return palette.getTileWidth();
   }
   
   public int getTileHeight()
   {
      return palette.getTileHeight();
   }
   
   public boolean isInBounds(int x, int y)
   {
      return x >= 0 && x < tilesWide &&
             y >= 0 && y < tilesTall;
   }
   
   // overwrite any existing base baseImage and create the whole thing
   public void createBaseImage()
   {
      int w = tilesWide * getTileWidth();
      int h = tilesTall * getTileHeight();
      baseImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
      for(int x = 0; x < tilesWide; x++)
      for(int y = 0; y < tilesTall; y++)
      {
         setBaseTile(x, y);
      }
   }
   
   // set a single tile on the base baseImage
   private void setBaseTile(int x, int y)
   {
      if(isInBounds(x, y))
      {
         SCTileStamp stamp = palette.getStamp(structArr[x][y].getCharacterIndex());
         int fg = structArr[x][y].getFGRGB();
         int bg = structArr[x][y].getBGRGB();
         stamp.stampImage(x * getTileWidth(), y * getTileHeight(), baseImage, fg, bg);
      }
   }
   
   // update tile, if the new information is different
   public void setTile(int x, int y, int index, int fgRGB, int bgRGB)
   {
      if(isInBounds(x, y) && !structArr[x][y].matches(index, fgRGB, bgRGB))
      {
         structArr[x][y].set(index, fgRGB, bgRGB);
         setBaseTile(x, y);
      }
   }
   public void setTile(int x, int y, int xIndex, int yIndex, int fgRGB, int bgRGB)
   {
      setTile(x, y, palette.xYToIndex(xIndex, yIndex), fgRGB, bgRGB);
   }
   
   // update tile index and paint, if the new information is different
   public void setTileIndex(int x, int y, int index)
   {
      if(isInBounds(x, y) && !structArr[x][y].matchesIndex(index))
      {
         structArr[x][y].setCharacterIndex(index);
         setBaseTile(x, y);
      }
   }
   public void setTileIndex(int x, int y, int xIndex, int yIndex)
   {
      setTileIndex(x, y, palette.xYToIndex(xIndex, yIndex));
   }
   
   // update foreground color and paint, if the new information is different
   public void setTileFG(int x, int y, int fg)
   {
      if(isInBounds(x, y) && !structArr[x][y].matchesFG(fg))
      {
         structArr[x][y].setFGRGB(fg);
         setBaseTile(x, y);
      }
   }
   
   // update tile index and paint, if the new information is different
   public void setTileBG(int x, int y, int bg)
   {
      if(isInBounds(x, y) && !structArr[x][y].matchesBG(bg))
      {
         structArr[x][y].setBGRGB(bg);
         setBaseTile(x, y);
      }
   }
   
   @Override
   public void paint(Graphics g)
   {
      super.paint(g);
      Graphics2D g2d = (Graphics2D)g;
      int w = getWidth();
      int h = getHeight();
      int xInset = 0;
      int yInset = 0;
      if(maintainRatio)
      {
         // limit by height
         if((getWidth() / sizeRatio) > getHeight())
         {
            h = getHeight();
            w = (int)(h * sizeRatio);
         }
         // limit by width
         else
         {
            w = getWidth();
            h = (int)(w / sizeRatio);
         }
         if(centerImage)
         {
            xInset = (getWidth() - w) / 2;
            yInset = (getHeight() - h) / 2;
         }
      }
      g2d.drawImage(baseImage, xInset, yInset, w, h, null);
   }
   
   // main for testing
   public static void main(String[] args)
   {
      JFrame frame = new JFrame();
      frame.setSize(500, 800);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
      SCTilePalette palette = new SCTilePalette("WidlerTiles_16x16.png", 16, 16, DEFAULT_BG_COLOR);
      
      SCPanel panel = new SCPanel(palette, 16, 16);
      for(int x = 0; x < 16; x++)
      for(int y = 0; y < 16; y++)
      {
         panel.setTileIndex(x, y, x, y);
      }
      for(int x = 0; x < 16; x++)
         panel.setTileFG(x, 5, Color.CYAN.getRGB());
      for(int x = 0; x < 16; x++)
         panel.setTileBG(x, 6, Color.CYAN.getRGB());
      frame.add(panel);
      frame.setVisible(true);
   }
}