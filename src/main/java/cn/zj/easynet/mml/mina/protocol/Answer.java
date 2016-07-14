package cn.zj.easynet.mml.mina.protocol;

//import org.apache.commons.lang3.StringUtils;

/**
 * Created by Jadedrip on 2014/5/26.
 */
@SuppressWarnings("unused")
public class Answer {
    private String command;
    private String id;
    private String code;
    private String description = null;

    public Answer(String command, String id) {
        this.command = command;
        this.id = id;
    }

    public String getCommand() {
        return command;
    }

    public String getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        if( description!=null && description.length()>2 ){
            if( description.charAt(0)=='"' && '"'==description.charAt(description.length()-1) ){
                this.description=description.substring(1, description.length()-1 );
            }
        }
    }
}
