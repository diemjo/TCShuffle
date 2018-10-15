package moe.karpador.tcshuffle;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PVector;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;

public class MainApp extends PApplet {

    private final String IMAGE_PATH = "/images/";
    static int IMAGE_WIDTH = 360;
    static int IMAGE_HEIGHT = 520;
    static final int IMAGE_GAP = 50;
    static final ArrayList<Maid> maids = new ArrayList<>(16);
    ArrayList<Maid> selectedMaids = new ArrayList<>(10);
    static PImage cardcover;
    PImage backgroundPattern;
    private int current = 0;
    boolean start = false;
    public static void main(String[] args) {
        PApplet.runSketch(new String[]{"MAIN"}, new MainApp());
    }

    public void setup() {
        Maid.startPos = new PVector((width-IMAGE_WIDTH)/2, (height-IMAGE_HEIGHT)/2);
        cardcover = getImage(IMAGE_PATH + "cardcover.jpg");
        cardcover.resize(360, 520);
        backgroundPattern = getImage(IMAGE_PATH + "hearts.jpg");
        initMaids();
        textAlign(CENTER, TOP);
        textSize(40);
    }

    public void settings() {
        if (displayWidth<(IMAGE_WIDTH+IMAGE_GAP)*5 || displayHeight<(IMAGE_HEIGHT+IMAGE_GAP)*3) {
            float factor = max((IMAGE_WIDTH+IMAGE_GAP)*5/displayWidth, (IMAGE_HEIGHT+IMAGE_GAP)*3/displayHeight);
            IMAGE_WIDTH = (int) (IMAGE_WIDTH/factor);
            IMAGE_HEIGHT = (int) (IMAGE_HEIGHT/factor);
        }
        size((IMAGE_WIDTH + IMAGE_GAP)*5, (IMAGE_HEIGHT+IMAGE_GAP)*3);
    }

    public void draw() {
        drawPattern();
        image(cardcover, Maid.startPos.x, Maid.startPos.y);
        if (!start) {
            fill(255, (int) ((Math.cos(((millis()/20)%100)/100.0*TWO_PI))*100)+155);
            text("Press Space to start", width/2, height/2+IMAGE_HEIGHT/2);
        }
        if (selectedMaids.size()>0) {
            for (int i = 0; i <= current; i++) {
                selectedMaids.get(i).draw();
            }
            selectedMaids.get(current).tick();
            if (start && selectedMaids.get(current).done) {
                if (current < 9) {
                    current++;
                } else {
                    start = false;
                }
            }
        }
    }

    public void keyPressed() {
        if (key == ' ') {
            start = true;
            current = 0;
            selectMaids();
        }
    }

    private void drawPattern() {
        for (int x=0; x<width; x+=backgroundPattern.width) {
            for (int y=0; y<height; y+=backgroundPattern.height) {
                image(backgroundPattern, x, y);
            }
        }
    }

    private void initMaids() {
        Maid maid;

        maid = new Maid(this, "Anise Greenaway", 7);
        maid.loadImage(IMAGE_PATH + "AniseGreenaway.jpg");
        maids.add(maid);

        maid = new Maid(this, "Azure Crescent", 2);
        maid.loadImage(IMAGE_PATH + "AzureCrescent.jpg");
        maids.add(maid);

        maid = new Maid(this, "Claire Saint-Juste", 3);
        maid.loadImage(IMAGE_PATH + "ClaireSaint-Juste.jpg");
        maids.add(maid);

        maid = new Maid(this, "Eliza Rosewater", 3);
        maid.loadImage(IMAGE_PATH + "ElizaRosewater.jpg");
        maids.add(maid);

        maid = new Maid(this, "Esquine Foret", 4);
        maid.loadImage(IMAGE_PATH + "EsquineForet.jpg");
        maids.add(maid);

        maid = new Maid(this, "Genevieve Daubigny", 4);
        maid.loadImage(IMAGE_PATH + "GenevieveDaubigny.jpg");
        maids.add(maid);

        maid = new Maid(this, "Kagari Ichinomiya", 3);
        maid.loadImage(IMAGE_PATH + "KagariIchinomiya.jpg");
        maids.add(maid);

        maid = new Maid(this, "Moine de Levévre", 4);
        maid.loadImage(IMAGE_PATH + "MoineDeLefevre.jpg");
        maids.add(maid);

        maid = new Maid(this, "Natsumi Fujikawa", 5);
        maid.loadImage(IMAGE_PATH + "NatsumiFujikawa.jpg");
        maids.add(maid);

        maid = new Maid(this, "Nena Wilder", 5);
        maid.loadImage(IMAGE_PATH + "NenaWilder.jpg");
        maids.add(maid);

        maid = new Maid(this, "Ophelia Grail", 6);
        maid.loadImage(IMAGE_PATH + "OpheliaGrail.jpg");
        maids.add(maid);

        maid = new Maid(this, "Rouge Crescent", 2);
        maid.loadImage(IMAGE_PATH + "RougeCrescent.jpg");
        maids.add(maid);

        maid = new Maid(this, "Safran Virginie", 3);
        maid.loadImage(IMAGE_PATH + "SafranVirginie.jpg");
        maids.add(maid);

        maid = new Maid(this, "Sainsbury Lockwood", 5);
        maid.loadImage(IMAGE_PATH + "SainsburyLockwood.jpg");
        maids.add(maid);

        maid = new Maid(this, "Tenalys Trent", 5);
        maid.loadImage(IMAGE_PATH + "TenalysTrent.jpg");
        maids.add(maid);

        maid = new Maid(this, "Viola Crescent", 2);
        maid.loadImage(IMAGE_PATH + "ViolaCrescent.jpg");
        maids.add(maid);
    }

    public PImage getImage(String path) {
        BufferedImage bimg = null;
        try {
            bimg = ImageIO.read(getClass().getResource(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        PImage img = new PImage(bimg.getWidth(), bimg.getHeight(), PConstants.ARGB);
        bimg.getRGB(0, 0, img.width, img.height, img.pixels, 0, img.width);
        img.updatePixels();
        return img;
    }

    public void selectMaids() {
        selectedMaids.clear();
        ArrayList<Integer> list = new ArrayList<>(16);
        for (int i=0; i<16; i++) {
            list.add(i);
        }
        int rand;
        for (int i=0; i<10; i++) {
            rand = (int) random(0,list.size());
            selectedMaids.add(maids.get(list.get(rand)).reset());
            list.remove(rand);
        }
        selectedMaids.sort(Comparator.comparingInt(maid -> maid.cost));
        for (int i=0; i<5; i++) {
            selectedMaids.get(i).targetPos = new PVector(IMAGE_GAP/2+(IMAGE_WIDTH+IMAGE_GAP)*i, IMAGE_GAP/2);
        }
        for (int i=5; i<selectedMaids.size(); i++) {
            selectedMaids.get(i).targetPos = new PVector(IMAGE_GAP/2+(IMAGE_WIDTH+IMAGE_GAP)*(i-5), IMAGE_GAP/2+(IMAGE_HEIGHT+IMAGE_GAP)*2);
        }
    }
}
