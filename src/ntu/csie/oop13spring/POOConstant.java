package ntu.csie.oop13spring;

import java.awt.Rectangle;
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
    
    public static final HashMap<Integer, Integer> Direction_MAP = new HashMap<>();
    
    static {
       Direction_MAP.put(STAT_DOWN, 0);
       Direction_MAP.put(STAT_UP, 2);
       Direction_MAP.put(STAT_RIGHT, 1);
       Direction_MAP.put(STAT_LEFT, 3);
       Direction_MAP.put(STAT_STOP, -1);
    };
}

final class POO_PICConstant{
    public static final Rectangle []ISAAC_HEAD_EYEOPENED = { new Rectangle(92, 109, 28, 30), 
                                                                new Rectangle(146, 109, 28, 30),
                                                                new Rectangle(199, 109, 28, 30),
                                                                new Rectangle(253, 109, 28, 30)};
    public static final Rectangle []ISAAC_HEAD_EYECLOSED = { new Rectangle(119, 109, 28, 30), 
                                                                new Rectangle(172, 109, 28, 30),
                                                                new Rectangle(226, 109, 28, 30),
                                                                new Rectangle(279, 109, 28, 30)};
    public static final Rectangle []JUDIS_HEAD_EYEOPENED = { new Rectangle(92, 145, 28, 30), 
                                                                new Rectangle(146, 145, 28, 30),
                                                                new Rectangle(199, 145, 28, 30),
                                                                new Rectangle(253, 145, 28, 30)};
    public static final Rectangle []JUDIS_HEAD_EYECLOSED = { new Rectangle(119, 109, 28, 30), 
                                                                new Rectangle(172, 109, 28, 30),
                                                                new Rectangle(226, 109, 28, 30),
                                                                new Rectangle(279, 109, 28, 30)};
    public static final Rectangle []BODY_HORIZONTAL = {new Rectangle(342, 59, 15, 11),
                                                         new Rectangle(368, 59, 15, 11),
                                                         new Rectangle(395, 59, 15, 11),
                                                         new Rectangle(422, 59, 15, 11),
                                                         new Rectangle(448, 59, 15, 11),
                                                         new Rectangle(476, 59, 15, 11),
                                                         new Rectangle(342, 85, 15, 11),
                                                         new Rectangle(368, 86, 15, 11)};
    public static final Rectangle []BODY_VERTICAL = {new Rectangle(342, 112, 15, 11),
                                                         new Rectangle(368, 112, 15, 11),
                                                         new Rectangle(395, 112, 15, 11),
                                                         new Rectangle(422, 112, 15, 11),
                                                         new Rectangle(448, 112, 15, 11),
                                                         new Rectangle(476, 112, 15, 11),
                                                         new Rectangle(342, 140, 15, 11),
                                                         new Rectangle(368, 140, 15, 11)};
}
