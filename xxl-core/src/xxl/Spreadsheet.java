package xxl;

import xxl.exceptions.UnrecognizedEntryException;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import java.util.Collections;
import java.util.Comparator;


/**
 * Classe que representa a spreadsheet.
 */
public class Spreadsheet implements Serializable{

    /*Numero de serie da classe */
    @Serial
    private static final long serialVersionUID = 202308312359L;

    /*estrutura de armazenamento */
    private StorageOne _storage;

    /*linha final da spreadsheet */
    private int _line;

    /*coluna final da spreadsheet */
    private int _column;

    /*nome do ficheiro da spreasheet */
    private String _filename = "";

    /*boolean que diz se a spreadsheet tem alteracoes nao guardadas */
    private boolean _changed = true;

    /*armazenamento do cutBuffer */
    private CutBuffer _cutBuffer = null;

    /*users da spreadsheet */
    private List<User> _users = new ArrayList<>();

    /*Construtor da Spreadsheet */
    public Spreadsheet(int line, int column){
        _line = line;
        _column = column;
        _storage = new StorageOne(line, column);
        _changed = true;
    
    }

    /**
     * 
     * @return String com o nome do ficheiro.
     */
    public String getfileName(){
        return _filename;
    }

    /**
     * 
     * @param name
     * Muda o nome da variavel que estava pelo nome dado.
     */
    public void setfileName(String name){
        _filename = name;
    }
    
    /**
     * 
     * @return line final que delimita a Spreadsheet.
     */
    public int getLine(){
        return _line;
    }

    /**
     * 
     * @return column final que delimita a Spreadsheet.
     */
    public int getColumn(){
        return _column;
    }

    /**
     * 
     * @return armazenamento das celulas da spreadsheet
     */
    public StorageOne getStorage(){
        return _storage;
    }
    
    /**
     * 
     * @return cutBuffer da spreadsheet
     */
    public CutBuffer getCutBuffer(){
        return _cutBuffer;
    }
    
    /**
     * adiciona um user a spreadsheet
     * 
     * @param user
     */
    public void addUser(User user){
        _users.add(user);
    }


    /**
     * muda o tamanho da spreadsheet
     * 
     * @param line
     * @param column
     * @param spread
     * 
     */
    public void changeSize(int line, int column, Spreadsheet spread){
        spread._line = line;
        spread._column = column;
    }


    /**
     * elimina o conteudo do address de uma celula
     * 
     * @param address
     */
    public void deleteContent(String address){
        String[] interval = address.split(":"); 

        //caso seja apenas uma celula
        if (interval.length == 1){
            String[] local = address.split(";");

            int line = Integer.parseInt(local[0]);
            int column = Integer.parseInt(local[1]);

            Cell cell = _storage.getCell(line, column);
            cell.setContent(null);
        }

        //caso seja um intervalo
        else{
            String firstCell[] = interval[0].split(";");
            String lastCell[] = interval[1].split(";");
            
            int line1 = Integer.parseInt(firstCell[0]);
            int column1 = Integer.parseInt(firstCell[1]);
            int line2 = Integer.parseInt(lastCell[0]);
            int column2 = Integer.parseInt(lastCell[1]);

            if (line1 == line2){
                for(int i=column1; i<=column2; i++){
                    Cell cell = _storage.getCell(line1, i);
                    cell.setContent(null);
                }
            }
            else if(column1 == column2){
                for (int i = line1; i<= line2; i++){
                    Cell cell = _storage.getCell(i, column1);
                    cell.setContent(null);
                }
            }
        }
    }

    
    /**
     * insere um conteudo especifico numa celula
     *
     * @param range
     * @param content
     */
    public void insertContents(String range, String content) throws UnrecognizedEntryException {
        String[] interval = range.split(":");
        
        if (interval.length == 1){
            String[] local = range.split(";");
            int line = Integer.parseInt(local[0]);
            int column = Integer.parseInt(local[1]);
            typeContent(line, column, content);
        }

        else{
            String firstCell[] = interval[0].split(";");
            String lastCell[] = interval[1].split(";");
            
            int line1 = Integer.parseInt(firstCell[0]);
            int column1 = Integer.parseInt(firstCell[1]);
            int line2 = Integer.parseInt(lastCell[0]);
            int column2 = Integer.parseInt(lastCell[1]);
            

            if (line1 == line2){
                for(int i=column1; i<=column2; i++){
                    typeContent(line2, i, content);
                }
            }
            else if(column1 == column2){
                for (int i = line1; i<= line2; i++){
                    typeContent(i, column1, content);
                }
            }
            
        }
    }
    

