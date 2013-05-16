package ntu.csie.oop13spring;

import java.util.HashMap;

public final class POOConstant {
    public static final int STAT_STOP = 0;
    public static final int STAT_LOCK = 1;
    public static final int STAT_LEFT = 1 << 1;
    public static final int STAT_RIGHT = 1 << 2;
    public static final int STAT_DOWN = 1 << 3;
    public static final int STAT_UP = 1 << 4;
    
    public static final int STAT_POISON = 1<<5;
    public static final int STAT_BURN = 1<<6;
    public static final int STAT_JUMP = 1<<7;
    public static final int STAT_GUARD = 1<<8;
    public static final int STAT_BLINK = 1<<9;
    
    public static final int STAT_USERDEFINE1 = 1<<31;
    public static final int STAT_USERDEFINE2 = 1<<30;
    public static final int STAT_USERDEFINE3 = 1<<29;
    public static final int STAT_USERDEFINE4 = 1<<28;
    
    public static final HashMap<Integer, Integer> Direction_MAP = new HashMap<>();
    
    static {
       Direction_MAP.put(STAT_DOWN, 2);
       Direction_MAP.put(STAT_UP, 0);
       Direction_MAP.put(STAT_RIGHT, 1);
       Direction_MAP.put(STAT_LEFT, 3);
       Direction_MAP.put(STAT_STOP, -1);
    };
}

final class POOSkillConstant{
    public static final int NORMAL = 0;
    public static final int IGNORE_GUARD = 1;
}