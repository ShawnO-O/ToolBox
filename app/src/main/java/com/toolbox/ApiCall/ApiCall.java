package com.toolbox.ApiCall;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 30033 on 2017/12/19.
 */
public class ApiCall {
    final String TAG = "ApiCall";
    private Activity Activity;
    private ApiCallBackNew apiCallBackNew;
    private String api_name;
    private String api_type;
    private HashMap<String, String> params;
    private boolean isGetting = false;
    //這邊應該只有推播追蹤會把它設為false
    private boolean needshowmsg = true;
    private String web_url;
    //最基本款的通用
    private void init(android.app.Activity Activity, HashMap<String, String> params, String api_name, String api_type, int delayTime){
        this.Activity = Activity;
        this.api_name = api_name;
        this.api_type = api_type;
        this.params = params;
        for (String key :params.keySet()){
            Log.d(TAG,"api_name:"+api_name+"-"+key+":"+params.get(key));
            Log.d("api_case","api_name:"+api_name+"-"+key+":"+params.get(key));
        }
        initApi();
    }

    //有call back
    public ApiCall(android.app.Activity Activity, HashMap<String, String> params, String api_name, String api_type, ApiCallBackNew apiCallBackNew){
        Log.d("shawn2232","new_url:"+ web_url);
        Log.d(TAG,"api_type:"+api_type);
        Log.d("api_case","api_type:"+api_type);
        this.apiCallBackNew = apiCallBackNew;
        init(Activity,params,api_name,api_type,0);
    }
    //有call back
    public ApiCall(android.app.Activity Activity, HashMap<String, String> params, String api_name, String api_type, ApiCallBackNew apiCallBackNew, boolean needshowmsg){
        this.needshowmsg = needshowmsg;
        Log.d("shawn2232","new_url:"+ web_url);
        Log.d(TAG,"api_type:"+api_type);
        Log.d("api_case","api_type:"+api_type);
        this.apiCallBackNew = apiCallBackNew;
        init(Activity,params,api_name,api_type,0);
    }
    //射後不理型
    public ApiCall(android.app.Activity Activity, HashMap<String, String> params, String api_name, String api_type){
        Log.d(TAG,"api_type:"+api_type);
        Log.d("api_case","api_type:"+api_type);
        init(Activity,params,api_name,api_type,0);
    }
    //延遲型
    public ApiCall(android.app.Activity Activity, HashMap<String, String> params, String api_name, String api_type, ApiCallBackNew apiCallBackNew, int delayTime){
        Log.d(TAG,"api_type:"+api_type);
        Log.d("api_case","api_type:"+api_type);
        init(Activity,params,api_name,api_type,delayTime);
    }
    //我忘記了...
    public ApiCall(Activity Activity, String api_name, String api_type, HashMap<String, String> params, ApiCallBackNew apiCallBackNew, Boolean needBack){
        Log.d(TAG,"api_type:"+api_type);
        Log.d("api_case","api_type:"+api_type);
        init(Activity,params,api_name,api_type,0);
    }

    private void initApi(){
        isGetting = true;
        doGetApi();
    }

