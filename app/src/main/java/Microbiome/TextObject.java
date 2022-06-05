package Microbiome;

import processing.core.PApplet;
import processing.core.PFont;

/**
 * Represents a text object that can be used as UI in the game.
 */
public class TextObject {

    protected int x;            //The Text's x-coordinate.
    protected int y;            //The Text's y-coordinate.
    protected int size;         //The Text's size.
    private String text;        //The Text's content.
    protected PFont font;       //The Text's font.
    protected int textAlign;    //The Text's align format.

    /**
     * Creates a new Text object.
     * @param x Position X.
     * @param y Position Y.
     * @param size Size of the text.
     * @param text String of the content in the text component.
     * @param color Color of the text, can use the preset in the enum.
     * @param font The font of the text.
     */
    public TextObject(int x, int y, int size, String text, PFont font, int textAlign) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.text = text;
        this.font = font;
        this.textAlign = textAlign;
    }

    /**
     * Draws the Text to the screen. Handles the graphics.
     * @param app The main program.
     */
    public void draw(PApplet app) {
        // The text() method is used to draw string onto the screen.
        // The first argument is the text, the second and third arguments are coordinates
        app.textFont(font);
        app.textSize(size);
        app.textAlign(textAlign, PApplet.CENTER);
        app.fill(255);
        app.text(this.text, this.x, this.y);
    }

    /**
     * Returns the x-coordinate.
     * @return Position X
     */
    public int getX() {
        return this.x;
    }

    /**
     * Returns the y-coordinate.
     * @return Position Y.
     */
    public int getY() {
        return this.y;
    }

    /**
     * Sets the x-coordinate.
     * @param x Position X.
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Sets the y-coordinate.
     * @param y Position Y.
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Sets the position on screen.
     * @param x Position X.
     * @param y Position Y.
     */
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Get the size of the text.
     * @return Size of the text.
     */
    public int getSize() {
        return size;
    }

    /**
     * Set the size of the text.
     * @param size The value of the size.
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * Get the text content in the object
     * @return The content String.
     */
    public String getText() {
        return text;
    }

    /**
     * Set the text content in the object.
     * @param text The content String.
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Get the font of the current text object.
     * @return The font of the text.
     */
    public PFont getFont() {
        return font;
    }

    /**
     * Set the font of the current text object.
     * @param font The font to be set.
     */
    public void setFont(PFont font) {
        this.font = font;
    }
}