package Microbiome;

import processing.core.PApplet;

/**
 * This is a representation of a microbiome.
 */
public class Agent {
    public Vector2<Double> position;   //Current position of the microbiome.
    public double angle = 0.0f;        //Current angle of the microbiome.
    public double moveSpeed = 100.0f;  //Move speed of the single microbiome. 
    public double turnSpeed = 80.0f;   //Turning speed of the single microbiome.

    public int colorR = 220;            //Color R value.
    public int colorG = 225;            //Color G value.
    public int colorB = 255;            //Color B value.

    public final int SENSOR_SIZE = 1;                       //The size of the sensor.
    public final double SENSOR_DISTANCE = 8;                //The distance of the sensor from the position.
    public final double SENSOR_OFFSET_ANGLE = Math.PI / 4;  //The angle offset of each sensor.

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
        int weightForward = sense(mainProgram, SENSOR_DISTANCE, 0, SENSOR_SIZE);
        int weightLeft = sense(mainProgram, SENSOR_DISTANCE, SENSOR_OFFSET_ANGLE, SENSOR_SIZE);
        int weightRight = sense(mainProgram, SENSOR_DISTANCE, -SENSOR_OFFSET_ANGLE, SENSOR_SIZE);

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
        //The int value of color is represented as XRGB
        //which is constructed in [0000 0000 RRRR RRRR GGGG GGGG BBBB BBBB]
        int B_MASK = 255;       //[0000 0000 0000 0000 0000 0000 1111 1111]
        int G_MASK = 255<<8;    //[0000 0000 0000 0000 1111 1111 0000 0000]
        int R_MASK = 255<<16;   //[0000 0000 1111 1111 0000 0000 0000 0000]

        //Use only single color part in bits and shift it to the right.
        //In this way we created a set of valid RGB values.
        int b = sample & B_MASK;
        int g = (sample & G_MASK)>>8;
        int r = (sample & R_MASK)>>16;

        return r + g + b;
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
            newX = Math.min(GlobalSettings.CANVAS_WIDTH - 1, Math.max(0, newX));
            newY = Math.min(GlobalSettings.CANVAS_HEIGHT - 1, Math.max(0, newY));
            angle = random * 2 * Math.PI;
        }

        position.x = newX;
        position.y = newY;
    }

    /**
     * Draw the agent.
     * @param mainProgram The program that renders.
     */
    public void draw(PApplet mainProgram){
        mainProgram.pixels[position.y.intValue() * GlobalSettings.CANVAS_WIDTH + position.x.intValue()] = mainProgram.color(colorR, colorG, colorB);
    }
}