    /**
     * muda o conteudo da celula
     * 
     * @param line
     * @param column
     * @param content
     */
    public void typeContent(int line, int column, String content) throws UnrecognizedEntryException{

        //vai a storage buscar a celula da spreadsheet na linha e coluna dadas
        Cell cell = _storage.getCell(line, column);
        
        //se o conteudo começa com "="
        if (content != null){
            if (content.charAt(0) == '='){
                if(Character.isDigit(content.charAt(1))){
                    //Caso seja uma referencia
                    String[] reference = content.split(";");
                    int lineRef = Integer.parseInt(reference[0].substring(1));
                    int columnRef = Integer.parseInt(reference[1]);
                    cell.setType(content, _storage.getCell(lineRef, columnRef));
                }
    
                else if(Character.isLetter(content.charAt(1))){
                    //Caso seja uma funcao
                    String[] newContent = content.split("\\(");
                    String functionContent = newContent[1];
                    
                    String[] binaryContents = functionContent.split(",");
                    
                    if (binaryContents.length == 2){
                        //caso seja uma funcao binaria
                        String firstContent = binaryContents[0];
                        String _secondContent = binaryContents[1];
                        String[] secondContent = _secondContent.split("\\)");
                        
                        // no caso do conteudo serem dois inteiros
                        if(firstContent.split(";").length == 1 && secondContent[0].split(";").length == 1){
                            Content c1 = new LiteralInt(firstContent, Integer.parseInt(firstContent));
                            Content c2 = new LiteralInt(secondContent[0], Integer.parseInt(secondContent[0]));
                            cell.setType(content, c1, c2);
                        }
                        
                        // no caso do conteudo ser uma referencia e um inteiro
                        else if(firstContent.split(";").length != 1 && secondContent[0].split(";").length == 1){
                            String[] newfirstContent = firstContent.split(";");
                            int lineRef = Integer.parseInt(newfirstContent[0]);
                            int columnRef = Integer.parseInt(newfirstContent[1]);
                            
                            Content c1 = new Ref("=" + firstContent, _storage.getCell(lineRef, columnRef));
                            Content c2 = new LiteralInt(secondContent[0], Integer.parseInt(secondContent[0]));
                            cell.setType(content, c1, c2);
                        }
                        
                        // no caso do conteudo ser um inteiro e uma referencia
                        else if(firstContent.split(";").length == 1 && secondContent[0].split(";").length != 1){            
                            String newsecondContent[] = secondContent[0].split(";");
                            int lineRef = Integer.parseInt(newsecondContent[0]);
                            int columnRef = Integer.parseInt(newsecondContent[1]);
                            
                            Content c1 = new LiteralInt(firstContent, Integer.parseInt(firstContent));
                            Content c2 = new Ref("=" + secondContent[0], _storage.getCell(lineRef, columnRef));
                            cell.setType(content, c1, c2);
                        }
                        
                        // no caso do conteudo serem duas referencias
                        else if(firstContent.split(";").length != 1 && secondContent[0].split(";").length != 1){
                            String newfirstContent[] = firstContent.split(";");
                            String newsecondContent[] = secondContent[0].split(";");
                            
                            int line1 = Integer.parseInt(newfirstContent[0]);
                            int column1 = Integer.parseInt(newfirstContent[1]);
                            int line2 = Integer.parseInt(newsecondContent[0]);
                            int column2 = Integer.parseInt(newsecondContent[1]);
                            
                            Content c1 = new Ref("=" + firstContent, _storage.getCell(line1, column1));
                            Content c2 = new Ref("=" + secondContent[0], _storage.getCell(line2, column2));
                            cell.setType(content, c1, c2);   
                        }
                    }
    
                    //caso seja uma funçao de intervalos
                    else{
                        String[] interval = functionContent.split(":");
                        String ref1 = interval[0];
                        String ref2 = interval[1].split("\\)")[0];
                        
                        String firstContent[] = ref1.split(";");
                        String secondContent[] = ref2.split(";");
    
                        int line1 = Integer.parseInt(firstContent[0]);
                        int column1 = Integer.parseInt(firstContent[1]);
                        int line2 = Integer.parseInt(secondContent[0]);
                        int column2 = Integer.parseInt(secondContent[1]);
    
                        List <Cell> intervalContents = new ArrayList<>();
    
                        if (line1 == line2){
                            for(int i=column1; i<=column2; i++){
                                Cell currentCell = _storage.getCell(line1, i);
                                intervalContents.add(currentCell);
                            }
                        }
                        else if(column1 == column2){
                            for (int i = line1; i<= line2; i++){
                                Cell currentCell = _storage.getCell(i, column2);
                                intervalContents.add(currentCell);
                            }
                        }
                        cell.setType(content, intervalContents);
                    }
                    
                }
            }
            
            // Caso seja um inteiro
            else if(content.charAt(0) == '-' || Character.isDigit(content.charAt(0))){
                cell.setType(content, Integer.parseInt(content));
            }
            
            // Caso seja uma String
            else if(content.charAt(0) == '\'')
                cell.setType(content);
            }
            
        }
        
