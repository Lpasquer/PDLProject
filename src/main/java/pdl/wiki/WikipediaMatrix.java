package pdl.wiki;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Classe principale avec l'interface utilisateur
 */
public class WikipediaMatrix
{
    private static String savePath = readSaveLocation();
    private static ArrayList<Page> pages = new ArrayList<>();
    private static ArrayList<Url> urls = new ArrayList<>();

    public static void main(String[] args)
    {
    	try {
  			Scanner scan;
  			String lien;
  			scan = new Scanner(new File(System.getProperty("user.dir") + "/wikipedia_links_list.txt"));
  			while(scan.hasNext()) {
  				lien = scan.next();
  				addLink(lien);
  			}
  			scan.close();
  			saveCSV();
  		} catch (FileNotFoundException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();	
  		}
    }


    /**
     * ajoute un lien dans la liste des liens à traiter
     */
    private static void addLink(String lien)
    {
            urls.add(new Url(lien));
    }

    /**
     * 
     * @return le chemin de sauvegarde des csv
     */
    public static String readSaveLocation() {
  		try {
  			Scanner scan;
  			String resultat;
  			scan = new Scanner(new File(System.getProperty("user.dir") + "/save_path.txt"));
  			if(scan.hasNext()) {
  				resultat = scan.next();
  				File Dir = new File(resultat);
  	            if(!Dir.exists()) resultat = "Le chemin de sauvegarde n'est pas valide";
  			}else {
  				resultat = System.getProperty("user.dir") + "/output";
  			}
  			scan.close();
  			return resultat;
  		} catch (FileNotFoundException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  	    	return "Le fichier 'save_path.txt' contenant le chemin de sauvegarde est introuvable.";
  		}
      }

    /**
     * Sauvegarde les CSV trouvé dans le lieu de stockage enregistré
     */
    private static void saveCSV()
    {
        Extractor htmlExtract = new HTMLExtractor();
        Extractor wikiExtract = new WikiTextExtractor();
        for (Url url : urls)
        {
            if (url.getTableCount() > 0)
            {
                Page page = new Page(url);
                page.setCsvListHtml(htmlExtract.getCSV(url));
                page.setCsvListWikitext(wikiExtract.getCSV(url));
                pages.add(page);
            }
        }
        pagestoFile();
    }

    /**
     * enregistre les Pages au format CSV
     */
    private static void pagestoFile()
    {
        for (Page page : pages)
        {
            createAndSave("html", page);
            createAndSave("wikitext", page);
        }
    }

    private static void createAndSave(String type, Page page)
    {
        int i = 0;
        List<List<String>> list = null;
        String dirname = type;
        switch (type)
        {
            case "html":
                list = page.getCsvListHtml();
                break;
            case "wikitext":
                list = page.getCsvListWikitext();
        }
        for (List<String> csvList : list)
        {
            String fileSeparator = System.getProperty("file.separator");
            File file = new File(savePath + fileSeparator + dirname + fileSeparator + page.getTitleWithoutSpace() + "-" + i + ".csv");
            try
            {
                new File(savePath + fileSeparator + dirname).mkdir();
                file.createNewFile();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            FileOutputStream fos = null;
            try
            {
                fos = new FileOutputStream(file.getAbsolutePath());
                for (String ligneCsv : csvList)
                {
                    fos.write(ligneCsv.getBytes());
                    fos.write(System.getProperty("line.separator").getBytes());
                }
                fos.flush();
                fos.close();
                System.out.println("'" + file.getAbsolutePath() + "' a été enregistré");
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            i++;
        }
    }

    /**
     * vérifie si il est possible ou non de parser un String en int
     *
     * @param value String à parser
     * @return true ssi un String peut être parsé en int
     */
    private static boolean tryParseInt(String value)
    {
        try
        {
            Integer.parseInt(value);
            return true;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }
}
