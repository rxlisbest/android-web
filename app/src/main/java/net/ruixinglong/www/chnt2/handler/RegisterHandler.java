package net.ruixinglong.www.chnt2.handler;

import android.content.ContentValues;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Map;

public class RegisterHandler implements RequestHandler {

    private Context context;

    public RegisterHandler(Context context) {
        this.context = context;
    }

    @RequestMapping(method = {RequestMethod.POST})
    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
        Map<String, String> params = HttpRequestParser.parseParams(request);

//        if (!params.containsKey("card_base64") || params.get("card_base64").length() == 0) {
//            StringEntity stringEntity = new StringEntity("The Card cannot be empty", "utf-8");
//
//            response.setStatusCode(400);
//            response.setEntity(stringEntity);
//            return;
//        }
        if (!params.containsKey("name") || params.get("name").length() == 0) {
            StringEntity stringEntity = new StringEntity("The Name cannot be empty", "utf-8");

            response.setStatusCode(400);
            response.setEntity(stringEntity);
            return;
        }
        if (!params.containsKey("telephone") || params.get("telephone").length() == 0) {
            StringEntity stringEntity = new StringEntity("The Phone cannot be empty", "utf-8");

            response.setStatusCode(400);
            response.setEntity(stringEntity);
            return;
        }
        if (!params.containsKey("email") || params.get("email").length() == 0) {
            StringEntity stringEntity = new StringEntity("The E-mail cannot be empty", "utf-8");

            response.setStatusCode(400);
            response.setEntity(stringEntity);
            return;
        }
        if (!params.containsKey("company") || params.get("company").length() == 0) {
            StringEntity stringEntity = new StringEntity("The Company cannot be empty", "utf-8");

            response.setStatusCode(400);
            response.setEntity(stringEntity);
            return;
        }
        if (!params.containsKey("position") || params.get("position").length() == 0) {
            StringEntity stringEntity = new StringEntity("The Postion cannot be empty", "utf-8");

            response.setStatusCode(400);
            response.setEntity(stringEntity);
            return;
        }

        DBHelper dbHelper = new DBHelper(this.context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues user = new ContentValues();
        user.put("name", params.get("name"));
        user.put("telephone", params.get("telephone"));
        user.put("email", params.get("email"));
        user.put("company", params.get("company"));
        user.put("position", params.get("position"));
        long rowid1 = db.insert("user", null, user);//返回新添记录的行号，与主键id无关


        ContentValues employee_user = new ContentValues();
        employee_user.put("user_id", rowid1);
        employee_user.put("employee_id", params.get("employee_id"));
//        employee_user.put("card", params.get("card_base64"));
        long rowid2 = db.insert("employee_user", null, employee_user);//返回新添记录的行号，与主键id无关

        if (rowid1 > 0 && rowid2 > 0) {
            JSONObject resultSet = new JSONObject();
            try {
                resultSet.put("user_id", rowid1);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            response.setStatusCode(200);
            response.addHeader("content-type", "application/json; charset=utf-8");
            StringEntity se = new StringEntity(resultSet.toString(), "utf-8");
            se.setContentType("application/json; charset=utf-8");
            response.setEntity(se);
        } else {
            response.setStatusCode(400);
            StringEntity se = new StringEntity("insert error", "utf-8");
            se.setContentType("application/json; charset=utf-8");
            response.setEntity(se);
        }
        return;
    }
}