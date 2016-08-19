package demo;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import bibliothek.gui.dock.common.*;
import bibliothek.gui.dock.common.menu.SingleCDockableListMenuPiece;
import bibliothek.gui.dock.facile.menu.RootMenuPiece;

public class ExampleDockingTemplate
{
	
	
	static SingleCDockable dockable = createDockable( "Cyan", Color.CYAN ) ;
	
	static int count=0;
	
	
    public static void main( String[] args )
        {
                JFrame frame = new JFrame( "Demo" );
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                CControl control = new CControl( frame );
                
                frame.add( control.getContentArea() );
                
                
                CGrid grid = new CGrid( control );
                
                SingleCDockable redDockableToAddTo = createDockable( "Red", 		Color.RED );
                
                grid.add( 0, 1, 1, 1, redDockableToAddTo );
                grid.add( 1, 0, 1, 1, createDockable( "Blue", 		Color.BLUE ) );
                grid.add( 1, 1, 1, 1, createDockable( "Yellow", 	Color.YELLOW ) );
                

                control.getContentArea().deploy( grid );
                
                SingleCDockable black = createDockable( "Black", Color.BLACK );
                control.addDockable( black );
                black.setLocation( CLocation.base().minimalNorth() );
                black.setVisible( true );
                
                
                RootMenuPiece menu = new RootMenuPiece( "Colors", false );
                menu.add( new SingleCDockableListMenuPiece( control ));
                JMenuBar menuBar = new JMenuBar();
                menuBar.add( menu.getMenu() );
                
                
                JButton itm = new JButton("Create Frame");
                itm.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent arg0) {
						SingleCDockable d = createDockable( "Yellow23"+count, Color.YELLOW );
						//grid.add( 2+count, 1, 1, 1, d );
						control.addDockable(d);
						d.setLocation( CLocation.base().normalEast(0.3) );
						d.setVisible( true );
						count++;
					}
				});
                menuBar.add(itm);
                
                frame.setJMenuBar( menuBar );
                
                frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
                frame.setBounds( 20, 20, 400, 400 );
                frame.setVisible( true );
                
            
                
                javax.swing.SwingUtilities.invokeLater(new Runnable()
                {
                	@Override
                	public void run()
                	{
                		SingleCDockable dockableAddedToDocable = createDockable("AnotherDockable", Color.BLACK );
                        @SuppressWarnings("static-access")
						CLocation locationToAddTo = redDockableToAddTo.getBaseLocation().base().normalEast(0.25);
                        dockableAddedToDocable.setLocation(locationToAddTo);
                        control.addDockable(dockableAddedToDocable);
                        dockableAddedToDocable.setVisible(true);
                	}
                });
                
                
                
        }
        
        public static SingleCDockable createDockable( String title, Color color )
        {
                JPanel panel = new JPanel();
                panel.setOpaque( true );
                panel.setBackground( color );
                DefaultSingleCDockable dockable = new DefaultSingleCDockable( title, title, panel );
                dockable.setCloseable( true );
                return dockable;
        }
}