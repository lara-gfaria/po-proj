package xxl;

import xxl.functions.binary.DoMul;
import xxl.functions.binary.DoSub;
import xxl.functions.binary.DoAdd;
import xxl.functions.binary.DoDiv;
import xxl.functions.interval.DoAverage;
import xxl.functions.interval.DoCoalesce;
import xxl.functions.interval.DoConcat;
import xxl.functions.interval.DoProduct;


public interface Visitor{

    String visitStr(LiteralStr str);
    String visitInt(LiteralInt inte);
    String visitRef(Ref ref);

    String visitAdd(DoAdd add);
    String visitMul(DoMul mul);
    String visitDiv(DoDiv div);
    String visitSub(DoSub sub);
    
    String visitCoa(DoCoalesce coa);
    String visitConc(DoConcat conc);
    String visitAve(DoAverage ave);
    String visitPro(DoProduct pro); 
      
}
