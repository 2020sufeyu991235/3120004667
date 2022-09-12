package package_01;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class Main {
    public void sentence_division(File file,List<Sentence> list_sentence){
        int c;
        List<Character> list_char=new LinkedList<>();
        try {
            BufferedReader bufferedReader=new BufferedReader(new FileReader(file));
            while((c= bufferedReader.read())!=-1){
                if(Character.toString(c).matches("[.¡££¡!?£¿¡­]")&&!list_char.isEmpty()){
                    list_sentence.add(new Sentence(list_char,list_char.size(),0));
                    list_char.clear();
                }
                else if(!Character.toString(c).matches("[,£¬¡¢£º:\"¡°¡±\s\n\r¡¶¡·*()£¨£©¡ª]"))
                    list_char.add((char)c);
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void contrast(Sentence sentence_origin,Sentence sentence_plagiarism){
        /*int size_origin=list_origin.size();
        int size_plagiarism=list_plagiarism.size();
        for(int i=0;i<size_origin;i++) {
            for (int j = 0; j < size_plagiarism; j++) {
                if(list_origin.get(i).equals(list_plagiarism.get(j))){
                    list_origin.remove(i);
                    list_plagiarism.remove(j);
                    size_origin-=1;
                    size_plagiarism-=1;
                    i=-1;
                    break;
                }
            }
        }*/
    }

    public static void main(String[] args) {
        List<Character> list_origin=new LinkedList<>();
        List<Character> list_plagiarism=new LinkedList<>();
        File file_origin=new File("./test_text/orig.txt");
        File file_plagiarism=new File("./test_text/orig_0.8_dis_15.txt");
        char c;
        try {
            BufferedReader bufferedReader_origin=new BufferedReader(new FileReader(file_origin));
            while((c= (char) bufferedReader_origin.read())!='¡£'){
                if(!Character.toString(c).matches("[,£¬¡¢£º:\"¡°\s\n\r]"))list_origin.add(c);
            }
            bufferedReader_origin.close();

            BufferedReader bufferedReader_plagiarism=new BufferedReader(new FileReader(file_plagiarism));
            while((c= (char) bufferedReader_plagiarism.read())!='¡£'){
                if(!Character.toString(c).matches("[,£¬¡¢£º:\"¡°\s\n\r]"))list_plagiarism.add(c);
            }
            bufferedReader_plagiarism.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(list_origin);
        System.out.println();
        System.out.println(list_plagiarism);
    }
}
