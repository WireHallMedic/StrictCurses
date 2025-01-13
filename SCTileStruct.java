/*
A class for holding the values for creating a tile; character index, foreground RGB, and background RGB.

We store these as the individual tile values are not otherwise stored.
*/

package StrictCurses;

public class SCTileStruct implements SCConstants
{
	private int characterIndex;
	private int fgRGB;
	private int bgRGB;


	public int getCharacterIndex(){return characterIndex;}
	public int getFGRGB(){return fgRGB;}
	public int getBGRGB(){return bgRGB;}


	public void setCharacterIndex(int c){characterIndex = c;}
	public void setFGRGB(int fg){fgRGB = fg;}
	public void setBGRGB(int bg){bgRGB = bg;}
   
   public SCTileStruct()
   {
      this(0, DEFAULT_FG_COLOR, DEFAULT_BG_COLOR);
   }
   
   public SCTileStruct(int i, int fg, int bg)
   {
      setCharacterIndex(i);
      setFGRGB(fg);
      setBGRGB(bg);
   }
   
   public void set(int i, int fg, int bg)
   {
      setCharacterIndex(i);
      setFGRGB(fg);
      setBGRGB(bg);
   }
   
   public boolean matches(int index, int fg, int bg)
   {
      return matchesIndex(index) &&
             matchesFG(fg) &&
             matchesBG(bg);
   }
   
   public boolean matchesIndex(int index)
   {
      return index == characterIndex;
   }
   
   public boolean matchesFG(int fg)
   {
      return fg == fgRGB;
   }
   
   public boolean matchesBG(int bg)
   {
      return bg == bgRGB;
   }
}