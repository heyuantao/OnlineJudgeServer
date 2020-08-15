package cn.heyuantao.onlinejudgeserver.core;

import cn.heyuantao.onlinejudgeserver.exception.MessageException;
import org.apache.commons.lang3.StringUtils;

/**
 * @author he_yu
 * 不同的语言类型
 */

public enum LanguageType {

    C("C语言",0),
    CPP("C++语言",1),
    JAVA("JAVA语言",3);

    private String name;
    private Integer value;

    LanguageType(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public static LanguageType getLanguageTypeByExtension(String extension)  {
        if(StringUtils.equalsIgnoreCase(extension,"c")){
            return LanguageType.C;
        }else if(StringUtils.equalsIgnoreCase(extension,"cpp")){
            return LanguageType.CPP;
        }else if(StringUtils.equalsIgnoreCase(extension,"java")){
            return LanguageType.JAVA;
        }else{
            throw new MessageException("扩展名:"+extension+"不被支持");
        }

    }

}
