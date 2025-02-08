package xxl.app.main;

import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import xxl.Calculator;

import java.io.FileNotFoundException;
import java.io.IOException;

import xxl.exceptions.UnavailableFileException;


/**
 * Open existing file.
 */
class DoOpen extends Command<Calculator> {

    DoOpen(Calculator receiver) {
        super(Label.OPEN, receiver);
    }

    @Override
    protected final void execute() throws CommandException {
       
       try {// se n null ver changes 
            if(_receiver.getSpreadsheet() != null){
                if ((_receiver.getSpreadsheet().hasChanged()) && Form.confirm(Prompt.saveBeforeExit())){
                    DoSave save = new DoSave(_receiver);
                    save.execute();
                }
            }
            String filename = Form.requestString(Prompt.openFile());
            _receiver.load(filename);
        } catch (UnavailableFileException e) {
            throw new FileOpenFailedException(e);
        }
        
    }

}