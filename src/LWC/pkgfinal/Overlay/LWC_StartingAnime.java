package LWC.pkgfinal.Overlay;

import LWC.pkgfinal.Effect.*;
import LWC.pkgfinal.LWC_RPG;
import LWC.pkgfinal.Loader.MapLoader;
import java.awt.Point;


public class LWC_StartingAnime extends LWC_EffectInterface {

    public LWC_StartingAnime(LWC_RPG rpg) {
        super(rpg);

        LWC_OrderedEffect order = new LWC_OrderedEffect(rpg);
        order.setMonitor(this);

        //scene1
        LWC_InterfaceNPCMsg msg1 = new LWC_InterfaceNPCMsg(rpg);
        msg1.addStr("今天天氣真好～\n想到昨天惡作劇的事，就覺得好笑，哈哈～");
        msg1.addStr("咦～\n那是什麼？");
        LWC_MoveAnimate showplayer1 = new LWC_MoveAnimate(rpg);
        showplayer1.config(rpg.getPlayer(), 2, new Point(300,0),"run");
        
        order.addEffect(new LWC_TalkEffect(rpg, msg1));

        //scene2
        LWC_ShowImages showfloor = new LWC_ShowImages(rpg);
        System.err.println(rpg.getPlayer());
        showfloor.config(rpg.imgPool().getFolderImages("img/Floor"), 5, 3, new Point(0, rpg.getPosition(rpg.getPlayer()).y + 30));
        LWC_ShowImages showmeteor = new LWC_ShowImages(rpg);
        showmeteor.config(rpg.imgPool().getFolderImages("img/meteor2"), 3, 3, new Point(0, 0));

        LWC_MoveAnimate showplayer2 = new LWC_MoveAnimate(rpg);
        showplayer2.config(rpg.getPlayer(), 1, new Point(5,0),"dizzy");
        LWC_ParallelEffect parallelEffect2 = new LWC_ParallelEffect(rpg);
        parallelEffect2.setMonitor(this);
        parallelEffect2.addEffect(showplayer2);
        parallelEffect2.addEffect(showmeteor);
        parallelEffect2.addEffect(showfloor);
        
        LWC_InterfaceNPCMsg msg2 = new LWC_InterfaceNPCMsg(rpg);
        msg2.addStr("天空有流星！？\n不對！怎麼會有隕石！？\n快逃！");
        LWC_InterfaceNPCMsg msg2_2 = new LWC_InterfaceNPCMsg(rpg);
        msg2_2.addStr("天阿～怎麼漸漸得看不見了\n好暈…");
        LWC_CancelEffect cancelEffect = new LWC_CancelEffect(rpg);
        cancelEffect.config(rpg.getPlayer(),"dizzy");
        LWC_FadeInAndOut fadeinout = new LWC_FadeInAndOut(rpg);
        fadeinout.setMonitor(this);
        order.addEffect(fadeinout);

        order.addEffect(new LWC_TalkEffect(rpg, msg2));
        order.addEffect(parallelEffect2);
        order.addEffect(new LWC_TalkEffect(rpg, msg2_2));
            
//scene3 in
        LWC_MapLoadEffect loadAfterEffect = new LWC_MapLoadEffect(rpg);
        loadAfterEffect.setConfig(new MapLoader("config/AnimeMap_After.txt"));
//modify

        LWC_FadeInAndOut fadeinout2 = new LWC_FadeInAndOut(rpg);
        LWC_InterfaceOpeningCinema msg3 = new LWC_InterfaceOpeningCinema(rpg);
        msg3.load_script("OpenCinema1");

        order.addEffect(fadeinout2);
        fadeinout2.setMonitor(this);
        fadeinout2.addEffect(loadAfterEffect);
        fadeinout2.getFadeOut().setDuration(3);
        fadeinout2.addEffect(new LWC_TalkEffect(rpg, msg3));
        

        LWC_ShowImages showdevil = new LWC_ShowImages(rpg);
        showdevil.config(rpg.imgPool().getFolderImages("img/devilshowup"),5, 1, new Point(rpg.getPosition(rpg.getPlayer()).x+400, rpg.getPosition(rpg.getPlayer()).y-250));
        LWC_ParallelEffect parallelEffect3 = new LWC_ParallelEffect(rpg);
        parallelEffect3.setMonitor(this);
        parallelEffect3.addEffect(showdevil);

        order.addEffect(parallelEffect3);
        
        LWC_InterfaceNPCMsg msg = new LWC_InterfaceNPCMsg(rpg);
        msg.addStr("？？？：哈哈哈～哈哈哈～");
        msg.addStr("？？？：自從我被關進荒蕪之門已經過了1000年了。");
        msg.addStr("？？？：現在終於可以重見天日。");
        msg.addStr("？？？：我要做的第一件事情便是統治這個人間界。");
        msg.addStr("？？？：讓你們知道老子我，不是好惹的！");
        LWC_TalkEffect talkEffect = new LWC_TalkEffect(rpg, msg);
        order.addEffect(talkEffect);
//scene4

        LWC_FadeInAndOut fadeinout3 = new LWC_FadeInAndOut(rpg);
        order.addEffect(fadeinout3);
        fadeinout3.setMonitor(this);
        fadeinout3.getFadeOut().setDuration(3);
        fadeinout3.addEffect(cancelEffect);
        fadeinout3.addEffect(loadAfterEffect);
        

        LWC_InterfaceNPCMsg msgAfter = new LWC_InterfaceNPCMsg(rpg);
        msgAfter.addStr("這裡是哪裡阿？");
        msgAfter.addStr("我記得…\n我好像在散步\n接著…接著就…");
        msgAfter.addStr("唉～頭好痛喔！");
        msgAfter.addStr("好冷喔…");
        LWC_TalkEffect talkAfterEffect = new LWC_TalkEffect(rpg, msgAfter);
        order.addEffect(talkAfterEffect);
//end scene4
//scene5
        // LWC_MapLoadEffect loadAfterEffect = new LWC_MapLoadEffect(rpg);
        // loadAfterEffect.setConfig(new MapLoader("config/AnimeMap_After.txt"));

        // LWC_FadeInAndOut fadeinout3 = new LWC_FadeInAndOut(rpg);
        // order.addEffect(fadeinout3);
        // fadeinout3.setMonitor(this);
        // fadeinout3.getFadeOut().setDuration(3);
        // fadeinout3.addEffect(cancelEffect);
        // fadeinout3.addEffect(loadAfterEffect);
/*
//end scene5
        LWC_MapLoadEffect loadEffect = new LWC_MapLoadEffect(rpg);
        loadEffect.setConfig(new MapLoader("config/FirstMap.txt"));

        LWC_FadeInAndOut fadeInAndOut = new LWC_FadeInAndOut(rpg);
        fadeInAndOut.setMonitor(this);
        fadeInAndOut.getFadeOut().setDuration(3);
        //fadeInAndOut.addEffect(cancelEffect);
        fadeInAndOut.addEffect(loadEffect);

        //in the game
        order.addEffect(fadeInAndOut);
*/
        this.addEffect((LWC_AbstractEffect) rpg.getPlayer().getRegister("Gravity"));
        this.addEffect(order);

    }
    
}
