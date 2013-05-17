package ntu.csie.oop13spring;

import java.awt.*;
import javax.swing.*;


public abstract class Item extends Effects{
    protected POOJComp comp;
    public Item(Container contain, String str, String des) {
        super(str, des);
        initComp(contain);
    }
    
    protected abstract void initComp(Container contain);
    public void dispose(){
        comp.dispose();
    }
    public void setPosition(POOCoordinate position){
        comp.setCenter(position);
    }
}

class Meat extends Item{
    public Meat(Container contain) {
        super(contain, "Meat", "Meat, restor 10 HP");
    }
    
    public Meat(Container contain, POOCoordinate position) {
        super(contain, "Meat", "Meat, restor 10 HP");
        setPosition(position);
    }
    
    @Override
    public void act(POOPet pet) {
        pet.setHP(pet.getHP() + 10 > 100 ? 100 : pet.getHP() + 10);
    }

    @Override
    protected void initComp(Container contain) {
        comp = new POOJComp(contain, new JLabel(POOUtil.getIcon(POOUtil.getCWD()+"images/item1.png")), 0, 0);
    }
}

class MushRoom extends Item{
    public MushRoom(Container contain) {
        super(contain, "MushRoom", "MushRoom, add 1 AGI");
    }
    
    public MushRoom(Container contain, POOCoordinate position) {
        super(contain, "MushRoom", "MushRoom, add 1 AGI");
        setPosition(position);
    }
    
    @Override
    public void act(POOPet pet) {
        pet.setAGI(pet.getAGI()+1 > 10 ? 10 : pet.getAGI()+1);
    }

    @Override
    protected void initComp(Container contain) {
        comp = new POOJComp(contain, new JLabel(POOUtil.getIcon(POOUtil.getCWD()+"images/mushroom.png")), 0, 0);
    }
}

class Soap extends Item{
    public Soap(Container contain) {
        super(contain, "Soap", "Soap, add 10 MP");
    }
    
    public Soap(Container contain, POOCoordinate position) {
        super(contain, "Soap", "Soap, add 10 MP");
        setPosition(position);
    }
    
    @Override
    public void act(POOPet pet) {
        pet.setMP(pet.getMP()+10 > 100 ? 100 : pet.getMP()+10);
    }

    @Override
    protected void initComp(Container contain) {
        comp = new POOJComp(contain, new JLabel(POOUtil.getIcon(POOUtil.getCWD()+"images/soap.png")), 0, 0);
    }
}