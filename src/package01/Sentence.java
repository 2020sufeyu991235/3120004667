package package01;

import java.util.List;

/**
 * @author 玊非玉
 */
public class Sentence {
    List<Character> list;
    int size, repeatNumber;
    public Sentence(List<Character> list,int size,int repeatNumber){
        this.list=list;
        this.size=size;
        this.repeatNumber=repeatNumber;
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

    public int getRepeatNumber() {
        return repeatNumber;
    }

    public void setRepeatNumber(int repeatNumber) {
        this.repeatNumber = repeatNumber;
    }
}
