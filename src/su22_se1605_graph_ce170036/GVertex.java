package su22_se1605_graph_ce170036;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author Pham Nhat Quang CE170036
 */
public class GVertex {

    /**
     * Radius of oval when drawn
     */
    public static final int RADIUS = 12;

    /**
     * Diameter of oval when drawn
     */
    public static final int DIAMETER = RADIUS * 2;

    /**
     * Font of info when drawn
     */
    public static final Font FONT = new Font("Arial", Font.PLAIN, 12);
    private int x, y; //Coordinates to draw
    private int value; //Info of vertex
    private boolean selected = false;//See if being selected

    /**
     * Creates new vertex
     * @param x Horizontal coordinate
     * @param y Vertical coordinate
     * @param value Value of vertex
     */
    public GVertex(int x, int y, int value) {
        this.x = x;
        this.y = y;
        this.value = value;
    }

    /**
     * Get x of vertex
     * @return x
     */
    public int getX() {
        return x;
    }

    /**
     * Set x of vertex
     * @param x value to set
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Get y of vertex
     * @return value of y
     */
    public int getY() {
        return y;
    }

    /**
     * Set Y of vertex
     * @param y value to set
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Get value of this vertex
     * @return Value of vertex
     */
    public int getValue() {
        return value;
    }
    
    /**
     * Get label of this vertex
     * @return Label of vertex
     */
    public String getLabel(){
        return "" + value;
    }

    /**
     * Set value of this vertex
     * @param value value to set
     */
    public void setValue(int value) {
        this.value = value;
    }

    /**
     * Check if this vertex is selected
     * @return Value of selected
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * Set selected status
     * @param isSelected Value to set
     */
    public void setSelected(boolean isSelected) {
        this.selected = isSelected;
    }

    /**
     * Find distance between two points
     * @param x1 x of point 1
     * @param y1 y of point 1
     * @param x2 x of point 2
     * @param y2 y of point 2
     * @return The distance
     */
    public static Double distance(int x1, int y1, int x2, int y2) {
        int _x = x1 - x2;
        int _y = y1 - y2;
        return Math.sqrt(_x * _x + _y * _y);
    }

    /**
     * Check if mouse position is inside of this vertex
     * @param mouseX Mouse horizontal coordinate
     * @param mouseY Mouse vertical coordinate
     * @return True if inside, false if not
     */
    public boolean isInside(int mouseX, int mouseY) {
        return distance(x, y, mouseX, mouseY) <= RADIUS;
    }

    /**
     * Draw this vertex
     * @param g Used to draw
     */
    public void draw(Graphics2D g) {
        g.setColor(isSelected() ? Color.red : Color.white);
        g.fillOval(x - RADIUS, y - RADIUS, DIAMETER, DIAMETER);

        g.setColor(isSelected() ? Color.yellow : Color.black);
        g.drawOval(x - RADIUS, y - RADIUS, DIAMETER, DIAMETER);

        centerString(g, new Rectangle(x - RADIUS, y - RADIUS, DIAMETER, DIAMETER), getLabel(), FONT);
    }

    /**
     * This method centers a <code>String</code> in a bounding
     * <code>Rectangle</code>.
     *
     * @param g - The <code>Graphics</code> instance.
     * @param r - The bounding <code>Rectangle</code>.
     * @param s - The <code>String</code> to center in the bounding rectangle.
     * @param font - The display font of the <code>String</code>
     *
     * @see java.awt.Graphics
     * @see java.awt.Rectangle
     * @see java.lang.String
     */
    public static void centerString(Graphics g, Rectangle r, String s,
            Font font) {
        FontRenderContext frc
                = new FontRenderContext(null, true, true);

        Rectangle2D r2D = font.getStringBounds(s, frc);
        int rWidth = (int) Math.round(r2D.getWidth());
        int rHeight = (int) Math.round(r2D.getHeight());
        int rX = (int) Math.round(r2D.getX());
        int rY = (int) Math.round(r2D.getY());

        int a = (r.width / 2) - (rWidth / 2) - rX;
        int b = (r.height / 2) - (rHeight / 2) - rY;

        g.setFont(font);
        g.drawString(s, r.x + a, r.y + b);
    }
    
    /**
     * Toggle selected state
     */
    public void toggleSelected(){
        this.selected = !this.selected;
    }
}
