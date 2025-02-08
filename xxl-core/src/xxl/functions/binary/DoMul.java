package xxl.functions.binary;

import xxl.Content;
import xxl.Visitor;

public class DoMul extends Binary{

    
    public DoMul(String specs, Content c1, Content c2){
        super(specs, c1, c2);
    }

    @Override
    public String accept(Visitor visitor) {
        return visitor.visitMul(this);
    }
}