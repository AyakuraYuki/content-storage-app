package cc.ayakurayuki.contentstorage.common.base

/**
 * Created by ayakurayuki on 2018/1/16-11:54. <br/>
 * Package: cc.ayakurayuki.contentstorage.common.base <br/>
 */
interface BaseDAO<T> {

  T get(String id)

  List<T> list()

  List<T> search(T t)

  int insert(T t)

  int update(T t)

  int delete(T t)

}
