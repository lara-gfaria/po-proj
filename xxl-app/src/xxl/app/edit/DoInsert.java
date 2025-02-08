package xxl.app.edit;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import xxl.Spreadsheet;

import xxl.exceptions.UnrecognizedEntryException;
import xxl.StorageOne;
import xxl.Calculator;
import xxl.Cell;


/**
 * Class for inserting data.
 */
class DoInsert extends Command<Spreadsheet> {

    DoInsert(Spreadsheet receiver) {
        super(Label.INSERT, receiver);
        addStringField("address", Prompt.address());
        addStringField("content", Prompt.content());
    }

    @Override
    protected final void execute() throws CommandException {
        try{
            _receiver.insertContents(stringField("address"), stringField("content"));

        } catch (UnrecognizedEntryException e){
            new UnrecognizedEntryException(e.getEntrySpecification());
        }
    }

}
