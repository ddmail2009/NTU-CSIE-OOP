package LWC.pkgfinal.Effect;

import LWC.pkgfinal.LWC_RPG;
import LWC.pkgfinal.Object.LWC_AbstractObject;
import java.awt.Point;
import java.awt.image.BufferedImage;

public class LWC_ShowImages extends LWC_AbstractAnimate {
    private Point position = new Point();
    private int offset = 20;
    private int speed = 10;
    private int iteration = 1;
    private LWC_AbstractObject obj;
    
    public LWC_ShowImages(LWC_RPG rpg) {
        super(rpg, "ShowImage");
    }
    
    public void config(BufferedImage []imgs, int speed, int iteration, Point position){
        this.img = imgs;
        this.speed = speed;
        setDuration(speed * imgs.length * iteration);
        this.position = new Point(position);
        this.iteration = iteration;
        if(counter > 1)
            rpg.setPosition(obj, position);
    }

    @Override
    protected boolean effect_update() {
        if(img == null) return false;
        else{
            if(counter == 1){          
                if(img == rpg.imgPool().getFolderImages("img/meteor2"))
                {
                    rpg.mp3Pool().setPlayer("meteor2", "music/Thunder9.mp3").play();
                }
                else if(img == rpg.imgPool().getFolderImages("img/devilshowup"))
                {
                    rpg.mp3Pool().setPlayer("devilshowup", "music/Blind.mp3").play();
                }
                if(iteration == 1)
                    setDuration(getDuration() + offset);

                rpg.addObj(new LWC_AbstractObject(rpg) {
                    @Override
                    public boolean action() {
                        if(counter >= getDuration()) return false;
                        return true;
                    }
                    
                    @Override
                    public BufferedImage show() {
                        if(iteration == 1 && (counter/speed) >= img.length)
                            return img[img.length-1];
                        else
                            return img[(counter/speed)%img.length];
                    }
                }, position);
            }
            
            if(counter >= getDuration()) return false;
            return true;
        }
    }
}
