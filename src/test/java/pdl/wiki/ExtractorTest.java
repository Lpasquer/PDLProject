package pdl.wiki;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Classe de test pour la classe Extractor
 */
public class ExtractorTest {

	Extractor extractorwiki;
	Extractor extractorhtml;
	String UrlWithTables;
	Map<String, Integer> nbtabliens;
	List<List<String>> csvTest;

	@BeforeEach
	public void setUp() throws Exception {
		extractorhtml = new HTMLExtractor();
		extractorwiki = new WikiTextExtractor();
		UrlWithTables = "https://fr.wikipedia.org/w/index.php?title=Championnat_d%27Allemagne_f%C3%A9minin_de_handball&oldid=160723522";
		nbtabliens = new HashMap<>();

		nbtabliens.put("https://en.wikipedia.org/w/index.php?title=Handball-Bundesliga_(women)&oldid=898595565", 5);
		nbtabliens.put("https://en.wikipedia.org/w/index.php?title=Communist_Party_of_the_Soviet_Union&oldid=931312112",
				2);
		nbtabliens.put("https://en.wikipedia.org/w/index.php?title=Soviet_Union&oldid=931330041", 1);
		nbtabliens.put("https://en.wikipedia.org/w/index.php?title=IS_tank_family&oldid=927279017", 1);

		csvTest = new ArrayList<List<String>>();
		for (int i = 1; i < 7; i++) {
			csvTest.add(readFile("inputdata" + File.separator + "PDL" + i + ".csv"));
		}
	}

	@Test
	public void getCSVHTML1() {
		compareNumberOfTable(extractorhtml);
	}
	
	@Test
	public void getCSVWikiText1() {
		compareNumberOfTable(extractorwiki);
	}
	
	private void compareNumberOfTable(Extractor extractor) {
		for (Entry<String, Integer> entry : nbtabliens.entrySet()) {
			int size = extractor.getCSV(new Url(entry.getKey())).size();
			assertTrue(entry.getValue() == size, "nombre de tableau trouvé incorrecte (lien:" + entry.getKey()
					+ "; prévu : )" + entry.getValue() + ", reçu : " + size);
		}
	}

	@Test
	public void getCSVWikiText2() {
		compareColLine(extractorwiki.getCSV(new Url(UrlWithTables)));
	}

	@Test
	public void getCSVHTML2() {
		compareColLine(extractorhtml.getCSV(new Url(UrlWithTables)));
	}

	private void compareColLine(List<List<String>> csv) {
		for (int i = 0; i < 6; i++) {
			List<String> file = csvTest.get(i);
			int extractNunberOfLine = csv.get(i).size();
			int fileNunberOfLine = file.size();
			assertTrue(fileNunberOfLine == extractNunberOfLine, "Nombre de lignes du CSV différent trouvé, prévu :"
					+ fileNunberOfLine + "; reçu :" + extractNunberOfLine + " CSV : " + (i + 1));

			for (int line = 0; line < file.size(); line++) {
				int extractNunberOfCol = countCol(csv.get(i).get(line));
				int fileNunberOfCol = countCol(file.get(line));
				assertTrue(fileNunberOfCol == extractNunberOfCol,
						"Nombre de colonnes du CSV différent trouvé, prévu :" + fileNunberOfCol + "; reçu :"
								+ extractNunberOfCol + " CSV : " + (i + 1) + " ligne : " + (line + 1));
			}
		}
	}

	@Test
	public void UrlWithOldIdWikitext() {
		Url url = new Url("https://en.wikipedia.org/w/index.php?title=IS_tank_family&oldid=927279017");
		List<List<String>> tables = extractorwiki.getCSV(url);

		assertTrue(tables.size() == 1, "L'url ( " + url.getLink() + " ) doit contenir une wikitable.");

		Page page = new Page(url);
		assertTrue(page.getTitleWithoutSpace().equals("ISTankFamily"),
				"Nom de page invalide. Prévu : IS_tank_family - Reçu : " + page.getTitleWithoutSpace());
	}

	@Test
	public void UrlRedirectWikitext() {
		Url url = new Url("https://en.wikipedia.org/wiki/Comparison_of_Android_e-book_reader_software");
		List<List<String>> tables = extractorwiki.getCSV(url);

		assertTrue(tables.size() != 0, "L'url ( " + url.getLink() + " ) doit contenir au moins une wikitable.");
	}

	@Test
	public void UrlWithSpecialCharacterWikitext() {
		Url url = new Url("https://en.wikipedia.org/wiki/Comparison_of_ALGOL_68_and_C++");
		List<List<String>> tables = extractorwiki.getCSV(url);

		assertTrue(tables.size() != 0, "L'url ( " + url.getLink() + " ) doit contenir au moins une wikitable.");
	}

	private List<String> readFile(String filename) {
		List<String> records = new ArrayList<String>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			String line;
			while ((line = reader.readLine()) != null) {
				records.add(line);
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return records;
	}

	private int countCol(String text) {
		return text.split(";").length;
	}
}
