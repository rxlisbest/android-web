package net.ruixinglong.www.chnt2.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    private static final String name = "chnt2.db";
    private static final int version = 1;

    public DBHelper(Context context) {
        super(context, name, null, version);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS user(" +
                "_id integer not null primary key autoincrement," +
                "name varchar(255)," +
                "telephone varchar(20)," +
                "email varchar(255)," +
                "create_time BIGINT(20)," +
                "update_time BIGINT(20)," +
                "card TEXT)";
        db.execSQL(sql);

        sql = "CREATE TABLE IF NOT EXISTS survey_question(" +
                "id integer not null primary key autoincrement," +
                "type int(3)," +
                "content varchar(255)," +
                "sort int(11)," +
                "create_time BIGINT(20)," +
                "update_time BIGINT(20))";
        db.execSQL(sql);
        Log.d("ERROR", sql);

        sql = "insert into `survey_question` ( `id`, `type`, `content`, `sort`, `create_time`, `update_time`) values ( '1', '2', '1、Overall, how would you rate this event？', '1', '1528693761', '1528693761')";
        db.execSQL(sql);
        sql = "insert into `survey_question` ( `id`, `type`, `content`, `sort`, `create_time`, `update_time`) values ( '2', '2', '2、How likely is it that you would recommend CHINT Electric to a friend or colleague?', '0', '1528693761', '1528693761')";
        db.execSQL(sql);
        sql = "insert into `survey_question` ( `id`, `type`, `content`, `sort`, `create_time`, `update_time`) values ( '3', '2', '3、Which industry are you in?', '0', '1528693761', '1528693761')";
        db.execSQL(sql);
        sql = "insert into `survey_question` ( `id`, `type`, `content`, `sort`, `create_time`, `update_time`) values ( '4', '2', '4、Are you?', '0', '1528693761', '1528693761')";
        db.execSQL(sql);
        sql = "insert into `survey_question` ( `id`, `type`, `content`, `sort`, `create_time`, `update_time`) values ( '5', '2', '5、Products you are interested in\\nComponent:', '0', '1528693761', '1528693761')";
        db.execSQL(sql);
        sql = "insert into `survey_question` ( `id`, `type`, `content`, `sort`, `create_time`, `update_time`) values ( '6', '2', '6、How long have you been a customer of our company?', '0', '1528693761', '1528693761')";
        db.execSQL(sql);
        sql = "insert into `survey_question` ( `id`, `type`, `content`, `sort`, `create_time`, `update_time`) values ( '7', '2', '7、How likely are you to replace your current product with CHINT’s product?', '0', '1528693761', '1528693761')";
        db.execSQL(sql);
        sql = "insert into `survey_question` ( `id`, `type`, `content`, `sort`, `create_time`, `update_time`) values ( '8', '1', '8、Have you ever heard about CHINT Electric?', '0', '1528693761', '1528693761')";
        db.execSQL(sql);
        sql = "insert into `survey_question` ( `id`, `type`, `content`, `sort`, `create_time`, `update_time`) values ( '9', '3', '9、Do you have any other comments, questions, or concerns:', '0', '1528693761', '1528693761')";
        db.execSQL(sql);

        sql = "CREATE TABLE IF NOT EXISTS survey_question_option(" +
                "id integer not null primary key autoincrement," +
                "parent_id int(11)," +
                "survey_question_id int(11)," +
                "type int(3)," +
                "content varchar(255)," +
                "sort int(11)," +
                "create_time BIGINT(20)," +
                "update_time BIGINT(20))";
        db.execSQL(sql);

        sql = "INSERT INTO `survey_question_option` VALUES ('1', '0', '1', '1', '5(Excellent)', '0', '1528693787', '1528693787'), ('2', '0', '1', '1', '4', '0', '1528693787', '1528693787'), ('3', '0', '1', '1', '3', '0', '1528693787', '1528693787'), ('4', '0', '1', '1', '2', '0', '1528693787', '1528693787'), ('5', '0', '1', '1', '1(Poor)', '0', '1528693787', '1528693787'), ('6', '0', '2', '1', '5(Extremely likely)', '0', '1528693787', '1528693787'), ('7', '0', '2', '1', '4', '0', '1528693787', '1528693787'), ('8', '0', '2', '1', '3', '0', '1528693787', '1528693787'), ('9', '0', '2', '1', '2', '0', '1528693787', '1528693787'), ('10', '0', '2', '1', '1(Not at all likely)', '0', '1528693787', '1528693787'), ('11', '0', '3', '1', '1. Power Grid', '0', '1528693787', '1528693787'), ('12', '0', '3', '1', '2. Power Generation', '0', '1528693787', '1528693787'), ('13', '0', '3', '1', '3. Petrochemical', '0', '1528693787', '1528693787'), ('14', '0', '3', '1', '4. Steel', '0', '1528693787', '1528693787'), ('15', '0', '3', '1', '5. Coal & Mining', '0', '1528693787', '1528693787'), ('16', '0', '3', '1', '6. Metallurgy', '0', '1528693787', '1528693787'), ('17', '0', '3', '1', '7. Water Treatment', '0', '1528693787', '1528693787'), ('18', '0', '3', '1', '8. Residential construction', '0', '1528693787', '1528693787'), ('19', '0', '3', '1', '9. Industrial construction', '0', '1528693787', '1528693787'), ('20', '0', '3', '1', '10. Commercial real estate', '0', '1528693787', '1528693787'), ('21', '0', '3', '1', '11. PV', '0', '1528693787', '1528693787'), ('22', '0', '3', '1', '12. Unclear energy', '0', '1528693787', '1528693787'), ('23', '0', '3', '1', '13. Wind energy', '0', '1528693787', '1528693787'), ('24', '0', '3', '1', '14. Mechanical support', '0', '1528693787', '1528693787'), ('25', '0', '3', '1', '15. Telecom', '0', '1528693787', '1528693787'), ('26', '0', '3', '2', '16. Others , please specify', '0', '1528693787', '1528693787'), ('27', '0', '4', '1', '1. Wholesaler', '0', '1528693787', '1528693787'), ('28', '0', '4', '1', '2. Agent of (ABB, Schneider, Moeller, Siemens, Hager,etc.)', '0', '1528693787', '1528693787'), ('29', '0', '4', '1', '3. Component Manufacturer', '0', '1528693787', '1528693787'), ('30', '0', '4', '1', '4. Panel Builder', '0', '1528693787', '1528693787'), ('31', '0', '4', '1', '5. Project Contractor', '0', '1528693787', '1528693787'), ('32', '0', '4', '1', '6. Consultative institution & Engineer', '0', '1528693787', '1528693787'), ('33', '0', '4', '1', '7. EPC', '0', '1528693787', '1528693787'), ('34', '0', '4', '1', '8. System integrator', '0', '1528693787', '1528693787'), ('35', '0', '4', '1', '9. OEM Hunter', '0', '1528693787', '1528693787'), ('36', '0', '5', '1', '1. ACB', '0', '1528693787', '1528693787'), ('37', '0', '5', '1', '2. MCCB', '0', '1528693787', '1528693787'), ('38', '0', '5', '1', '3. MCB', '0', '1528693787', '1528693787'), ('39', '0', '5', '1', '4. RCBO', '0', '1528693787', '1528693787'), ('40', '0', '5', '1', '5. Surge Protective Device', '0', '1528693787', '1528693787'), ('41', '0', '5', '1', '6. Relay', '0', '1528693787', '1528693787'), ('42', '0', '5', '1', '7. Contactor', '0', '1528693787', '1528693787'), ('43', '0', '5', '1', '8. ATSE', '0', '1528693787', '1528693787'), ('44', '0', '5', '1', '9. Switchgear', '0', '1528693787', '1528693787'), ('45', '0', '6', '1', '1. This is my first purchase', '0', '1528693787', '1528693787'), ('46', '0', '6', '1', '2. Less than 6 months', '0', '1528693787', '1528693787'), ('47', '0', '6', '1', '3. < 3years', '0', '1528693787', '1528693787'), ('48', '0', '6', '1', '4. 3 or more years', '0', '1528693787', '1528693787'), ('49', '0', '6', '1', '5. I haven’t made a purchase yet', '0', '1528693787', '1528693787'), ('50', '0', '7', '1', '5(Extremely likely)', '0', '1528693787', '1528693787'), ('51', '0', '7', '1', '4', '0', '1528693787', '1528693787'), ('52', '0', '7', '1', '3', '0', '1528693787', '1528693787'), ('53', '0', '7', '1', '2', '0', '1528693787', '1528693787'), ('54', '0', '7', '1', '1(Not at all likely)', '0', '1528693787', '1528693787'), ('55', '0', '8', '1', '1. No', '0', '1528693787', '1528693787'), ('56', '0', '8', '1', '2. Yes, from', '0', '1528693787', '1528693787'), ('57', '56', '0', '1', '2.1 website', '0', '1528693787', '1528693787'), ('58', '56', '0', '1', '2.2 Media', '0', '1528693787', '1528693787'), ('59', '56', '0', '1', '2.3 Expo', '0', '1528693787', '1528693787'), ('60', '56', '0', '1', '2.4 Sales', '0', '1528693787', '1528693787'), ('61', '56', '0', '1', '2.5 Friend’s recommendation', '0', '1528693787', '1528693787'), ('62', '56', '0', '2', '2.6 Others', '0', '1528693787', '1528693787'), ('63', '0', '4', '2', 'Others , please specify', '0', '1528693787', '1528693787'), ('64', '0', '5', '2', 'Others', '0', '1528693787', '1528693787');";
        db.execSQL(sql);

        sql = "CREATE TABLE IF NOT EXISTS employee(" +
                "id integer not null primary key autoincrement," +
                "name varchar(255)," +
                "event_name varchar(255)," +
                "create_time BIGINT(20)," +
                "update_time BIGINT(20))";
        db.execSQL(sql);

        sql = "CREATE TABLE IF NOT EXISTS employee_user(" +
                "id integer not null primary key autoincrement," +
                "employee_id int(11)," +
                "user_id int(11)," +
                "create_time BIGINT(20)," +
                "update_time BIGINT(20))";
        db.execSQL(sql);

        sql = "CREATE TABLE IF NOT EXISTS survey_log(" +
                "id integer not null primary key autoincrement," +
                "user_id int(11)," +
                "create_time BIGINT(20)," +
                "update_time BIGINT(20))";
        db.execSQL(sql);

        sql = "CREATE TABLE IF NOT EXISTS survey_question_log(" +
                "id integer not null primary key autoincrement," +
                "survey_log_id int(11)," +
                "survey_question_id int(11)," +
                "answer text(1000)," +
                "create_time BIGINT(20)," +
                "update_time BIGINT(20))";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // do nothing
    }
}
