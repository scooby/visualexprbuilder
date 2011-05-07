package quicktests;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;

public class OnePiece2 extends JPanel {

	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.translate((getBounds().getWidth() - PieceWidth()) / 2, (getBounds().getHeight() - PieceHeight()) / 2);
		g2d.setStroke(new BasicStroke(1.0f));
		g2d.draw(BorderShape());
	}

	private double PieceHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	private double PieceWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	private Shape BorderShape() {
		// TODO Auto-generated method stub
		return null;
	}

	public static void main(String args[]) {
		JFrame f = new JFrame("OnePiece");
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {System.exit(0);}
		});
		OnePiece op = new OnePiece();
		f.getContentPane().add("Center", op);
		f.setSize(300, 300);
		f.pack();
		f.setVisible(true);
	}
}
