import cc.ayakurayuki.contentstorage.common.util.JsonMapper
import cc.ayakurayuki.contentstorage.common.util.RandomKeyUtils
import org.junit.Test

/**
 * Created by ayakurayuki on 2018/2/7-14:36. <br/>
 * Package:  <br/>
 */
class Playground {

    @Test
    void randomEmergencyKey() {
        for (i in 1..10) {
            println RandomKeyUtils.emergencyKey
        }
    }

    @Test
    void json() {
        def list = []
        for (i in 1..2) {
            def key = new Key()
            key.key = "$i"
            key.effective = true
            list.add(key)
        }
        def json = JsonMapper.toJsonString(list)
        println json
        ((Key) list[1]).effective = false
        json = JsonMapper.toJsonString(list)
        println json
        list = JsonMapper.fromJsonString(json, List.class) as List<Key>
        println JsonMapper.toJsonString(list)
    }

    class Key {
        String key
        boolean effective

        @Override
        String toString() {
            return "key: $key, effective: $effective"
        }
    }

}
