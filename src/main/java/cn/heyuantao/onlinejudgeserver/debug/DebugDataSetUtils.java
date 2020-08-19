package cn.heyuantao.onlinejudgeserver.debug;

import cn.heyuantao.onlinejudgeserver.core.LanguageType;
import cn.heyuantao.onlinejudgeserver.core.ProblemTestCase;
import cn.heyuantao.onlinejudgeserver.domain.ProblemRequestDTO;
import cn.heyuantao.onlinejudgeserver.exception.MessageException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author he_yu
 */

@Component
public class DebugDataSetUtils {

    /**
     * 生成一个默认的问题请求,用于代码的调试
     * @return
     * @throws IOException
     */
    public ProblemRequestDTO getSampleProblemDTO() throws IOException {
        String sourceCodePath    = "data/problem01/sourcecode.c";
        String testCase01InPath  = "data/problem01/test1.in";
        String testCase01OutPath = "data/problem01/test1.out";
        String testCase02InPath  = "data/problem01/test2.in";
        String testCase02OutPath = "data/problem01/test2.out";

        List<String> inPathList = new ArrayList<>();
        List<String> outPathList = new ArrayList<>();
        inPathList.add(testCase01InPath);
        inPathList.add(testCase02InPath);
        outPathList.add(testCase01OutPath);
        outPathList.add(testCase02OutPath);

        ProblemRequestDTO problem = getProblemDTO(sourceCodePath,inPathList,outPathList);
        return problem;
    }

    /**
     * 根据源代码和测试文件的路径创建一个 ProblemRequestDTO 的数据结构
     * @param problemPath
     * @param testInPathList
     * @param testOutPathList
     * @return
     * @throws IOException
     */
    public ProblemRequestDTO getProblemDTO(String problemPath, List<String> testInPathList, List<String> testOutPathList) throws IOException {
        if(testInPathList.size()!=testOutPathList.size()){
            throw new MessageException("测试文件的数目不匹配");
        }
        ProblemRequestDTO problem = new ProblemRequestDTO();
        problem.setSourceCode(readFileContentWithFullPath(problemPath));

        List<ProblemTestCase> testCaseList = new ArrayList<ProblemTestCase>();
        for(Integer i=0;i<testInPathList.size();i++){
            ProblemTestCase testCase = new ProblemTestCase();
            testCase.setInput(readFileContentWithFullPath(testInPathList.get(i)));
            testCase.setTarget(readFileContentWithFullPath(testOutPathList.get(i)));
            testCaseList.add(testCase);
        }

        problem.setTestCaseList(testCaseList);
        problem.setLangExtension(LanguageType.C.getExtension());
        problem.setMemoryLimit(128);
        problem.setTimeLimit(2);
        problem.setIsSpecialJudge(Boolean.FALSE);
        problem.setNotifyAddress("http://localhost/test");

        return problem;
    }

    /**
     * 从路径直接读取文本文件的内容
     * @param fullPath
     * @return
     * @throws IOException
     */
    private String readFileContentWithFullPath(String fullPath) throws IOException {
        BufferedReader fileReader = new BufferedReader(new FileReader(fullPath));
        String oneLine = null;
        StringBuilder stringBuilder = new StringBuilder();
        while((oneLine = fileReader.readLine())!=null ){
            stringBuilder.append(oneLine);
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

}
