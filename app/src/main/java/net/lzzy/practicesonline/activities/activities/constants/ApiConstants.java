package net.lzzy.practicesonline.activities.activities.constants;

import net.lzzy.practicesonline.activities.activities.utils.AppUtils;

/**
 * Created by lzzy_gxy on 2019/4/15.
 * Description:
 */
public class ApiConstants {
    private static final String Ip= AppUtils.loadServerSetting(AppUtils.getContext()).first;
    private static final String PORT=AppUtils.loadServerSetting(AppUtils.getContext()).second;
    private static final String PROTOCOL="http://";

    public static final String URL_APT=PROTOCOL.concat(Ip).concat(":").concat(PORT);
}
