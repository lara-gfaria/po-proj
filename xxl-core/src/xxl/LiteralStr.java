package xxl;

/**classe para um tipo de conteudo */
public class LiteralStr extends Content{
    private String _string;


    public LiteralStr(String content){
        super(content);
        _string = content;
    }

    @Override
    public String toString(){
        return _string;
    }

    @Override
    public String accept(Visitor v) {
        return v.visitStr(this);
    }

}