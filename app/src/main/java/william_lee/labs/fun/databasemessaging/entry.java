package william_lee.labs.fun.databasemessaging;

/**
 * Created by william_lee on 7/9/16.
 */
public class entry {

    private int id;
    private String content, time;

    public entry(int id, String content, String time){
        this.id=id;
        this.content = content;
        this.time = time;
    }

    public int getId(){
        return id;
    }

    public String getContent(){
        return content;
    }

    public String getTime(){
        return time;
    }

}