    /**
     * se a spreadsheet sofrer alteracoes, o changed muda o seu estado
     * @param changed
     */
    public void setChanged(boolean changed) {
        _changed = changed;
    }
    
    /**
     * retorna o estado atual do changed
     * @return
     */
    public boolean hasChanged() {
        return _changed;
    }
    
    /**
     * retorna a linha do endereço em string recebido
     * @param str
     * @return inteiro da line da celula representada na string
     */
     public int strGetLine(String str){
         String cel[] = str.split(";");
         int lineCel= Integer.parseInt(cel[0]);
         return lineCel;
        }
        
        /**
        * retorna a coluna do endereço em string recebido
         * @param str
         * @return inteiro da coluna da celula representada na string
         */
        public int strGetCol(String str){
        String cel[] = str.split(";");
        int colCel= Integer.parseInt(cel[1]);
        return colCel;
    }
    

    /**
     * verifica se a celula pertence a spreadsheet
     * @param lineCell
     * @param columnCell
     * @return boolean se a celula e valida ou nao
     */
    public boolean isCellSpreadsheet (int lineCell,int columnCell){
        if(lineCell<=_line && columnCell<=_column && lineCell >= 1 && columnCell>= 1){
            return true;
        }
        return false;
    }
    
    
    /**
     * retorna o conteudo da string (celula ou intervalo) recebidos
     * @param str
     * @return string com o valor do conteudo de cada celula
     * @throws UnrecognizedEntryException
     */
    public String visualizer(String str) throws UnrecognizedEntryException{
        String cels[] = str.split(":");

        //e uma referencia    
        if(cels.length == 1 ){      
            int line=strGetLine(str);
            int column=strGetCol(str);
            if(isCellSpreadsheet (line, column)){            
                Cell cel =  _storage.getCell(line, column);
                if (cel.getContent() == null)
                    return line +";"+ column+"|"+"";
                    else
                    return line +";"+ column+"|"+ cel.toString(); 
                }
            else 
            throw new UnrecognizedEntryException(str);        
        }

        //e um intervalo
        else if(cels.length == 2){ 
            int line1=strGetLine(cels[0]);
            int column1=strGetCol(cels[0]);
            
            int line2=strGetLine(cels[1]);
            int column2=strGetCol(cels[1]);
            
            String strIntervalo = "";
            
            if(isCellSpreadsheet (line1, column1) && isCellSpreadsheet (line2, column2)){
                
                if(line1==line2){
                    for (int i=column1; i <= column2; i++){
                        Cell cel =  _storage.getCell(line1, i);
                        if (cel.getContent() == null)
                        strIntervalo += line1 +";"+ i +"|"+"";
                        else
                        strIntervalo += line1 +";"+ i +"|"+cel.toString();
                        
                        if (i != column2)
                        strIntervalo += "\n";
                    }
                    return strIntervalo;
                }
                else if(column1==column2){
                    for (int i=line1; i <= line2; i++){
                        Cell cel =  _storage.getCell(i, column1);
                        if (cel.getContent() == null)
                        strIntervalo += i +";"+ column1 +"|"+"";
                        else
                        strIntervalo += i +";"+ column1 +"|"+cel.toString(); 
                        
                        if (i != line2)
                        strIntervalo += "\n";
                    }
                    return strIntervalo;   
                }
                else
                    throw new UnrecognizedEntryException(str);
            }
            else
                throw new UnrecognizedEntryException(str);
        } 
        else
            throw new UnrecognizedEntryException(str);
    }

        
    /**
     * corta o conteudo de uma celula para o cutbuffer
     * @param address
     * @throws UnrecognizedEntryException
     */
    public void cut(String address) throws UnrecognizedEntryException{
        copy(address);
        deleteContent(address);
    }

