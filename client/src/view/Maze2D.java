package view;



import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;

public class Maze2D extends MazeDisplayer{

	public int characterX;
	public int characterY;
	public int exitX;
	public int exitY;
	private int z;
	private GameSound sound1;
	private GameSound sound2;
	 public Maze2D(Composite parent,int style){
	        super(parent, SWT.DOUBLE_BUFFERED);
	        this.sound1=new GameSound("elmar.wav");
	        this.sound1.play();
	        this.sound2=new GameSound("Engine-Noise.wav");
	    	// set a white background   (red, green, blue)
	    	setBackground(new Color(null, 255, 255, 255));
	    	addPaintListener(new PaintListener() {
				
				@Override
				public void paintControl(PaintEvent e) {
					  e.gc.setBackground(new Color(null,0,0,0));
					  Image image=new Image(e.display, "sources/elmar1.jpg");
					  Image image2=new Image(e.display, "sources/bugs3.png");
					  Image image3=new Image(e.display, "sources/wall5.jpg");
					   int width=getSize().x;
					   int height=getSize().y;

					   int w=width/mazeData[0].length;
					   int h=height/mazeData.length;

					   for(int i=0;i<mazeData.length;i++)
					      for(int j=0;j<mazeData[i].length;j++){
					          int x=j*w;
					          int y=i*h;
					          if(mazeData[i][j]!=0)
					              //e.gc.fillRectangle(x,y,w,h);
					        	  e.gc.drawImage(image3, 0, 0, image3.getBounds().width, image3.getBounds().height,x, y, (int)w,(int)h);
					        
					          if (j==characterX && i==characterY) {
					        /*
					        	 e.gc.setBackground(new Color(null,255,0,0));
					        	 e.gc.fillRectangle(x, y, w, h);
					        	 e.gc.setBackground(new Color(null,0,0,0));
					        	 
					           */
					        	  e.gc.drawImage(image, 0, 0, image.getBounds().width, image.getBounds().height,x, y, (int)w,(int)h);
				
					        	
					 

					          }
					          if (j==exitX && i==exitY) {
					        	  /*
						        	 e.gc.setBackground(new Color(null,0,255,0));
						        	 e.gc.fillRectangle(x, y, w, h);
						        	 e.gc.setBackground(new Color(null,0,0,0));
						          */
						        	 e.gc.drawImage(image2, 0, 0, image2.getBounds().width, image2.getBounds().height,x, y, (int)w,(int)h);
						        	


						          }       
					      }
					}
			});
	 }
	 

		private boolean moveCharacter(int x,int y){
			if(x>=0 && x<mazeData[0].length && y>=0 && y<mazeData.length && mazeData[y][x]==0){
				characterX=x;
				characterY=y;
				this.sound2.play();
				redraw();
				return true;
			}
			return false;
		}
		
		/* (non-Javadoc)
		 * @see view.MazeDisplayer#moveUp()
		 */
		@Override
		public boolean moveUp() {
			int x=characterX;
			int y=characterY;
			y=y-1;
			return moveCharacter(x, y);

		}
		/* (non-Javadoc)
		 * @see view.MazeDisplayer#moveDown()
		 */
		@Override
		public boolean moveDown() {
			int x=characterX;
			int y=characterY;
			y=y+1;
			return moveCharacter(x, y);
		}
		
		/* (non-Javadoc)
		 * @see view.MazeDisplayer#moveLeft()
		 */
		@Override
		public boolean moveLeft() {
			int x=characterX;
			int y=characterY;
			x=x-1;
			return moveCharacter(x, y);
		}
		
		/* (non-Javadoc)
		 * @see view.MazeDisplayer#moveRight()
		 */
		@Override
		public boolean moveRight() {
			int x=characterX;
			int y=characterY;
			x=x+1;
			return moveCharacter(x, y);
		}
		
		@Override
		public boolean setCharacterPosition(int row, int col) {
			characterX=col;
			characterY=row;
			return moveCharacter(col,row);
		}

		@Override
		public void setGoalPostion(int x,int y)
		{
			this.exitX=y;
			this.exitY=x;
			redraw();
			/*
			getDisplay().syncExec(new Runnable() {
				
				@Override
				public void run() {
					redraw();
				}
			});
			*/
		}

		@Override
		public void setLevel(int z) {
			this.z=z;
		}

		@Override
		public int getLevel() {
			return this.z;  
		}


	

}
