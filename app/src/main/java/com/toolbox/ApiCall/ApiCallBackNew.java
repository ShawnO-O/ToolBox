package com.toolbox.ApiCall;

import org.json.JSONObject;

/**
 * Created by 30033 on 2017/12/19.
 */
public interface ApiCallBackNew {
    void onSucess(JSONObject jsonObj);
    void onError(JSONObject jsonObj);
}
