package com.fairplay.database;

import android.database.Cursor;

import com.fairplay.database.entity.DaoMaster;
import com.fairplay.database.entity.DaoSession;
import com.fairplay.database.entity.ExamPlace;
import com.fairplay.database.entity.ExamPlaceDao;
import com.fairplay.database.entity.GroupInfo;
import com.fairplay.database.entity.GroupInfoDao;
import com.fairplay.database.entity.Item;
import com.fairplay.database.entity.ItemDao;
import com.fairplay.database.entity.StudentGroupItem;
import com.fairplay.database.entity.MultipleItem;
import com.fairplay.database.entity.MultipleItemDao;
import com.fairplay.database.entity.MultipleResult;
import com.fairplay.database.entity.MultipleResultDao;
import com.fairplay.database.entity.RoundResult;
import com.fairplay.database.entity.RoundResultDao;
import com.fairplay.database.entity.Schedule;
import com.fairplay.database.entity.ScheduleDao;
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
    private GroupInfoDao groupInfoDao;
    private ExamPlaceDao examPlaceDao;

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
        groupInfoDao = daoSession.getGroupInfoDao();
        examPlaceDao = daoSession.getExamPlaceDao();
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
    }

    public Map<String,Object> getItemStudenCount(String itemCode,String subItemCode){
        StringBuffer sqlBuf = new StringBuffer();
        sqlBuf.append("select count(distinct "+ StudentGroupItemDao.Properties.StudentCode.columnName+") as stu_count from "+StudentGroupItemDao.TABLENAME);
        sqlBuf.append(" where "+StudentGroupItemDao.Properties.ItemCode.columnName + "=? ");
        sqlBuf.append(" and "+StudentGroupItemDao.Properties.SubitemCode.columnName + "=? ");
        Cursor c = daoSession.getDatabase().rawQuery(sqlBuf.toString(), new String[]{itemCode,subItemCode});
        Map<String, Object> countMap = new HashMap<>();

        if (c.moveToNext()) {
            int count = c.getInt(0);
            countMap.put("count", count);
        }
        c.close();

        return countMap;
    }

    public List<MultipleResult> getMultioleResult(Long roundResultId) {
        return multipleResultDao.queryBuilder().where(MultipleResultDao.Properties.RoundId.eq(roundResultId)).list();
    }

    public void insertMultipResult(MultipleResult multipleResult1) {
        multipleResultDao.insertInTx(multipleResult1);
    }
    public Item getItemByItemCode(String itemCode,String subitemCode) {
        return itemDao.queryBuilder().where(ItemDao.Properties.ItemCode.eq(itemCode),ItemDao.Properties.SubitemCode.eq(subitemCode)).limit(1).unique();
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

    public RoundResult getRoundResultById(long roundId) {
        return roundResultDao.queryBuilder().where(RoundResultDao.Properties.Id.eq(roundId)).limit(1).unique();
    }

    public long insertMqttBean(StudentGroupItem mqttBean) {
        StudentGroupItem bean = studentGroupItemDao.queryBuilder().where(StudentGroupItemDao.Properties.ItemCode.eq(mqttBean.getItemCode()),
                StudentGroupItemDao.Properties.SubitemCode.eq(mqttBean.getSubitemCode()), StudentGroupItemDao.Properties.StudentCode.eq(mqttBean.getStudentCode())).limit(1).unique();
        if (bean != null){
            return bean.getId();
        }
        return studentGroupItemDao.insert(mqttBean);
    }

    public StudentGroupItem getMQTTBean(long mqttId) {
        return studentGroupItemDao.queryBuilder().where(StudentGroupItemDao.Properties.Id.eq(mqttId)).unique();
    }
    public List<RoundResult> getStuRoundResult(String studentCode, String itemCode, String subItemCode) {
        return roundResultDao.queryBuilder().where(RoundResultDao.Properties.StudentCode.eq(studentCode),
                RoundResultDao.Properties.ItemCode.eq(itemCode),
                RoundResultDao.Properties.SubitemCode.eq(subItemCode))
                .list();
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

    public List<StudentGroupItem> getMQTTBean(String itemCode, String subItemCode) {
        return studentGroupItemDao.queryBuilder().where(StudentGroupItemDao.Properties.ItemCode.eq(itemCode),
                StudentGroupItemDao.Properties.SubitemCode.eq(subItemCode))
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

    public List<StudentGroupItem> getMQTTBean(String stuCode) {
        return studentGroupItemDao.queryBuilder().where(StudentGroupItemDao.Properties.StudentCode.eq(stuCode)).list();
    }

    public void updateResultStateUnload(RoundResult result) {
        result.setUpdateState(1);
        roundResultDao.update(result);
    }

    public StudentGroupItem getMQTTBean(String itemCode, String subItemCode, String studentCode) {
        return studentGroupItemDao.queryBuilder().where(StudentGroupItemDao.Properties.ItemCode.eq(itemCode),StudentGroupItemDao.Properties.SubitemCode.eq(subItemCode),StudentGroupItemDao.Properties.StudentCode.eq(studentCode)).unique();
    }

    public List<RoundResult> getRoundResultUploadState() {
        return roundResultDao.queryBuilder().where(RoundResultDao.Properties.UpdateState.eq(0)).list();
    }

    public void close() {
        db.close();
    }

    public void insertGroupInfo(GroupInfo dataGroup) {
        groupInfoDao.insertInTx(dataGroup);
    }

    public StudentGroupItem getMQTTBean(String itemCode, String subItemCode, String studentCode, int examType, String examplaceName,String scheduleNo) {
        return studentGroupItemDao.queryBuilder().where(StudentGroupItemDao.Properties.ItemCode.eq(itemCode),
                StudentGroupItemDao.Properties.SubitemCode.eq(subItemCode),
                StudentGroupItemDao.Properties.StudentCode.eq(studentCode),
                StudentGroupItemDao.Properties.ExamStatus.eq(examType),
                StudentGroupItemDao.Properties.ExamPlaceName.eq(examplaceName),
                StudentGroupItemDao.Properties.ScheduleNo.eq(scheduleNo)).unique();
    }

    public List<RoundResult> getStuRoundResult(String studentCode, String itemCode, String subItemCode, int examType, String examplaceName,String scheduleNo) {
        return roundResultDao.queryBuilder().where(RoundResultDao.Properties.StudentCode.eq(studentCode),
                RoundResultDao.Properties.ItemCode.eq(itemCode),
                RoundResultDao.Properties.SubitemCode.eq(subItemCode),
                RoundResultDao.Properties.ExamType.eq(examType),
                RoundResultDao.Properties.ExamPlaceName.eq(examplaceName),
                RoundResultDao.Properties.ScheduleNo.eq(scheduleNo)).list();
    }

    public List<Schedule> getAllSchedule() {
        return scheduleDao.loadAll();
    }

    public void insertExamPlace(ExamPlace place) {
        examPlaceDao.insertOrReplaceInTx(place);
    }

    public List<ExamPlace> getAllExamplace() {
        return examPlaceDao.loadAll();
    }

    public List<GroupInfo> getAllGroupInfo(String scheduleNo, String examplaceName, String itemCode, String subItemCode) {
        return groupInfoDao.queryBuilder().where(GroupInfoDao.Properties.ScheduleNo.eq(scheduleNo),
                GroupInfoDao.Properties.ExamPlaceName.eq(examplaceName),
                GroupInfoDao.Properties.ItemCode.eq(itemCode),
                GroupInfoDao.Properties.SubItemCode.eq(subItemCode)).list();
    }

    public List<StudentGroupItem> getMQTTBean(String itemCode, String subItemCode, String scheduleNo, String examplaceName) {
        return studentGroupItemDao.queryBuilder().where(StudentGroupItemDao.Properties.ItemCode.eq(itemCode),
                StudentGroupItemDao.Properties.SubitemCode.eq(subItemCode),
                StudentGroupItemDao.Properties.ScheduleNo.eq(scheduleNo),
                StudentGroupItemDao.Properties.ExamPlaceName.eq(examplaceName)).list();
    }

    public List<StudentGroupItem> getMQTTBean(String itemCode, String subItemCode, String scheduleNo, String examplaceName, GroupInfo groupInfo) {
        return studentGroupItemDao.queryBuilder().where(StudentGroupItemDao.Properties.ItemCode.eq(itemCode),
                StudentGroupItemDao.Properties.SubitemCode.eq(subItemCode),
                StudentGroupItemDao.Properties.ScheduleNo.eq(scheduleNo),
                StudentGroupItemDao.Properties.ExamPlaceName.eq(examplaceName),
                StudentGroupItemDao.Properties.GroupNo.eq(groupInfo.groupNo),
                StudentGroupItemDao.Properties.GroupType.eq(groupInfo.getGroupType())).list();
    }
    public List<StudentGroupItem> getMQTTBean(String stuCode,String itemCode, String subItemCode, String scheduleNo, String examplaceName, GroupInfo groupInfo) {
        return studentGroupItemDao.queryBuilder().where(StudentGroupItemDao.Properties.ItemCode.eq(itemCode),
                StudentGroupItemDao.Properties.SubitemCode.eq(subItemCode),
                StudentGroupItemDao.Properties.ScheduleNo.eq(scheduleNo),
                StudentGroupItemDao.Properties.ExamPlaceName.eq(examplaceName),
                StudentGroupItemDao.Properties.GroupNo.eq(groupInfo.groupNo),
                StudentGroupItemDao.Properties.GroupType.eq(groupInfo.getGroupType()),
                StudentGroupItemDao.Properties.StudentCode.eq(stuCode)).list();
    }
    public List<StudentGroupItem> getMQTTBean(String stuCode,String itemCode, String subItemCode, String scheduleNo, String examplaceName) {
        return studentGroupItemDao.queryBuilder().where(StudentGroupItemDao.Properties.ItemCode.eq(itemCode),
                StudentGroupItemDao.Properties.SubitemCode.eq(subItemCode),
                StudentGroupItemDao.Properties.ScheduleNo.eq(scheduleNo),
                StudentGroupItemDao.Properties.ExamPlaceName.eq(examplaceName),
                StudentGroupItemDao.Properties.StudentCode.eq(stuCode)).list();
    }

    public List<RoundResult> getStuRoundResult(String studentCode, String itemCode, String subitemCode, String scheduleNo, String examplaceName) {
        return roundResultDao.queryBuilder().where(RoundResultDao.Properties.StudentCode.eq(studentCode),
                RoundResultDao.Properties.ItemCode.eq(itemCode),
                RoundResultDao.Properties.SubitemCode.eq(subitemCode),
                RoundResultDao.Properties.ScheduleNo.eq(scheduleNo),
                RoundResultDao.Properties.ExamPlaceName.eq(examplaceName),
                RoundResultDao.Properties.IsLastResult.eq(1)).list();
    }

    public List<RoundResult> getStuRoundResult(String studentCode, String itemCode, String subitemCode, String scheduleNo, String examplaceName, GroupInfo groupInfo) {
        return roundResultDao.queryBuilder().where(RoundResultDao.Properties.StudentCode.eq(studentCode),
                RoundResultDao.Properties.ItemCode.eq(itemCode),
                RoundResultDao.Properties.SubitemCode.eq(subitemCode),
                RoundResultDao.Properties.ScheduleNo.eq(scheduleNo),
                RoundResultDao.Properties.ExamPlaceName.eq(examplaceName),
                RoundResultDao.Properties.IsLastResult.eq(1),
                RoundResultDao.Properties.GroundNo.eq(groupInfo.groupNo),
                RoundResultDao.Properties.Grouptype.eq(groupInfo.getGroupType())).list();
    }

    public List<RoundResult> getStuRoundResult(String studentCode,String itemCode, String subitemCode, String scheduleNo, String examPlaceName, int groupNo, int groupType) {
        return roundResultDao.queryBuilder().where(RoundResultDao.Properties.StudentCode.eq(studentCode),
                RoundResultDao.Properties.ItemCode.eq(itemCode),
                RoundResultDao.Properties.SubitemCode.eq(subitemCode),
                RoundResultDao.Properties.ScheduleNo.eq(scheduleNo),
                RoundResultDao.Properties.ExamPlaceName.eq(examPlaceName),
                RoundResultDao.Properties.IsLastResult.eq(1),
                RoundResultDao.Properties.GroundNo.eq(groupNo),
                RoundResultDao.Properties.Grouptype.eq(groupType)).list();
    }
}
