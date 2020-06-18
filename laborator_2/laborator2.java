package riw;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class laborator_2 
{
	private static final String calea_fisiere_text = "C:\\Users\\dgorc\\Desktop\\Laboratoare_RIW\\laborator_2\\riwtxt\\fisier.txt";
	
	private HashMap<String, Integer> exceptii = null;
    private HashMap<String, Integer> stopWords = null;
	
	public void setExceptions(HashMap<String, Integer> exceptii)
    {
        this.exceptii = exceptii;
    }

    public void setStopWords(HashMap<String, Integer> stopWords) 
    {
        this.stopWords = stopWords;
    }
	
	public static HashMap<String, Integer> getCuvante_Din_Fisier(File file)
    {
        HashMap<String, Integer> words = new HashMap<>();
        try 
        {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = "";
            while((line = bufferedReader.readLine()) != null)
            {
                words.put(line.toLowerCase().trim(), 1);
            }
            bufferedReader.close();
            fileReader.close();
        } 
        catch (IOException e) 
        {
            System.out.println("Eroare la citirea fisierului: " + file.getName());
            e.printStackTrace();
        }
        return words;
    }
	
	public static String getText(File file)
    {
        FileReader fileReader;
        
        try 
        {
            fileReader = new FileReader(file);            
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String text = "";
            String line = "";
            line = bufferedReader.readLine();
            
            while(line != null)
            {
                text += line + " ";
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
            fileReader.close();
            return text;
        }
        catch (IOException e) 
        {
            // TODO Auto-generated catch block
            System.out.println("Eroare la citirea fisierului: " + file.getName());
            e.printStackTrace();
        }
        
        return ""; 
    }
	
	private void parseFisier(File file)
    {
        String text = "";
		text.getText(file);
        
        text = text.trim();
       
        if(text.length() == 0)
        {
            return;
        }
       
        text = text.replaceAll("\\s{2,}", " ");
        
        String[] cuvinte = text.toLowerCase().split("[^a-zA-Z0-9]");
        
        HashMap<String, Integer> frecventa_cuvinte = new HashMap<>();
        for(String cuvant : cuvinte)
        {
            if(cuvant.length() > 0) 
            {
                if(frecventa_cuvinte.containsKey(cuvant)) 
                {
                    frecventa_cuvinte.replace(cuvant, frecventa_cuvinte.get(cuvant) + 1);
                }
                else
                {
                    if(!stopWords.containsKey(cuvant)) 
                    {
                        frecventa_cuvinte.put(cuvant, 1);
                    }
                    else if(exceptii.containsKey(cuvant))
                    {
                        frecventa_cuvinte.put(cuvant, 1);
                    }
                }
            }
        }
	
	public static void main(String[] args) 
	{
		setStopWords(getCuvante_Din_Fisier(new File("stopWords.txt"))); 
		setExceptions(getCuvante_Din_Fisier(new File("exceptii.txt")));
		
		parseFisier(calea_fisiere_text);
	}	
}
