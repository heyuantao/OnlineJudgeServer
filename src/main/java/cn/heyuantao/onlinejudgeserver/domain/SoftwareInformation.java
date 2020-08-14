package cn.heyuantao.onlinejudgeserver.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author he_yu
 * 软件的基本信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SoftwareInformation implements Serializable {
    private String version;
    private String title;
    private String description;

    /**
     * runStatus is dev or prod
     */
    private String runStatus;
}
