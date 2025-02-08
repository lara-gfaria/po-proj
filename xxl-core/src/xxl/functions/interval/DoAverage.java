package xxl.functions.interval;

import java.util.List;
import xxl.Cell;
import xxl.Visitor;


public class DoAverage extends Interval {
    public DoAverage(String content, List<Cell> intervalContents){
        super(content, intervalContents);
    }

    @Override
    public String accept(Visitor visitor) {
        return visitor.visitAve(this);
    }
}
