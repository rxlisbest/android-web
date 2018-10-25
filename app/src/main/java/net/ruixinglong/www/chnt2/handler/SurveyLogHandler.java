package net.ruixinglong.www.chnt2.handler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.yanzhenjie.andserver.RequestHandler;
import com.yanzhenjie.andserver.RequestMethod;
import com.yanzhenjie.andserver.annotation.RequestMapping;
import com.yanzhenjie.andserver.util.HttpRequestParser;

import net.ruixinglong.www.chnt2.helper.DBHelper;

import org.apache.httpcore.HttpException;
import org.apache.httpcore.HttpRequest;
import org.apache.httpcore.HttpResponse;
import org.apache.httpcore.entity.StringEntity;
import org.apache.httpcore.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

public class SurveyLogHandler implements RequestHandler {

    private Context context;

    public SurveyLogHandler(Context context) {
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @RequestMapping(method = {RequestMethod.POST})
    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
        Map<String, String> params = HttpRequestParser.parseParams(request);

        DBHelper dbHelper = new DBHelper(this.context);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String searchQuery = "SELECT * FROM survey_question";
        Cursor cursor = db.rawQuery(searchQuery, null);

        JSONArray resultSet = cursor2json(cursor);
//        if(true){
//            response.setStatusCode(400);
//            response.addHeader("content-type", "application/json; charset=utf-8");
//            StringEntity se = new StringEntity(resultSet.toString(), "utf-8");
//            se.setContentType("application/json; charset=utf-8");
//            response.setEntity(se);
//            return ;
//        }
        int do_count = 0;
        for (int i = 0; i < resultSet.length(); i++) {
            try {
                JSONObject job = resultSet.getJSONObject(i); // 遍历 jsonarray 数组，把每一个对象转成 json 对象
                searchQuery = "SELECT * FROM survey_question_option WHERE survey_question_id = "
                        + job.get("id");
                cursor = db.rawQuery(searchQuery, null);
                JSONArray children = cursor2json(cursor);
                if (params.containsKey("answer." + resultSet.getJSONObject(i).get
                        ("id"))) {
                    if (params.get("answer." + resultSet.getJSONObject(i).get
                            ("id")).length() != 0) {
                        do_count++;
                    }
                } else {
                    for (int ii = 0; ii < children.length(); ii++) {
                        if (params.containsKey("answer." + resultSet.getJSONObject(i).get
                                ("id") + "." + children.getJSONObject(ii).get
                                ("id"))) {
                            do_count++;
                            break;
                        }
                    }
                }

                job.put("children", children);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (resultSet.length() > do_count) {
            int undo_count = resultSet.length() - do_count;
            response.setStatusCode(400);
            StringEntity stringEntity = new StringEntity("There is " + undo_count + " question " +
                    "unanswered",
                    "utf-8");
            response.setEntity(stringEntity);
            return;
        }

        // 插入survey_log表
        db = dbHelper.getWritableDatabase();
        ContentValues survey_log = new ContentValues();
        survey_log.put("user_id", params.get("user_id"));
        long rowid = db.insert("survey_log", null, survey_log);//返回新添记录的行号，与主键id无关

        for (int i = 0; i < resultSet.length(); i++) {
            try {
                JSONObject job = resultSet.getJSONObject(i); // 遍历 jsonarray 数组，把每一个对象转成 json 对象
                searchQuery = "SELECT * FROM survey_question_option WHERE survey_question_id = "
                        + job.get("id");
                cursor = db.rawQuery(searchQuery, null);
                JSONArray children = cursor2json(cursor);

                JSONArray answer = new JSONArray();
                if (resultSet.getJSONObject(i).get
                        ("type").toString() == "3") {
                    if (params.get("answer." + resultSet.getJSONObject(i).get
                            ("id")).length() != 0) {
                        JSONObject answer_object = new JSONObject();
                        answer_object.put("id", "0");
                        answer_object.put("text", params.get("answer." + resultSet.getJSONObject(i).get
                                ("id")));
                        answer.put(answer_object);
                    }
                } else {
                    for (int ii = 0; ii < children.length(); ii++) {
                        if (params.containsKey("answer." + resultSet.getJSONObject(i).get
                                ("id") + "." + children.getJSONObject(ii).get
                                ("id"))) {
                            JSONObject answer_object = new JSONObject();
                            answer_object.put("id", children.getJSONObject(ii).get
                                    ("id").toString());
                            if(params.containsKey("answer_text." + resultSet.getJSONObject(i).get("id") + "." + children.getJSONObject(ii).get("id")) && params.get("answer_text." + resultSet.getJSONObject(i).get("id") + "." + children.getJSONObject(ii).get("id")).length() > 0){
                                 answer_object.put("text", params.get("answer_text." + resultSet.getJSONObject(i).get("id")).toString());
                            }
                            else{
                                 answer_object.put("text", "");
                            }
                            answer.put(answer_object);
                        }
                    }
                }


                ContentValues survey_question_log = new ContentValues();
                survey_question_log.put("survey_log_id", rowid + "");
                survey_question_log.put("survey_question_id", job.get("id").toString());
                survey_question_log.put("answer", answer.toString());
                long rowid2 = db.insert("survey_question_log", null, survey_question_log);
                //返回新添记录的行号，与主键id无关
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


//        searchQuery = "SELECT * FROM survey_question_log";
//        cursor = db.rawQuery(searchQuery, null);
//
//        resultSet = cursor2json(cursor);
//        if(true){
//            response.setStatusCode(400);
//            response.addHeader("content-type", "application/json; charset=utf-8");
//            StringEntity se = new StringEntity(resultSet.toString(), "utf-8");
//            se.setContentType("application/json; charset=utf-8");
//            response.setEntity(se);
//            return ;
//        }

        if (true) {
            JSONObject r = new JSONObject();
            try {
                r.put("msg", "success");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            response.setStatusCode(200);
            response.addHeader("content-type", "application/json; charset=utf-8");
            StringEntity se = new StringEntity(r.toString(), "utf-8");
            se.setContentType("application/json; charset=utf-8");
            response.setEntity(se);
            return;
        }
    }

    private org.json.JSONArray cursor2json(Cursor cursor) {
        org.json.JSONArray resultSet = new org.json.JSONArray();
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            int totalColumn = cursor.getColumnCount();
            JSONObject rowObject = new JSONObject();
            for (int i = 0; i < totalColumn; i++) {
                if (cursor.getColumnName(i) != null) {
                    try {
                        if (cursor.getString(i) != null) {
                            rowObject.put(cursor.getColumnName(i), cursor.getString(i));
                        } else {
                            rowObject.put(cursor.getColumnName(i), "");
                        }
                    } catch (Exception e) {
                        // Log.d("TAG_NAME", e.getMessage());
                    }
                }
            }
            resultSet.put(rowObject);
            cursor.moveToNext();
        }
        return resultSet;
    }
}