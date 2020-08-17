package cn.heyuantao.onlinejudgeserver.core;

import cn.heyuantao.onlinejudgeserver.exception.MessageException;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @author he_yu
 * 不同的语言类型
 */

public enum LanguageType {

    C("C语言",0,"c"),
    CPP("C++语言",1, "cpp"),
    JAVA("JAVA语言",3,"java");

    /**
     * 分别存储语言的描述、序号、和文件扩展名
     */
    private String name;
    private Integer value;
    private String extension;

    LanguageType(String name, Integer value, String extension) {
        this.name = name;
        this.value = value;
        this.extension = extension;
    }


    public static LanguageType getLanguageTypeByExtension(String extension)  {
        if(StringUtils.equalsIgnoreCase(extension,C.extension)){
            return LanguageType.C;
        }else if(StringUtils.equalsIgnoreCase(extension,CPP.extension)){
            return LanguageType.CPP;
        }else if(StringUtils.equalsIgnoreCase(extension,JAVA.extension)){
            return LanguageType.JAVA;
        }else{
            throw new MessageException("扩展名:"+extension+"不被支持");
        }

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

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
}
