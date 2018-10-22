package net.ruixinglong.www.chnt2.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    private static final String name = "chnt.db";
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
                "card varchar(255))";
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
        sql = "insert into `survey_question` ( `id`, `type`, `content`, `sort`, `create_time`, `update_time`) values ( '8', '2', '8、Have you ever heard about CHINT Electric?', '0', '1528693761', '1528693761')";
        db.execSQL(sql);
        sql = "insert into `survey_question` ( `id`, `type`, `content`, `sort`, `create_time`, `update_time`) values ( '9', '3', '9、Do you have any other comments, questions, or concerns:', '0', '1528693761', '1528693761')";
        db.execSQL(sql);

        sql = "CREATE TABLE IF NOT EXISTS survey_question_option(" +
                "id integer not null primary key autoincrement," +
                "survey_question_id int(11)," +
                "type int(3)," +
                "content varchar(255)," +
                "sort int(11)," +
                "create_time BIGINT(20)," +
                "update_time BIGINT(20))";

        db.execSQL(sql);

        sql = "INSERT INTO `survey_question_option` VALUES ('1', '1', '1', 'Excellent', '0'," +
                " '1528693787', '1528693787'), ('2', '1', '1', 'Very good', '0', '1528693787', '1528693787'), ('3', '1', '1', 'Good', '0', '1528693787', '1528693787'), ('4', '1', '1', 'Fair', '0', '1528693787', '1528693787'), ('5', '1', '1', 'Poor', '0', '1528693787', '1528693787'), ('6', '2', '1', 'Extremely likely', '0', '1528693787', '1528693787'), ('7', '2', '1', 'Very Likely', '0', '1528693787', '1528693787'), ('8', '2', '1', 'Somewhat Likely', '0', '1528693787', '1528693787'), ('9', '2', '1', 'Not so likely', '0', '1528693787', '1528693787'), ('10', '2', '1', 'Not at all likely', '0', '1528693787', '1528693787'), ('11', '3', '1', 'Power Grid', '0', '1528693787', '1528693787'), ('12', '3', '1', 'Power Generation', '0', '1528693787', '1528693787'), ('13', '3', '1', 'Petrochemical', '0', '1528693787', '1528693787'), ('14', '3', '1', 'Steel', '0', '1528693787', '1528693787'), ('15', '3', '1', 'Coal & Mining', '0', '1528693787', '1528693787'), ('16', '3', '1', 'Metallurgy', '0', '1528693787', '1528693787'), ('17', '3', '1', 'Water Treatment', '0', '1528693787', '1528693787'), ('18', '3', '1', 'Residential construction', '0', '1528693787', '1528693787'), ('19', '3', '1', 'Industrial construction', '0', '1528693787', '1528693787'), ('20', '3', '1', 'Commercial real estate', '0', '1528693787', '1528693787'), ('21', '3', '1', 'PV', '0', '1528693787', '1528693787'), ('22', '3', '1', 'Unclear energy', '0', '1528693787', '1528693787'), ('23', '3', '1', 'Wind energy', '0', '1528693787', '1528693787'), ('24', '3', '1', 'Mechanical support', '0', '1528693787', '1528693787'), ('25', '3', '1', 'Telecom', '0', '1528693787', '1528693787'), ('26', '3', '1', 'Others', '0', '1528693787', '1528693787'), ('27', '4', '1', 'Wholesaler', '0', '1528693787', '1528693787'), ('28', '4', '1', 'Agent of (ABB, Schneider, Moeller, Siemens, Hager)', '0', '1528693787', '1528693787'), ('29', '4', '1', 'Component Manufacturer', '0', '1528693787', '1528693787'), ('30', '4', '1', 'Panel Builder', '0', '1528693787', '1528693787'), ('31', '4', '1', 'Project Contractor', '0', '1528693787', '1528693787'), ('32', '4', '1', 'Consultative institution & Engineer', '0', '1528693787', '1528693787'), ('33', '4', '1', 'EPC', '0', '1528693787', '1528693787'), ('34', '4', '1', 'System integrator', '0', '1528693787', '1528693787'), ('35', '4', '1', 'OEM Hunter', '0', '1528693787', '1528693787'), ('36', '5', '1', 'ACB', '0', '1528693787', '1528693787'), ('37', '5', '1', 'MCCB', '0', '1528693787', '1528693787'), ('38', '5', '1', 'MCB', '0', '1528693787', '1528693787'), ('39', '5', '1', 'RCBO', '0', '1528693787', '1528693787'), ('40', '5', '1', 'Surge Protective Device', '0', '1528693787', '1528693787'), ('41', '5', '1', 'Relay', '0', '1528693787', '1528693787'), ('42', '5', '1', 'Contactor', '0', '1528693787', '1528693787'), ('43', '5', '1', 'ATSE', '0', '1528693787', '1528693787'), ('44', '5', '1', 'Switchgear', '0', '1528693787', '1528693787'), ('45', '6', '1', 'This is my first purchase', '0', '1528693787', '1528693787'), ('46', '6', '1', 'Less than 6 months', '0', '1528693787', '1528693787'), ('47', '6', '1', '< 3years', '0', '1528693787', '1528693787'), ('48', '6', '1', '3 or more years', '0', '1528693787', '1528693787'), ('49', '6', '1', 'I haven’t made a purchase yet', '0', '1528693787', '1528693787'), ('50', '7', '1', 'Extremely likely', '0', '1528693787', '1528693787'), ('51', '7', '1', 'Very Likely', '0', '1528693787', '1528693787'), ('52', '7', '1', 'Somewhat Likely', '0', '1528693787', '1528693787'), ('53', '7', '1', 'Not so likely', '0', '1528693787', '1528693787'), ('54', '7', '1', 'Not at all likely', '0', '1528693787', '1528693787'), ('55', '8', '1', 'Yes, from', '0', '1528693787', '1528693787'), ('56', '8', '1', 'website', '0', '1528693787', '1528693787'), ('57', '8', '1', 'Media', '0', '1528693787', '1528693787'), ('58', '8', '1', 'Expo', '0', '1528693787', '1528693787'), ('59', '8', '1', 'Sales', '0', '1528693787', '1528693787'), ('60', '8', '1', 'Friend’s recommendation', '0', '1528693787', '1528693787'), ('61', '8', '2', 'Others', '0', '1528693787', '1528693787')";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // do nothing
    }
}
