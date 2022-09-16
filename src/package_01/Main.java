package package_01;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

public class Main {
    //划分句子
    public void sentence_division(File file,List<Sentence> list_sentence){
        int c;
        List<Character> list_char=new LinkedList<>();
        try {
            //BufferedReader bufferedReader=new BufferedReader(new FileReader(file));
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
            while((c= bufferedReader.read())!=-1){
                if(Character.toString(c).matches("[.。！!?？…]")&&!list_char.isEmpty()){
                    List<Character> list_clone=new LinkedList<>(list_char);
                    list_sentence.add(new Sentence(list_clone,list_char.size(),0));
                    list_char.clear();
                }
                else if(!Character.toString(c).matches("[,，、：:\"“”\s\n\r《》*()（）—]"))
                    list_char.add((char)c);
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //句子之间连续对比
    public void continuous_contrast(List<Sentence> list_sentence_origin,List<Sentence> list_sentence_plagiarism) {
        int repeat_number, size;
        float repeat_rate;
        int i;
        System.out.println(list_sentence_plagiarism.size());
        for (i = 0; i < list_sentence_plagiarism.size(); i++) {
            size = list_sentence_plagiarism.get(i).getSize();
            for (int j = i; j < list_sentence_origin.size(); j++) {
                repeat_number = contrast(list_sentence_origin.get(j), list_sentence_plagiarism.get(i));
                repeat_rate = (float) repeat_number / size;
                if (size < 10) {
                    if (repeat_rate > 0.75) {
                        list_sentence_plagiarism.get(i).setRepeat_number(repeat_number);
                        break;
                    }
                } else {
                    if (repeat_rate > 0.7) {
                        list_sentence_plagiarism.get(i).setRepeat_number(repeat_number);
                        break;
                    }
                }
            }
            if(list_sentence_plagiarism.get(i).getRepeat_number()==0) {
                for (int j = 0; j < i; j++) {
                    repeat_number = contrast(list_sentence_origin.get(j), list_sentence_plagiarism.get(i));
                    repeat_rate = (float) repeat_number / size;
                    if (size < 10) {
                        if (repeat_rate > 0.75) {
                            list_sentence_plagiarism.get(i).setRepeat_number(repeat_number);
                            break;
                        }
                    } else {
                        if (repeat_rate > 0.7) {
                            list_sentence_plagiarism.get(i).setRepeat_number(repeat_number);
                            break;
                        }
                    }
                }
                //System.out.println(i);
            }
        }
        //System.out.println(i);
    }

    //两个句子之间对比
    public int contrast(Sentence sentence_origin,Sentence sentence_plagiarism){
        List<Character> list_origin=sentence_origin.getList();
        List<Character> list_plagiarism=new LinkedList<>(sentence_plagiarism.getList());
        int plagiarism_size=sentence_plagiarism.getSize();
        list_plagiarism.removeAll(list_origin);
        return plagiarism_size-list_plagiarism.size();
    }

    //计算重复率
    public float calculate(List<Sentence> list_sentence_plagiarism){
        int size = 0,repeat_number=0;
        for (Sentence sentence : list_sentence_plagiarism) {
            size += sentence.getSize();
            repeat_number += sentence.getRepeat_number();
        }
        return (float)Math.round((float)repeat_number/size*100)/100;
    }


    public static void main(String[] args) {
        List<Sentence> list_sentence_origin = new LinkedList<>();
        List<Sentence> list_sentence_plagiarism = new LinkedList<>();
        File file_origin = new File("./test_text/orig.txt");
        File file_plagiarism = new File("./test_text/orig_0.8_dis_15.txt");
        /*File file_origin = new File(args[0]);
        File file_plagiarism = new File(args[1]);*/
        Main main=new Main();
        main.sentence_division(file_origin,list_sentence_origin);
        main.sentence_division(file_plagiarism,list_sentence_plagiarism);
        main.continuous_contrast(list_sentence_origin,list_sentence_plagiarism);
        System.out.println("重复率为："+main.calculate(list_sentence_plagiarism));
    }
}
