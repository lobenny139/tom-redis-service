package com.tom.redis.service;

public interface IRedisService {
    /**
     * 普通缓存获取
     * @param key 键
     * @return 值
     */
    public Object get(String key);

    /**
     * 普通缓存获取
     * @param key 键
     * @param dbIndex
     * @return 值
     */
    public Object get(int dbIndex, String key);

    /**
     * 普通缓存放入
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean set(String key, Object value);

    /**
     * 普通缓存放入并设置时间
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean set(String key, Object value, long time);

    /**
     * 普通缓存放入并设置时间
     * @param dbindex
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean set(int dbindex, String key, Object value, long time);

    /**
     * delete from redis
     * @param dbIndex
     * @param keys
     */
    public void del(int dbIndex, String... keys);

    /**
     * delete from redis
      * @param keys
     */
    public void del( String... keys);

}
