package LWC.pkgfinal;

import LWC.pkgfinal.Effect.*;
import LWC.pkgfinal.Loader.MapLoader;
import LWC.pkgfinal.Object.*;
import LWC.pkgfinal.Overlay.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;

/**
 * The main class of LWC_World
 */
public class LWC_RPG{
    private LWC_EventRecorder analyser = new LWC_EventRecorder(this);
    
    private void writeObject(ObjectOutputStream out) throws IOException{
        System.err.println("Effect Number: " + effectList.size());
        out.defaultWriteObject();
    }
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
        System.err.println("READING");
        in.defaultReadObject();
        pauseInterfaces = new ArrayList<>();
        Keys = new LWC_Keys();
    }
    
    protected String map_name = "";
    protected LWC_TriggerSolidMap solid_map;
    
    protected LWC_Player player = null;
    protected HashMap<LWC_AbstractObject, Point> position = new HashMap<>();
    protected ArrayList<LWC_AbstractTrigger> triggerList = new ArrayList<>();
    protected ArrayList<LWC_AbstractTime> effectList = new ArrayList<>();
    
    protected ArrayList<LWC_AbstractInterface> interfaces = new ArrayList<>();
    transient protected ArrayList<LWC_AbstractInterface> pauseInterfaces = new ArrayList<>();
    
    transient protected LWC_Keys Keys = new LWC_Keys();
    
    transient protected LWC_RPG_Panel panel = null;
    transient protected LWC_Main main = null;
    protected MapLoader mapLoader = null;
    
    public LWC_ImagePool imgPool(){
        return main.imgPool;
    }
    public LWC_MP3Pool mp3Pool(){
        return main.mp3Pool;
    }
    public String getPlotState(){
        return analyser.getPlotState();
    }

    public LWC_RPG(LWC_Main main) {
        this.main = main;
    }
    
    public void setMap(MapLoader maploader){
        synchronized(main.lock){
            mapLoader = maploader;
 
            if(mp3Pool().getPlayer("littleGirl") != null)
                mp3Pool().getPlayer("littleGirl").stop();

            for (Iterator< Map.Entry<LWC_AbstractObject, Point> > it = position.entrySet().iterator(); it.hasNext();) {
                Map.Entry<LWC_AbstractObject, Point> entry = it.next();
                if(entry.getKey() != player) it.remove();
            }
            triggerList.clear();

            solid_map = maploader.getSolid(this);
            maploader.load(this);
            setPosition(player, mapLoader.getEnterPoint(this));
            panel.setViewPoint(mapLoader.getViewPoint(this));
            
            map_name = maploader.getName(this);
        }
    }
    
    public Dimension getWindowSize(){
        return panel.getViewArea().getSize();
    }
    public int getWidth(){
        return getWindowSize().width;
    }
    public int getHeight(){
        return getWindowSize().height;
    }
    public String getMapName(){
        return map_name;
    }
    public LWC_Player getPlayer(){
        return player;
    }
    /**
     * Indicate the which one is the player, add to ObjList if the player is not in the list
     * @param player the player
     */
    public void setPlayer(LWC_Player player){
        if(!position.containsKey(player)){
            position.put(player, new Point());
        }
        this.player = player;
    }
    /**
     * add LWC_AbstractObject into this class, default location = (0,0) respect to the background
     * @param obj the object
     */
    public void addObj(LWC_AbstractObject obj){
        position.put(obj, new Point());
    }
    /**
     * add LWC_AbstractObject into this class
     * @param obj the object
     * @param position the location respect to the background
     */
    public void addObj(LWC_AbstractObject obj, Point position){
        synchronized(main.lock){
            analyser.record("Add", obj);
            this.position.put(obj, position);
        }
    }
    /**
     * add TriggerOverlay to this class
     * @param overlay triggerOverlay
     */
    public void addTriggerOverlay(LWC_AbstractTrigger overlay){
        triggerList.add(overlay);
    }
    /**
     * remove TriggerOverlay from this class
     * @param overlay the triggerOverlay
     */
    public void removeTriggerOverlay(LWC_AbstractTrigger overlay){
        triggerList.remove(overlay);
    }
    /**
     * add the effect to this class, if effect isn't timeEffect, act it immediately instead of insert into list
     * @param effect the effect
     * @param who if effect isn't instant, which obj should it acts to
     */
    synchronized public void addEffect(LWC_AbstractEffect effect){
        if(effect instanceof LWC_AbstractTime) {
            effectList.add((LWC_AbstractTime) effect);
            ((LWC_AbstractTime)effect).update();
        }
        else if(effect instanceof LWC_Effect2Null)
            ((LWC_Effect2Null)effect).act();
        else if(effect instanceof LWC_Effect2Object) 
            ((LWC_Effect2Object)effect).act();
    }
    /**
     * Check if the effect is in the list
     * @param effect the effect to be checked
     * @return true if the effect is in the list
     */
    public boolean hasEffect(LWC_AbstractEffect effect){
        if(effect instanceof LWC_AbstractTime)
            return effectList.contains((LWC_AbstractTime)effect);
        else return false;
    }
    public boolean isSolid(Rectangle area){
        if(solid_map != null && solid_map.isTriggered(area)) return true;
        else return false;
    }
    
    public Point getWindowPosition(LWC_AbstractObject obj){
        return new Point(position.get(obj).x - panel.getViewArea().x, position.get(obj).y - panel.getViewArea().y);
    }
    /**
     * move the obj by vector, and trigger event
     * @param obj the object which want to be moved
     * @param relative the moving vector
     * @param records the record point of where the solid point is
     * @return the actual move vector
     */
    public Point movePosition(LWC_AbstractObject obj, Point relative, boolean block_Check){
        if(obj == null || !position.containsKey(obj)) return new Point();
        else{
            Point origin = position.get(obj);
            Point relativePoint = new Point(relative);
            Point new_position = new Point(origin.x+relativePoint.x, origin.y+relativePoint.y);
            if(relative.x == 0 && relative.y == 0)return relative;

            BufferedImage img = obj.show();
            if(block_Check == true){
                if(isSolid(new Rectangle(new_position, LWC_Util.getImgDim(img)))) 
                    solid_map.act(this, obj, relativePoint);
                else 
                    solid_map.ignore(this, obj, relative);
                
                if(relativePoint.x == 0 && relativePoint.y == 0) return new Point();
                new_position = new Point(origin.x+relativePoint.x, origin.y+relativePoint.y);    
            }
            else setPosition(obj, new_position);

            if(obj == player){
                if(relative.x > 0 && getWindowPosition(obj).x > panel.getViewArea().width*3/4)
                    panel.ViewAreaMove(new Point(relative.x, 0));
                else if(relative.x < 0 && getWindowPosition(obj).x < panel.getViewArea().width/4)
                    panel.ViewAreaMove(new Point(relative.x, 0));
                if(relative.y > 0 && getWindowPosition(obj).y > panel.getViewArea().height*3/4)
                    panel.ViewAreaMove(new Point(0, relative.y));
                else if(relative.y < 0 && getWindowPosition(obj).y < panel.getViewArea().height/4)
                    panel.ViewAreaMove(new Point(0, relative.y));
            }

            for(int i=triggerList.size()-1; i>=0; i--){
                LWC_AbstractTrigger overlay = triggerList.get(i);
                if(overlay.isTriggered(new Rectangle(new_position, LWC_Util.getImgDim(img))) == true)
                    overlay.act(this, obj, relative);
                else
                    overlay.ignore(this, obj, relative);
            }
            return relative;
        }
    }
    /**
     * get All LWC_AbstractCharacter in the list
     * @param block the character you don't want to include
     * @return ArrayList of character which matches the query
     */
    @SuppressWarnings("unchecked")
    public ArrayList<LWC_AbstractObject> getObjList(){
        LWC_AbstractObject[] objList = new LWC_AbstractObject[0];
        objList = position.keySet().toArray(objList);
        return new ArrayList<>(Arrays.asList(objList));
    }
    /**
     * Get the position of the LWC_AbstractObject
     * @param obj the LWC_AbstractObject
     * @return the LWC_AbstractObject's position
     */
    public Point getPosition(LWC_AbstractObject obj){
        if(position.containsKey(obj)) 
            return new Point(position.get(obj));
        else return new Point();
    }
    /**
     * Set the position of the LWC_AbstractObject without solid area and trigger area check
     * @param obj the LWC_AbstractObject
     * @param p the position
     * @return the previous value associated with LWC_AbstractObject, or null if there was no mapping for LWC_AbstractObject
     */
    public Point setPosition(LWC_AbstractObject obj, Point p){
        return position.put(obj, new Point(p));
    }
    
    public Dimension getBackgroundSize(){
        return LWC_Util.getImgDim(mapLoader.getBackground(this));
    }
    
    synchronized public void addInterface(LWC_AbstractInterface iter){
        if(iter != null){
            analyser.record("Add", iter);
            interfaces.add(iter);
        }
    }
    
    synchronized public void addPauseInterface(LWC_AbstractInterface pause){
        if(pause != null){
            analyser.record("Add", pause);
            pauseInterfaces.add(pause);
        }
    }
    
    public boolean hasInterface(LWC_AbstractInterface empty) {
        return interfaces.contains(empty);
    }

    public LWC_Keys getKeys(){
        return Keys;
    }
    
    /**
     * updates every objects, move them, and updates every effect status
     * @return false if the game is in its end state
     */
    synchronized public boolean update(){
        Keys.update();
                
        if(pauseInterfaces.isEmpty()){
            if(player != null && player.isDead() ){
                addPauseInterface(new LWC_EndScreen(this));
                return true;
            }
            if(Keys.isTyped(KeyEvent.VK_ESCAPE))
                addPauseInterface(new LWC_InterfaceExit(this));
            if(Keys.isTyped(KeyEvent.VK_I))
                addPauseInterface(new LWC_InterfaceItems(this, player));
                
            ArrayList<LWC_AbstractObject> obj = getObjList();
            for(int i=obj.size()-1; i>=0; i--)
                if(obj.get(i).update() == false){
                    analyser.record("Delete", obj.get(i));
                    position.remove(obj.get(i));
                }

            for (int i=effectList.size()-1; i>=0; i--)
                if( effectList.get(i).update() == false ){
                    analyser.record("Delete", effectList.get(i));
                    effectList.remove(i);
                }

            for (int i=interfaces.size()-1; i>=0; i--){
                if(interfaces.get(i).update() == false){
                    analyser.record("Delete", interfaces.get(i));
                    interfaces.remove(i);
                }
            }
        }
        else{
            int last_index = pauseInterfaces.size() - 1;
            if(pauseInterfaces.get(last_index).update() == false){
                analyser.record("Delete", pauseInterfaces.get(last_index));
                pauseInterfaces.remove(last_index);
            }
        }
        return true;
    }

    public void RPG_init() {
        main.RPG_init();
    }
    public void RPG_Load(String string) {
        main.RPG_Load(string);
    }
    public void RPG_Save(String fileName) {
        main.RPG_Save(fileName);
    }
}
