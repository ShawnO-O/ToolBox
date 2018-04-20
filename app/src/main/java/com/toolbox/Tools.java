package com.toolbox;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tools {
    /**
     * 将每三个数字加上逗号处理（通常使用金额方面的编辑）
     * @param str 需要处理的字符串
     * @return 处理完之后的字符串
     */
    protected String addComma(String str) {
        DecimalFormat decimalFormat = new DecimalFormat(",###");
        return decimalFormat.format(Double.parseDouble(str));
    }

    /********e-mail驗證***************/
    //因為web端的驗證需要比較長的時間，所以直接在app做驗證的動作
    protected boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,8}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);

        return m.matches();
    }
}
