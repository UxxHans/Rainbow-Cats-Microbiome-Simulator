package Microbiome.AgentPresets;

public class AgentPreset {
    public String name;                 //Name of the preset.
    public double moveSpeed;            //Move speed of the single microbiome. 
    public double turnSpeed;            //Turning speed of the single microbiome.

    public int sensorSize;              //The size of the sensor.
    public double sensorDistance;       //The distance of the sensor from the position.
    public double sensorOffsetAngle;    //The angle offset of each sensor.

    public int colorR;                  //Color R value.
    public int colorG;                  //Color G value.
    public int colorB;                  //Color B value.

    public AgentPreset(String name, double moveSpeed, double turnSpeed, int sensorSize, double sensorDistance, 
    double sensorOffsetAngle, int colorR, int colorG, int colorB) {

        this.name = name;
        this.moveSpeed = moveSpeed;
        this.turnSpeed = turnSpeed;
        this.sensorSize = sensorSize;
        this.sensorDistance = sensorDistance;
        this.sensorOffsetAngle = sensorOffsetAngle;
        this.colorR = colorR;
        this.colorG = colorG;
        this.colorB = colorB;
    }
}