    /**
     * copia o conteudo de uma celula para o cutbuffer
     * @param address
     * @throws UnrecognizedEntryException
     */
    public void copy(String address) throws UnrecognizedEntryException{
        Cell [] ArrayFinal;
        
        String [] interval = address.split(":");
            
        //é cell devolve o array de 1 cell
        if (interval.length == 1){
            ArrayFinal = new Cell[1];

            String[] local = address.split(";");
            int line = Integer.parseInt(local[0]);
            int column = Integer.parseInt(local[1]);

            if(isCellSpreadsheet(line, column)){
                Cell cell = new Cell(_storage.getCell(line, column));            
                ArrayFinal[0] = cell;
                _cutBuffer = new CutBuffer(ArrayFinal, 1, 1);
            }
            else 
                throw new UnrecognizedEntryException(address);
            }
            
            
            //é range 
        else{
            String firstCell[] = interval[0].split(";");
            String lastCell[] = interval[1].split(";");
            
            int line1 = Integer.parseInt(firstCell[0]);
            int column1 = Integer.parseInt(firstCell[1]);
            int line2 = Integer.parseInt(lastCell[0]);
            int column2 = Integer.parseInt(lastCell[1]);

            if(isCellSpreadsheet(line1, column1) && isCellSpreadsheet(line2, column2)){
                
                if (line1 == line2){
                    ArrayFinal = new Cell[column2 - column1 +1];
                    int j=0;
                    for(int i=column1; i<=column2; i++){
                        Cell cell = new Cell(_storage.getCell(line1, i));
                        ArrayFinal[j] = cell;
                        j++;
                    }
                    _cutBuffer = new CutBuffer(ArrayFinal, 1, column2);
                }
                else if(column1 == column2){
                    ArrayFinal = new Cell[line2 - line1 +1];
                    int j=0;
                    for (int i = line1; i<= line2; i++){
                        Cell cell = new Cell(_storage.getCell(i, column1));
                        ArrayFinal[j] = cell;
                        j++;
                    }
                    _cutBuffer = new CutBuffer(ArrayFinal, line2, 1);
                }
            }
            else 
            throw new UnrecognizedEntryException(address);
            
        }
    }
    

