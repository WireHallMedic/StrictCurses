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
import java.awt.event.*;
import java.util.*;

public class SCPanel extends JPanel implements SCConstants, MouseMotionListener, MouseListener
{
   private SCTileStruct[][] structArr;
   private SCTilePalette palette;
   private BufferedImage baseImage;
   private double sizeRatio;
   private int tilesWide;
   private int tilesTall;
   private boolean maintainRatio;
   private boolean centerImage;
   private int imageWidth;
   private int imageHeight;
   private int imageXInset;
   private int imageYInset;
   private int mouseLocTileX;
   private int mouseLocTileY;
   
   public int getTilesWide(){return tilesWide;}
   public int getTilesTall(){return tilesTall;}
   public boolean getMaintainRatio(){return maintainRatio;}
   public boolean getCenterImage(){return centerImage;}
   public int getImageWidth(){return imageWidth;}
   public int getImageHeight(){return imageHeight;}
   public int getImageXInset(){return imageXInset;}
   public int getImageYInset(){return imageYInset;}
   
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
      mouseLocTileX = -1;
      mouseLocTileY = -1;
      addMouseMotionListener(this);
      addMouseListener(this);
   }
   
   public int[] getMouseLocTile()
   {
      int[] pos = {mouseLocTileX, mouseLocTileY};
      return pos;
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
   private void createBaseImage()
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
   
   // sets the tiles in a box of width w and height h, starting at [x, y]
   public void fillTile(int x, int y, int w, int h, int index, int fgRGB, int bgRGB)
   {
      for(int xx = 0; xx < w; xx++)
      for(int yy = 0; yy < h; yy++)
         setTile(xx + x, yy + y, index, fgRGB, bgRGB);
   }
   public void fillTile(int x, int y, int w, int h, int xIndex, int yIndex, int fgRGB, int bgRGB)
   {
      for(int xx = 0; xx < w; xx++)
      for(int yy = 0; yy < h; yy++)
         setTile(xx + x, yy + y, xIndex, yIndex, fgRGB, bgRGB);
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
   
   // sets the tile indexes in a box of width w and height h, starting at [x, y]
   public void fillTileIndex(int x, int y, int w, int h, int index)
   {
      for(int xx = 0; xx < w; xx++)
      for(int yy = 0; yy < h; yy++)
         setTileIndex(xx + x, yy + y, index);
   }
   public void fillTile(int x, int y, int w, int h, int xIndex, int yIndex)
   {
      for(int xx = 0; xx < w; xx++)
      for(int yy = 0; yy < h; yy++)
         setTileIndex(xx + x, yy + y, xIndex, yIndex);
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
   
   // sets the tile foregrounds in a box of width w and height h, starting at [x, y]
   public void fillTileFG(int x, int y, int w, int h, int fg)
   {
      for(int xx = 0; xx < w; xx++)
      for(int yy = 0; yy < h; yy++)
         setTileFG(xx + x, yy + y, fg);
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
   
   // sets the tile foregrounds in a box of width w and height h, starting at [x, y]
   public void fillTileBG(int x, int y, int w, int h, int bg)
   {
      for(int xx = 0; xx < w; xx++)
      for(int yy = 0; yy < h; yy++)
         setTileBG(xx + x, yy + y, bg);
   }
   
   public int getTileIndex(int x, int y)
   {
      if(isInBounds(x, y))
      {
         return structArr[x][y].getCharacterIndex();
      }
      return -1;
   }
   
   public int getTileFGColor(int x, int y)
   {
      if(isInBounds(x, y))
      {
         return structArr[x][y].getFGRGB();
      }
      return -1;
   }
   
   public int getTileBGColor(int x, int y)
   {
      if(isInBounds(x, y))
      {
         return structArr[x][y].getBGRGB();
      }
      return -1;
   }
   
   private void setImageProperties()
   {
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
      imageWidth = w;
      imageHeight = h;
      imageXInset = xInset;
      imageYInset = yInset;
   }
   
   // write a string on a single line
   public void writeLine(int startX, int startY, String str)
   {
      for(int i = 0; i < str.length(); i++)
         setTileIndex(startX + i, startY, str.charAt(i));
   }
   
   // write a string bound within a box, breaking on newlines and as needed
   public void writeBox(int startX, int startY, int width, int height, String str)
   {
      // split the string on spaces
      String[] splitList = str.split(" ");
      Vector<String> strList = new Vector<String>();
      // make list of words
      for(int i = 0; i < splitList.length; i++)
      {
         // short words go on list
         if(splitList[i].length() < width)
            strList.add(splitList[i]);
         // long words are chopped up and put on list
         else
         {
            String longStr = splitList[i];
            while(longStr.length() >= width)
            {
               strList.add(longStr.substring(0, width - 1));
               longStr = longStr.substring(width - 1, longStr.length());
            }
            strList.add(longStr);
         }
      }
      
      int rowNum = 0;
      int colNum = 0;
      int curWordIndex = 0;
      while(rowNum < height && curWordIndex < strList.size())
      {
         String curStr = strList.elementAt(curWordIndex);
         // write a word, if there's room
         if(colNum + curStr.length() <= width)
         {
            for(int i = 0; i < curStr.length(); i++)
            {
               // newline
               if(curStr.charAt(i) == '\n')
               {
                  if(colNum != 0)
                  {
                     colNum = 0;
                     rowNum++;
                  }
               }
               else
               {
                  // write character
                  setTileIndex(startX + colNum, startY + rowNum, curStr.charAt(i));
                  colNum++;
               }
            }
            // put a space, if there's room
            if(colNum < width - 1)
            {
               setTileIndex(startX + colNum, startY + rowNum, ' ');
               colNum++;
            }
            // no room for space means end of line
            else
            {
               colNum = 0;
               rowNum++;
            }
            // next word
            curWordIndex++;
         }
         // no room for word, do carriage return
         else
         {
            colNum = 0;
            rowNum++;
         }
      }
   }
   
   // returns a list of all the instances of the passed text in the specified box.
   // Return value is a vector of x, y pairs, as the length is known
   public Vector<int[]> findText(int originX, int originY, int width, int height, String text)
   {
      Vector<int[]> foundList = new Vector<int[]>();
      int prospectXStart = -1;
      int prospectYStart = -1;
      int curLen;
      for(int y = 0; y < height; y++)
      {
         curLen = 0;
         for(int x = 0; x < width; x++)
         {
            if(getTileIndex(x + originX, y + originY) == text.charAt(curLen))
            {
               // new prosepct, set the start
               if(curLen == 0)
               {
                  prospectXStart = x + originX;
                  prospectYStart = y + originY;
               }
               curLen++;
               // if we're at the string length, we have a match
               if(curLen == text.length())
               {
                  int[] hit = {prospectXStart, prospectYStart};
                  foundList.add(hit);
                  curLen = 0;
               }
            }
            // character doesn't match, reset current length
            else
               curLen = 0;
         }
      }
      return foundList;
   }
   
   @Override
   public void paint(Graphics g)
   {
      super.paint(g);
      Graphics2D g2d = (Graphics2D)g;
      setImageProperties();
      g2d.drawImage(baseImage, imageXInset, imageYInset, imageWidth, imageHeight, null);
   }
   
   // MouseMotionListener methods
   public void mouseMoved(MouseEvent me)
   {
      double visibleTileWidth = imageWidth / (double)tilesWide;
      double visibleTileHeight = imageHeight / (double)tilesTall;
      mouseLocTileX = (int)((me.getX() - imageXInset) / visibleTileWidth);
      mouseLocTileY = (int)((me.getY() - imageYInset) / visibleTileHeight);
      if(!isInBounds(mouseLocTileX, mouseLocTileY))
      {
         mouseLocTileX = -1;
         mouseLocTileY = -1;
      }
   }
   public void mouseDragged(MouseEvent me){}
   
   // MouseListener methods
   public void mouseClicked(MouseEvent me){}
   public void mousePressed(MouseEvent me){}
   public void mouseReleased(MouseEvent me){}
   public void mouseEntered(MouseEvent me){}
   public void mouseExited(MouseEvent me)
   {
      mouseLocTileX = -1;
      mouseLocTileY = -1;
   }
   
   // main for testing
   public static void main(String[] args)
   {
      JFrame frame = new JFrame();
      frame.setSize(1500, 800);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setLayout(new GridLayout(1, 3));
      
      SCTilePalette palette = new SCTilePalette("WidlerTiles_16x16.png", 16, 16, DEFAULT_BG_COLOR);
      
      SCPanel panel1 = new SCPanel(palette, 16, 17);
      SCPanel panel2 = new SCPanel(palette, 16, 17);
      SCPanel panel3 = new SCPanel(palette, 16, 17);
      panel2.setMaintainRatio(false);
      
      panel1.writeLine(0, 0, " Maintain Ratio");
      panel2.writeLine(0, 0, " Don't Maintain");
      
      for(int x = 0; x < 16; x++)
      for(int y = 0; y < 16; y++)
      {
         panel1.setTileIndex(x, y + 1, x, y);
         panel2.setTileIndex(x, y + 1, x, y);
      }
      for(int x = 0; x < 16; x++)
      {
         panel1.setTileFG(x, 5, Color.CYAN.getRGB());
         panel2.setTileFG(x, 5, Color.CYAN.getRGB());
      }
      for(int x = 0; x < 16; x++)
      {
         panel1.setTileBG(x, 6, Color.CYAN.getRGB());
         panel2.setTileBG(x, 6, Color.CYAN.getRGB());
      }
      
      panel1.fillTileFG(0, 0, 4, 4, Color.MAGENTA.getRGB());
      panel1.fillTileBG(2, 2, 4, 4, Color.YELLOW.getRGB());
      
      String str = "The quick brown\nfox jumped over the lazy dog's back.";
      panel3.writeBox(0, 0, 10, 10, str);
      panel3.fillTileBG(0, 0, 10, 10, Color.GRAY.getRGB());
      
      str = "ThisIsThePlaceForWordsThatAreTooBigForTheBoxThatHasBeenAssignedToContainThem";
      panel3.writeBox(0, 11, 16, 4, str);
      panel3.fillTileBG(0, 11, 16, 4, Color.GRAY.getRGB());
      
      Vector<int[]> theList = panel3.findText(0, 11, 16, 4, "The");
      for(int i = 0; i < theList.size(); i++)
      {
         int startX = theList.elementAt(i)[0];
         int startY = theList.elementAt(i)[1];
         panel3.fillTileFG(startX, startY, 3, 1, Color.CYAN.getRGB());
      }
      
      frame.add(panel1);
      frame.add(panel2);
      frame.add(panel3);
      frame.setVisible(true);
      while(true)
      {
         try
         {
//             Thread.sleep(500);
//             int[] loc = panel1.getMouseLocTile();
//             int[] loc2 = panel2.getMouseLocTile();
//             System.out.println(String.format("Mouse at [%d, %d], [%d, %d]", loc[0], loc[1], loc2[0], loc2[1]));
         }
         catch(Exception ex)
         {
            System.out.println("Exception");
            return;
         }
      }
      
   }
}