package pdl.wiki;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


/**
 * Classe de test pour la classe PageChecker
 */
public class PageCheckerTest
{

    /**
     * V�rifie si la page test�e existe
     */
    @Test
    public void existingPagesTest_withHttps()
    {
        Map<String, Integer> urlMap = new HashMap<>();
        urlMap.put("https://fr.wikipedia.org/w/index.php?title=Jeux_olympiques_d%27%C3%A9t%C3%A9&oldid=163777314", 5);
        urlMap.put("https://fr.wikipedia.org/w/index.php?title=Wikip%C3%A9dia:Accueil_principal&oldid=155532403", 0);
        urlMap.put("https://fr.wikipedia.org/w/index.php?title=Jeux_olympiques&oldid=163506830", 3);
        urlMap.put("https://fr.wikipedia.org/w/index.php?title=Jeux_olympiques_d%27hiver&oldid=161852389", 6);
        urlMap.put("https://fr.wikipedia.org/w/index.php?title=Jean_Capdouze&oldid=162701968", 1);
        for (Map.Entry set : urlMap.entrySet())
        {
            assertEquals((int) set.getValue(), PageChecker.urlCheck(set.getKey().toString()).size());
        }
    }

    /**
     * V�rifie si une page test�e sans http peut �tre test�e
     * (la fonction ajoute elle m�me le http manquant)
     */
    @Test
    public void existingPagesTest_WithoutHttps()
    {
        Map<String, Integer> urlMap = new HashMap<>();
        urlMap.put("http://fr.wikipedia.org/w/index.php?title=Jeux_olympiques_d%27%C3%A9t%C3%A9&oldid=163777314",  5);
        urlMap.put("fr.wikipedia.org/w/index.php?title=Jeux_olympiques_d%27%C3%A9t%C3%A9&oldid=163777314",  5);
        urlMap.put("http://fr.wikipedia.org/w/index.php?title=Wikip%C3%A9dia:Accueil_principal&oldid=155532403", 0);
        urlMap.put("fr.wikipedia.org/w/index.php?title=Wikip%C3%A9dia:Accueil_principal&oldid=155532403", 0);
        urlMap.put("http://fr.wikipedia.org/w/index.php?title=Jeux_olympiques&oldid=163506830", 3);
        urlMap.put("fr.wikipedia.org/w/index.php?title=Jeux_olympiques&oldid=163506830", 3);
        urlMap.put("http://fr.wikipedia.org/w/index.php?title=Jeux_olympiques_d%27hiver&oldid=161852389", 6);
        urlMap.put("fr.wikipedia.org/w/index.php?title=Jeux_olympiques_d%27hiver&oldid=161852389", 6);
        urlMap.put("http://fr.wikipedia.org/w/index.php?title=Jean_Capdouze&oldid=162701968", 1);
        urlMap.put("fr.wikipedia.org/w/index.php?title=Jean_Capdouze&oldid=162701968", 1);
        for (Map.Entry set : urlMap.entrySet())
        {
            assertEquals((int) set.getValue(), PageChecker.urlCheck(set.getKey().toString()).size());
        }
    }

    /**
     * V�rifie si la page est valide ou non
     */
    @Test
    public void pageNotValid()
    {
        List<String> urlToTest = new ArrayList<>();
        urlToTest.add("https://forum.xda-developers.com/");
        urlToTest.add("https://github.com/vad101010/PDLProject");
        urlToTest.add("https://www.google.fr/");
        for (String url : urlToTest)
        {
            assertNull(PageChecker.urlCheck(url));
        }
    }

}