package cn.zj.easynet.common.service;

/**
 * @author liyalong
 */
public interface ILiteUserService2 {

    public interface CommandId {
        public static final byte CID_GET_USER_INFO = 1; // 获取个人信息

        public static final byte CID_GET_USER_INFO_LIST = 2; // 批量获取个人信息

        public static final byte CID_UPDATE_USER_INFO = 3; // 更新个人信息

        public static final byte CID_ADD_FRIEND = 4; // 添加好友（无需验证）

        public static final byte CID_UPDATE_FRIEND = 5; // 更新好友信息

        public static final byte CID_GET_FRIEND_LIST = 6; // 获取好友列表

        public static final byte CID_ADD_FRIEND_VERIFY = 7; // 发送添加好友的验证信息

        public static final byte CID_ADD_FRIEND_VERIFY_NOTIFY = 8; // 通知有人请求添加你为好友

        public static final byte CID_ADD_FRIEND_NOTIFY = 9; // 通知有人已经添加你为好友

        public static final byte CID_ADD_FRIEND_ACCEPT = 10; // 接受别人的添加好友请求

        public static final byte CID_FIND_USER = 11; // 查找用户（仅普通号）

        public static final byte CID_ADD_FRIEND_LIST = 12; // 批量添加好友

        public static final byte CID_ADD_FRIEND_ACCEPT_NOTIFY = 13; // 通知有人已经接受了你的添加好友请求

        public static final byte CID_ADD_FRIEND_NEW = 14; // 新版添加好友入口

        public static final byte CID_FIND_USER_EX = 15; // 查找用户（普通号+公众号）

        public static final byte CID_GET_USER_PRIVILEGE = 16; // 用户的特权判断

        public static final byte CID_FRIEND_RECOMMEND = 17; // 好友推荐

        public static final byte CID_QUERY_YILIAO_ACCOUNT_LIST = 20; // 批量查询哪些翼聊用户已开通翼信

        public static final byte CID_GET_ASK_USER_INFO = 21; // 获取问一问个人资料

        public static final byte CID_UPDATE_ASK_USER_INFO = 22; // 更新问一问个人资料
        
        public static final byte CID_RADAR_REPORT = 23; // radar report
        
        public static final byte CID_RADAR_QUIT = 24; // radar退出
        
        public static final byte CID_GET_FRIDE_HI_UNREAD_COUNT = 25; // 获取拼车招呼数

        public static final byte CID_SYNC_START = 100;

        public static final byte CID_SYNC_USER_INFO = CID_SYNC_START
            + CID_GET_USER_INFO;

        public static final byte CID_SYNC_USER_INFO_LIST = CID_SYNC_START
            + CID_GET_USER_INFO_LIST;

        public static final byte CID_SYNC_FRIEND_LIST = CID_SYNC_START
            + CID_GET_FRIEND_LIST;

        public static final byte CID_SYNC_ASK_USER_INFO = CID_SYNC_START
            + CID_GET_ASK_USER_INFO;

        /*
         * 多点登录的数据实时同步通知
         */
        public static final byte CID_OL_SYNC_UPDATE_USER_INFO = 0 - CID_UPDATE_USER_INFO;

        public static final byte CID_OL_SYNC_ADD_FRIEND = 0 - CID_ADD_FRIEND;

        public static final byte CID_OL_SYNC_UPDATE_FRIEND = 0 - CID_UPDATE_FRIEND;
    }

    public interface UserInfoTag {
        public static final int ID = 1;

        public static final int YID = 2;

        public static final int ECP_ID = 3;

        public static final int MAIL_ID = 4;

        public static final int MOBILE = 5;

        public static final int NICK = 6;

        public static final int SIGNATURE = 7;

        public static final int ICON = 8;

        public static final int SEX = 9;

        public static final int BIRTHDAY = 10;

        public static final int CITY = 11;

        public static final int SOCIAL_ADDR = 12;

        public static final int BITS = 13;

        public static final int TIMETAG = 14;

        public static final int VALID_FLAG = 15;

        public static final int CONFIG = 16;

        public static final int EMAIL = 17;

        public static final int BKIMAGE = 18;
    }

    public interface FindUserResponseTag {
        public static final byte USER = 1; // 普通号

        public static final byte PA = 2; // 公众号
    }

    public interface PrivilegeTag {
        public static final int VIDEO = 1; // 视频通话
    }

    public interface ExResponseCode {
        public static final short PEER_FRIEND_SIZE_LIMIT = 1; // 接受添加好友请求时，对方的好友数到了上限
    }

    public interface FriendRecommendTag {
        public static final int UID = 1; // 被推荐人的uid

        public static final int TYPE = 2; // 推荐的来源，如通讯录、VOIP等

        public static final int SRCID = 3; // 来源账号，如手机号，邮箱号等

        public static final int MSG = 4; // 推荐文案

        public static final int TIMETAG = 5; // 推荐时间
    }

}
