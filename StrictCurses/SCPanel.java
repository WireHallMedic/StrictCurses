/*
A JPanel that displays an image made of tiles.

Dynamically resizes the image to fill the screen. You can choose to keep the original ratio, or not.

If the ratio is retained, you can choose to either center the image (default), or place it in the upper-right corner.

If the ratio is not retained, the image will fill the panel
*/

package StrictCurses;

import javax.swing.*;
import java.awt.image.*;

public class SCPanel extends JPanel implements SCConstants
{
   private SCTileStruct[][] structArr;
   private SCTilePalette palette;
   private BufferedImage resizedImage;
   private BufferedImage baseImage;
   private double sizeRatio;
   private int tilesWide;
   private int tilesTall;
   private boolean redrawF;
   
   public SCPanel(SCTilePalette tilePalette, int widthInTiles, int heightInTiles)
   {
      super();
      palette = tilePalette;
      tilesWide = widthInTiles;
      tilesTall = heightInTiles;
      structArr = new SCTileStruct[tilesWide][tilesTall];
      for(int x = 0; x < tilesWide; x++)
      for(int y = 0; y < tilesTall; y++)
         structArr[x][y] = new SCTileStruct();
      redrawF = true;
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
   
   // overwrite any existing base image and create the whole thing
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
      redrawF = true;
   }
   
   // set a single tile on the base image
   private void setBaseTile(int x, int y)
   {
      if(isInBounds(x, y))
      {
         SCTileStamp stamp = palette.getStamp(structArr[x][y].getCharacterIndex());
         int fg = structArr[x][y].getFGRGB();
         int bg = structArr[x][y].getBGRGB();
         stamp.stampImage(x * getTileWidth(), y * getTileHeight(), baseImage, fg, bg);
         redrawF = true;
      }
   }
}