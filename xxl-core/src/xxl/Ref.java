package xxl;

/**classe para um tipo de conteudo */
public class Ref extends Content{
    private Cell _cell;

    public Ref(String content, Cell cell){
        super(content);
        _cell = cell;
    }

    @Override
    public String toString(){
        try{
            String[] content = _cell.toString().split("=");
            return content[0] + getcontentString();

        } catch (Exception e){
            return "#VALUE" + getcontentString();

        }
    }

    @Override
    public String accept(Visitor visitor) {
        return visitor.visitRef(this);
    }
}
