package package_01;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.Matchers.closeTo;
import static org.junit.Assert.*;
import static package_01.main.getFile;

import org.junit.Test;

public class mainTest {
    main main=new main();

    @Test
    public void test_getFile() {
        //正常路径
        String path="./test_text/orig.txt";
        //文件不存在的路径
        String path_null="./test_text/orig_null.txt";

        File file =getFile(path);
        assertNotNull(file);
        file=getFile(path_null);
        assertNull(file);
    }

    @Test
    public void getEncoding() {
        //不同编码的文件
        String path_GB18030="./test_text/Test_GB18030.txt";
        String path_UTF8_BOM="./test_text/Test_UTF_BOM.txt";
        String path_UTF8_NULLBOM="./test_text/Test_UTF8.txt";
        String path_UTF16_LE="./test_text/Test_UTF16LE.txt";
        String path_UTF16_BE="./test_text/Test_UTF16BE.txt";

        String encoding;
        encoding= main.getEncoding(Path.of(path_GB18030));
        assertEquals("GB18030",encoding);
        encoding=main.getEncoding(Path.of(path_UTF8_NULLBOM));
        assertEquals("UTF-8",encoding);
        encoding=main.getEncoding(Path.of(path_UTF8_BOM));
        assertEquals("UTF-8",encoding);
        encoding=main.getEncoding(Path.of(path_UTF16_BE));
        assertEquals("UTF-16BE",encoding);
        encoding=main.getEncoding(Path.of(path_UTF16_LE));
        assertEquals("UTF-16LE",encoding);
    }

    @Test
    public void sentence_division() {
        String path="./test_text/Test_Sentence_Division.txt";
        File file=new File(path);
        List<Sentence> list=new LinkedList<>();
        main.sentence_division(file,list,"UTF-8");
        /*for (Sentence sentence : list) {
            System.out.println(sentence.getList());
        }*/
        assertEquals(3,list.size());
    }

    @Test
    public void contrast() {
        //用来对比的句子，模拟划分后的句子已去除标点
        String string_origin="今天是星期天天气晴今天晚上我要去看电影";
        String string_plagiarism="今天是周天天气晴朗我晚上要去看电影";

        List<Character> list_origin=new LinkedList<>();
        List<Character> list_plagiarism=new LinkedList<>();
        for(int i=0;i<string_origin.length();i++){
            list_origin.add(string_origin.charAt(i));
        }
        Sentence sentence_origin=new Sentence(list_origin,string_origin.length(),0);
        for(int i=0;i<string_plagiarism.length();i++){
            list_plagiarism.add(string_plagiarism.charAt(i));
        }
        Sentence sentence_plagiarism=new Sentence(list_plagiarism,string_plagiarism.length(),0);

        int repeat_number=main.contrast(sentence_origin,sentence_plagiarism);
        assertEquals(15,repeat_number);
    }

    @Test
    public void continuous_contrast() {
        File file_origin=new File("./test_text/Test_Continuous_Contrast_Origin.txt");
        File file_plagiarism=new File("./test_text/Test_Continuous_Contrast_Plagiarism.txt");
        List<Sentence>list_sentence_origin=new LinkedList<>();
        List<Sentence>list_sentence_plagiarism=new LinkedList<>();
        //划分句子
        main.sentence_division(file_origin,list_sentence_origin,"UTF-8");
        main.sentence_division(file_plagiarism,list_sentence_plagiarism,"UTF-8");
        //原文
        //文件编辑格式查看帮助。句子连续对比。
        //今天是星期天，天气晴，今天晚上我要去看电影。你好世界，再见十阶。

        //抄袭
        //你好十阶，再见世哈。"今天是周天，天气晴朗，我晚上要去看电影。
        //连见对比句子。文件格式查看呵呵帮助。

        //预测结果（每句重复字数） 7，15，5，8

        //句子连续对比
        main.continuous_contrast(list_sentence_origin,list_sentence_plagiarism);
        assertEquals(7,list_sentence_plagiarism.get(0).getRepeat_number());
        assertEquals(15,list_sentence_plagiarism.get(1).getRepeat_number());
        assertEquals(5,list_sentence_plagiarism.get(2).getRepeat_number());
        assertEquals(8,list_sentence_plagiarism.get(3).getRepeat_number());
    }

    @Test
    public void calculate() {
        List<Sentence> list=new LinkedList<>();
        list.add(new Sentence(new LinkedList<>(),10,6));
        list.add(new Sentence(new LinkedList<>(),31,29));
        System.out.println(main.calculate(list));
        //assertThat(main.calculate(list),closeTo(0.66,0.02));
    }

    @Test
    public void writeFile() {
        File file=new File("./test_text/Test_Write.txt");
        Double calculate=0.66;
        main.writeFile(file,calculate);
    }

    @Test
    public void test_main() {
        String[] strings=new String[3];
        strings[0]="./test_text/Test_Continuous_Contrast_Origin.txt";
        strings[1]="./test_text/Test_Continuous_Contrast_Plagiarism.txt";
        strings[2]="./test_text/Test_Write.txt";
        package_01.main.main(strings);
        String calculate=null;
        try {
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(new FileInputStream(new File(strings[2])), StandardCharsets.UTF_8));
            calculate=bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals("0.85",calculate);
    }
}