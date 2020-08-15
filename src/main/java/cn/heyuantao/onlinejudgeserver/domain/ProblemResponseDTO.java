package cn.heyuantao.onlinejudgeserver.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author he_yu
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProblemResponseDTO implements Serializable {
    /**
     * 对该问题生成的序号
     */
    private String id;

    /**
     * 返回的通知地址
     */
    private String notifyAddress;
}
