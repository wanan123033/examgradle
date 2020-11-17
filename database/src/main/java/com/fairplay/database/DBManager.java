package com.fairplay.database;

import android.database.Cursor;
import android.util.Log;

import com.fairplay.database.entity.DaoMaster;
import com.fairplay.database.entity.DaoSession;
import com.fairplay.database.entity.DataRtiveBean;
import com.fairplay.database.entity.Item;
import com.fairplay.database.entity.ItemDao;
import com.fairplay.database.entity.MqttBean;
import com.fairplay.database.entity.MqttBeanDao;
import com.fairplay.database.entity.MultipleItem;
import com.fairplay.database.entity.MultipleItemDao;
import com.fairplay.database.entity.MultipleResult;
import com.fairplay.database.entity.MultipleResultDao;
import com.fairplay.database.entity.RoundResult;
import com.fairplay.database.entity.RoundResultDao;
import com.fairplay.database.entity.Schedule;
import com.fairplay.database.entity.ScheduleDao;
import com.fairplay.database.entity.StudentGroupItem;
import com.fairplay.database.entity.StudentGroupItemDao;
import com.gwm.util.ContextUtil;

import org.greenrobot.greendao.database.Database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBManager {
    private static DBManager mInstance;
    public static final String DB_NAME = "examgradle";
    private DBOpenHelper helper;
    private Database db;
    private DaoSession daoSession;
    private DaoMaster daoMaster;
    private static final String DB_PASSWORD = "fplwwj";
    private ItemDao itemDao;
    private RoundResultDao roundResultDao;
    private MultipleResultDao multipleResultDao;
    private ScheduleDao scheduleDao;
    private MultipleItemDao multipleItemDao;
    private StudentGroupItemDao studentGroupItemDao;
    private MqttBeanDao mqttBeanDao;

    public static synchronized DBManager getInstance() {
        if (mInstance == null) {
            mInstance = new DBManager();
            //使用单例来保证数据库初始化,这该死的机器竟然可能在不需要的情况下清除application类的数据
            mInstance.initDB();
        }
        return mInstance;
    }

    public void initDB() {
        helper = new DBOpenHelper(ContextUtil.get(), DB_NAME);
        db = BuildConfig.DEBUG ? helper.getWritableDb() : helper.getEncryptedReadableDb(DB_PASSWORD);
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        itemDao = daoSession.getItemDao();
        roundResultDao = daoSession.getRoundResultDao();
        multipleResultDao = daoSession.getMultipleResultDao();
        scheduleDao = daoSession.getScheduleDao();
        multipleItemDao = daoSession.getMultipleItemDao();
        studentGroupItemDao = daoSession.getStudentGroupItemDao();
        mqttBeanDao = daoSession.getMqttBeanDao();
    }

    public Long insertRoundResult(RoundResult result) {
        RoundResult roundResult = roundResultDao.queryBuilder().where(RoundResultDao.Properties.ItemCode.eq(result.getItemCode()),
                RoundResultDao.Properties.SubitemCode.eq(result.getSubitemCode()),
                RoundResultDao.Properties.ScheduleNo.eq(result.getScheduleNo()),
                RoundResultDao.Properties.ExamPlaceName.eq(result.getExamPlaceName()),
                RoundResultDao.Properties.GroundNo.eq(result.getGroundNo()),
                RoundResultDao.Properties.StudentCode.eq(result.getStudentCode()),
                RoundResultDao.Properties.RoundNo.eq(result.getRoundNo()),
                RoundResultDao.Properties.IsLastResult.eq(1)).limit(1).unique();
        if (roundResult != null){
            roundResult.setIsLastResult(0);
            roundResultDao.update(roundResult);
        }
        return roundResultDao.insertOrReplace(result);
    }

    public long insertItem(Item item) {
        return itemDao.insertOrReplace(item);
    }


    public int getUnUploadNum() {
        return 0;
    }

    public void clear() {
        scheduleDao.deleteAll();
        itemDao.deleteAll();
        multipleItemDao.deleteAll();
        roundResultDao.deleteAll();
        multipleResultDao.deleteAll();
        mqttBeanDao.deleteAll();
    }

    public Map<String,Object> getItemStudenCount(String itemCode,String subItemCode){
        StringBuffer sqlBuf = new StringBuffer();
        sqlBuf.append("select count(distinct "+MqttBeanDao.Properties.StudentCode.columnName+") as stu_count from "+MqttBeanDao.TABLENAME);
        sqlBuf.append(" where "+MqttBeanDao.Properties.ItemCode.columnName + "=? ");
        sqlBuf.append(" and "+MqttBeanDao.Properties.SubitemCode.columnName + "=? ");
        Cursor c = daoSession.getDatabase().rawQuery(sqlBuf.toString(), new String[]{itemCode,subItemCode});
        Map<String, Object> countMap = new HashMap<>();

        if (c.moveToNext()) {
            int count = c.getInt(0);
            countMap.put("count", count);
        }
        c.close();

        return countMap;
    }

    public RoundResult getLastRoundResult(String studentCode) {
        RoundResult roundResult = roundResultDao.queryBuilder()
                .where(RoundResultDao.Properties.StudentCode.eq(studentCode),RoundResultDao.Properties.IsLastResult.eq(1))
                .limit(1).unique();
        if (roundResult == null){
            roundResult = new RoundResult();
        }
        return roundResult;
    }

    public List<RoundResult> getRoundResultByStuCode(String studentCode) {
        return roundResultDao.queryBuilder().where(RoundResultDao.Properties.StudentCode.eq(studentCode)).list();
    }

    public List<MultipleResult> getMultioleResult(Long roundResultId) {
        return multipleResultDao.queryBuilder().where(MultipleResultDao.Properties.RoundId.eq(roundResultId)).list();
    }

    public void insertMultipResult(MultipleResult multipleResult1) {
        multipleResultDao.insertInTx(multipleResult1);
    }

    public Item getItemByItemCode(String itemCode) {
        return itemDao.queryBuilder().where(ItemDao.Properties.ItemCode.eq(itemCode)).limit(1).unique();
    }
    public Item getItemByItemCode(String itemCode,String subitemCode) {
        return itemDao.queryBuilder().where(ItemDao.Properties.ItemCode.eq(itemCode),ItemDao.Properties.SubitemCode.eq(subitemCode)).limit(1).unique();
    }

    public List<Item> getItemList() {
        return itemDao.loadAll();
    }

    public void insertSchedule(Schedule schedule) {
        scheduleDao.insert(schedule);
    }

    public void insertMultipItem(MultipleItem multipleItem) {
        multipleItemDao.insert(multipleItem);
    }

    public List<MultipleItem> getMultipleItemList(Long itemId) {
        return multipleItemDao.queryBuilder().where(MultipleItemDao.Properties.ItemId.eq(itemId)).list();
    }

    public List<RoundResult> getRoundResultByStuItem(String studentCode, String itemCode, String subItemCode) {
        return roundResultDao.queryBuilder().where(RoundResultDao.Properties.StudentCode.eq(studentCode),
                RoundResultDao.Properties.ItemCode.eq(itemCode),
                RoundResultDao.Properties.SubitemCode.eq(subItemCode)).list();
    }

    public void insertGroup(StudentGroupItem studentGroupItem) {
        studentGroupItemDao.insertInTx(studentGroupItem);
    }

    public Item getItemByItemId(long itemId) {
        return itemDao.queryBuilder().where(ItemDao.Properties.Id.eq(itemId)).limit(1).unique();
    }

    public StudentGroupItem getGroupById(long groupId) {
        return studentGroupItemDao.queryBuilder().where(StudentGroupItemDao.Properties.Id.eq(groupId)).limit(1).unique();
    }

    public RoundResult getRoundResultById(long roundId) {
        return roundResultDao.queryBuilder().where(RoundResultDao.Properties.Id.eq(roundId)).limit(1).unique();
    }

    public Schedule getScheduleById(long scheduleId) {
        return scheduleDao.queryBuilder().where(ScheduleDao.Properties.Id.eq(scheduleId)).limit(1).unique();
    }

    public long insertMqttBean(MqttBean mqttBean) {
        MqttBean bean = mqttBeanDao.queryBuilder().where(MqttBeanDao.Properties.ItemCode.eq(mqttBean.getItemCode()),
                MqttBeanDao.Properties.SubitemCode.eq(mqttBean.getSubitemCode()), MqttBeanDao.Properties.StudentCode.eq(mqttBean.getStudentCode())).limit(1).unique();
        if (bean != null){
            return bean.getId();
        }
        return mqttBeanDao.insert(mqttBean);
    }

    public MqttBean getMQTTBean(long mqttId) {
        return mqttBeanDao.queryBuilder().where(MqttBeanDao.Properties.Id.eq(mqttId)).unique();
    }

    public List<DataRtiveBean> getAllData(int limit, int offset) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("select R.*,I.*,M.* from "+RoundResultDao.TABLENAME+" R,"+ItemDao.TABLENAME+" I,"+MqttBeanDao.TABLENAME+" M");
        stringBuffer.append(" where R."+RoundResultDao.Properties.StudentCode.columnName +"=M."+MqttBeanDao.Properties.StudentCode.columnName);
        stringBuffer.append(" and R."+RoundResultDao.Properties.ItemCode.columnName +"=M."+MqttBeanDao.Properties.ItemCode.columnName);
        stringBuffer.append(" and R."+RoundResultDao.Properties.SubitemCode.columnName +"=M."+MqttBeanDao.Properties.SubitemCode.columnName);
        stringBuffer.append(" and R."+RoundResultDao.Properties.ItemCode.columnName +"=I."+ItemDao.Properties.ItemCode.columnName);
        stringBuffer.append(" and R."+RoundResultDao.Properties.SubitemCode.columnName +"=I."+ItemDao.Properties.SubitemCode.columnName);
        stringBuffer.append(" and R."+RoundResultDao.Properties.ExamType.columnName +"=M."+MqttBeanDao.Properties.ExamStatus.columnName);
        if (limit != -1)
            stringBuffer.append(" limit " + offset + "," + limit);
        Cursor cursor = daoSession.getDatabase().rawQuery(stringBuffer.toString(), null);
        List<DataRtiveBean> dataRtiveBeans = new ArrayList<>();
        while (cursor.moveToNext()){
            Log.e("TAG====>","-------------");
            DataRtiveBean bean = new DataRtiveBean();
            bean.studentCode = cursor.getString(cursor.getColumnIndex(RoundResultDao.Properties.StudentCode.columnName));
            bean.examPlaceName = cursor.getString(cursor.getColumnIndex(MqttBeanDao.Properties.ExamPlaceName.columnName));
            bean.itemName = cursor.getString(cursor.getColumnIndex(ItemDao.Properties.ItemName.columnName));
            dataRtiveBeans.add(bean);
        }
        return dataRtiveBeans;
    }

    public List<RoundResult> getStuRoundResult(String studentCode, String itemCode, String subItemCode) {
        return roundResultDao.queryBuilder().where(RoundResultDao.Properties.StudentCode.eq(studentCode),
                RoundResultDao.Properties.ItemCode.eq(itemCode),
                RoundResultDao.Properties.SubitemCode.eq(subItemCode)).list();
    }
    public List<RoundResult> getStuRoundResult(String studentCode, String itemCode, String subItemCode,String examPlaceName,String groupNo,int examStatus) {
        return roundResultDao.queryBuilder().where(RoundResultDao.Properties.StudentCode.eq(studentCode),
                RoundResultDao.Properties.ItemCode.eq(itemCode),
                RoundResultDao.Properties.SubitemCode.eq(subItemCode),
                RoundResultDao.Properties.ExamPlaceName.eq(examPlaceName),
                RoundResultDao.Properties.GroundNo.eq(groupNo),
                RoundResultDao.Properties.ExamType.eq(examStatus),
                RoundResultDao.Properties.IsLastResult.eq(1)).list();

    }

    public List<MqttBean> getMQTTBean(String itemCode, String subItemCode) {

        return mqttBeanDao.queryBuilder().where(MqttBeanDao.Properties.ItemCode.eq(itemCode),
                MqttBeanDao.Properties.SubitemCode.eq(subItemCode))
                .list();
    }

    public List<Item> getBigItems() {
        String sql = "select * from "+ItemDao.TABLENAME+" where "+ItemDao.Properties.ItemCode.columnName +"="+ItemDao.Properties.SubitemCode.columnName;
        Cursor cursor = daoSession.getDatabase().rawQuery(sql, null);
        List<Item> items = new ArrayList<>();
        while (cursor.moveToNext()){
            Item item = itemDao.readEntity(cursor, 0);
            items.add(item);
        }
        return items;
    }

    public List<Item> getSmoatItems(String itemCode) {
        String sql = "select * from "+ItemDao.TABLENAME+
                " where "+ItemDao.Properties.ItemCode.columnName+"=?"+
                " and "+ItemDao.Properties.ItemCode.columnName+"!="+ItemDao.Properties.SubitemCode.columnName;
        Cursor cursor = daoSession.getDatabase().rawQuery(sql,new String[]{itemCode});
        List<Item> itemList = new ArrayList<>();
        while (cursor.moveToNext()){
            Item item = itemDao.readEntity(cursor, 0);
            itemList.add(item);
        }
        return itemList;
    }

    public List<MqttBean> getMQTTBean(String stuCode) {
        return mqttBeanDao.queryBuilder().where(MqttBeanDao.Properties.StudentCode.eq(stuCode)).list();
    }

    public void updateResultStateUnload(RoundResult result) {
        result.setUpdateState(1);
        roundResultDao.update(result);
    }

    public MqttBean getMQTTBean(String itemCode, String subItemCode, String studentCode) {
        return mqttBeanDao.queryBuilder().where(MqttBeanDao.Properties.ItemCode.eq(itemCode),MqttBeanDao.Properties.SubitemCode.eq(subItemCode),MqttBeanDao.Properties.StudentCode.eq(studentCode)).unique();
    }

    public List<RoundResult> getRoundResultUploadState() {
        return roundResultDao.queryBuilder().where(RoundResultDao.Properties.UpdateState.eq(0)).list();
    }

    public void close() {
        db.close();
    }
}
