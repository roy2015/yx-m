package cn.zj.easynet.device.config;

import cn.zj.easynet.device.flag.Flags;

/**
 * 游戏bind字段，
 * <p>
 * 1.第一位控制是否绑定了此游戏，0表示绑定了，1表示解绑了。
 * <p>
 * 2.第二位控制是开启此游戏的提醒功能，0表示提醒，1表示不提醒
 * 
 * @author jiangzhouyun@corp.netease.com
 */
public class GameInfoBindFlag extends Flags {

    /**
     * 标记位 0 = 是否绑定了此游戏，0表示绑定了，1表示解绑了。
     */
    public static final int ID_BIND_GAME = 0;

    /**
     * 标记位 1 = 开启此游戏的提醒功能，0表示提醒，1表示不提醒
     */
    public static final int ID_GAME_MSG_PUSH = 1;

    /**
     * 是否绑定了此游戏，0表示绑定了，1表示解绑了。
     */
    public static final Integer FLAG_BIND_GAME = 1 << ID_BIND_GAME;

    /**
     * 开启此游戏的提醒功能，0表示提醒，1表示不提醒
     */
    public static final Integer FLAG_GAME_MSG_PUSH = 1 << ID_GAME_MSG_PUSH;

    /**
     * 默认构造函数
     */
    public GameInfoBindFlag() {
        super();
    }

    /**
     * 构造函数
     * 
     * @param flags
     */
    public GameInfoBindFlag(Integer flags) {
        super(flags);
    }

    /**
     * true绑定了此游戏，false解绑了
     * 
     * @return true,显示
     */
    public boolean isBindGame() {
        return !((flagsValue & FLAG_BIND_GAME) == FLAG_BIND_GAME);
    }

    /**
     * 是否推送game msg
     * 
     * @return true为 推送,false不推送
     */
    public boolean isPushGameNotify() {
        return !((flagsValue & FLAG_GAME_MSG_PUSH) == FLAG_GAME_MSG_PUSH);
    }

    /**
     * 设置bind, true设置开启, false设置关闭
     * 
     * @param isBindGame
     */
    public void setBindGame(boolean isBindGame) {
        if (!isBindGame) {
            flagsValue |= FLAG_BIND_GAME;
        } else {
            flagsValue &= ~FLAG_BIND_GAME;
        }
    }

    /**
     * 设置status, true消息提醒，false,不提醒
     * 
     * @param isMsgNotify
     */
    public void setGameMsgNotify(boolean isMsgNotify) {
        if (!isMsgNotify) {
            flagsValue |= FLAG_GAME_MSG_PUSH;
        } else {
            flagsValue &= ~FLAG_GAME_MSG_PUSH;
        }
    }

    public static void main(String[] args) {

        int config = 0;
        GameInfoBindFlag flag = new GameInfoBindFlag(config);

        //config = 0;
        config = 0;
        flag.setFlagsValue(config);
        System.out.println("isBindGame:" + flag.isBindGame());
        System.out.println("isPushGameNotify:" + flag.isPushGameNotify());
        config = 3;
        flag.setFlagsValue(config);;
        System.out.println("isBindGame:" + flag.isBindGame());
        System.out.println("isPushGameNotify:" + flag.isPushGameNotify());

        flag.setBindGame(true);
        flag.setGameMsgNotify(true);
        System.out.println("flag:" + flag.getFlagsValue());
    }
}
