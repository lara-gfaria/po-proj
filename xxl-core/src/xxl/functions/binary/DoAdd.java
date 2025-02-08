package xxl.functions.binary;

import xxl.Content;
import xxl.Visitor;

public class DoAdd extends Binary{

  
    public DoAdd(String specs, Content c1, Content c2){
        super(specs, c1, c2);
    }
    

    @Override
    public String accept(Visitor visitor) {
        return visitor.visitAdd(this);
    }
}