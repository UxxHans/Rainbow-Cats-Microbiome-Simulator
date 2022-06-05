package Microbiome;

import Microbiome.AgentPresets.*;
import processing.core.PApplet;
import processing.core.PFont;

/**
 * The main program of the project.
 */
public class App extends PApplet {

    public Agent[] agents;
    public int totalAgents = 80000;
    public double maxSpawnDistance = 200;
    public double minSpawnDistance = 100;

    public double maxMoveSpeed = 1500;
    public double maxTurnSpeed = 1500;
    public double maxSensorOffsetAngle = 2 * Math.PI;
    public double maxSensorDistance = 200;

    public int currentAgentPresetIndex = 0;
    public AgentPreset[] agentPresets = new AgentPreset[]{
        new DefaultPreset(),
        new BreadBug(),
        new Vein(),
        new Venom(),
        new StarDust(),
        new LiquidGem(),
        new WaterBug(),
        new BloodStealer(),
        new Gene(),
        new Virus(),
        new Alpha(),
        new Beta(),
        new FireFly(),
        new FlourBug(),
        new Parallel(),
        new COVID(),
        new Cell()
    };

    public String keyPressed;
    public PFont font;
    public TextObject titleText;
    public TextObject presetText;
    public TextObject instructionText;

    public TextObject speedText;
    public TextObject sensorText;
    public TextObject colorText;

    /**
     * Initialise the setting of the window size.
    */
    public void settings() {
        size(GlobalSettings.CANVAS_WIDTH, GlobalSettings.CANVAS_HEIGHT + GlobalSettings.BOTTOM_PRESERVE);

        //No smoothing of graphics.
        noSmooth();
    }

    /**
     * Load all resources. Initialise the elements.
    */
    public void setup() {
        //Set frame rate of the game.
        frameRate(GlobalSettings.FRAME_RATE);

        //Set window name.
        surface.setTitle("Microbiome");

        //Set black background.
        background(0);

        //No border.
        noStroke();

        //Setup the texts
        int paddings = 20;
        font = createFont(this.getClass().getResource("Quicksand-Light.ttf").getPath(), 128, true);
        titleText       = new TextObject(paddings, GlobalSettings.CANVAS_HEIGHT + paddings * 1, 15, "MICROBIOME", font, PApplet.LEFT);
        presetText      = new TextObject(paddings, GlobalSettings.CANVAS_HEIGHT + paddings * 2, 11, "Preset", font, PApplet.LEFT);
        instructionText = new TextObject(paddings, GlobalSettings.CANVAS_HEIGHT + paddings * 3, 11, "Left & Right - Switch Species | Enter - Random Species", font, PApplet.LEFT);

        speedText       = new TextObject(GlobalSettings.CANVAS_WIDTH - paddings, GlobalSettings.CANVAS_HEIGHT + paddings * 1, 11, "Speed", font, PApplet.RIGHT);
        sensorText      = new TextObject(GlobalSettings.CANVAS_WIDTH - paddings, GlobalSettings.CANVAS_HEIGHT + paddings * 2, 11, "Sensor", font, PApplet.RIGHT);
        colorText       = new TextObject(GlobalSettings.CANVAS_WIDTH - paddings, GlobalSettings.CANVAS_HEIGHT + paddings * 3, 11, "Color", font, PApplet.RIGHT);

        //Spawn the agents
        spawnAgents();
    }

    /**
     * Spawn agents with next preset.
     */
    public void nextAgentPreset(){
        currentAgentPresetIndex = currentAgentPresetIndex + 1 >= agentPresets.length ? 0 : currentAgentPresetIndex + 1;
        spawnAgents();
    }

    /**
     * Spawn agents with previous preset.
     */
    public void previousAgentPreset(){
        currentAgentPresetIndex = currentAgentPresetIndex - 1 < 0 ? agentPresets.length - 1 : currentAgentPresetIndex - 1;
        spawnAgents();
    }

