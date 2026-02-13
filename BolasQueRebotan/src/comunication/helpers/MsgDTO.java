package comunication.helpers;

import java.io.Serializable;

public class MsgDTO implements Serializable {
    private int header;
    private BolaDTO payload;

    public MsgDTO (int header, BolaDTO payload){
        this.header = header;
        this.payload = payload;
    }

    public int getCode (){
        return header;
    }

    public BolaDTO getBall (){
        return payload;
    }
}
