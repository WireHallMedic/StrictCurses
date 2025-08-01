package StrictCurses;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class StrictCursesDemo extends JFrame implements SCConstants, ActionListener
{
   private SCPanel panel1;
   private SCPanel panel2;
   private SCPanel panel3;
   
   private int lastPanel = -1;
   private int lastX = -1;
   private int lastY = -1;
   private int lastFG = 0;
   private int lastBG = 0;
      
   public StrictCursesDemo()
   {
      super();
      setSize(1500, 800);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setLayout(new GridLayout(1, 3));
      
      SCTilePalette palette = new SCTilePalette("/StrictCurses/WidlerTiles_16x16.png", 16, 16, DEFAULT_BG_COLOR);
      
      panel1 = new SCPanel(palette, 16, 17);
      panel2 = new SCPanel(palette, 16, 17);
      panel3 = new SCPanel(palette, 16, 20);
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
      panel3.writeBox(0, 0, 10, 6, str);
      panel3.fillTileBG(0, 0, 10, 6, Color.GRAY.getRGB());
      
      str = "ThisIsThePlaceForWordsThatAreTooBigForTheBoxThatHasBeenAssignedToContainThem";
      panel3.writeBox(0, 7, 16, 3, str);
      panel3.fillTileBG(0, 7, 16, 3, Color.GRAY.getRGB());
      
      Vector<int[]> theList = panel3.findText(0, 7, 16, 3, "The");
      for(int i = 0; i < theList.size(); i++)
      {
         int startX = theList.elementAt(i)[0];
         int startY = theList.elementAt(i)[1];
         panel3.fillTileFG(startX, startY, 3, 1, Color.CYAN.getRGB());
      }
      
      setBoxTiles();
      setWallTiles();
      
      add(panel1);
      add(panel2);
      add(panel3);
      setVisible(true);
      
      javax.swing.Timer timer = new javax.swing.Timer(1000 / 60, this);
      timer.start();
   }
   
   private int[][] getAdjacencyArray(char[][] map)
   {
      int width = map.length;
      int height = map[0].length;
      int[][] adjacencyArr = new int[width][height];
      for(int x = 0; x < width - 1; x++)
      for(int y = 0; y < height; y++)
         if(map[x][y] == '#')
            adjacencyArr[x+1][y] += ADJACENT_LEFT;
      for(int x = 1; x < width; x++)
      for(int y = 0; y < height; y++)
         if(map[x][y] == '#')
            adjacencyArr[x-1][y] += ADJACENT_RIGHT;
      for(int x = 0; x < width; x++)
      for(int y = 0; y < height - 1; y++)
         if(map[x][y] == '#')
            adjacencyArr[x][y+1] += ADJACENT_TOP;
      for(int x = 0; x < width; x++)
      for(int y = 1; y < height; y++)
         if(map[x][y] == '#')
            adjacencyArr[x][y-1] += ADJACENT_BOTTOM;
      return adjacencyArr;
   }
   
   private void setBoxTiles()
   {
      char[][] boxArr = {{'#','#','#','#','#'},
                         {'#',' ','#',' ','#'},
                         {'#','#','#','#','#'},
                         {'#',' ','#',' ','#'},
                         {'#','#','#','#','#'}};
      int width = boxArr.length;
      int height = boxArr[0].length;
      int[][] adjacencyArr = getAdjacencyArray(boxArr);
      
      int startX = 0;
      int startY = 10;
      for(int x = 0; x < width; x++)
      for(int y = 0; y < height; y++)
         if(boxArr[x][y] == '#')
            panel3.setTileIndex(x + startX, y + startY, BOX_TILE_ORIGIN + adjacencyArr[x][y]);
   }
   
   private void setWallTiles()
   {
      char[][] wallArr = {{' ',' ','#',' ','#','#',' '},
                          {'#','#','#','#','#','#','#'},
                          {'#','#',' ','#',' ','#','#'},
                          {' ','#','#','#','#','#',' '},
                          {'#','#',' ','#',' ','#','#'},
                          {' ','#','#','#','#','#',' '},
                          {' ',' ','#',' ','#','#',' '}};
      int width = wallArr.length;
      int height = wallArr[0].length;
      int[][] adjacencyArr = getAdjacencyArray(wallArr);
      
      int startX = 8;
      int startY = 10;
      for(int x = 0; x < width; x++)
      for(int y = 0; y < height; y++)
         if(wallArr[x][y] == '#')
            panel3.setTileIndex(x + startX, y + startY, WALL_TILE_ORIGIN + adjacencyArr[x][y]);
   }
   
   public void actionPerformed(ActionEvent ae)
   {
      int xLoc = Math.max(Math.max(panel1.getMouseLocTile()[0], panel2.getMouseLocTile()[0]), panel3.getMouseLocTile()[0]);
      int yLoc = Math.max(Math.max(panel1.getMouseLocTile()[1], panel2.getMouseLocTile()[1]), panel3.getMouseLocTile()[1]);
      int panelNum = 1;
      if(panel2.getMouseLocTile()[0] != -1)
         panelNum = 2;
      if(panel3.getMouseLocTile()[0] != -1)
         panelNum = 3;
      panel3.fillTile(0, 19, 19, 1, ' ', DEFAULT_FG_COLOR, DEFAULT_BG_COLOR);
      panel3.writeLine(0, 19, String.format("Panel%d [%d][%d]", panelNum, xLoc, yLoc));
      flipColors(panelNum, xLoc, yLoc);
      repaint();
   }
   
   private void flipColors(int p, int x, int y)
   {
      // no change if in same tile
      if(p == lastPanel && x == lastX && y == lastY)
         return;
      
      SCPanel curPanel = null;
      
      // revert previous
      if(lastX != -1 && lastY != -1)
      {
         switch(lastPanel)
         {
            case 1 : curPanel = panel1; break;
            case 2 : curPanel = panel2; break;
            case 3 : curPanel = panel3; break;
         }
         if(curPanel != null)
         {
            curPanel.setTileFG(lastX, lastY, lastFG);
            curPanel.setTileBG(lastX, lastY, lastBG);
            lastFG = -1;
            lastBG = -1;
            lastPanel = -1;
            curPanel = null;
         }
      }
      
      // no change if mouse not in area
      if(x == -1 || y == -1)
         return;
      
      // switch colors of current tile
      switch(p)
      {
         case 1 : curPanel = panel1; break;
         case 2 : curPanel = panel2; break;
         case 3 : curPanel = panel3; break;
      }
      if(curPanel != null)
      {
         lastX = x;
         lastY = y;
         lastPanel = p;
         lastFG = curPanel.getTileFGColor(x, y);
         lastBG = curPanel.getTileBGColor(x, y);
         curPanel.setTileFG(x, y, lastBG);
         curPanel.setTileBG(x, y, lastFG);
      }
      
   }
   
   public static void main(String[] args)
   {
      StrictCursesDemo scd = new StrictCursesDemo();
   }
}