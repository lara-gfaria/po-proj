package xxl;

/**classe abstrata que trata dos tipos de funcao que o conteudo de uma cell pode ter */
public abstract class Function extends Content{

    public Function(String content){
        super(content);
    }


    @Override
    public String toString(){
        Visitor visitor = new VisitorGetValue();
        return accept(visitor) +  getcontentString();
    }

}