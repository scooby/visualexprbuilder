package quicktests;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import javax.swing.*;
import net.samuel.ben.*;

public class OnePiece extends JPanel {
	private static final long serialVersionUID = -2574375926597058507L;

	public OnePiece() {
	super();
	inset = new Rectangle2D.Double();
	strk = new BasicStroke(1.0f);
    }
    Rectangle2D.Double inset;
    BasicStroke strk;
    
    private Point2D p(double mx, double my) {
	Rectangle2D r = inset;
	return new Point2D.Double(r.getX() + mx * r.getWidth(), r.getY() + my * r.getHeight());
    }
    private double px(double x) {
	Rectangle2D r = inset;
	return r.getX() + x * r.getWidth();
    }
    private double py(double y) {
	Rectangle2D r = inset;
	return r.getY() + y * r.getHeight();
    }
    private double dx(double x) {
	Rectangle2D r = inset;
	return x * r.getWidth();
    }
    private double dy(double y) {
	Rectangle2D r = inset;
	return y * r.getHeight();
    }
/*public SquigglyPath(Point2D from, Point2D to, double phase, double wavelength, double amplitude) 
    public static double nextPhase(double p, double wl, double l)
public NibPath(double _x, double _y, double _w, double _h, Class<?> c) */
    public void paint(Graphics g) {
	Rectangle2D r = getBounds();
	inset.setRect(r.getX() + 0.05 * r.getWidth(),
	    r.getY() + 0.05 * r.getHeight(), r.getWidth() * 0.9, r.getHeight() * 0.9);

	GeneralPath gp = new GeneralPath();
	gp.append(SegmentPath.moveto(p(0.0, 0.0)), false);
	double w = dx(0.3); double a = 3.0; 
	// North border
	double hp = 0.0;
	gp.append(new SquigglyPath(p(0.0, 0.0), p(0.4, 0.0), hp, w, a), false);
	gp.append(NibPath.rpf(px(0.4), py(0.0), dx(0.2), dy(0.2),
	    0x384a3315).forwardPath(), false);
	hp = SquigglyPath.nextPhase(hp, w, dx(0.6));
	gp.append(new SquigglyPath(p(0.6, 0.0), p(1.0, 0.0), hp, w, a), false);
	// East
	double vp = 0.3;
	gp.append(new SquigglyPath(p(1.0, 0.0), p(1.0, 0.8), vp, w, a), false);
	// South
	hp = 0.5;
	gp.append(new SquigglyPath(p(1.0, 0.8), p(0.6, 0.8), hp, w, a), false);
	gp.append(NibPath.rpf(px(0.4), py(0.8), dx(0.2), dy(0.2), 
	    0x384a3315).reversePath(), false);
	hp = SquigglyPath.nextPhase(hp, w, dx(0.6));
	gp.append(new SquigglyPath(p(0.4, 0.8), p(0.0, 0.8), hp, w, a), false);
	// West
	vp = 0.8;
	gp.append(new SquigglyPath(p(0.0, 0.8), p(0.0, 0.0), vp, w, a), false);
	gp.append(SegmentPath.close(), false);
	Graphics2D g2d = (Graphics2D) g;
	g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
	    RenderingHints.VALUE_ANTIALIAS_ON);
	//g2d.fill(inset);
	//gp.append(new CompoundPath(p), false);
	g2d.setPaint(Color.GRAY);
	g2d.fill(gp);
	g2d.setPaint(Color.BLACK);
	g2d.setStroke(strk);
	g2d.draw(gp);
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
