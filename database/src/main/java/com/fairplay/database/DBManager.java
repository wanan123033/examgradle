package com.fairplay.database;

import android.database.Cursor;

import com.fairplay.database.entity.DaoMaster;
import com.fairplay.database.entity.DaoSession;
import com.fairplay.database.entity.GroupDao;
import com.fairplay.database.entity.Item;
import com.fairplay.database.entity.ItemDao;
import com.fairplay.database.entity.RoundResult;
import com.fairplay.database.entity.RoundResultDao;
import com.fairplay.database.entity.Student;
import com.fairplay.database.entity.StudentDao;
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
        moData();
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
        studentDao.insertInTx(student);
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

    public void insertRoundResult(RoundResult result) {
        roundResultDao.insertInTx(result);
    }

    public void getRoundResult(String studentCode, int round) {

    }

    public List<Student> getStudentByItemCode(String itemCode, int limit, int offset) {
        StringBuffer sqlBuf = new StringBuffer("SELECT S.* FROM " + StudentDao.TABLENAME + " S");
        sqlBuf.append(" WHERE S." + StudentDao.Properties.StudentCode.columnName + " IN ( ");
        sqlBuf.append(" SELECT  " + StudentItemDao.Properties.StudentCode.columnName);
        sqlBuf.append(" FROM " + StudentItemDao.TABLENAME);
        sqlBuf.append(" WHERE  " + StudentItemDao.Properties.ItemCode.columnName + " = ?)");
        if (limit != -1)
            sqlBuf.append(" limit " + offset + "," + limit);
        Cursor c = daoSession.getDatabase().rawQuery(sqlBuf.toString(), new String[]{itemCode});
        List<Student> students = new ArrayList<>();
        while (c.moveToNext()) {
            Student student = studentDao.readEntity(c, 0);
            students.add(student);
        }
        c.close();
        return students;
    }

    public void insertItem(Item item) {
        itemDao.insertOrReplaceInTx(item);
    }

    public void insertStudentItem(StudentItem studentItem) {
        studentItemDao.insert(studentItem);
    }

    public int getUnUploadNum() {
        return 0;
    }

    public void clear() {

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
}
