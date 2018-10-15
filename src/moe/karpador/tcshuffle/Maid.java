package moe.karpador.tcshuffle;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PVector;
import sun.applet.Main;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class Maid {
    private final double INCREMENT = 0.03;
    public static PVector startPos;
    PApplet parent;
    public String name;
    public PImage image;
    public int cost;
    public PVector pos;
    public PVector targetPos;
    private float progress = -6;
    private float turnProgess = 0;
    public boolean done = false;
    private boolean turning = true;

    public Maid(PApplet parent, String name, int cost) {
        this.parent = parent;
        this.name = name;
        this.cost = cost;
        pos = new PVector(startPos.x, startPos.y);
        targetPos = new PVector();
    }

    public void loadImage(String path) {
        BufferedImage bimg = null;
        try {
            bimg = ImageIO.read(getClass().getResource(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        PImage img = new PImage(bimg.getWidth(), bimg.getHeight(), PConstants.ARGB);
        bimg.getRGB(0, 0, img.width, img.height, img.pixels, 0, img.width);
        img.updatePixels();
        img.resize(MainApp.IMAGE_WIDTH, MainApp.IMAGE_HEIGHT);
        image = img;
    }

    public void draw() {
        if (turning) {

        }
        else {
            parent.image(image, pos.x, pos.y);
        }
    }

    public void tick() {
        if (turning) {
            turnProgess += INCREMENT;
            if (turnProgess>=1) {
                turnProgess = 1;
                turning = false;
            }
            if (turnProgess<0.5) {
                drawBackground(turnProgess);
            }
            else {
                drawForeground(turnProgess-0.5);
            }
        }
        else {
            progress += INCREMENT*7;
            if (progress >= 6) {
                progress = 6;
                done = true;
                return;
            }
            float smoothprogress = (float) (Math.exp(progress)/(Math.exp(progress)+1));
            pos.x = (targetPos.x - startPos.x) * smoothprogress + startPos.x;
            pos.y = (targetPos.y - startPos.y) * smoothprogress + startPos.y;
        }
    }

    private void drawForeground(double turnProgess) {
        int w = (int) (MainApp.IMAGE_WIDTH*Math.sin(turnProgess*PConstants.PI));
        PImage img = image.copy();
        img.resize(w, img.height);
        parent.image(img, pos.x+image.width/2-img.width/2, pos.y);
    }

    private void drawBackground(double turnProgess) {
        int w = (int) (MainApp.IMAGE_WIDTH*Math.cos(turnProgess*PConstants.PI));
        PImage img = MainApp.cardcover.copy();
        img.resize(w, img.height);
        parent.image(img, pos.x+image.width/2-img.width/2, pos.y);
    }

    public Maid reset() {
        pos.x = startPos.x;
        pos.y = startPos.y;
        progress = -6;
        turnProgess = 0;
        done = false;
        turning = true;
        return this;
    }
}
