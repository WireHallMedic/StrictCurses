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
}