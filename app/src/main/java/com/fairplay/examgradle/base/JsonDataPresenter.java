package com.fairplay.examgradle.base;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import com.fairplay.examgradle.contract.MMKVContract;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gwm.annotation.json.JSON;
import com.gwm.annotation.json.Param;
import com.gwm.util.ContextUtil;
import com.orhanobut.logger.Logger;
import com.tencent.mmkv.MMKV;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public abstract class JsonDataPresenter<J extends JsonDataPresenter.HttpBaseBean,D> extends BaseDataPresenter<D> {
    private Object jsonCreator;
    private static final String BASESTRING = "0123456789abcdefghijklmnopqrstuvwxyz";
    private static String CHECK_CODE = "fp2018";
    private static String AES_KEY = CHECK_CODE;
    private MMKV mmkv;

    public JsonDataPresenter(Class<J> clazz) {
        super();
        mmkv = MMKV.defaultMMKV();
        try {
            jsonCreator = Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{clazz}, new GenJsonString());
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
    }
    public J getJsonCreator() {
        return (J) jsonCreator;
    }
    protected JSONArray getListString(List<String> strs){
        if (strs == null || strs.isEmpty()){
            return new JSONArray();
        }
        JSONArray array = new JSONArray();
        for (String string : strs){
            array.put(string);
        }
        return array;
    }

    /**
     * 加密data
     * @param data
     * @return
     */
    protected String aesEncodeString(String data){
        // 加密
        SecretKey secretKey = new SecretKeySpec(AES_KEY.getBytes(), "AES");
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encoderStr = cipher.doFinal(data.getBytes("UTF-8"));
            String hexE = new String(Hex.encodeHex(encoderStr));
            return hexE;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getSignData(String signData) {
        String sign = new String(Hex.encodeHex(DigestUtils.sha1(signData)));
        Logger.i("getSignData===>" + sign);
        String randomS = randomString(BASESTRING, 10);
        AES_KEY = CHECK_CODE + randomS;
        String startString = sign.substring(0, 8);
        String endString = sign.substring(8, sign.length());
        return startString + randomS + endString;

    }

    public static String randomString(String baseString, int length) {
        final StringBuilder sb = new StringBuilder();

        if (length < 1) {
            length = 1;
        }
        int baseLength = baseString.length();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
//            int number = getRandom().nextInt(baseLength);
            int number = random.nextInt(baseLength);
            sb.append(baseString.charAt(number));
        }
        return sb.toString();
    }
    protected String genJsonString(long bizType,String data){
        String lastUpdateTime = mmkv.getString(MMKVContract.LASTUPDATETIME,"0");
        String msEquipment = getDeviceInfo();
        String signData = getSignData(data);
        String token = mmkv.getString(MMKVContract.TOKEN,"");
        return getJsonCreator().sign(bizType,
                aesEncodeString(data),
                lastUpdateTime,
                msEquipment,
                System.currentTimeMillis()+"",
                signData,
                token);
    }

    public static String getDeviceInfo() {
        TelephonyManager phone = (TelephonyManager) ContextUtil.get().getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        WifiManager wifi = (WifiManager) ContextUtil.get().getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        return wifi.getConnectionInfo().getMacAddress() + "," + phone.getDeviceId() + "," + getCpuName() + "," + phone.getNetworkOperator();
    }
    private static String getCpuName() {
        String str1 = "/proc/cpuinfo";
        String str2 = "";
        try {
            FileReader fr = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(fr);
            while ((str2 = localBufferedReader.readLine()) != null) {
                if (str2.contains("Hardware")) {
                    return str2.split(":")[1];
                }
            }
            localBufferedReader.close();
        } catch (IOException e) {
        }
        return null;
    }
    public interface HttpBaseBean{
        @JSON
        String sign(@Param("bizType")long bizType,
                    @Param("data")String data,
                    @Param("lastUpdateTime")String lastUpdateTime,
                    @Param("msEquipment")String msEquipment,
                    @Param("requestTime")String requestTime,
                    @Param("sign")String sign,
                    @Param("token")String token);
    }
}
