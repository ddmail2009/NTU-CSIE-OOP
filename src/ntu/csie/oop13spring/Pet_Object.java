package ntu.csie.oop13spring;

public abstract class Pet_Object extends POOPet {
    private String ImagePath;

    protected void SetImagePath(String ImagePath){
        this.ImagePath = ImagePath;
    }
    protected String[] GetAllPath(){
        String []str = new String[4];
        str[0] = ImagePath+"0"+".png";
        str[1] = ImagePath+"1"+".png";
        str[2] = ImagePath+"2"+".png";
        str[3] = ImagePath+"3"+".png";
        return str;
    }
    protected int GetStatus(){
        return 0;
    }
    
    public abstract void init(int HP, int MP, int AGI, String str, MoveState movestate, String image);
    public abstract Skills[] getSkillList();
    public abstract void setMovestate(MoveState movestate);
    
    @Override
    public String toString(){
        return String.format("%s, HP:%d MP:%d AGI:%d", getName(), getHP(), getMP(), getAGI());
    }
}