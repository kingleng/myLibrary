package com.cpsdna.app.mykotlinapplication;

import android.text.TextUtils;
import android.util.Log;

import com.cpsdna.app.mykotlinapplication.util.TestSaveUtils;

import static android.content.Context.MODE_PRIVATE;

/**
 * 作者：jksfood on 2017/4/24 10:47
 */

public class SharePreHelper {

    /**
     * 热门问题 true居中对齐(默认) false左对齐
     *
     * @param left_mid
     */
    public static void setLeftMid(boolean left_mid) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putBoolean("left_mid", left_mid).apply();
    }

    /**
     * 热门问题 true居中对齐(默认) false左对齐
     */
    public static boolean getLeftMid() {
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getBoolean("left_mid", true);
    }

    /**
     * 设置是否开启动态热门数据
     * @param open
     */
    public static void setOpenDynamicHotQuestions(boolean open) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putBoolean("openDynamicHotQuestions", open).apply();
    }

    public static boolean getOpenDynamicHotQuestions() {
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getBoolean("openDynamicHotQuestions", false);
    }

    /**
     * 设置是否开启动态热门数据
     * @param open
     */
    public static void setOpenDuplex(boolean open) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putBoolean("openDuplex", open).apply();
    }

    public static boolean getOpenDuplex() {
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getBoolean("openDuplex", false);
    }

    /**
     * 设置是否开启律师留言
     * @param open
     */
    public static void setOpenLawyerMessage(boolean open) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putBoolean("openLawyerMessage", open).apply();
    }

    public static boolean getOpenLawyerMessage() {
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getBoolean("openLawyerMessage", true);
    }

    /**
     * 缓存动态热门数据
     * @param content
     */
    public static void setDynamicHotQuestions(String content) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putString("dynamicHotQuestions", content).apply();
    }
    public static String getDynamicHotQuestions() {
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getString("dynamicHotQuestions", "");
    }

    /**
     * 设置是否英文版
     * @param open
     */
    public static void setEnglishVersion(boolean open) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putBoolean("openEnglishVersion", open).apply();
    }

    public static boolean getEnglishVersion() {
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getBoolean("openEnglishVersion", false);
    }

    /**
     * 设置双工页底部功能
     * @param duplexBottom
     */
    public static void setDuplexBottom(String duplexBottom) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putString("duplexBottom", duplexBottom).apply();
    }

    /**
     * 设置双工页底部功能 (1.找律师 2.找文书 3.算费用 4.找法官 5.查机构 6.品风采 )
     */
    public static String getDuplexBottom() {
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getString("duplexBottom", "1,2,3,4,5,6");
    }

    /**
     * 接口异常忽略
     *
     * @param isIgnore
     */
    public static void setInterfaceIgnore(boolean isIgnore) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putBoolean("isIgnore", isIgnore).apply();
    }

    /**
     * 接口异常忽略 默认false 不忽略
     */
    public static boolean getInterfaceIgnore() {
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getBoolean("isIgnore", false);
    }

    /**
     * 设置热词
     *
     * @param content
     */
    public static void setHotWords(String content) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putString("hotwords", content).apply();
    }

    /**
     * 获取热词
     *
     * @return
     */
    public static String getHotWords() {
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getString("hotwords", "");
    }

    /**
     * 设置热门问题
     *
     * @param content
     */
    public static void setHotQuestions(String content) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putString("hotquestions", content).apply();
    }

    /**
     * 获取热门问题
     *
     * @return
     */
    public static String getHotQuestions() {
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getString("hotquestions", "");
    }

    /**
     * 设置热门法律问题
     */
    public static void setHotLawsQuestions(String content) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putString("hotLawQuestions", content).apply();
    }

    /**
     * 获取热门法律问题
     */
    public static String getHotLawsQuestions() {
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getString("hotLawQuestions", "");
    }

    /**
     * 设置语音类型
     * 0为讯飞，1为阿里
     *
     * @param type
     */
    public static void setVoiceType(int type) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putInt("voiceType", type).apply();
    }

    /**
     * 获取语音类型
     * 0为讯飞，1为阿里
     */
    public static int getVoiceType() {
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getInt("voiceType", 0);
    }

    /**
     * 设置设备id
     *
     * @param deviceId
     */
    public static void setDeviceId(String deviceId) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putString("deviceId", deviceId).apply();
        TestSaveUtils.wtiteFileFromBytes(deviceId);
    }

    public static String getDeviceId() {
        String deviceId = TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getString("deviceId", "");
        if(TextUtils.isEmpty(deviceId)){
            deviceId = TestSaveUtils.getFileData();
            TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putString("deviceId", deviceId).apply();
            Log.e("asd", TestSaveUtils.getFileData());
        }
        return deviceId;
    }

    /**
     * 存储标签
     *
     * @param tag
     */
    public static void setQuestionTag(String tag) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putString("questionTag", tag).apply();
    }

    /**
     * 获取标签
     *
     * @return
     */
    public static String getQuestionTag() {
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getString("questionTag", "");

    }

    /**
     * 设置问题内容
     *
     * @param content
     */
    public static void setContent(String content) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putString("question", content).apply();
    }

    /**
     * 初始化结果保存
     *
     * @return
     */
    public static String getInitData() {
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getString("initData", "");

    }

    /**
     * 初始化结果保存
     *
     * @param content
     */
    public static void setInitData(String content) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putString("initData", content).apply();
    }

    /**
     * 获取问题内容
     *
     * @return
     */
    public static String getContent() {
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getString("question", "");

    }

    /**
     * 设置地理位置
     *
     * @param content
     */
    public static void setLocation(String content) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putString("location", content).apply();
    }

    /**
     * 获取地理位置
     *
     * @return
     */
    public static String getLocation() {
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getString("location", "南京");

    }

    /**
     * 设置机构id
     *
     * @param industryId
     */
    public static void setIndustryId(String industryId) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putString("industryId", industryId).apply();
    }

    /**
     * 获取机构id
     *
     * @return
     */
    public static String getIndustryId() {
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getString("industryId", "");

    }


    /**
     * 设置eventId
     *
     * @param eventId
     */
    public static void setEventId(String eventId) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putString("eventId", eventId).apply();
    }

    /**
     * 获取eventId
     *
     * @return
     */
    public static String getEventId() {
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getString("eventId", "");

    }

    /**
     * 设置机构描述
     */
    public static void setEventDetail(String eventDetail) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putString("eventDetail", eventDetail).apply();
    }

    /**
     * 获取机构描述
     */
    public static String getEventDetail() {
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getString("eventDetail", "");
    }

    /**
     * 设置courtName
     *
     * @param courtName
     */
    public static void setCourtName(String courtName) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putString("courtName", courtName).apply();
    }

    /**
     * 获取courtName
     *
     * @return
     */
    public static String getCourtName() {
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getString("courtName", "");

    }

    /**
     * 设置昵称
     */
    public static void setNickName(String nickName) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putString("nickName", nickName).apply();
    }

    /**
     * 获取昵称
     */
    public static String getNickName() {

        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getString("nickName", "");

    }

    /**
     * 设置房间号
     */
    public static void setVideoRoomId(String roomId) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putString("roomId", roomId).apply();
    }

    /**
     * 获取房间号
     */
    public static String getVideoRoomId() {
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getString("roomId", "");

    }

    /**
     * 设置性别
     */
    public static void setGender(int gender) {
        if (gender == 0) {
            TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putString("gender", "女").apply();
        } else if (gender == 1) {
            TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putString("gender", "男").apply();
        }
    }

    /**
     * 获取性别
     */
    public static String getGender() {
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getString("gender", "");

    }

    /**
     * 设置管辖范围
     */
    public static void setEventArea(String eventArea) {

        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putString("eventArea", eventArea).commit();
    }

    /**
     * 获取管辖范围
     */
    public static String getEventArea() {
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getString("eventArea", "");
    }

    /**
     * 设置 案由   用来获取 诉讼风险
     *
     * @param cause
     */
    public static void setCause(String cause) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putString("cause", cause).apply();
    }

    /**
     * 获取当前问题的案由
     *
     * @return
     */
    public static String getCause() {
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getString("cause", "");

    }

    /**
     * 设置问题的QID
     *
     * @param qid
     */
    public static void setQuestionQid(String qid) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putString("qid", qid).apply();

    }

    /**
     * 获取问题的QID
     *
     * @return
     */
    public static String getQuestionQid() {
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getString("qid", "");
    }

    /**
     * 设置GPS坐标组的信息
     *
     * @param msg
     */
    public static void setGPSMsgs(String msg) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putString("gpsmsgs", msg).apply();
    }

    /**
     * 获取GPS坐标组的信息
     */
    public static String getGPSMsgs() {
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getString("gpsmsgs", "");
    }

    /**
     * 设置欢迎语
     *
     * @param content
     */
    public static void setHelloWord(String content) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putString("helloword", content).apply();
    }

    /**
     * 获取欢迎语
     */
    public static String getHelloWord() {
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getString("helloword", "");
    }

    /**
     * 设置机构二维码图标地址
     *
     * @param industryQRURL
     */
    public static void setIndustryQRURL(String industryQRURL) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putString("industryQRURL", industryQRURL).apply();
    }

    /**
     * 获取机构二维码图标地址
     */
    public static String getIndustryQRURL() {
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getString("industryQRURL", "");
    }

    /**
     * 设置法院服务模块信息
     *
     * @param serviceInfo
     */
    public static void setServiceInfo(String serviceInfo) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putString("serviceInfo", serviceInfo).apply();
    }

    /**
     * 获取法院服务模块信息
     */
    public static String getServiceInfo() {
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getString("serviceInfo", "");
    }

    /**
     * 设置法院风采页为默认页面
     *
     * @param isCourtDefault
     */
    public static void setIsCourtDefault(boolean isCourtDefault) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putBoolean("isCourtDefault", isCourtDefault).apply();
    }

    public static boolean getIsCourtDefault() {
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getBoolean("isCourtDefault", false);
    }

    /**
     * 设置默认返回页面的时间
     *单位  秒
     * @param time
     */
    public static void setBackPagerTime(int time) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putInt("backTime", time).apply();
    }

    /**
     * 获取默认返回页面的时间
     * 单位  秒
     */
    public static int getBackPagerTime() {
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getInt("backTime", 60);
    }

    /**
     * 设置低电量充电阈值
     * @param lowPowerValue
     */
    public static void setLowPowerValue(int lowPowerValue) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putInt("lowPowerValue", lowPowerValue).apply();
    }

    /**
     * 获取低电量充电阈值
     */
    public static int getLowPowerValue() {
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getInt("lowPowerValue", 20);
    }

    /**
     * 设置 机构风采的网页连接 或 图片名
     */
    public static void setIndustryStyle(String url) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putString("IndustryStyle", url).apply();
    }

    /**
     * 获取 机构风采的网页连接 或 图片名
     */
    public static String getIndustryStyle() {
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getString("IndustryStyle", "");
    }

    /**
     * 设置 是否是正式版位置
     */

    public static void setIsFullLocation(boolean isFullLocation) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putBoolean("isFullLocation", isFullLocation).apply();
    }

    /**
     * 获取是否正式版位置查询
     *
     * @return
     */
    public static boolean getIsFullLocation() {
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getBoolean("isFullLocation", false);

    }

    /**
     * 获取案件查询图片名字
     *
     * @param imgName
     */
    public static void setCaseSearchImgName(String imgName) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putString("CaseSearchImgName", imgName).apply();

    }

    public static String getCaseSearchImgName() {
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getString("CaseSearchImgName", "");
    }

    public static void setOpenNoticesImgName(String imgName) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putString("OpenNoticesImgName", imgName).apply();

    }

    public static void setDeliverNoticesImgName(String imgName) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putString("DeliverNoticesImgName", imgName).apply();

    }

    public static void setDocumentImgName(String imgName) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putString("DocumentImgName", imgName).apply();

    }

    public static String getOpenNoticesImgName() {
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getString("OpenNoticesImgName", "");
    }

    public static String getDeliverNoticesImgName() {
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getString("DeliverNoticesImgName", "");
    }

    public static String getDocumentImgName() {
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getString("DocumentImgName", "");
    }

    /**
     * 设置自定义回答数据集合
     */
    public static void setCustomMsgListData(String content) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putString("CustomMsgData", content).apply();
    }

    /**
     * 获取自定义回答数据集合
     */
    public static String getCustomMsgListData() {
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getString("CustomMsgData", "");
    }

    /**
     * 设置 是否使用自定义打招呼
     */

    public static void setIsCustomMsg(boolean isCustomMsg) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putBoolean("isCustomMsg", isCustomMsg).apply();
    }

    /**
     * 获取是否使用自定义打招呼
     *
     * @return
     */
    public static boolean getIsCustomMsg() {
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getBoolean("isCustomMsg", false);

    }

    //////////////////以下为存储法律援助相关

    /**
     * 设置申请人姓名
     *
     * @param content
     */
    public static void setProposerName(String content) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putString("ProposerName", content).apply();
    }

    /**
     * 获取申请人姓名
     *
     * @return
     */
    public static String getProposerName() {
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getString("ProposerName", "");
    }

    /**
     * 设置申请人身份证
     *
     * @param content
     */
    public static void setProposerId(String content) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putString("ProposerId", content).apply();
    }

    /**
     * 获取申请人s身份证
     *
     * @return
     */
    public static String getProposerId() {
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getString("ProposerId", "");
    }

    /**
     * 设置申请人手机号
     *
     * @param content
     */
    public static void setProposerPhone(String content) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putString("ProposerPhone", content).apply();
    }

    /**
     * 获取申请人手机号
     *
     * @return
     */
    public static String getProposerPhone() {
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getString("ProposerPhone", "");
    }

    /**
     * 设置 申请人性别
     *
     * @param content
     */
    public static void setProposerSex(int content) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putInt("ProposerSex", content).apply();
    }

    /**
     * 获取申请人性别
     *
     * @return
     */
    public static int getProposerSex() {
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getInt("ProposerSex", 1);
    }

    /**
     * 设置 申请人地址
     *
     * @param content
     */
    public static void setProposerAddress(String content) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putString("ProposerAddress", content).apply();
    }

    /**
     * 获取申请人地址
     *
     * @return
     */
    public static String getProposerAddress() {
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getString("ProposerAddress", "");
    }

    /**
     * 设置 申请人地址区划码
     *
     * @param content
     */
    public static void setProposerAddressCode(String content) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putString("ProposerAddressCode", content).apply();
    }

    /**
     * 获取申请人地址区划码
     *
     * @return
     */
    public static String getProposerAddressCode() {
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getString("ProposerAddressCode", "");
    }

    /**
     * 设置申请人家庭收入
     *
     * @param content
     */
    public static void setMonthlyIncome(int content) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putInt("MonthlyIncome", content).apply();
    }


    /**
     * 获取申请人家庭收入
     *
     * @return
     */
    public static int getMonthlyIncome() {
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getInt("MonthlyIncome", 0);
    }

    /**
     * 设置申请人案由
     *
     * @param content
     */
    public static void setProposerCause(String content) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putString("ProposerCause", content).apply();
    }

    /**
     * 获取申请人案由
     *
     * @return
     */
    public static String getProposerCause() {
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getString("ProposerCause", "");
    }

    /**
     * 设置申请人案由编号
     *
     * @param num
     */
    public static void setProposerCauseNum(int num) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putInt("ProposerCauseNum", num).apply();

    }
    /**
     * 设置申请人案由编号
     **/
    public static int getProposerCauseNum() {
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getInt("ProposerCauseNum",0);

    }

    /**
     * 设置申请人的类型
     *
     * @param content
     */
    public static void setProposerType(String content) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putString("ProposerType", content).apply();
    }

    /**
     * 获取申请人的类型
     *
     * @return
     */
    public static String getProposerType() {
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getString("ProposerType", "");
    }

    /**
     * 设置申请人类型编码
     *
     * @param num
     */
    public static void setProposerTypeNum(int num) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putInt("ProposerTypeNum", num).apply();

    }

    /**
     * 获取申请人类型编码
     *
     * @return
     */
    public static int getProposerTypeNum() {

        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getInt("ProposerTypeNum", 0);

    }

    /**
     * 设置申请人的人群类别   哪类人群
     *
     * @param content
     */
    public static void setProposerHumanType(String content) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putString("ProposerHumanType", content).apply();
    }

    /**
     * 获取申请人的人群类别  哪类人群
     *
     * @return
     */
    public static String getProposerHumanType() {
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getString("ProposerHumanType", "");
    }

    /**
     * 设置申请人的人群类别编码  哪类人群
     *
     * @param num
     */
    public static void setProposerHumanTypeNum(int num) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putInt("ProposerHumanTypeNum", num).apply();
    }

    /**
     * 获取申请人的人群类别编码
     */
    public static int getProposerHumanTypeNum() {
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getInt("ProposerHumanTypeNum", 0);
    }

    /**
     * 设置案件描述
     *
     * @param content
     */
    public static void setInputDetail(String content) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putString("InputDetail", content).apply();
    }

    /**
     * 获取案件描述
     *
     * @return
     */
    public static String getInputDetail() {
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getString("InputDetail", "");
    }

    /**
     * 设置当事人的姓名
     *
     * @param content
     */
    public static void setLitigantName(String content) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putString("LitigantName", content).apply();

    }

    /**
     * 获取当事人姓名
     *
     * @return
     */
    public static String getLitigantName() {
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getString("LitigantName", "");


    }

    /**
     * 设置当事人的地址
     *
     * @param content
     */
    public static void setLitigantAddress(String content) {

        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putString("LitigantAddress", content).apply();

    }

    /**
     * 获取当事人地址
     *
     * @return
     */
    public static String getLitigantAddress() {

        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getString("LitigantAddress", "");
    }

    /**
     * 设置当事人的区划码
     *
     * @param content
     */
    public static void setLitigantAddressCode(String content) {

        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putString("LitigantAddressCode", content).apply();

    }

    /**
     * 获取当事人区划码
     *
     * @return
     */
    public static String getLitigantAddressCode() {

        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getString("LitigantAddressCode", "");
    }

    /**
     * 获取当事人手机号
     *
     * @return
     */
    public static String getLitigantPhone() {
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getString("LitigantPhone", "");
    }

    /**
     * 设置当事人手机号
     *
     * @return
     */
    public static void setLitigantPhone(String content) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putString("LitigantPhone", content).apply();
    }
    ///////  以上为法律援助相关


    /**
     * 存储起诉状文书模板数据
     *
     * @param content
     */
    public static void setTemplateMap1(String content) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putString("qisuzhuang", content).apply();
    }

    /**
     * 获取起诉状文书模板数据
     */
    public static String getTemplateMap1() {
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getString("qisuzhuang", "");
    }

    /**
     * 存储上诉状文书模板数据
     *
     * @param content
     */
    public static void setTemplateMap2(String content) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putString("shangsuzhuang", content).apply();
    }

    /**
     * 获取上诉状文书模板数据
     */
    public static String getTemplateMap2() {
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getString("shangsuzhuang", "");
    }

    /**
     * 存储申请书文书模板数据
     *
     * @param content
     */
    public static void setTemplateMap3(String content) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putString("shenqingshu", content).apply();
    }

    /**
     * 获取申请书文书模板数据
     */
    public static String getTemplateMap3() {
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getString("shenqingshu", "");
    }

    /**
     * 存储合同模板文书模板数据
     *
     * @param content
     */
    public static void setTemplateMap4(String content) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putString("hetongmuban", content).apply();
    }

    /**
     * 获取合同模板文书模板数据
     */
    public static String getTemplateMap4() {
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getString("hetongmuban", "");
    }

    /**
     * 存储其他文书文书模板数据
     *
     * @param content
     */
    public static void setTemplateMap5(String content) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putString("qitawenshu", content).apply();
    }

    /**
     * 获取其他文书文书模板数据
     */
    public static String getTemplateMap5() {
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getString("qitawenshu", "");
    }

    /**
     * 设置热门问题的名字
     * 劳动争议#劳动#2000问
     * @param content
     */
    public static void setHotQuestionNameInfo(String content) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putString("hotQuestionNameInfo", content).apply();
    }
    public static String getHotQuestionNameInfo() {
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getString("hotQuestionNameInfo", "");
    }
    /**
     * 设置热门问题的内容
     * 劳动争议#劳动#2000问
     * @param content
     */
    public static void setHotQuestionContentInfo(String content) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putString("hotQuestionContentInfo", content).apply();
    }
    public static String getHotQuestionContentInfo() {
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getString("hotQuestionContentInfo", "");
    }

    /**
     * 设置离开充电
     * @param content
     */
    public static void setTime1(String content) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putString("time1",content).apply();
    }

    public static String getTime1() {
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getString("time1", "2500");
    }

    /**
     * 设置开始充电
     * @param content
     */
    public static void setTime2(String content) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putString("time2",content).apply();
    }

    public static String getTime2() {
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getString("time2", "2500");
    }

    /**
     * 设置低电量充电
     * @param content
     */
    public static void setLowBatteryLevel(String content) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putString("lowBattery", content).apply();
    }

    public static String getLowBatteryLevel() {
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getString("lowBattery", "20");
    }

    /**
     * 设置高电量离开充电
     * @param content
     */
    public static void setHighBatteryLevel(String content) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putString("highBattery", content).apply();
    }

    public static String getHighBatteryLevel() {
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getString("highBattery", "80");
    }

    /**
     * 设置法律援助用户生成的ID
     */
    public static void setLegalAidUserInfoID(String content) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putString("LegalAidUserInfoID", content).apply();

    }
    /**
     * 设置法律援助用户生成的ID
     */
    public static String getLegalAidUserInfoID() {
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getString("LegalAidUserInfoID", "");

    }

    public static void setIdCardInfo(String name) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putString("IDCardInfo", name).apply();
    }

    public static String getIdCardInfo() {
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getString("IDCardInfo", "");
    }

    /**
     * 设置语音识别类型
     * @param asrType
     */
    public static void setAsrType(String asrType){
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putString("asrType", asrType).apply();
    }

    public static String getAsrType(){
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getString("asrType", "cloud");
    }

    /**
     * 设置是否调用lawquestion接口，默认调用
     */
    public static void setLawQuestionType(String lawQuestionType){
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putString("lawQuestionType", lawQuestionType).apply();
    }

    public static String getLawQuestionType(){
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getString("lawQuestionType","不启用");
    }

    /**
     * 设置是否调用图灵接口，默认不调用
     */
    public static void setIsNeedTuling(boolean isNeedTuling){
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putBoolean("isNeedTuling", isNeedTuling).apply();
    }

    public static boolean getIsNeedTuling(){
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getBoolean("isNeedTuling",false);
    }

    /**
     * 设置是否启用标准答案样式，默认调用
     */
    public static void setIsNeedStandardAnswer(boolean isNeedStandardAnswer){
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putBoolean("isNeedStandardAnswer", isNeedStandardAnswer).apply();
    }

    public static boolean getIsNeedStandardAnswer(){
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getBoolean("isNeedStandardAnswer",false);
    }

    /**
     * 语音识别资源种类
     * xunfei
     * ali
     * tengxun
     * @param voiceKind
     */
    public static void setVoiceKind(String voiceKind) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putString("voiceKind", voiceKind).apply();
    }

    public static String getVoiceKind() {
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getString("voiceKind","xunfei");
    }

    /**
     * 设置 人脸识别自动登出时间间隔
     *单位  秒
     * @param faceOutLoginTime
     */
    public static void setFaceOutLoginTime(int faceOutLoginTime) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putInt("faceOutLoginTime", faceOutLoginTime).apply();
    }

    /**
     * 获取 人脸识别自动登出时间间隔
     * 单位  秒
     */
    public static int getFaceOutLoginTime() {
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getInt("faceOutLoginTime", 5);
    }

    /**
     * 设置 人民法院报 web缓存时间
     *单位  秒
     * @param fayuanbaoWebCacheTime
     */
    public static void setFayuanbaoWebCacheTime(Long fayuanbaoWebCacheTime) {
        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putLong("fayuanbaoWebCacheTime", fayuanbaoWebCacheTime).apply();
    }

    /**
     * 获取 人民法院报 web缓存时间
     * 单位  秒
     */
    public static Long getFayuanbaoWebCacheTime() {
        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getLong("fayuanbaoWebCacheTime", 5);
    }

