package pdl.wiki;

import org.jsoup.nodes.Element;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Permet de récupérer un lien, de tester sa validité et d'ajouter le nombre de
 * tableau présent à une base retourne le lien,sa validité et le nombre de
 * tableau
 */
public class Url {
	private String link;
	private boolean valid;
	private int tableCount;
	private List<Element> listTables;
	private String oldId;
	private String pageName;

	public Url(String link) {
		this.link = link;
		this.listTables = PageChecker.urlCheck(link);
		this.tableCount = (listTables != null ? listTables.size() : -1);
		this.valid = tableCount > -1;
		findParameter();

	}

	/**
	 * Permet de récupérer le lien url
	 *
	 * @return retourne le lien
	 */
	public String getLink() {
		return link;
	}

	/**
	 * Permet de tester si le lien est valide
	 *
	 * @return retourne vrai si le lien est valide, faux sinon
	 */
	public boolean isValid() {
		return valid;
	}

	/**
	 * @return une liste des Element tables trouvés sur la page
	 */
	public List<Element> getListTables() {
		return listTables;
	}

	/**
	 * Permet de récupérer le nombre de tableau qu'il y a dans la page
	 *
	 * @return retourne le nombre de tableau de la page
	 */
	public int getTableCount() {
		return tableCount;
	}

	/**
	 * @return le diminutif de la locale utilisée pour l'API
	 */
	public String getLang() {
		String dns = link.split("//")[1].split("/")[0];
		String lang = dns.split("\\.")[0];
		return lang.equals("www") ? "en" : lang;
	}

	/**
	 * Retourne le titre de la page.
	 * @return Titre de la page.
	 */
	public String getPageName() {
		if (pageName == null) {

			String[] tabUrl = link.split("/");
			String path = tabUrl[tabUrl.length - 1];
			if (path.contains("?")) {
				path = path.split("\\?")[0];
			}
			if (path.contains("#")) {
				path = path.split("#")[0];
			}
			return path;
		} else {
			return pageName;
		}
	}
	
	/**
	 * Permet de trouver les paramètres oldid & title d'une url.
	 */
	private void findParameter() {
		Pattern pattern = Pattern.compile("(\\?|\\&)([^=]+)\\=([^&]+)");
		Matcher matcher = pattern.matcher(link);

		while (matcher.find()) {
			switch (matcher.group(2)) {
			case "oldid":
				oldId = matcher.group(3);
				break;
			case "title":
				pageName = matcher.group(3);
				break;
			}
		}
	}

	/**
	 * Retourne l'id de la révision.
	 * 
	 * @return L'id de la révision, null si aucun id.
	 */
	public String getOldId() {
		return oldId;
	}
	
	/**
	 * 
	 * @return True si il y a un id de révision, sinon False.
	 */
	public boolean asOldId() {
		return oldId != null;
	}

	@Override
	public String toString() {
		if (!valid) {
			return link + "(Lien invalide !)";
		}
		return link + "(" + tableCount + ((tableCount) > 1 ? " tableaux trouvés)" : " tableau trouvé)");
	}
}