    /**
     * Set text of the information.
     * @param moveSpeed The movement speed of the agents.
     * @param turnSpeed The turn speed of the agents.
     * @param sensorSize The sensor size of the agents.
     * @param sensorDistance The sensor distance of the agents.
     * @param sensorOffsetAngle The sensor offset angle in radian of the agents.
     * @param colorR The red color value of each agent.
     * @param colorG The green color value of each agent.
     * @param colorB The blue color value of each agent.
     */
    public void setText(double moveSpeed, double turnSpeed, int sensorSize, double sensorDistance, double sensorOffsetAngle, int colorR, int colorG, int colorB){
        speedText.setText("Move Speed: " + (int)moveSpeed + " Nm/s | Turn Speed: " + (int)turnSpeed + " Nm/s");
        sensorText.setText("Sensor Distance: " + (int)sensorDistance + " Nm | Sensor Offset Angle: " + (int)(sensorOffsetAngle * 100) / 100.0 + " Rad");
        colorText.setText("Red: " + colorR + " | Green: " + colorG + " | Blue: " + colorB);
    }

    /**
     * Spawn agents with current values in class.
     */
    public void spawnAgents(){
        AgentPreset agentPreset = agentPresets[currentAgentPresetIndex];
        presetText.setText(agentPreset.name);
        spawnAgents(totalAgents, minSpawnDistance + Math.random() * (maxSpawnDistance - minSpawnDistance), agentPreset);
    }

    /**
     * Spawn agents with preset.
     * @param agentPreset The preset of the agent.
     */
    public void spawnAgents(int totalAgents, double spawnAreaSize, AgentPreset agentPreset){
        spawnAgents(totalAgents, spawnAreaSize, agentPreset.moveSpeed, agentPreset.turnSpeed, 
        agentPreset.sensorSize, agentPreset.sensorDistance, agentPreset.sensorOffsetAngle, 
        agentPreset.colorR, agentPreset.colorG, agentPreset.colorB);
    }

    /**
     * Spawn random agent.
     */
    public void spawnAgentsRandom(){
        presetText.setText("Random Species RAND-" + (int)(Math.random() * 1000));
        spawnAgents(totalAgents, minSpawnDistance + Math.random() * (maxSpawnDistance - minSpawnDistance), 
        Math.random() * maxMoveSpeed,  Math.random() * maxTurnSpeed,
        1,  Math.random() * maxSensorDistance, Math.random() * maxSensorOffsetAngle,
        (int)(Math.random() * 255 / 3), (int)(Math.random() * 255 / 3), (int)(Math.random() * 255 / 3));
    }

    /**
     * Spawn agents with parameters.
     * @param totalAgents Total agents to be spawned.
     * @param spawnAreaSize Maximum distance of the spawn area.
     * @param moveSpeed The movement speed of the agents.
     * @param turnSpeed The turn speed of the agents.
     * @param sensorSize The sensor size of the agents.
     * @param sensorDistance The sensor distance of the agents.
     * @param sensorOffsetAngle The sensor offset angle in radian of the agents.
     * @param colorR The red color value of each agent.
     * @param colorG The green color value of each agent.
     * @param colorB The blue color value of each agent.
     */
    public void spawnAgents(int totalAgents, double spawnAreaSize, double moveSpeed, double turnSpeed, int sensorSize, double sensorDistance, double sensorOffsetAngle, int colorR, int colorG, int colorB){
        //Set black background.
        background(0);

        //Spawn agents in a circle.
        agents = new Agent[totalAgents];

        //If the spawn area is larger than screen size, set the spawn area to the maximum of screen size.
        int midWidth = GlobalSettings.CANVAS_WIDTH / 2;
        int midHeight = GlobalSettings.CANVAS_HEIGHT / 2;
        if(spawnAreaSize >= midWidth || spawnAreaSize >= midHeight) spawnAreaSize = Math.min(midWidth, midHeight);

        for(int i=0; i<totalAgents; i++){
            
            double randomAngle = 2 * Math.PI * Math.random();
            double randomDistance = spawnAreaSize * Math.random();

            double randomPosX = GlobalSettings.CANVAS_WIDTH / 2.0 + Math.cos(randomAngle) * randomDistance;
            double randomPoxY = GlobalSettings.CANVAS_HEIGHT / 2.0 + Math.sin(randomAngle) * randomDistance;

            Vector2<Double> randomPos = new Vector2<Double>(randomPosX, randomPoxY);
            agents[i] = new Agent(randomPos, randomAngle, moveSpeed, turnSpeed, sensorSize, sensorDistance, sensorOffsetAngle, colorR, colorG, colorB);
        }

        //Set text.
        setText(moveSpeed, turnSpeed, sensorSize, sensorDistance, sensorOffsetAngle, colorR, colorG, colorB);
    }

