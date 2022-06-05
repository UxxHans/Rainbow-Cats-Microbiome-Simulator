package Microbiome;

import processing.core.PApplet;

/**
 * This is a representation of a microbiome.
 */
public class Agent {
    private Vector2<Double> position;   //Current position of the microbiome.
    private double angle = 0.0f;        //Current angle of the microbiome.
    private double moveSpeed = 100.0;  //Move speed of the single microbiome. 
    private double turnSpeed = 80.0;   //Turning speed of the single microbiome.

    private int colorR = 255;            //Color R value.
    private int colorG = 255;            //Color G value.
    private int colorB = 255;            //Color B value.

    private int sensorSize = 1;                       //The size of the sensor.
    private double sensorDistance = 8;                //The distance of the sensor from the position.
    private double sensorOffsetAngle = Math.PI / 4;   //The angle offset of each sensor.

    private final int BORDER_SIZE = 15;

    public Agent(Vector2<Double> position, double angle, double moveSpeed, double turnSpeed,
     int sensorSize, double sensorDistance, double sensorOffsetAngle, int colorR, int colorG, int colorB) {

        this.position = position;
        this.angle = angle;

        this.moveSpeed = moveSpeed;
        this.turnSpeed = turnSpeed;

        this.sensorSize = sensorSize;
        this.sensorDistance = sensorDistance;
        this.sensorOffsetAngle = sensorOffsetAngle;

        this.colorR = colorR;
        this.colorG = colorG;
        this.colorB = colorB;
    }

    public Agent(Vector2<Double> position, double angle) {
        this.position = position;
        this.angle = angle;
    }

    /**
     * Sense the trail of other agents.
     * @param mainProgram The program that renders.
     * @param sensorDistance The distance from the position.
     * @param sensorAngleOffset The angle offset from the curren direction.
     * @param sensorSize The size of the sensor.
     * @return The sum of RGB value sensed within the sensor..
     */
    public int sense(PApplet mainProgram, double sensorDistance, double sensorAngleOffset, int sensorSize){
        double sensorAngle = angle + sensorAngleOffset;
        Vector2<Double> direction = new Vector2<Double>(Math.cos(sensorAngle), Math.sin(sensorAngle));
        Vector2<Double> sensorPos = new Vector2<Double>(position.x + direction.x * sensorDistance, position.y + direction.y * sensorDistance);

        int sum = 0;
        for(int x = -sensorSize; x <= sensorSize; x++){
            for(int y = -sensorSize; y <= sensorSize; y++){
                int pos = (sensorPos.y.intValue() + y) * GlobalSettings.CANVAS_WIDTH + (sensorPos.x.intValue() + x);

                if(sensorPos.x + x < 0 || sensorPos.x + x >= GlobalSettings.CANVAS_WIDTH || sensorPos.y + y < 0 || sensorPos.y + y >= GlobalSettings.CANVAS_HEIGHT) continue;
                sum += sumRGB(mainProgram.pixels[pos]);
            }
        }

        return sum;
    }

    /**
     * Steer the angle according to the sensors.
     * @param mainProgram The main program that renders.
     * @param randomSteerStrength The random number generated.
     */
    public void steer(PApplet mainProgram, double randomSteerStrength){
        int weightForward = sense(mainProgram, sensorDistance, 0, sensorSize);
        int weightLeft = sense(mainProgram, sensorDistance, sensorOffsetAngle, sensorSize);
        int weightRight = sense(mainProgram, sensorDistance, -sensorOffsetAngle, sensorSize);

        //Move forward if the weight forward is dominant.
        if(weightForward > weightLeft && weightForward > weightRight){
            angle += 0;
        }
        //Move randomly if the weight left and right is larger than forward.
        else if(weightForward < weightLeft && weightForward < weightRight){
            angle += (randomSteerStrength - 0.5) * 2 * turnSpeed * GlobalSettings.DELTA_TIME;
        }
        //Move Right
        else if(weightLeft < weightRight){
            angle -= randomSteerStrength * turnSpeed * GlobalSettings.DELTA_TIME;
        }
        //Move Left
        else if(weightLeft > weightRight){
            angle += randomSteerStrength * turnSpeed * GlobalSettings.DELTA_TIME;
        }
    }

    /**
     * Sum the RGB values.
     * @param sample The color int.
     * @return The sum of RGB.
     */
    public int sumRGB(int sample){
        int[] color = getColor(sample);
        return color[0] + color[1] + color[2];
    }

    /**
     * Convert int color value to RGB int array.
     * @param sample The int color value.
     * @return Returns the RGB int array. rgb[0]=R, rgb[1]=G, rgb[2]=B. 
     */
    public int[] getColor(int sample){
        //The int value of color is represented as XRGB
        //which is constructed in [0000 0000 RRRR RRRR GGGG GGGG BBBB BBBB]
        int B_MASK = 255;       //[0000 0000 0000 0000 0000 0000 1111 1111]
        int G_MASK = 255<<8;    //[0000 0000 0000 0000 1111 1111 0000 0000]
        int R_MASK = 255<<16;   //[0000 0000 1111 1111 0000 0000 0000 0000]

        //Use only single color part in bits and shift it to the right.
        //In this way we created a set of valid RGB values.
        int[] rgb = new int[3];
        rgb[2] = sample & B_MASK;           //B
        rgb[1] = (sample & G_MASK)>>8;      //G
        rgb[0] = (sample & R_MASK)>>16;     //R

        return rgb;
    }

    /**
     * Move the agent.
     * @param mainProgram The program that renders.
     */
    public void tick(PApplet mainProgram){
        double random = Math.random();
        Vector2<Double> direction = new Vector2<Double>(Math.cos(angle), Math.sin(angle));
        
        double newX = position.x + direction.x * moveSpeed * GlobalSettings.DELTA_TIME;
        double newY = position.y + direction.y * moveSpeed * GlobalSettings.DELTA_TIME;

        steer(mainProgram, random);

        if(newX < 0 || newX >= GlobalSettings.CANVAS_WIDTH || newY < 0 || newY >= GlobalSettings.CANVAS_HEIGHT){
            newX = Math.min(GlobalSettings.CANVAS_WIDTH - BORDER_SIZE, Math.max(BORDER_SIZE, newX));
            newY = Math.min(GlobalSettings.CANVAS_HEIGHT - BORDER_SIZE, Math.max(BORDER_SIZE, newY));
            angle = random * 2 * Math.PI;
        }

        position.x = newX;
        position.y = newY;
    }

    /**
     * Draw the agent. Each agent add the value of the pixel with its color.
     * @param mainProgram The program that renders.
     */
    public void draw(PApplet mainProgram){
        
        int originalColor = mainProgram.pixels[position.y.intValue() * GlobalSettings.CANVAS_WIDTH + position.x.intValue()];
        int[] originalRGB = getColor(originalColor);

        int finalR = Math.min(255, originalRGB[0] + colorR);
        int finalG = Math.min(255, originalRGB[1] + colorG);
        int finalB = Math.min(255, originalRGB[2] + colorB);
          
        mainProgram.pixels[position.y.intValue() * GlobalSettings.CANVAS_WIDTH + position.x.intValue()] = mainProgram.color(finalR, finalG, finalB);
    }
}
