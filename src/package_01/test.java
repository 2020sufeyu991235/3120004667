package package_01;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

public class test {
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
        int size_origin=list_origin.size();
        int size_plagrarism=list_plagiarism.size();
        for(int i=0;i<size_origin;i++) {
            for (int j = 0; j < size_plagrarism; j++) {
                if(list_origin.get(i).equals(list_plagiarism.get(j))){
                    list_origin.remove(i);
                    list_plagiarism.remove(j);
                    size_origin-=1;
                    size_plagrarism-=1;
                    i=-1;
                    break;
                }
            }
        }
        System.out.println(list_origin);
        System.out.println();
        System.out.println(list_plagiarism);
    }
}