    /**
     * Change agent preset using keyboard.
     */
    public void keyPressed(){
        final int LEFT = 37;
        final int RIGHT = 39;

        switch(this.keyCode){
            case LEFT:
                previousAgentPreset();
                break;
            case RIGHT:
                nextAgentPreset();
                break;
            case ENTER:
                spawnAgentsRandom();
                break;
            default:
                if(key != CODED)
                    keyPressed = String.valueOf(key);
                break;
        }
    }

    /**
     * Draw all elements by current frame.
    */
    public void draw() {

        loadPixels();

        //Move agents.
        thread("tickThread");

        //Blur the pixels
        blur();
        
        //Draw agents.
        for (Agent a : agents) { 
            a.draw(this); 
        }

        updatePixels();

        //Draw mouse
        fill(255);    
        rectMode(CENTER);
        rect(mouseX, mouseY, 2, 2);

        //Draw any character
        if(keyPressed != null){
            fill(255);
            textFont(font);
            textSize(128);
            textAlign(CENTER, CENTER);
            text(keyPressed.toUpperCase(), mouseX, mouseY);
            keyPressed = null;
        }

        //Draw Text UI
        fill(0);
        rectMode(CORNER);
        rect(0, GlobalSettings.CANVAS_HEIGHT, GlobalSettings.CANVAS_WIDTH, GlobalSettings.BOTTOM_PRESERVE);
        titleText.draw(this);
        presetText.draw(this);
        instructionText.draw(this);
        sensorText.draw(this);
        colorText.draw(this);
        speedText.draw(this);
    }

    /**
     * Put logic of each agent on another thread
     */
    public void tickThread(){
        for (int i = 0; i < totalAgents; i++) { 
            if(agents[i] == null) return;
            agents[i].tick(this);
        }
    }

    /**
     * 3x3 blur function.
     */
    public void blur() {
        //The int value of color is represented as XRGB
        //which is constructed in [0000 0000 RRRR RRRR GGGG GGGG BBBB BBBB]
        int B_MASK = 255;       //[0000 0000 0000 0000 0000 0000 1111 1111]
        int G_MASK = 255<<8;    //[0000 0000 0000 0000 1111 1111 0000 0000]
        int R_MASK = 255<<16;   //[0000 0000 1111 1111 0000 0000 0000 0000]
        
        float diffuseSpeed = 10f;
        int darkenSpeed = 2000;
        
        int darkenDelta = (int)(darkenSpeed * GlobalSettings.DELTA_TIME);
        int[] processedPixels = new int[GlobalSettings.TOTAL_PIXELS];

        for(int x=0; x<GlobalSettings.CANVAS_WIDTH; x++){
            for(int y=0; y<GlobalSettings.CANVAS_HEIGHT; y++){

                int sumR = 0;
                int sumG = 0;
                int sumB = 0;

                for(int offsetX=-1; offsetX<2; offsetX++){
                    for(int offsetY=-1; offsetY<2; offsetY++){
                        int sampleX = x + offsetX;
                        int sampleY = y + offsetY;
                        if(sampleX>=0 && sampleX<GlobalSettings.CANVAS_WIDTH && sampleY>=0 && sampleY<GlobalSettings.CANVAS_HEIGHT){
                            int sample = pixels[sampleY * GlobalSettings.CANVAS_WIDTH + sampleX];

                            //Use only single color part in bits and shift it to the right.
                            //In this way we created a set of valid RGB values.
                            int b = sample & B_MASK;
                            int g = (sample & G_MASK)>>8;
                            int r = (sample & R_MASK)>>16;

                            sumR += r;
                            sumG += g;
                            sumB += b;
                        }
                    }
                }

                int avgR = sumR / 9;
                int avgG = sumG / 9;
                int avgB = sumB / 9;

                int resultColor = color(avgR-darkenDelta, avgG-darkenDelta, avgB-darkenDelta);
                int currentColor = pixels[y * GlobalSettings.CANVAS_WIDTH + x];

                processedPixels[y * GlobalSettings.CANVAS_WIDTH + x] = lerpColor(currentColor, resultColor, diffuseSpeed * (float)GlobalSettings.DELTA_TIME);
            }
        }
        
        for(int i = 0; i < GlobalSettings.TOTAL_PIXELS; i++){
            pixels[i] = processedPixels[i];
        }
        
    }

    public static void main(String[] args) {
        PApplet.main("Microbiome.App");
    }
}
