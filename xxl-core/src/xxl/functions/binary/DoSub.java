package xxl.functions.binary;

import xxl.Content;
import xxl.Visitor;

public class DoSub extends Binary{

    public DoSub(String specs, Content c1, Content c2){
        super(specs, c1, c2);
    }


    @Override
    public String accept(Visitor v) {
        return v.visitSub(this);
    }
}