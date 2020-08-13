package cn.heyuantao.onlinejudgeserver.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author he_yu
 * 系统用户配置
 */

@NoArgsConstructor
@Data
@ToString
public class SysUser implements Serializable {
    private String name;
    private String password;

    public SysUser(String name, String password) {
        this.name = name;
        this.password = password;
    }
}
