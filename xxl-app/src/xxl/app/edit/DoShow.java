package xxl.app.edit;

import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import xxl.Spreadsheet;
import xxl.StorageOne;
import xxl.exceptions.UnrecognizedEntryException;
import xxl.Calculator;
import xxl.Cell;


/**
 * Class for searching functions.
 */

class DoShow extends Command<Spreadsheet> {


    DoShow(Spreadsheet receiver) {
        super(Label.SHOW, receiver);
        addStringField("address", Prompt.address());
    }
    
    @Override
    protected final void execute() throws CommandException  {
        try{
            _display.popup(_receiver.visualizer(stringField("address")));
        }
        catch (UnrecognizedEntryException e){
            throw new InvalidCellRangeException(e.getEntrySpecification());
        }
    }

}
