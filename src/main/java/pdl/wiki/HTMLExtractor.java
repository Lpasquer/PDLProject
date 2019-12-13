package pdl.wiki;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe permettant la conversion de tables d'une page HTML en format CSV
 */
public class HTMLExtractor implements Extractor
{
    /**
     * Convertit les tables d'une page url, validée par PageChecker en une collection de tables au format CSV
     * <p>
     * url de la page Wikipédia contentant les tables
     *
     * @return une collection de tables au format CSV
     */
    @Override
    public List<List<String>> getCSV(Url purl)
    {

        List<List<String>> listeDeList = new ArrayList<>();
        List<Element> listTables = purl.getListTables();
        for (Element e : listTables)
        {
        	Table table = new Table();
            List<String> csvData = new ArrayList<>();

//            csvData.add(getTableHeader(e));
            // Suppression des tags <sup> et <sub>, étant trop souvent des liens
            e.getElementsByTag("sup").remove();
            e.getElementsByTag("sub").remove();

            Elements ligne = e.select("tr");
            int i = 0;
            for (Element line : ligne)
            {	
            	Elements col = line.select("td, th");
            	int j=0;
                for (Element cellule : col) {
                	int rowspan = 1;
                	if(!cellule.attr("rowspan").isEmpty()) {
                		rowspan = Integer.parseInt(cellule.attr("rowspan"));
                	} 
                	int colspan = 1;
                	if(!cellule.attr("colspan").isEmpty()) {
                		colspan = Integer.parseInt(cellule.attr("colspan"));
                	}
                	String value = cellule.text();
                	table.addValue(i, j, rowspan, colspan, value);
                	j++;
                }
                i++;
            }
            listeDeList.add(table.getCSVLines());
        }
        return listeDeList;
    }

    private String getTableHeader(Element e)
    {
        String csvHeader = "";
        Elements header = e.select("thead tr td");
        for (Element colHeader : header)
        {
            csvHeader += colHeader.text();
        }
        return csvHeader;
    }

}
