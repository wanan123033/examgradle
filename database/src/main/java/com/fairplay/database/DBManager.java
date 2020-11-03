package com.fairplay.database;

import android.database.Cursor;

import com.fairplay.database.entity.DaoMaster;
import com.fairplay.database.entity.DaoSession;
import com.fairplay.database.entity.GroupDao;
import com.fairplay.database.entity.Item;
import com.fairplay.database.entity.ItemDao;
import com.fairplay.database.entity.MultipleItem;
import com.fairplay.database.entity.MultipleItemDao;
import com.fairplay.database.entity.MultipleResult;
import com.fairplay.database.entity.MultipleResultDao;
import com.fairplay.database.entity.RoundResult;
import com.fairplay.database.entity.RoundResultDao;
import com.fairplay.database.entity.Schedule;
import com.fairplay.database.entity.ScheduleDao;
import com.fairplay.database.entity.Student;
import com.fairplay.database.entity.StudentDao;
import com.fairplay.database.entity.StudentGroupItem;
import com.fairplay.database.entity.StudentGroupItemDao;
import com.fairplay.database.entity.StudentItem;
import com.fairplay.database.entity.StudentItemDao;
import com.gwm.util.ContextUtil;

import org.greenrobot.greendao.database.Database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBManager {
    private static DBManager mInstance;
    private StudentDao studentDao;
    public static final String DB_NAME = "examgradle";
    private DBOpenHelper helper;
    private Database db;
    private DaoSession daoSession;
    private DaoMaster daoMaster;
    private static final String DB_PASSWORD = "fplwwj";
    private ItemDao itemDao;
    private StudentItemDao studentItemDao;
    private RoundResultDao roundResultDao;
    private MultipleResultDao multipleResultDao;
    private ScheduleDao scheduleDao;
    private MultipleItemDao multipleItemDao;
    private StudentGroupItemDao studentGroupItemDao;

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
        studentDao = daoSession.getStudentDao();
        itemDao = daoSession.getItemDao();
        roundResultDao = daoSession.getRoundResultDao();
        studentItemDao = daoSession.getStudentItemDao();
        multipleResultDao = daoSession.getMultipleResultDao();
        scheduleDao = daoSession.getScheduleDao();
        multipleItemDao = daoSession.getMultipleItemDao();
        studentGroupItemDao = daoSession.getStudentGroupItemDao();
    }

    public List<Student> queryStudentFeatures() {
        return studentDao.queryBuilder()
                .where(StudentDao.Properties.FaceFeature.isNotNull())
                .where(StudentDao.Properties.FaceFeature.notEq(""))
                .list();
    }

    public Student queryStudentByIDCode(String code) {
        return null;
    }

    public Student queryStudentByStuCode(String code) {
        return studentDao.queryBuilder().where(StudentDao.Properties.StudentCode.eq(code)).unique();
    }

    public StudentItem queryStuItemByStuCode(String studentCode) {
        return null;
    }

    public void insertStudent(Student student) {
        studentDao.insertOrReplaceInTx(student);
    }

    public void moData(){
//        for(int i = 0 ; i < 10 ; i++){
//            Student student = new Student();
//            student.setSex(0);
//            student.setSchoolName("五华中学");
//            student.setStudentCode("202010130000"+i);
//            student.setStudentName("六六六"+i);
//            student.setDownloadTime("1234567");
//        }
    }

    public Long insertRoundResult(RoundResult result) {
        return roundResultDao.insertOrReplace(result);
    }

    public List<Student> getStudentByItemCode(String itemCode,String subItemCode, int limit, int offset) {
        StringBuffer sqlBuf = new StringBuffer("SELECT S.* FROM " + StudentDao.TABLENAME + " S");
        sqlBuf.append(" WHERE S." + StudentDao.Properties.StudentCode.columnName + " IN ( ");
        sqlBuf.append(" SELECT  " + StudentItemDao.Properties.StudentCode.columnName);
        sqlBuf.append(" FROM " + StudentItemDao.TABLENAME);
        sqlBuf.append(" WHERE  " + StudentItemDao.Properties.ItemCode.columnName + " = ? ");
        sqlBuf.append(" AND  " + StudentItemDao.Properties.SubitemCode.columnName + " = ?) ");
        if (limit != -1)
            sqlBuf.append(" limit " + offset + "," + limit);
        Cursor c = daoSession.getDatabase().rawQuery(sqlBuf.toString(), new String[]{itemCode,subItemCode});
        List<Student> students = new ArrayList<>();
        while (c.moveToNext()) {
            Student student = studentDao.readEntity(c, 0);
            students.add(student);
        }
        c.close();
        return students;
    }

    public long insertItem(Item item) {
        return itemDao.insertOrReplace(item);
    }

    public void insertStudentItem(StudentItem studentItem) {
        studentItemDao.insert(studentItem);
    }

    public int getUnUploadNum() {
        return 0;
    }

    public void clear() {
        roundResultDao.deleteAll();
        studentItemDao.deleteAll();
        studentDao.deleteAll();
        scheduleDao.deleteAll();
        itemDao.deleteAll();
        multipleResultDao.deleteAll();
        multipleItemDao.deleteAll();
        studentGroupItemDao.deleteAll();
    }

    public Map<String,Object> getItemStudenCount(String itemCode){
        StringBuffer sqlBuf = new StringBuffer();
        sqlBuf.append("select count(*) as stu_count,");
        sqlBuf.append("count(case when s." + StudentDao.Properties.Sex.columnName + "=0 then " + StudentDao.Properties.Sex.columnName + " end) as man_count,");
        sqlBuf.append("count(case when s." + StudentDao.Properties.Sex.columnName + "=1 then " + StudentDao.Properties.Sex.columnName + " end) as woman_count");
        sqlBuf.append("  from " + StudentDao.TABLENAME + " s ");
        sqlBuf.append("where s."+StudentDao.Properties.StudentCode.columnName+" in (");
        sqlBuf.append("select "+StudentItemDao.Properties.StudentCode.columnName+" from "+StudentItemDao.TABLENAME+"  ");
        sqlBuf.append("where "+StudentItemDao.Properties.ItemCode.columnName+"= ? )");
        Cursor c = daoSession.getDatabase().rawQuery(sqlBuf.toString(), new String[]{itemCode});
        Map<String, Object> countMap = new HashMap<>();

        if (c.moveToNext()) {
            int count = c.getInt(0);
            int man_count = c.getInt(1);
            int women_count = c.getInt(2);

            countMap.put("count", count);
            countMap.put("man_count", man_count);
            countMap.put("women_count", women_count);
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
}
