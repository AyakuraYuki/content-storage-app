package cc.ayakurayuki.contentstorage.common.util

/**
 * Created by ayakurayuki on 2018/2/7-14:03. <br/>
 * Package: cc.ayakurayuki.contentstorage.common.util <br/>
 */
final class RandomKeyUtils {

    private static final def CHARACTERS = "abcdefghijklmnopqrstuvwxyz"
    private static final def NUMBERS = "0123456789"
    private static final def KEY_NODE = "${NUMBERS}${CHARACTERS}".toCharArray()
    private static final def KEY_NODE_WITHOUT_NUMBERS = "${CHARACTERS}".toCharArray()

    static String getEmergencyKey() {
        def key = ''
        def random = new Random()
        for (i in 1..4) {
            key += KEY_NODE_WITHOUT_NUMBERS[random.nextInt(KEY_NODE_WITHOUT_NUMBERS.length)]
        }
        key += '-'
        for (i in 1..4) {
            key += KEY_NODE[random.nextInt(KEY_NODE.length)]
        }
        return key
    }

}