    /**
     * cola o conteudo do cutbuffer na celula recebida
     * @param address
     */
    public void paste(String address){
        
        if(_cutBuffer.getCutBuffer() != null){  
            
            String [] interval = address.split(":");
            
            //recebe uma celula
            if (interval.length == 1){
                //é cell
                String[] local = address.split(";");
                int line = Integer.parseInt(local[0]);
                int column = Integer.parseInt(local[1]);


                if(isCellSpreadsheet(line, column)){

                    //se o conteudo do cutbuffer for de apenas uma celula
                    if(_cutBuffer.getCutBufferLine() == 1 && _cutBuffer.getCutBufferColumn()==1){
                        Cell newCell = _cutBuffer.getCutBuffer().getCell(1, 1);
                        _storage.setCell(line, column, newCell);
                    }
                    
                    //se o conteudo do cutbuffer for de um intervalo
                    //caso o intervalo seja em linha
                    if(_cutBuffer.getCutBufferLine() == 1){
                        //se der para colocar todo o conteudo no spreadsheet sem ultrapassar limites
                        if(isCellSpreadsheet(line, column -2 + _cutBuffer.getCutBufferColumn())){
                            for(int i=1; i <= _cutBuffer.getCutBufferColumn(); i++){
                                Cell newCell = _cutBuffer.getCutBuffer().getCell(1, i);
                                _storage.setCell(line, column + i -1, newCell);
                            }  
                        }                    
                    }
                    //caso o intervalo seja em coluna
                    else if(_cutBuffer.getCutBufferColumn()==1){
                        //se der para colocar todo o conteudo na spreadsheet sem ultrapassar limites 
                        if(isCellSpreadsheet(line + _cutBuffer.getCutBufferLine() -2, column )){
                            for(int i=1; i <= _cutBuffer.getCutBufferLine(); i++){
                                Cell newCell = _cutBuffer.getCutBuffer().getCell(i, 1);
                                _storage.setCell(line + i -1, column, newCell);
                            }
                        }
                    }
                }
            }

            //recebe um intervalo
            else if (interval.length == 2){
                String firstCell[] = interval[0].split(";");
                String lastCell[] = interval[1].split(";");
                
                int line1 = Integer.parseInt(firstCell[0]);
                int column1 = Integer.parseInt(firstCell[1]);
                int line2 = Integer.parseInt(lastCell[0]);
                int column2 = Integer.parseInt(lastCell[1]);

                if(isCellSpreadsheet(line1, column1) && isCellSpreadsheet(line2, column2))
                    //verificar se o intervalo e do mesmo tamanho que o conteudo que esta no cutbuffer
                    if(line2 -line1 +1 == _cutBuffer.getCutBufferLine() && column2-column1 +1 == _cutBuffer.getCutBufferColumn())
                        paste(interval[0]);
            }            
        }
    }
    
    /**
     * procura nas celulas da spreadsheet o valor recebido
     * @param value
     * @return
     */
    public String showValue(String value){
        Visitor visitor = new VisitorGetValue();
        
        String finalString = "";
        
        for (int i=1; i <= getLine(); i++){
            for(int j=1; j <= getColumn(); j++){
                Cell currentCell = _storage.getCell(i, j);
                if(currentCell.getContent()!=null){
                    String[] content = currentCell.getContent().accept(visitor).split("=");
                    
                    if(content[0].equals(value))
                        finalString +=  i + ";" + j + "|" + currentCell.toString() + "\n";  
                }
            }        
        }

        if (finalString == ""){
            return finalString;
        }
        else{
            return finalString.substring(0, finalString.length()-1);
        }       
        
    }
    
    /**
     * procura na spreadsheet as celulas com funcoes com segmentos da string recebida
     * @param value
     * @return
     */
    public String showFunction(String value) {
        List<String> resultsList = new ArrayList<>();

        for (int i = 1; i <= getLine(); i++) {
            for (int j = 1; j <= getColumn(); j++) {
                Cell currentCell = _storage.getCell(i, j);
                if (currentCell.getContent() != null) {
                    String[] content = currentCell.getContent().toString().split("\\(");
                    if (content[0].contains("=" + value)) {
                        resultsList.add(i + ";" + j + "|" + currentCell.toString());
                    }
                }
            }
        }

        //ordena a lista de acordo com o nome da função, linha e coluna
        Collections.sort(resultsList, new Comparator<String>() {

            @Override
            public int compare(String element1, String element2) {
                String[] content1 = element1.split("\\|");
                String[] content2 = element2.split("\\|");

                // Compara o nome da função
                int compareName = content1[1].compareTo(content2[1]);
                if (compareName != 0) {
                    return compareName;
                }

                // Se o nome da função for o mesmo, compara a linha
                String[] cell1 = content1[0].split(";");
                String[] cell2 = content2[0].split(";");

                int compareLine = Integer.compare(Integer.parseInt(cell1[0]), Integer.parseInt(cell2[0]));
                if (compareLine != 0) {
                    return compareLine;
                }

                // Se a linha for a mesma, compara a coluna
                return Integer.compare(Integer.parseInt(cell1[1]), Integer.parseInt(cell2[1]));
            }
        });

        //constroi a string de resultado a partir da lista ordenada
        StringBuilder finalString = new StringBuilder();
        for (int i = 0; i < resultsList.size(); i++) {
            String result = resultsList.get(i);
            finalString.append(result);
            if (i < resultsList.size() - 1) {
                finalString.append("\n");
            }
        }

        return finalString.toString().trim();
    }

}