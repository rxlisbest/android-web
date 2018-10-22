package net.ruixinglong.www.chnt2.handler;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Map;

public class LoginHandler implements RequestHandler {

    private Context context;

    public LoginHandler(Context context) {
        this.context = context;
    }

    @RequestMapping(method = {RequestMethod.POST})
    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
        Map<String, String> params = HttpRequestParser.parseParams(request);

        if (!params.containsKey("name")) {
            StringEntity stringEntity = new StringEntity("The content cannot be empty", "utf-8");

            response.setStatusCode(400);
            response.setEntity(stringEntity);
            return;
        }
        if (!params.containsKey("event_name")) {
            StringEntity stringEntity = new StringEntity("The content cannot be empty", "utf-8");

            response.setStatusCode(400);
            response.setEntity(stringEntity);
            return;
        }

        DBHelper dbHelper = new DBHelper(this.context);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String searchQuery = "SELECT * FROM survey_question_option";
        Cursor cursor = db.rawQuery(searchQuery, null);

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

        // ContentValues values = new ContentValues();
        // values.put("name", "炸死特");
        // values.put("email", 4);
        // long rowid = db.insert("user", null, values);//返回新添记录的行号，与主键id无关
        // String rowid_string = String.valueOf(rowid);

        if (true) {
            String name = URLDecoder.decode(params.get("name"), "utf-8");
            response.setStatusCode(400);
            response.addHeader("content-type", "application/json; charset=utf-8");
            StringEntity se = new StringEntity(resultSet.toString(), "utf-8");
            se.setContentType("application/json; charset=utf-8");
            response.setEntity(se);
            return;
        }

        // JSONArray resultSet = new JSONArray();

        String userName = URLDecoder.decode(params.get("username"), "utf-8");
        String password = URLDecoder.decode(params.get("password"), "utf-8");

        if ("123".equals(userName) && "123".equals(password)) {
            StringEntity stringEntity = new StringEntity("Login Succeed", "utf-8");

            response.setStatusCode(200);
            response.setEntity(stringEntity);
        } else {
            StringEntity stringEntity = new StringEntity("Login Failed", "utf-8");

            response.setStatusCode(400);
            response.setEntity(stringEntity);
        }
    }
}