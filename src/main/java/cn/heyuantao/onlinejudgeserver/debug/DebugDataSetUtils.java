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
import java.util.Random;
import java.util.UUID;

/**
 * @author he_yu
 */

@Component
public class DebugDataSetUtils {

    /**
     * 随机选择一个问题
     * @return
     * @throws IOException
     */
    public ProblemRequestDTO getSampleProblemDTO() throws IOException {
        Random random = new Random();
        Boolean status = random.nextBoolean();
        if(status.equals(Boolean.TRUE)){
            return getSampleProblem01DTO();
        }else{
            return getSampleProblem02DTO();
        }
    }

    public ProblemRequestDTO getSampleProblem01DTO() throws IOException {
        String sourceCodePath    = "data/problem01/sourcecode.c";
        String testCase01InPath  = "data/problem01/test1.in";
        String testCase01OutPath = "data/problem01/test1.out";
        String testCase02InPath  = "data/problem01/test2.in";
        String testCase02OutPath = "data/problem01/test2.out";

        List<String> inPathList = new ArrayList<String>();
        List<String> outPathList = new ArrayList<String>();
        inPathList.add(testCase01InPath);
        inPathList.add(testCase02InPath);
        outPathList.add(testCase01OutPath);
        outPathList.add(testCase02OutPath);

        ProblemRequestDTO problem = getProblemDTO(sourceCodePath,inPathList,outPathList);
        return problem;
    }

    public ProblemRequestDTO getSampleProblem02DTO() throws IOException {
        String sourceCodePath    = "data/problem02/sourcecode.c";
        String testCase01InPath  = "data/problem02/test1.in";
        String testCase01OutPath = "data/problem02/test1.out";
        String testCase02InPath  = "data/problem02/test2.in";
        String testCase02OutPath = "data/problem02/test2.out";

        List<String> inPathList = new ArrayList<String>();
        List<String> outPathList = new ArrayList<String>();
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

        //生成一个五位的随机码，放在通知网址的后部
        String notifyUrl = String.format("%s%s/","http://localhost:8080/api/v1/debug/", UUID.randomUUID().toString().replace("-", "").toLowerCase().substring(0,5));
        problem.setNotifyAddress(notifyUrl);

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
