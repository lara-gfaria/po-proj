package xxl.functions.interval;

import xxl.Function;
import java.util.List;
import xxl.Cell;


public abstract class Interval extends Function{

    private List<Cell> _intervalContents;

    public Interval(String content, List<Cell> intervalContents){
        super(content);
        _intervalContents = intervalContents;
    }

    public List<Cell> getListContents(){
        return _intervalContents;
    }
}