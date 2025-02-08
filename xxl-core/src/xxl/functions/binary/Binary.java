package xxl.functions.binary;

import xxl.Content;
import xxl.Function;


public abstract class Binary extends Function{

    private Content _c1;
    private Content _c2;

    public Binary(String content, Content c1, Content c2){
        super(content);
        _c1 = c1;
        _c2 = c2;
    }

   
    public Content getFirstContent(){
        return _c1;
    }

 
    public Content getSecondContent(){
        return _c2;
    }
}
