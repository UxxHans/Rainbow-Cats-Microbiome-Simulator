package Microbiome;

import processing.core.PApplet;

/**
 * The main program of the project.
 */
public class App extends PApplet {

    public int totalAgents = 10000;
    public Agent[] agents;

    /**
     * Initialise the setting of the window size.
    */
    public void settings() {
        size(GlobalSettings.CANVAS_WIDTH, GlobalSettings.CANVAS_HEIGHT);
        noSmooth(); //No smoothing of graphics.
    }

    /**
     * Load all resources. Initialise the elements.
    */
    public void setup() {
        frameRate(GlobalSettings.FRAME_RATE);       //Set frame rate of the game.
        noStroke();                                 //No border on the geometry.
        
        //Set black background.
        background(0);

        //Spawn agents in a circle.
        agents = new Agent[totalAgents];
        for(int i=0; i<totalAgents; i++){
            final double maxDistance = 100;
            double randomAngle = 2 * Math.PI * Math.random();
            double randomDistance = maxDistance * Math.random();

            double randomPosX = GlobalSettings.CANVAS_WIDTH / 2.0 + Math.cos(randomAngle) * randomDistance;
            double randomPoxY = GlobalSettings.CANVAS_HEIGHT / 2.0 + Math.sin(randomAngle) * randomDistance;

            Vector2<Double> randomPos = new Vector2<Double>(randomPosX, randomPoxY);
            agents[i] = new Agent(randomPos, randomAngle);
        }
    }

    /**
     * Draw all elements by current frame.
    */
    public void draw() {
        loadPixels();

        //Blur the pixels
        blur();

        //Move and draw agents.
        for (Agent a : agents) { 
            a.tick(this);
            a.draw(this); 
        }

        updatePixels();
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
        int darkenSpeed = 1000;
        
        int darkenDelta = (int)(darkenSpeed * GlobalSettings.DELTA_TIME);

        for(int x=0; x<GlobalSettings.CANVAS_WIDTH; x++){
            for(int y=0; y<GlobalSettings.CANVAS_HEIGHT; y++){

                int sumR = 0;
                int sumG = 0;
                int sumB = 0;

                for(int offsetX=-1; offsetX<2; offsetX++){
                    for(int offsetY=-1; offsetY<2; offsetY++){
                        int sampleX = x+offsetX;
                        int sampleY = y+offsetY;
                        if(sampleX>=0 && sampleX<GlobalSettings.CANVAS_WIDTH && sampleY>=0 && sampleY<GlobalSettings.CANVAS_HEIGHT){
                            int sample = pixels[sampleY*GlobalSettings.CANVAS_WIDTH+sampleX];

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

                pixels[y*GlobalSettings.CANVAS_WIDTH + x] = lerpColor(currentColor, resultColor, diffuseSpeed * (float)GlobalSettings.DELTA_TIME);
            }
        }
    }

    public static void main(String[] args) {
        PApplet.main("Microbiome.App");
    }
}
