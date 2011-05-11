package quicktests;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class OnePiece2 extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6304004113864637817L;

	@Override
	public void paint(final Graphics g) {
		final Graphics2D g2d = (Graphics2D) g;
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

	public static void main(final String args[]) {
		final JFrame f = new JFrame("OnePiece");
		f.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent e) {System.exit(0);}
		});
		final OnePiece op = new OnePiece();
		f.getContentPane().add("Center", op);
		f.setSize(300, 300);
		f.pack();
		f.setVisible(true);
	}
}
