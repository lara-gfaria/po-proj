package xxl;

import java.util.List;
import java.util.ArrayList;


public class User {
    String _name = "root";
    private List<Spreadsheet> _arraySpreadsheets = null;
    
    public User(){
        _arraySpreadsheets = new ArrayList<>();
    }

    public void addSpreadshet(Spreadsheet spread){
        _arraySpreadsheets.add(spread);
    }

}