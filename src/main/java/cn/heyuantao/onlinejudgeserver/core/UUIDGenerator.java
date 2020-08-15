package cn.heyuantao.onlinejudgeserver.core;


import java.util.UUID;

/**
 * 生成在ID，并将其用于Redis中做完Solution的key
 */
public class UUIDGenerator {
    /**
     * 生成唯一的序号
     * @return
     */
    private static String getUIID(){
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }


    /**
     * 生成solution的key
     * @return
     */
    public static String generateSolutionKey(){
        return getUIID();
    }
}