//    /**
//     * 法院功能项设置
//     */
//    public static void setFunctionConfigFY(String functionConfigFY) {
//        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putString("functionConfigFY", functionConfigFY).apply();
//    }
//
//    /**
//     * 获取 法院功能项设置
//     */
//    public static String getFunctionConfigFY() {
//        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getString("functionConfigFY",
//                Constant.FUNCTION_CONFIG_FY);
//    }
//
//    /**
//     * 司法厅功能项设置
//     */
//    public static void setFunctionConfigSFT(String functionConfigSFT) {
//        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putString("functionConfigSFT", functionConfigSFT).apply();
//    }
//
//    /**
//     * 获取司法厅功能项设置
//     */
//    public static String getFunctionConfigSFT() {
//        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getString("functionConfigSFT",
//                Constant.FUNCTION_CONFIG_SFT);
//    }
//
//    /**
//     * 其它功能项设置
//     */
//    public static void setFunctionConfigOther(String functionConfigOther) {
//        TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).edit().putString("functionConfigOther", functionConfigOther).apply();
//    }
//
//    /**
//     * 获取其它功能项设置
//     */
//    public static String getFunctionConfigOther() {
//        return TopApplication.getTopApplication().getSharedPreferences(TopApplication.APP_SP, MODE_PRIVATE).getString("functionConfigOther",
//                Constant.FUNCTION_CONFIG_OTHER);
//    }
}
