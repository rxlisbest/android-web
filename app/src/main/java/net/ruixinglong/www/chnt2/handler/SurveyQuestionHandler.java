package net.ruixinglong.www.chnt2.handler;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yanzhenjie.andserver.RequestHandler;
import com.yanzhenjie.andserver.RequestMethod;
import com.yanzhenjie.andserver.annotation.RequestMapping;

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

public class SurveyQuestionHandler implements RequestHandler {

    private Context context;

    public SurveyQuestionHandler(Context context) {
        this.context = context;
    }

    @RequestMapping(method = {RequestMethod.GET})
    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {

        DBHelper dbHelper = new DBHelper(this.context);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String searchQuery = "SELECT * FROM survey_question";
        Cursor cursor = db.rawQuery(searchQuery, null);

        JSONArray resultSet = cursor2json(cursor);

        for(int i=0;i<resultSet.length();i++){
            try {
                JSONObject job = resultSet.getJSONObject(i); // 遍历 jsonarray 数组，把每一个对象转成 json 对象
                searchQuery = "SELECT * FROM survey_question_option WHERE survey_question_id = "
                        + job.get("id");
                cursor = db.rawQuery(searchQuery, null);
                job.put("children", cursor2json(cursor));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        if (true) {
            response.setStatusCode(200);
            response.addHeader("content-type", "application/json; charset=utf-8");
            StringEntity se = new StringEntity(resultSet.toString(), "utf-8");
            se.setContentType("application/json; charset=utf-8");
            response.setEntity(se);
            return;
        }
    }

    private JSONArray cursor2json(Cursor cursor) {
        JSONArray resultSet = new JSONArray();
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