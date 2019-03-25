package net.ruixinglong.www.chnt2.handler;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

public class RemarkHandler implements RequestHandler {

    private Context context;

    public RemarkHandler(Context context) {
        this.context = context;
    }

    @RequestMapping(method = {RequestMethod.POST})
    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
        Map<String, String> params = HttpRequestParser.parseParams(request);

        if (!params.containsKey("content") || params.get("content").length() == 0) {
            StringEntity stringEntity = new StringEntity("The content cannot be empty", "utf-8");

            response.setStatusCode(400);
            response.setEntity(stringEntity);
            return;
        }

        DBHelper dbHelper = new DBHelper(this.context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues remark = new ContentValues();
        remark.put("content", params.get("content"));
        long rowid = db.insert("survey_remark", null, remark);//返回新添记录的行号，与主键id无关
        Log.e("Roy", rowid + "");

        if (rowid > 0) {
            JSONObject resultSet = new JSONObject();

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