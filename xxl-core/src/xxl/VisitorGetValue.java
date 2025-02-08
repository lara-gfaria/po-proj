package xxl;

import java.util.List;

import xxl.functions.binary.DoMul;
import xxl.functions.binary.DoSub;
import xxl.functions.binary.DoAdd;
import xxl.functions.binary.DoDiv;
import xxl.functions.interval.DoAverage;
import xxl.functions.interval.DoCoalesce;
import xxl.functions.interval.DoConcat;
import xxl.functions.interval.DoProduct;


public class VisitorGetValue implements Visitor{

    public String visitStr(LiteralStr str){
        return str.toString();        
    }

    public String visitInt(LiteralInt inte){
        return inte.toString();        
    }

    public String visitRef(Ref ref){
        return ref.toString();    
    }

    public String visitAdd(DoAdd add){
        Visitor visitor = new VisitorGetValue();

        String firstContent = add.getFirstContent().accept(visitor);
        String secondContent = add.getSecondContent().accept(visitor);
        
        String[] value1 = firstContent.split("=");
        String[] value2 = secondContent.split("=");

        try{

            int v1 = Integer.parseInt(value1[0]);
            int v2 = Integer.parseInt(value2[0]);
             
            int result = v1 + v2;
            return result + "";

        } catch(NumberFormatException e){return "#VALUE";}
 
    }

    public String visitSub(DoSub sub){
        Visitor visitor = new VisitorGetValue();

        String firstContent = sub.getFirstContent().accept(visitor);
        String secondContent = sub.getSecondContent().accept(visitor);
        
        String[] value1 = firstContent.split("=");
        String[] value2 = secondContent.split("=");
         
        try{
            int v1 = Integer.parseInt(value1[0]);
            int v2 = Integer.parseInt(value2[0]);
             
            int result = v1 - v2;
            return result + "";
        } catch(NumberFormatException e){return "#VALUE";}
     
    }
    
    public String visitMul(DoMul mul){
        Visitor visitor = new VisitorGetValue();

        String firstContent = mul.getFirstContent().accept(visitor);
        String secondContent = mul.getSecondContent().accept(visitor);
        
        String[] value1 = firstContent.split("=");
        String[] value2 = secondContent.split("=");
        try{
            int v1 = Integer.parseInt(value1[0]);
            int v2 = Integer.parseInt(value2[0]);
    
            int result = v1 * v2;
            return result + "";
        }
        catch(NumberFormatException e){return "#VALUE";}

    }

    public String visitDiv(DoDiv div){
        Visitor visitor = new VisitorGetValue();

        String firstContent = div.getFirstContent().accept(visitor);
        String secondContent = div.getSecondContent().accept(visitor);
        
        String[] value1 = firstContent.split("=");
        String[] value2 = secondContent.split("=");

        try{
            int v1 = Integer.parseInt(value1[0]);
            int v2 = Integer.parseInt(value2[0]);
            if (v2 == 0)
                return "#VALUE";

            int result = v1 / v2;
            return result + "";
        }
        catch(NumberFormatException e){return "#VALUE";}   
    }


    public String visitConc(DoConcat conc){
        List <Cell> intervalContents = conc.getListContents();
        int numCells = intervalContents.size();
        String result = "'";
        
        
        for (int i=0; i < numCells; i++){
            Content cellContent = intervalContents.get(i).getContent();

            if(cellContent != null){
                if ((cellContent.toString()).charAt(0) == '\''){
                    String content = (cellContent.toString()).substring(1);
                    result += content;
                }
            }
        }

        return result;        
    }
    
    public String visitCoa(DoCoalesce coa){
        List <Cell> intervalContents = coa.getListContents();
        int numCells = intervalContents.size();
    
        for (int i=0; i < numCells; i++){
            Content cellContent = intervalContents.get(i).getContent();

            if(cellContent != null){
                if ((cellContent.toString()).charAt(0) == '\''){
                    return cellContent.toString();
                }
            }
        }
        return "";       
    }

    public String visitAve(DoAverage ave){
        Visitor visitor = new VisitorGetValue();

        List <Cell> intervalContents = ave.getListContents();
        int numCells = intervalContents.size();
        int total = 0;

        try{
            for (int i=0; i < numCells; i++){
                Content cellContent = intervalContents.get(i).getContent();
                if(cellContent == null)
                    return "#VALUE";
                total += Integer.parseInt(cellContent.accept(visitor));
            }

            int result = total / numCells;
            return result + "";


        } catch(NumberFormatException e){
            return "#VALUE";
        }     
    }

    public String visitPro(DoProduct pro){
        Visitor visitor = new VisitorGetValue();

        List <Cell> intervalContents = pro.getListContents();
        int numCells = intervalContents.size();
        int total = 1;
        
        try{
            for (int i=0; i < numCells; i++){
                Content cellContent = intervalContents.get(i).getContent();
                if(cellContent == null)
                    return "#VALUE";
                total *= Integer.parseInt(cellContent.accept(visitor));
            }
            return total + "";
        } 
        catch(NumberFormatException e){
            return "#VALUE";
        }    
    }


    
}
