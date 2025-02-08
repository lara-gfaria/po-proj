package xxl;
import xxl.functions.binary.DoAdd;
import xxl.functions.binary.DoDiv;
import xxl.functions.binary.DoMul;
import xxl.functions.binary.DoSub;
import xxl.functions.interval.DoAverage;
import xxl.functions.interval.DoCoalesce;
import xxl.functions.interval.DoConcat;
import xxl.functions.interval.DoProduct;
import xxl.Cell;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;

import xxl.exceptions.UnrecognizedEntryException;


public class Cell implements Serializable{

    @Serial
    private static final long serialVersionUID = 202308312359L;

    private Content _content;


    public Cell(){
        _content = null;
    }


    public Cell(Cell cell){
        _content = cell.getContent();
    }


    public Content getContent(){
        return _content;
    }


    public void setContent(Content content){
        _content = content;
    }

    
    public Content getCell (Cell cel){
        return _content;
    }

    
    public void setCell (Cell cell, Content c){
        _content = c;
    }


/**dependendo do tipo de argumentos que recebe vai mudar o tipo de conteudo
 * para o conteudo expecifico
 */
    
    /**
     * muda o tipo de conteudo para uma Referencia
     * 
     * @param content
     * @param cell
     * 
     */
    public void setType(String content, Cell cell){
        _content = new Ref(content, cell);
    }

    
    /**
     * muda o tipo de conteudo para um LiteralInt
     * 
     * @param content
     * @param valor
     * 
     */
    public void setType(String content, int valor){
        _content = new LiteralInt(content, valor);
    }


    /**
     * muda o tipo do conteudo para um LiteralStr
     * 
     * @param content
     * 
     */
    public void setType(String content){
        _content = new LiteralStr(content);
    }
    
    /**
     * muda o tipo do conteudo dependendo da operacao recebida na string content
     * 
     * @param content
     * @param c1
     * @param c2
     * 
     */
    public void setType(String content, Content c1, Content c2) throws UnrecognizedEntryException{
        String[] newcontent = content.split("\\(");

        if (newcontent[0].equals("=ADD")){
            _content = new DoAdd(content, c1, c2);

        }
        else if(newcontent[0].equals("=SUB")){
            _content = new DoSub(content, c1, c2);
        }
        else if(newcontent[0].equals("=DIV")){
            _content = new DoDiv(content, c1, c2);
        }
        else if(newcontent[0].equals("=MUL")){
            _content = new DoMul(content, c1, c2);
        }
        else{
            throw new UnrecognizedEntryException(newcontent[0].substring(1));
        }
    }

    /**
     * muda o tipo do conteudo dependendo da operacao recebida na string content
     * @param content
     * @param intervalContents
     * @throws UnrecognizedEntryException
     */
    public void setType(String content, List<Cell> intervalContents) throws UnrecognizedEntryException{
        String[] newcontent = content.split("\\(");

        if (newcontent[0].equals("=AVERAGE")){
            _content = new DoAverage(content, intervalContents);
        }
        else if(newcontent[0].equals("=PRODUCT")){
            _content = new DoProduct(content, intervalContents);
        }
        else if(newcontent[0].equals("=CONCAT")){
            _content = new DoConcat(content, intervalContents);
        }
        else if(newcontent[0].equals("=COALESCE")){
            _content = new DoCoalesce(content, intervalContents);
        }
        else{
            throw new UnrecognizedEntryException(newcontent[0].substring(1));
        }
    }
    
    @Override
    public String toString(){
        return _content.toString();       
    }
    

}
