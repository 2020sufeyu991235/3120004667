package package_01;

import java.util.List;

public class Sentence {
    List<Character> list;
    int size,repeat_number;
    public Sentence(List<Character> list,int size,int repeat_number){
        this.list=list;
        this.size=size;
        this.repeat_number=repeat_number;
    }

    public List<Character> getList() {
        return list;
    }

    public void setList(List<Character> list) {
        this.list = list;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getRepeat_number() {
        return repeat_number;
    }

    public void setRepeat_number(int repeat_number) {
        this.repeat_number = repeat_number;
    }
}
