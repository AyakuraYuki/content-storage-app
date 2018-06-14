package cc.ayakurayuki.contentstorage.common.base

/**
 * Created by ayakurayuki on 2018/1/16-11:21. <br/>
 * Package: cc.ayakurayuki.contentstorage.common.base <br/>
 */
class BaseBean {

    final static String SECRET = "secret"
    final static String EMERGENCY = "emergency"

    /* ====================================== */
    /* =============== Redis ================ */
    /* ====================================== */
    final static String ACCOUNT_2FA_EMERGENCY_CODES_KEY = "account:{0}:2fa:emergency:codes"

}
