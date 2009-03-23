import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;

public class OnePiece extends JPanel {

/*
    This is just a test implementation of rendering a single puzzle piece. That's why the code is a mess.
*/
    
/* 
0->    TOP OF PIECE
| X__       ___       ___       _X_    |  ___       ___       X__     ^
vX   \     /   \     /   \     / | \   | /   \     /   \     X        |
 |    \___/     \___/     \___/ /   \___X     \___/     \___/|        |
  \          ^                 |         |                    \    nib row
   \         |                  \       /                      \      |
    |        |                   \_____/                        |     v
L   |        |           ---------------                        |R    ^
E  /         |          |               |                      / I    |
F /          |          |    padding    |                     /  G    |
T|           |          |    -------    |                    |   H    |
 |           |          |   |       |   |                    |   T    |
  \     body height     |   |       |   |                     \       |
S  \         |          |   | LABEL |   |                      \ S label row
I   |        |          |   |       |   |                       |I    |
D   |        |          |   |       |   |                       |D    |
E  /         |          |    -------    |                      / E    |
  /  <-------+----------+---------------+-- Spacing --------> /       |
 |           |          |               |                    |        |
 |           v           ---------------                     |        v
  X__       ___   |   ___|      ___       ___       ___       X__     ^
 / \ \     /   \  |  /   X     /   \     /   \     /   \     / \      |
    | \___/     \X__/     \___/     \___/     \___/     \___/   |     |
 <-->           |          |     ^                                 nib row
 Slanty-         \        / nib descent                               |
 BoxWidth         \______/       v                                    v
BOTTOM OF PIECE                                                         
 <--------------------------- Piece Width ------------------>NEXT SIDE
*/

    public double PieceTop = 0; // Corresponds to TOP OF PIECE
    public double PieceLeft = 0; // Corresponds to LEFT SIDE
    
    public double BorderAmplitude = 1; // pts
    public double BorderWavelength() { return BorderAmplitude * 16; }
    public double BorderStep() { return BorderWavelength() / 8; }
    public double BorderWidth() { return BorderAmplitude * 2; }
    public double NibRowHeight() { return BorderWidth() + NibDescent; }
    public double NibWidth = 8; // pts
    public double NibDescent = 4; // pts
    public double NibHorizontalSpacing = 6; // pts (Must be at least this space between nibs and nibs or sides)
    public double LabelPadding = 2; // pts
    public double LabelMinHeight = 4; // pts
    public double LabelHeight = 24; // calculated
    public double LabelWidth = 60; // calculated
    public double SpaceWidth = 120; // Space needed for child pieces
    public double BodyWidth() { return Math.max(LabelWidth + LabelPadding * 2, SpaceWidth); }
    public double LabelRowHeight() { return NibDescent * 2 + LabelPadding * 2 + LabelHeight; }
    public double SlantyAngle_ = 0; // based on piece geometry (either 70, 0, 20)
    public double SlantyAngle() { return Math.toRadians(SlantyAngle_); }
    public double SlantyBoxHeight() { return NibRowHeight() + LabelRowHeight() + BorderWidth(); }
/*    Simplified from
    	SlantyHeight2 = BorderWidth * sin s
	SlantyWidth2 = BorderWidth * cos s
	SlantyHeight1 = SlantyHeight - SlantyHeight2
	SlantyWidth1 = SlantyHeight1 * tan s
	SlantyWidth = SlantyWidth1 + SlantyWidth2 */
    public double SlantyBoxWidth() { return  Math.tan(SlantyAngle()) * (SlantyBoxHeight() - Math.sin(SlantyAngle()) * BorderWidth()) + Math.cos(SlantyAngle()) * BorderWidth(); }
    public double BodyHeight() { return LabelRowHeight() + NibDescent; }
    public double LabelTop() { return PieceTop + (BodyHeight() - LabelHeight) / 2 + NibDescent; }
    public double PieceWidth() { return SlantyBoxWidth() + BodyWidth(); }
    public double PieceHeight() { return LabelRowHeight() + NibRowHeight(); }
    public int RowNum = 0;
    public double NibRowTop() { return PieceTop + RowNum * PieceHeight(); }
    public double NibRowBorderPhase() { return Math.toRadians(RowNum * 15); }
    public double NibRowBorderWave(double x) { return Math.sin(NibRowBorderPhase() + x * 2 * Math.PI / BorderWavelength()) * BorderAmplitude + NibRowTop() + BorderAmplitude; }
    public int SideNum = 0;
    public double SideCenterY() { return PieceTop + SlantyBoxHeight() / 2; }
    public double SideCenterX() { return PieceLeft + PieceWidth() * SideNum + SlantyBoxWidth() / 2; }
    public double SideBorderPhase() { return Math.toRadians(SideNum * 15); }
    public double SideBorderWave(double y) { return Math.sin(SideBorderPhase() + (y - SideCenterY()) * 2 * Math.PI / BorderWavelength()) * BorderAmplitude + Math.tan(SlantyAngle()) * (y - SideCenterY()) + SideCenterX(); }
    
