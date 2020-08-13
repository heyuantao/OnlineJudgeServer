package cn.heyuantao.onlinejudgeserver.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author he_yu
 * 软件的基本信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SoftwareInformation {
    private String version;
    private String title;
    private String description;

    /**
     * runStatus is dev or prod
     */
    private String runStatus;
}
