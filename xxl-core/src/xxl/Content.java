package xxl;

import java.io.Serial;
import java.io.Serializable;


/**classe abstrata que trata dos tipos de conteudo que uma cell pode ter */

public abstract class Content implements Serializable{

    @Serial
    private static final long serialVersionUID = 202308312359L;

    private String _content;

    public Content(String content) {
        _content = content;
    }


    public String getcontentString(){
        return _content;
    }

   public abstract String accept(Visitor visitor);

}