    // p = phase, w = wavelength, a = amplitude, t = top
    // sine formula: y = sin(p + x * 2 * pi / w) * a + t
    // l = left, slope = 1
    // corner line: (x - l) / (y - t) = 1
    //		y = x - l + t
    // intersection: x - l + t = sin(p + x * 2 * pi / w) * a + t
    
    public Rectangle2D BorderOutRect() {
	return new Rectangle2D.Double(PieceLeft, PieceTop, PieceWidth() +
	    SlantyBoxWidth(), PieceHeight() + NibRowHeight());
    }
    public Rectangle2D BorderInRect() {
	int t = SideNum;
	SideNum = 0;
	Rectangle2D r = new Rectangle2D.Double(SideCenterX(), PieceTop +
	    BorderAmplitude, PieceWidth(), BodyHeight());
	SideNum = t;
	return r;
    }
    public Shape BorderShape() {
	GeneralPath g = new GeneralPath();
	Rectangle2D bi = BorderInRect();
	Rectangle2D bo = BorderOutRect();
	int _r = RowNum, _s = SideNum;
	RowNum = 0;
	int i;
	int p = (int) (bi.getWidth() / BorderStep());
	p += p % 2;
	double s = bi.getWidth() / (p + 1);
	double x = bi.getX();
	// start NW
	g.moveTo((float) x, (float) NibRowBorderWave(x));
	x += s;
	for(i = 1; i < p; i += 2) { // to NE
	    g.quadTo((float) x, (float) NibRowBorderWave(x), 
		(float) (x + s), (float) NibRowBorderWave(x + s));
	    x += s * 2;
	}	
	SideNum = 1;
	p = (int) (bi.getHeight() / BorderStep());
	p += p % 2;
	s = bi.getHeight() / (p + 1);
	double y = bi.getY();
	// do NE corner
	g.quadTo((float) (bi.getMaxX() + bo.getMaxX()) / 2, 
	    (float) (bi.getY() + bo.getY()) / 2,
	    (float) SideBorderWave(y), (float) y);
	y += s;
	for(i = 1; i < p; i += 2) { // to SE
	    g.quadTo((float) SideBorderWave(y), (float) y, 
		(float) SideBorderWave(y + s), (float) (y + s));
	    y += s * 2;
	}
	RowNum = 1;
	p = (int) (bi.getWidth() / BorderStep());
	p += p % 2;
	s = bi.getWidth() / (p + 1);
	x = bi.getMaxX();
	// do SE corner
	g.quadTo((float) (bi.getMaxX() + bo.getMaxX()) / 2, 
	    (float) (bi.getMaxY() + bo.getMaxY()) / 2,
	    (float) x, (float) NibRowBorderWave(x));
	x -= s;
	for(i = 1; i < p; i += 2) { // to SW
	    g.quadTo((float) x, (float) NibRowBorderWave(x), 
		(float) (x - s), (float) NibRowBorderWave(x - s));
	    x -= s * 2;
	}	

	SideNum = 0;
	p = (int) (bi.getHeight() / BorderStep());
	p += p % 2;
	s = bi.getHeight() / (p + 1);
	y = bi.getMaxY();
	// do SW corner
	g.quadTo((float) (bi.getX() + bo.getX()) / 2, 
	    (float) (bi.getMaxY() + bo.getMaxY()) / 2,
	    (float) SideBorderWave(y), (float) y);
	y -= s;
	for(i = 1; i < p; i += 2) { // to NW
	    g.quadTo((float) SideBorderWave(y), (float) y, 
		(float) SideBorderWave(y - s), (float) (y - s));
	    y -= s * 2;
	}
	RowNum = 0;
	x = bi.getX();
	// do NW corner
	g.quadTo((float) (bi.getX() + bo.getX()) / 2, 
	    (float) (bi.getY() + bo.getY()) / 2,
	    (float) x, (float) NibRowBorderWave(x));
	g.closePath();
	RowNum = _r;
	SideNum = _s;
	return g;
    }
    
    public void paint(Graphics g) {
	Graphics2D g2d = (Graphics2D) g;
	g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
	    RenderingHints.VALUE_ANTIALIAS_ON);
	g2d.translate((getBounds().getWidth() - PieceWidth()) / 2, (getBounds().getHeight() - PieceHeight()) / 2);
	g2d.setStroke(new BasicStroke(1.0f));
	g2d.draw(BorderShape());
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
