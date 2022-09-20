package package01;

import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

public class Main {
    //判断文件是否存在
    public static File getFile(String path){
        File file=new File(path);
        if(file.exists()){
            return file;
        }
        else{
            return null;
        }
    }

    //返回文件编码
    public String getEncoding(Path path){
        String encoding;
        try {
            byte[] bytes= Files.readAllBytes(path);
            //字符集检测类
            CharsetDetector charsetDetector=new CharsetDetector();
            //设置要检测的文本数据
            charsetDetector.setText(bytes);
            //匹配字符集类，detect()方法返回最可能匹配的字符集
            CharsetMatch charsetMatch=charsetDetector.detect();
            encoding=charsetMatch.getName();
            //匹配常用的字符集
            if("GB18030".equals(encoding)|| "UTF-8".equals(encoding)|| "UTF-16LE".equals(encoding)|| "UTF-16BE".equals(encoding)){
                return encoding;
            }
            else {
                //返回所有可能的字符集
                CharsetMatch[] charsetMatches=charsetDetector.detectAll();
                for(CharsetMatch match:charsetMatches){
                    encoding=match.getName();
                    if("GB18030".equals(encoding)|| "UTF-8".equals(encoding)|| "UTF-16LE".equals(encoding)|| "UTF-16BE".equals(encoding)){
                        return encoding;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //划分句子
    public void sentence_division(File file,List<Sentence> list_sentence,String encoding){
        int c;
        List<Character> list_char=new LinkedList<>();
        try {
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(new FileInputStream(file), encoding));
            while((c= bufferedReader.read())!=-1){
                if(Character.toString(c).matches("[.。！!?？…]")){
                    if(!list_char.isEmpty()) {
                        List<Character> list_clone = new LinkedList<>(list_char);
                        list_sentence.add(new Sentence(list_clone, list_char.size(), 0));
                        list_char.clear();
                    }
                }
                else if(!Character.toString(c).matches("[,，、：:\"“”\s\n\r《》*()（）—]")) {
                    list_char.add((char)c);
                }
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //两个句子之间对比
    public int contrast(Sentence sentence_origin,Sentence sentence_plagiarism){
        List<Character> list_origin=sentence_origin.getList();
        List<Character> list_plagiarism=new LinkedList<>(sentence_plagiarism.getList());
        int plagiarism_size=sentence_plagiarism.getSize();
        list_plagiarism.removeAll(list_origin);
        return plagiarism_size-list_plagiarism.size();
    }

    //句子之间连续对比
    public void continuous_contrast(List<Sentence> list_sentence_origin,List<Sentence> list_sentence_plagiarism) {
        int repeat_number, size;
        float repeat_rate;
        int i;
        for (i = 0; i < list_sentence_plagiarism.size(); i++) {
            size = list_sentence_plagiarism.get(i).getSize();
            for (int j = i; j < list_sentence_origin.size(); j++) {
                repeat_number = contrast(list_sentence_origin.get(j), list_sentence_plagiarism.get(i));
                repeat_rate = (float) repeat_number / size;
                if (size < 10) {
                    if (repeat_rate > 0.75) {
                        list_sentence_plagiarism.get(i).setRepeatNumber(repeat_number);
                        break;
                    }
                } else {
                    if (repeat_rate > 0.7) {
                        list_sentence_plagiarism.get(i).setRepeatNumber(repeat_number);
                        break;
                    }
                }
            }
            if(list_sentence_plagiarism.get(i).getRepeatNumber()==0) {
                for (int j = 0; j < i; j++) {
                    repeat_number = contrast(list_sentence_origin.get(j), list_sentence_plagiarism.get(i));
                    repeat_rate = (float) repeat_number / size;
                    if (size < 10) {
                        if (repeat_rate > 0.75) {
                            list_sentence_plagiarism.get(i).setRepeatNumber(repeat_number);
                            break;
                        }
                    } else {
                        if (repeat_rate > 0.7) {
                            list_sentence_plagiarism.get(i).setRepeatNumber(repeat_number);
                            break;
                        }
                    }
                }
            }
        }
    }

    //计算重复率
    public double calculate(List<Sentence> list_sentence_plagiarism){
        int size = 0,repeat_number=0;
        for (Sentence sentence : list_sentence_plagiarism) {
            size += sentence.getSize();
            repeat_number += sentence.getRepeatNumber();
        }
        return (double)Math.round((float)repeat_number/size*100)/100;
    }

    //写入重复率
    public void writeFile(File file,double calculate){
        try {
            PrintWriter printWriter=new PrintWriter(file);
            printWriter.write(String.valueOf(calculate));
            printWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        //该列表存放句子对象
        List<Sentence> list_sentence_origin = new LinkedList<>();
        List<Sentence> list_sentence_plagiarism = new LinkedList<>();
        /*File file_origin = new File("./test_text/orig.txt");
        File file_plagiarism = new File("./test_text/orig_0.8_dis_15.txt");
        File file_calculate=new File("./test_text/Test_Write.txt");*/
        //文件读取
        File file_origin=getFile(args[0]);
        File file_plagiarism=getFile(args[1]);
        File file_calculate=getFile(args[2]);
        if(file_origin==null||file_plagiarism==null||file_calculate==null){
            System.out.println("检测到文件不存在，程序终止");
            System.exit(1);
        }

        Main main=new Main();

        //句子划分生成句子对象存放进列表
        Path path_origin= Path.of(file_origin.getPath());
        String encoding_origin=main.getEncoding(path_origin);
        if(encoding_origin==null){
            System.out.println("无法识别文件编码，程序终止");
            System.exit(1);
        }
        main.sentence_division(file_origin,list_sentence_origin,encoding_origin);

        Path path_plagiarism= Path.of(file_plagiarism.getPath());
        String encoding_plagiarism=main.getEncoding(path_plagiarism);
        if(encoding_plagiarism==null){
            System.out.println("无法识别文件编码，程序终止");
            System.exit(1);
        }
        main.sentence_division(file_plagiarism,list_sentence_plagiarism,encoding_plagiarism);
        //两个列表对比句子重复部分
        main.continuous_contrast(list_sentence_origin,list_sentence_plagiarism);
        //写入重复率
        main.writeFile(file_calculate,main.calculate(list_sentence_plagiarism));
    }
}
