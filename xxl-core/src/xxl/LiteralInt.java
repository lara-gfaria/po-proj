package xxl;

/**classe para um tipo de conteudo */
public class LiteralInt extends Content{
    private int _nro;

    /**
     * Construtor do conteudo LiteralInt.
     * @param content
     * @param num
     */
    public LiteralInt(String content, int num){
        super(content);
        _nro = num;
    }


    @Override
    public String toString(){
        return _nro + "";
    }


    @Override
    public String accept(Visitor v) {
        return v.visitInt(this);
    }
    

}