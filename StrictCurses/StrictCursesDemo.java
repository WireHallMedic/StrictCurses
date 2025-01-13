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
      
   public StrictCursesDemo()
   {
      super();
      setSize(1500, 800);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setLayout(new GridLayout(1, 3));
      
      SCTilePalette palette = new SCTilePalette("WidlerTiles_16x16.png", 16, 16, DEFAULT_BG_COLOR);
      
      panel1 = new SCPanel(palette, 16, 17);
      panel2 = new SCPanel(palette, 16, 17);
      panel3 = new SCPanel(palette, 16, 17);
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
      
      add(panel1);
      add(panel2);
      add(panel3);
      setVisible(true);
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
   
   public void actionPerformed(ActionEvent ae)
   {
   
   }
}