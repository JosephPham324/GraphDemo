package su22_se1605_graph_ce170036;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 *
 * @author Pham Nhat Quang CE170036
 */
public class GEdge {

    /**
     * Padding x
     */
    public static final int PADDING_X = 12;

    /**
     * Padding y
     */
    public static final int PADDING_Y = 8;

    /**
     * Label width
     */
    public static final int LABEL_W = PADDING_X * 2;

    /**
     * Label height
     */
    public static final int LABEL_H = PADDING_Y * 2;

    /**
     * Label radius (to draw round corner)
     */
    public static final int LABEL_R = 10;

    /**
     * Font used when display
     */
    public static final Font FONT = new Font("Arial", Font.PLAIN, 10);
    private int value;
    private GVertex start;
    private GVertex end;
    private boolean selected; 
    private int x, y; //Center location of the edge

    /**
     * Create new Graph Edge
     * @param value Value of edge
     * @param start Start vertex of edge
     * @param end End vertex of edge
     * @param selected Is this edge selected
     */
    public GEdge(int value, GVertex start, GVertex end, boolean selected) {
        this.value = value;
        this.start = start;
        this.end = end;
        this.selected = selected;
        calculateCenterLocation();
    }

    
    private void calculateCenterLocation() {
        this.x = (start.getX() + end.getX()) / 2;
        this.y = (start.getY() + end.getY()) / 2;
    }

    /**
     * Get value of edge
     * @return value of edge
     */
    public int getValue() {
        return value;
    }

    /**
     * Set value of edge
     * @param value value to set
     */
    public void setValue(int value) {
        this.value = value;
    }

    /**
     * Get start vertex of edge
     * @return start vertex
     */
    public GVertex getStart() {
        return start;
    }

    /**
     * Set start vertex of edge
     * @param start Vertex to set
     */
    public void setStart(GVertex start) {
        this.start = start;
    }

    /**
     * Get end vertex of edge
     * @return End vertex of edge
     */
    public GVertex getEnd() {
        return end;
    }

    /**
     * Set end vertex of edge
     * @param end Vertex to set
     */
    public void setEnd(GVertex end) {
        this.end = end;
    }

    /**
     * Is this edge selected
     * @return selected status of this edge
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * Set selected status of edge
     * @param selected value to set
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /**
     * Get horizontal coordinate of edge center
     * @return value of x
     */
    public int getX() {
        return x;
    }

    /**
     * Set horizontal coordinate of edge center
     * @param x value to set
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Get vertical coordinate of edge center
     * @return value of y
     */
    public int getY() {
        return y;
    }

    /**
     * Set vertical coordinate of edge center
     * @param y value to set
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Check if mouse position is inside of edge
     * @param mouseX x coordinate of mouse
     * @param mouseY y coordinate of mouse
     * @return true if inside of edge, false if not
     */
    public boolean isInside(int mouseX, int mouseY) {
        int x1 = x - PADDING_X;
        int y1 = y - PADDING_Y;
        int x2 = x + PADDING_X;
        int y2 = y + PADDING_Y;
        return x1 <= mouseX && mouseX <= x2 && y1 <= mouseY && mouseY <= y2;
    }

    /**
     * Draw this edge
     * @param g Used to draw
     */
    public void draw(Graphics2D g) {
        g.setColor(isSelected() ? Color.red : Color.black);
        g.drawLine(start.getX(), start.getY(), end.getX(), end.getY());
        
        calculateCenterLocation();
        g.setColor(isSelected() ? Color.yellow : Color.gray);
        g.fillRoundRect(this.x - PADDING_X, this.y - PADDING_Y, LABEL_W, LABEL_H, LABEL_R, LABEL_R);
        g.setColor(isSelected() ? Color.red : Color.black);
        GVertex.centerString(g, new Rectangle(this.x - PADDING_X, this.y - PADDING_Y, LABEL_W, LABEL_H), this.getValue() + "", FONT);
    }
}
