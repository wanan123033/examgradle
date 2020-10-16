package com.fairplay.database;

import com.fairplay.database.entity.DaoMaster;
import com.fairplay.database.entity.DaoSession;
import com.fairplay.database.entity.Student;
import com.fairplay.database.entity.StudentDao;
import com.fairplay.database.entity.StudentItem;
import com.gwm.util.ContextUtil;

import org.greenrobot.greendao.database.Database;

import java.util.List;

public class DBManager {
    private static DBManager mInstance;
    private StudentDao studentDao;
    private static final String DB_NAME = "examgradle";
    private DBOpenHelper helper;
    private Database db;
    private DaoSession daoSession;
    private DaoMaster daoMaster;
    private static final String DB_PASSWORD = "fplwwj";

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
        return null;
    }

    public StudentItem queryStuItemByStuCode(String studentCode) {
        return null;
    }

    public void insertStudent(Student student) {
        studentDao.insertInTx(student);
    }

    public void moData(){
        for(int i = 0 ; i < 10 ; i++){
            Student student = new Student();
            student.setSex(0);
            student.setSchoolName("五华中学");
            student.setStudentCode("202010130000"+i);
            student.setStudentName("六六六"+i);
            student.setDownloadTime("1234567");
        }
    }
}
