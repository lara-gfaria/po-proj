package xxl;

import java.io.IOException;
import java.io.FileNotFoundException;

import java.io.ObjectOutputStream;

import java.io.FileReader;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;

import xxl.exceptions.ImportFileException;
import xxl.exceptions.MissingFileAssociationException;
import xxl.exceptions.UnavailableFileException;
import xxl.exceptions.UnrecognizedEntryException;


/**
 * classe que representa a aplicacao da spreadsheet
 * implementa as funcoes da spreadsheet
 * 
 */
public class Calculator {

    /* spreadsheet onde serao implementadas as funcoes*/
    private Spreadsheet _spreadsheet = null;

    /* nome do ficheiro*/
    private String _filename = ""; 

    /*user da calculator */
    private User _currentUser = new User();

    /**
     * muda a spreadsheet atual pela recebida
     * @param newSpreadsheet
     */
    public void setSpreadsheet(Spreadsheet newSpreadsheet){
        _spreadsheet = newSpreadsheet;
        _spreadsheet.addUser(_currentUser);
    }

    /**
     * retorna a spreadsheet atual
     * @return
     */
    public Spreadsheet getSpreadsheet() {
        return _spreadsheet;
    }


    /**
     * salva a serializacao do estado da aplicaçaao dentro do ficheiro associado a spreadsheet atual
     * @throws FileNotFoundException se por alguma razao o ficheiro nao puder ser criado ou aberto
     * @throws MissingFileAssociationException se a atual spreadsheet nao tiver um ficheiro
     * @throws IOException se houver algum erro de serializacao 
     */
    public void save() throws FileNotFoundException, MissingFileAssociationException, IOException {
		if (_filename == "") {
			throw new MissingFileAssociationException();
		}

		try (ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(_filename)))) {
			oos.writeObject(_spreadsheet);
            oos.close();
			_spreadsheet.setChanged(false);
		} 
    }

    /**
      * salva a serializacao do estado da aplicacao dentro do ficheiro especifico. 
      A spreadsheet atual e associada a um ficheiro
      * @param filename o nome do ficheiro
      * @throws FileNotFoundException se por alguma razao o ficheiro nao puder ser criado ou aberto
      * @throws MissingFileAssociationException se a spreadsheet atual nao tiver um ficheiro
      * @throws IOException se houver algum erro de serializacao no estado da spreadsheet
      */
    public void saveAs(String filename) throws FileNotFoundException, MissingFileAssociationException, IOException {
        _filename = filename;
        save();
    }


    /**
     * 
     * @param filename
     * @throws UnavailableFileException
     */
    public void load(String filename) throws UnavailableFileException {
        try (ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filename)))) {
			_filename = filename;
			_spreadsheet = (Spreadsheet) ois.readObject();
			_spreadsheet.setChanged(false);
		} catch (IOException | ClassNotFoundException e) {
			throw new UnavailableFileException(filename);
		}
    }


    /**
     * le o texto do input do ficheiro e cria as entidades referidas
     * @param filename
     * @throws ImportFileException
     */
    public void importFile(String filename) throws ImportFileException {
        if (filename == null || filename.equals(""))
            return;
        
        try(BufferedReader in = new BufferedReader(new FileReader(filename))){
            String file;

            //le as primeiras duas linhas para obter o tamanho da spreadsheet
            String firstLine = in.readLine();
            String[] lines = firstLine.split("=");
            int numLines = Integer.parseInt(lines[1]);
            String secondLine = in.readLine();
            String[] columns = secondLine.split("=");
            int numColumns = Integer.parseInt(columns[1]);
            _spreadsheet = new Spreadsheet(numLines, numColumns);
            _spreadsheet.setfileName(filename);
            
            //percorre o ficheiro recebido para inserir o conteudo que e recebido
            //nas linhas seguinte
            while ((file = in.readLine()) != null){
                String[] fields = file.split("\\|");
                if(fields.length == 2)
                    _spreadsheet.insertContents(fields[0], fields[1]);
            }
        } catch (IOException | UnrecognizedEntryException e){
            throw new ImportFileException(filename, e);
        }
    }    
}