    private void doGetApi(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                getOkHttp(params, api_name, api_type);
            }
        }.start();
    }
    private JSONObject jsonObjectTmp = null;

    /***ok_http 2017.5.23 原新增*****/
    public void getOkHttp(HashMap<String, String> paramsMap, String api_name, String type) {
        Log.d("shawn2232","new_url:"+ web_url);
        Log.d("shawn2232","new_url:");
        this.api_name = api_name;
        String response_tmp;
        Response response;
        //from:android
        paramsMap.put("from", "1");

        /**okHttp連線設定**/
        OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        Call call = null;
        if (type.equals("get")) {
            call = mOkHttpClient.newCall(GetRequest(paramsMap, "api/"+api_name));
        } else if (type.equals("post")) {
            call = mOkHttpClient.newCall(PostRequest(paramsMap, "api/"+api_name));
        } else if (type.equals("delete")) {
            call = mOkHttpClient.newCall(deleteRequest(paramsMap, "api/"+api_name));
        } else if (type.equals("patch")){
            call = mOkHttpClient.newCall(patchRequest(paramsMap, "api/"+api_name));
        }

        try {
            response = call.execute();
            response_tmp = response.body().string();
            Log.d(TAG,"124 response_tmp:"+response_tmp);
            jsonObjectTmp = new JSONObject(response_tmp);
            Log.d(TAG,"124 jsonObjectTmp:"+jsonObjectTmp);
            if (response.isSuccessful() ) {
                handlerResult.sendEmptyMessage(jsonObjectTmp.optInt("status_code"));
            }else{
                handlerResult.sendEmptyMessage(99);
            }
//            if (response.isSuccessful() ) {
//                Log.d("shawn9998","api:"+api_name+":code:"+response.code() +":module:"+ paramsMap.get("module")+":action:"+ paramsMap.get("action"));
//                response_tmp = response.body().string();
//                Log.d("shawn3333", "response_tmp:" + response_tmp);
//                if (response_tmp.contains("系統維護") || response_tmp.equals("")) {
//                    //系統正在維護中~ 這邊有抓到確定是在維護中沒錯
////					test_str = "系統正在維護中，請稍後在嘗試";
////					mHandler.sendEmptyMessage(1);
////					if(!isFixing && getCont().toString().contains("Index")) {
////						isFixing = true;
////						Intent intent = new Intent();
////						intent.setClass(Activity.this, Maintenance.class);
////						startActivity(intent);
////					}
//                } else {
//                    jsonObjectTmp = new JSONObject(response_tmp);
//                    apiCallBackNew.onSucess(jsonObjectTmp);
//                    Log.d("shawn9998","status_code:"+jsonObjectTmp.optInt("status_code")+":module:"+paramsMap.get("module"));
//                    if (jsonObjectTmp.optInt("status_code")>=400 && jsonObjectTmp.optInt("status_code")<500){
//                        //程式錯誤
//                        jsonError = jsonObjectTmp;
//                        handlerError.sendEmptyMessage(0);
//
//                    }
//                }
//            } else {
//                handlerResult.sendEmptyMessage(response.code());
            //也許這邊可以直接重連
//				handlerCheckWeb.sendEmptyMessage(0);
//            }
        } catch (Exception e) {
            jsonObjectTmp = null;
            handlerResult.sendEmptyMessage(99);
            Log.d("shawn9997","e:"+e);
        }
//        if (Activity.progressDialog != null) {
//            Activity.closeLoading();
//        }
    }

    private Handler handlerResult = new Handler() {
        public void handleMessage(Message msg) {
            Log.d(TAG,"msg:"+msg.what);

            if(jsonObjectTmp == null ){
                showRetryDialog();
            }else if(msg.what == 99){
                //沒有東西? 是否要重連.....X
                if(jsonObjectTmp.optInt("status_code") == 400){
                    Log.d(TAG,"179");
                    if (!jsonObjectTmp.isNull("message") && needshowmsg) {
//                        Activity.showToast(jsonObjectTmp.optString("message"));
                    }
                }
            }else if(msg.what>=200 && msg.what<300){
                Log.d(TAG,"177:"+jsonObjectTmp.optInt("status_code"));
                if(jsonObjectTmp.optInt("status_code") == 400){
                    Log.d(TAG,"179");
                    if (!jsonObjectTmp.isNull("message")&& needshowmsg) {
//                        Activity.showToast(jsonObjectTmp.optString("message"));
                    }
                }else if(apiCallBackNew!=null) {
                    Log.d(TAG,"184");
                    apiCallBackNew.onSucess(jsonObjectTmp);
                }
            }else if(msg.what>301 && msg.what<500){
                //這邊應該要有錯誤訊息 - 50
                //如果有messages 就不用管他
                Log.d(TAG,"190:"+jsonObjectTmp.optInt("status_code"));

                if (!jsonObjectTmp.isNull("messages")) {

                }else if (!jsonObjectTmp.isNull("message")) {
//                    if(needshowmsg)
//                        Activity.showToast(jsonObjectTmp.optString("message"));
                } else {
                    showErrorDialog();
                }
                if(apiCallBackNew!=null)
                    apiCallBackNew.onError(jsonObjectTmp);
            }else{
                //還會有什麼狀況......?
            }
        }
    };

    private void showRetryDialog(){
//        DialogUtils.showNoNetworkDialog(Activity, new DialogUtils.DialogListener() {
//            @Override
//            public void ok() {
//                initApi();
//            }
//
//            @Override
//            public void cancel() {
//                //如果不做任何事情 就會直接關閉 對話方塊
////                finish();
////                pageChange(Flag.SCROLL_RIGHT);
//            }
//        });
    }

    private void showErrorDialog(){
        if(jsonObjectTmp.optString("message").contains("填寫履歷基本資料") || jsonObjectTmp.optString("message").contains("履歷資料") ){
//            DialogUtils.showNeedEditResume(Activity,new DialogUtils.DialogListener(){
//                @Override
//                public void cancel() {
//                    //如果不做任何事情 就會直接關閉 對話方塊
//                    //因為所在地不同...一個dialog,一個是頁面
//                    if( jsonObjectTmp.optString("message").contains("履歷資料")){
//
//                    }else {
//                        Activity.finish();
//                        Activity.pageChange(Flag.SCROLL_RIGHT);
//                    }
//                }
//                @Override
//                public void ok() {
//                    Intent intent = new Intent();
//                    intent.setClass(Activity, ProFileNew.class);
//                    Activity.startActivity(intent);
//                    if( jsonObjectTmp.optString("message").contains("履歷資料")){
//
//                    }else {
//                        Activity.finish();
//                    }
//                    Activity.pageChange(Flag.SCROLL_LEFT);
//
//                }
//            });
//        }else{
//            if(needshowmsg)
//                Activity.showToast(jsonObjectTmp.optString("message"));
//
        }
    }



    private Request deleteRequest(HashMap<String, String> paramsMap, String api_name) {
        return new Request.Builder().url(web_url + api_name).delete(initBuilder(paramsMap).build())
                .header("Content-Type", "text/html; charset=utf-8")
                .build();
    }
    private Request patchRequest(HashMap<String, String> paramsMap, String api_name) {
        return new Request.Builder().url(web_url + api_name).patch(initBuilder(paramsMap).build())
                .header("Content-Type", "text/html; charset=utf-8")
                .build();
    }
    private Request PostRequest(HashMap<String, String> paramsMap, String api_name) {
        Log.d("getUrl","getUrl:"+web_url + api_name);
        return new Request.Builder().url(web_url + api_name).post(initBuilder(paramsMap).build())
                .header("Content-Type", "text/html; charset=utf-8")
                .build();
    }
    private Request GetRequest(HashMap<String, String> paramsMap, String api_name) {
        int pos = 0;
        String getUrl = web_url + api_name + "?";
        for (String key : paramsMap.keySet()) {
            if (paramsMap.get(key) != null) {
                if (pos != 0) getUrl += "&";
                getUrl += key + "=" + paramsMap.get(key);
            }
            pos++;
        }
        Log.d("getUrl","getUrl:"+getUrl);
        return new Request.Builder().url(getUrl)
                .header("Content-Type", "text/html; charset=utf-8")
                .build();
    }
    String case_;
    //api_傳送內容 初始化
    private FormBody.Builder initBuilder(HashMap<String, String> paramsMap){
        FormBody.Builder builder = new FormBody.Builder();
        String mid = "";
        int pos = 0;
        Log.d("posthttp", "-----------------------" + api_name + " start ----------------");
        try {
//            mid = new String(Base64.decode(Activity.getM_id()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        case_ = web_url + api_name + "?isweb=1&mid=" + mid + "&";
        for (String key : paramsMap.keySet()) {
            //參數轉換
            if (paramsMap.get(key) != null) {
                builder.add(key, paramsMap.get(key));
//                Log.d("posthttp", key + "=" + paramsMap.get(key));
                if (pos != 0) case_ += "&";
                case_ += key + "=" + paramsMap.get(key);
            }
            pos++;
        }
        Log.d("getUrl","case_:"+case_);
        return builder;
    }
}
