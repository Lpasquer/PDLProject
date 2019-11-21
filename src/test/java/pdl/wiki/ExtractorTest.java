package pdl.wiki;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Classe de test pour la classe Extractor
 */
public class ExtractorTest
{

    Extractor extractorwiki;
    Extractor extractorhtml;
    String UrlWithTables;
    Map<String, Integer> nbtabliens;
    List<List<String>> csvwiki;
    List<List<String>> csvhtml;
    List<String> csvTest;

    @BeforeEach
    public void setUp() throws Exception
    {
        extractorhtml = new HTMLExtractor();
        extractorwiki = new WikiTextExtractor();
        UrlWithTables = "https://fr.wikipedia.org/w/index.php?title=Championnat_d%27Allemagne_f%C3%A9minin_de_handball&oldid=160723522";
        nbtabliens = new HashMap<>();
        nbtabliens.put("https://fr.wikipedia.org/w/index.php?title=Th%C3%BCringer_HC&oldid=161132172", 1);
        nbtabliens.put("https://fr.wikipedia.org/w/index.php?title=Championnat_d%27Allemagne_f%C3%A9minin_de_handball&oldid=160723522", 6);
        nbtabliens.put("https://fr.wikipedia.org/w/index.php?title=Parti_communiste_de_l%27Union_sovi%C3%A9tique&oldid=160293234", 3);
        nbtabliens.put("https://fr.wikipedia.org/w/index.php?title=Union_des_r%C3%A9publiques_socialistes_sovi%C3%A9tiques&oldid=163482866", 2);
        csvTest = new ArrayList<>();
        for (int i = 1; i < 6; i++)
        {
            csvTest.add(FileUtils.readFileToString(new File("inputdata" + File.separator + "PDL" + i + ".csv")));
        }
    }
    
    @Test
    public void getCSVHTML()
    {
        for(Entry<String, Integer> entry: nbtabliens.entrySet()) {  
            int htmlSize = extractorhtml.getCSV(new Url(entry.getKey())).size();
            assertTrue( entry.getValue() == htmlSize,"nombre de tableau trouvé incorrecte (extractor HTML, lien:" + entry.getKey() + "; prévu : )" + entry.getValue() + ", reçu : " + htmlSize);
        }
    }

    @Test
    public void getCSVWikiText()
    {
        for (Entry<String, Integer> entry: nbtabliens.entrySet())
        {
            int wikitextSize = extractorwiki.getCSV(new Url(entry.getKey())).size();
            assertTrue(entry.getValue() == wikitextSize,"nombre de tableau trouvé incorrecte (extractor wiki, lien:" + entry.getKey() + "; prévu : )" + entry.getValue() + ", reçu : " + wikitextSize);
        }
    }
    
    @Test
    public void getCSV2HTML() throws IOException
    {
        csvhtml = extractorhtml.getCSV(new Url(UrlWithTables));
        for (int i = 0; i < 5; i++)
        {
            int htmlsize = csvhtml.get(i).size();
            int csvsize =countCsvLines(csvTest.get(i), false);
            assertTrue(csvhtml.get(i).size() == countCsvLines(csvTest.get(i), false), "Nombre de lignes du CSV différent trouvé (HTML), reçu :" + htmlsize + "; prévu :" + csvsize);
        }
    }
    
    @Test
    public void getCSV2WikiText() throws IOException
    {
        csvwiki = extractorwiki.getCSV(new Url(UrlWithTables));
        for (int i = 0; i < 5; i++)
        {
            int wikisize = csvwiki.get(i).size();
            int csvsize =countCsvLines(csvTest.get(i), false);
            assertTrue(countCsvLines(csvwiki.get(i).get(0), true) == countCsvLines(csvTest.get(i), true), "Nombre de colonnes du CSV différent trouvé (Wiki), reçu :" + wikisize + "; prévu :" + csvsize);
        }
    }
    //retourne le nombre de lignes ou colonnes du fichier text CSV
    private int countCsvLines(String csv, boolean col) throws IOException
    {
        InputStream is = new ByteArrayInputStream(csv.getBytes());
        try
        {
            byte[] c = new byte[1024];
            int nbLigCol = 0;
            int nbCharLu = 0;
            boolean fichierVide = true;
            while ((nbCharLu = is.read(c)) != -1)
            {
                fichierVide = false;
                for (int i = 0; i < nbCharLu; ++i)
                {
                    if (col)
                    {
                        if (c[i] == '\n')
                        {
                            return nbLigCol;
                        }
                        else if (c[i] == ';')
                        {
                            nbLigCol++;
                        }
                    }
                    else
                    {
                        if (c[i] == '\n')
                        {
                            nbLigCol++;
                        }
                    }
                }
            }
            return (nbLigCol == 0 && !fichierVide) ? 1 : nbLigCol;
        }
        finally
        {
            is.close();
        }
    }

}