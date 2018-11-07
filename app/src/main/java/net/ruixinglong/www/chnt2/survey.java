package net.ruixinglong.www.chnt2;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.yanzhenjie.andserver.AndServer;
import com.yanzhenjie.andserver.Server;
import com.yanzhenjie.andserver.filter.HttpCacheFilter;
import com.yanzhenjie.andserver.website.AssetsWebsite;

import net.ruixinglong.www.chnt2.bean.SurveyLogBean;
import net.ruixinglong.www.chnt2.handler.LoginHandler;
import net.ruixinglong.www.chnt2.handler.RegisterHandler;
import net.ruixinglong.www.chnt2.handler.SurveyLogHandler;
import net.ruixinglong.www.chnt2.handler.SurveyQuestionHandler;
import net.ruixinglong.www.chnt2.helper.DBHelper;
import net.ruixinglong.www.chnt2.util.ExcelUtils;
import net.ruixinglong.www.chnt2.util.NetUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class survey extends AppCompatActivity {
    private WebView webView;

    private ValueCallback<Uri[]> mUploadCallbackAboveL;
    private ValueCallback<Uri> mUploadCallbackBelow;
    private Uri imageUri;
    private int REQUEST_CODE = 1234;

    DBHelper dbHelper = new DBHelper(this);

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setTitle("INVESTIGATION");
        getWindow().setNavigationBarColor(Color.parseColor("#000000"));
        if (getSupportActionBar() != null) {
            // getSupportActionBar().hide();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        webView = (WebView) findViewById(R.id.webview);
        webView.loadUrl("http://" + NetUtils.getLocalIPAddress() + ":8080/login.html");
        webView = (WebView) findViewById(R.id.webview);

        WebSettings wSet = webView.getSettings();
        //从ListView中获得URL
        String url = getIntent().getStringExtra("content_url");
        //加载URL
        webView.loadUrl(url);
        //开启JavaScript的功能
        wSet.setJavaScriptEnabled(true);
        // 开启本地存储
        wSet.setDomStorageEnabled(true);
        //开启缓存，无网络时加载本地内容
        wSet.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //关闭打开第三方浏览器
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return true;
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    // progressBar.setVisibility(View.GONE);//加载完网页进度条消失
                } else {
                    // progressBar.setProgress(newProgress);//设置进度值
                    // progressBar.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                }
            }

            /**
             * 8(Android 2.2) <= API <= 10(Android 2.3)回调此方法
             */
            private void openFileChooser(ValueCallback<Uri> uploadMsg) {
                Log.e("WangJ", "运行方法 openFileChooser-1");
                // (2)该方法回调时说明版本API < 21，此时将结果赋值给 mUploadCallbackBelow，使之 != null
                mUploadCallbackBelow = uploadMsg;
                takePhoto();
            }

            /**
             * 11(Android 3.0) <= API <= 15(Android 4.0.3)回调此方法
             */
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                Log.e("WangJ", "运行方法 openFileChooser-2 (acceptType: " + acceptType + ")");
                // 这里我们就不区分input的参数了，直接用拍照
                openFileChooser(uploadMsg);
            }

            /**
             * 16(Android 4.1.2) <= API <= 20(Android 4.4W.2)回调此方法
             */
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                Log.e("WangJ", "运行方法 openFileChooser-3 (acceptType: " + acceptType + "; capture: " + capture + ")");
                // 这里我们就不区分input的参数了，直接用拍照
                openFileChooser(uploadMsg);
            }

            /**
             * API >= 21(Android 5.0.1)回调此方法
             */
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> valueCallback, FileChooserParams fileChooserParams) {
                Log.e("WangJ", "运行方法 onShowFileChooser");
                // (1)该方法回调时说明版本API >= 21，此时将结果赋值给 mUploadCallbackAboveL，使之 != null
                mUploadCallbackAboveL = valueCallback;
                takePhoto();
                return true;
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("WangJ", data.toString());
        if (requestCode == REQUEST_CODE) {
            // 经过上边(1)、(2)两个赋值操作，此处即可根据其值是否为空来决定采用哪种处理方法
            if (mUploadCallbackBelow != null) {
                chooseBelow(resultCode, data);
            } else if (mUploadCallbackAboveL != null) {
                chooseAbove(resultCode, data);
            } else {
                Toast.makeText(this, "发生错误", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 调用相机
     */
    private void takePhoto() {
        // 指定拍照存储位置的方式调起相机
        String filePath = Environment.getExternalStorageDirectory() + File.separator
                + Environment.DIRECTORY_PICTURES + File.separator;
        String fileName = "IMG_" + DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
        imageUri = Uri.fromFile(new File(filePath + fileName));
        Log.d("WangJ", fileName);

//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//        startActivityForResult(intent, REQUEST_CODE);


        // 选择图片（不包括相机拍照）,则不用成功后发刷新图库的广播
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        startActivityForResult(Intent.createChooser(i, "Image Chooser"), REQUEST_CODE);

//        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//
//        Intent Photo = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//
//        Intent chooserIntent = Intent.createChooser(Photo, "Image Chooser");
//        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Parcelable[]{captureIntent});
//
//        startActivityForResult(chooserIntent, REQUEST_CODE);
    }


    /**
     * Android API < 21(Android 5.0)版本的回调处理
     *
     * @param resultCode 选取文件或拍照的返回码
     * @param data       选取文件或拍照的返回结果
     */
    private void chooseBelow(int resultCode, Intent data) {
        Log.e("WangJ", "返回调用方法--chooseBelow");

        if (RESULT_OK == resultCode) {
            updatePhotos();

            if (data != null) {
                // 这里是针对文件路径处理
                Uri uri = data.getData();
                if (uri != null) {
                    Log.e("WangJ", "系统返回URI：" + uri.toString());
                    mUploadCallbackBelow.onReceiveValue(uri);
                } else {
                    mUploadCallbackBelow.onReceiveValue(null);
                }
            } else {
                // 以指定图像存储路径的方式调起相机，成功后返回data为空
                Log.e("WangJ", "自定义结果：" + imageUri.toString());
                mUploadCallbackBelow.onReceiveValue(imageUri);
            }
        } else {
            mUploadCallbackBelow.onReceiveValue(null);
        }
        mUploadCallbackBelow = null;
    }

    /**
     * Android API >= 21(Android 5.0) 版本的回调处理
     *
     * @param resultCode 选取文件或拍照的返回码
     * @param data       选取文件或拍照的返回结果
     */
    private void chooseAbove(int resultCode, Intent data) {
        Log.e("WangJ", "返回调用方法--chooseAbove");

        if (RESULT_OK == resultCode) {
            updatePhotos();

            if (data != null) {
                // 这里是针对从文件中选图片的处理
                Uri[] results;
                Uri uriData = data.getData();
                if (uriData != null) {
                    results = new Uri[]{uriData};
                    for (Uri uri : results) {
                        Log.e("WangJ", "系统返回URI：" + uri.toString());
                    }
                    mUploadCallbackAboveL.onReceiveValue(results);
                } else {
                    Log.e("WangJ", "系统返回URI：没有");
//                    Uri contentUri1 = getImageContentUri(this, new File(imageUri.toString().replace
//                            ("file://", "")));
                    String test = "content://com.google.android.apps.photos.contentprovider/-1/1/content%3A%2F%2Fmedia%2Fexternal%2Fimages%2Fmedia%2F45/ORIGINAL/NONE/11787233";

                    Log.e("WangJ", test);
                    Uri test2 = Uri.parse(test);
                    Log.e("WangJ", test2.toString());
                    mUploadCallbackAboveL.onReceiveValue(new Uri[]{test2});
                }
            } else {
                Log.e("WangJ", "自定义结果：" + imageUri.toString());
                mUploadCallbackAboveL.onReceiveValue(new Uri[]{imageUri});
            }
        } else {
            mUploadCallbackAboveL.onReceiveValue(null);
        }
        mUploadCallbackAboveL = null;
    }

    private void updatePhotos() {
        Log.e("WangJ", imageUri.toString());
        // 该广播即使多发（即选取照片成功时也发送）也没有关系，只是唤醒系统刷新媒体文件
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(imageUri);
        sendBroadcast(intent);
    }

    /**
     * 创建菜单
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.index, menu); //通过getMenuInflater()
        // 方法得到MenuInflater对象，再调用它的inflate()方法就可以给当前活动创建菜单了，第一个参数：用于指定我们通过哪一个资源文件来创建菜单；第二个参数：用于指定我们的菜单项将添加到哪一个Menu对象当中。
        return true; // true：允许创建的菜单显示出来，false：创建的菜单将无法显示。
    }

    /**
     * 菜单的点击事件
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.id_export_item:
                Toast.makeText(this, "正在导出EXECL数据", Toast.LENGTH_SHORT).show();
                // 查看数据
                SQLiteDatabase db = dbHelper.getReadableDatabase();

                String searchQuery = "SELECT * FROM survey_log";
                Cursor cursor = db.rawQuery(searchQuery, null);
                JSONArray survey_log = cursor2json(cursor);

                List<List> list = new ArrayList<List>();

                for (int i = 0; i < survey_log.length(); i++) {
                    try {
                        JSONObject job = survey_log.getJSONObject(i); // 遍历 jsonarray 数组，把每一个对象转成 json 对象
                        searchQuery = "SELECT * FROM user WHERE _id = " + job.get("user_id").toString();
                        // Log.d("RoyExcel", searchQuery);
                        cursor = db.rawQuery(searchQuery, null);
                        JSONArray user = cursor2json(cursor);
                        if (user.length() > 0) {
                            List<String> row = new ArrayList<String>();
                            row.add(user.getJSONObject(0).get("name").toString());
                            row.add(user.getJSONObject(0).get("telephone").toString());
                            row.add(user.getJSONObject(0).get("email").toString());

                            searchQuery = "SELECT * FROM survey_question_log WHERE survey_log_id " +
                                    "= " + job.get("id") + " ORDER BY survey_question_id ASC";
                            // Log.d("RoyExcel", searchQuery);
                            cursor = db.rawQuery(searchQuery, null);
                            JSONArray survey_question_log = cursor2json(cursor);

                            for (int ii = 0; ii < survey_question_log.length(); ii++) {
                                JSONObject job1 = survey_question_log.getJSONObject(ii); // 遍历 jsonarray
                                List<String> option_text = new ArrayList<String>();
                                JSONArray answer = new JSONArray(job1.get("answer")
                                        .toString());
                                // 选择题
                                // 数组，把每一个对象转成 json 对象

                                for (int iii = 0; iii < answer.length(); iii++) {
                                    JSONObject job2 = answer.getJSONObject(iii);

                                    // 简答题
                                    if (job1.get("survey_question_id").toString().equals("9")) {
                                        Log.d("RoyJ", job2.get("text").toString());
                                        option_text.add(job2.get("text").toString());
                                    } else {
                                        searchQuery = "SELECT * FROM survey_question_option WHERE " +
                                                "id " +
                                                "= " + job2.get("id");
                                        // Log.d("RoyExcel", searchQuery);
                                        cursor = db.rawQuery(searchQuery, null);
                                        JSONArray option = cursor2json(cursor);

                                        if (option.length() > 0) {
                                            option_text.add(option.getJSONObject(0).get("content")
                                                    .toString() + " " + job2.get("text"));
                                        }
                                    }
                                }
                                String a = String.join(";", option_text);

                                row.add(a);
                            }

                            list.add(row);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                File tempPath = Environment.getExternalStorageDirectory();
                //文件夹是否已经存在
                if (!tempPath.exists()) {
                    tempPath.mkdirs();
                }
                String fileName = tempPath + "/Download/customers_" + DateFormat.format
                        ("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".xls";

                searchQuery = "SELECT * FROM survey_question ORDER BY id ASC";
                cursor = db.rawQuery(searchQuery, null);
                JSONArray survey_question = cursor2json(cursor);
                String[] title = {"姓名", "电话", "邮箱", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
                for (int i = 0; i < survey_question.length(); i++) {
                    JSONObject j = null;
                    try {
                        j = survey_question.getJSONObject(i);
                        title[i + 3] = j.get("content").toString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                Log.d("RoyExcel", survey_question.toString());
                Log.d("Roy", fileName);
                ExcelUtils.initExcel(fileName, title);
                ExcelUtils.writeObjListToExcel(list, fileName, this);
                Toast.makeText(this, "导出成功", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }

        return true;
    }


    private Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();

        Log.e("WangJJ", "1");
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);
        Log.e("WangJJ", "2");
        if (cursor != null && cursor.moveToFirst()) {
            Log.e("WangJJ", "3");
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));

            Log.e("WangJJ", "4");
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            Log.e("WangJJ", "5");
            if (imageFile.exists()) {
                Log.e("WangJJ", "6");
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                Log.e("WangJJ", "7");
                return null;
            }
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
