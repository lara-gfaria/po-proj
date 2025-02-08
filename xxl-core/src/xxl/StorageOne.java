package xxl;

/**classe da estrutura de armazenamento do tipo lista de lista de cells da spreadsheet*/
public class StorageOne extends Storage { 
  
  private Cell [][] _arrayCells;


  public StorageOne(int maxLin, int maxCol){
    _arrayCells = new Cell[maxLin][maxCol];
    for(int i=0; i <maxLin; i++){
      for(int j=0; j <maxCol; j++){
        _arrayCells[i][j] = new Cell();
      }
    }
  }  



  public void setCell(int line, int column, Cell cell){
    if (line >= 0 && line <= _arrayCells.length && column >= 0 && column <= _arrayCells[0].length){
      _arrayCells[line-1][column-1] = cell;
    }
  }

  
  public Cell getCell(int line, int column){
    return _arrayCells[line-1][column-1];
  }
}