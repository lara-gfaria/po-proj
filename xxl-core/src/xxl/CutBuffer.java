package xxl;

/*classe que guarda os conteudos que estao a ser copiados*/
public class CutBuffer{
    private StorageOne _cutbuffer;
    private int _line;
    private int _column;
    
    
    public CutBuffer(Cell[] array, int line, int column){
        _cutbuffer = new StorageOne(line, column);
        _column = column;
        _line = line;

        int n=0;
        for(int i=1; i<=line; i++){
            for(int j=1; j<=column; j++){
                _cutbuffer.setCell(i, j, array[n]);
                n++;
            }
        }
    }

    public StorageOne getCutBuffer(){
        return _cutbuffer;
    }

    public int getCutBufferLine(){
        return _line;
    }

    public int getCutBufferColumn(){
        return _column;
    }


    @Override
    public String toString(){
        String showString = "";

        if(_cutbuffer != null){
            for(int i=1; i<=_line; i++){
                for(int j=1; j<=_column; j++){
                    if(_cutbuffer.getCell(i, j).getContent() == null)
                        showString += i + ";" + j + "|" + "" +"\n";
                    else
                        showString += i + ";" + j + "|" + _cutbuffer.getCell(i, j).toString() + "\n";
                }
            }
            return showString.substring(0, showString.length()-1);
        }

        else
            return showString;
    }   
} 