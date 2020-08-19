package cn.heyuantao.onlinejudgeserver.run;


import cn.heyuantao.onlinejudgeserver.core.LanguageType;
import cn.heyuantao.onlinejudgeserver.core.Problem;
import cn.heyuantao.onlinejudgeserver.core.ProblemResourceLimit;
import cn.heyuantao.onlinejudgeserver.core.ProblemTestCase;
import cn.heyuantao.onlinejudgeserver.domain.ProblemRequestDTO;
import cn.heyuantao.onlinejudgeserver.exception.MessageException;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProblemGenerator {


    @Test
    public void displayProblem() throws IOException {
        String sourceCodePath    = getFullDirWithFilename("problem01/sourcecode.c");
        String testCase01InPath  = getFullDirWithFilename("problem01/test1.in");
        String testCase01OutPath = getFullDirWithFilename("problem01/test1.out");
        String testCase02InPath  = getFullDirWithFilename("problem01/test2.in");
        String testCase02OutPath = getFullDirWithFilename("problem01/test2.out");

        List<String> inPathList = new ArrayList<>();
        List<String> outPathList = new ArrayList<>();
        inPathList.add(testCase01InPath);
        inPathList.add(testCase02InPath);
        outPathList.add(testCase01OutPath);
        outPathList.add(testCase02OutPath);

        ProblemRequestDTO problem = getProblem(sourceCodePath,inPathList,outPathList);
        //System.out.print(problem);
        System.out.print(problem.getSourceCode());
        System.out.print(problem.getTestCaseList().get(0).getInput());
        System.out.print(problem.getTestCaseList().get(0).getTarget());
    }

    /**
     * 生成一个测试用的数据
     * @return
     */
    public ProblemRequestDTO getOneProblem() throws IOException {
        String sourceCodePath    = getFullDirWithFilename("problem01/sourcecode.c");
        String testCase01InPath  = getFullDirWithFilename("problem01/test1.in");
        String testCase01OutPath = getFullDirWithFilename("problem01/test1.out");
        String testCase02InPath  = getFullDirWithFilename("problem01/test2.in");
        String testCase02OutPath = getFullDirWithFilename("problem01/test2.out");

        List<String> inPathList = new ArrayList<>();
        List<String> outPathList = new ArrayList<>();
        inPathList.add(testCase01InPath);
        inPathList.add(testCase02InPath);
        outPathList.add(testCase01OutPath);
        outPathList.add(testCase02OutPath);

        ProblemRequestDTO problem = getProblem(sourceCodePath,inPathList,outPathList);
        return problem;
    }

    public ProblemRequestDTO getProblem(String problemPath, List<String> testInPathList, List<String> testOutPathList) throws IOException {
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

    public String readFileContentWithFullPath(String fullPath) throws IOException {
        BufferedReader fileReader = new BufferedReader(new FileReader(fullPath));
        String oneLine = null;
        StringBuilder stringBuilder = new StringBuilder();
        while((oneLine = fileReader.readLine())!=null ){
            stringBuilder.append(oneLine);
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    public String getFullDirWithFilename(String fileName){
        String projectPath = System.getProperty("user.dir");
        String dataPath = "/src/test/data/";
        String fullPath = String.format("%s%s%s",projectPath,dataPath,fileName);
        return fullPath;
    }

}
