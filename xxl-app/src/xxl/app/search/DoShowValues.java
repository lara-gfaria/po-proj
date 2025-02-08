package xxl.app.search;

import pt.tecnico.uilib.menus.Command;
import xxl.Spreadsheet;

import xxl.app.edit.InvalidCellRangeException;
import xxl.exceptions.UnrecognizedEntryException;

/**
 * Command for searching content values.
 */
class DoShowValues extends Command<Spreadsheet> {

    DoShowValues(Spreadsheet receiver) {
        super(Label.SEARCH_VALUES, receiver);
        addStringField("Value", Prompt.searchValue());
    }

    @Override
    protected final void execute(){
        _display.popup(_receiver.showValue(stringField("Value")));
    }
}
