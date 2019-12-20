package pdl.wiki;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.sweble.wikitext.parser.WikitextParser;
import org.sweble.wikitext.parser.WikitextPostprocessor;
import org.sweble.wikitext.parser.nodes.WtBody;
import org.sweble.wikitext.parser.nodes.WtExternalLink;
import org.sweble.wikitext.parser.nodes.WtInternalLink;
import org.sweble.wikitext.parser.nodes.WtListItem;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtTable;
import org.sweble.wikitext.parser.nodes.WtTableCell;
import org.sweble.wikitext.parser.nodes.WtTableHeader;
import org.sweble.wikitext.parser.nodes.WtTableImplicitTableBody;
import org.sweble.wikitext.parser.nodes.WtTableRow;
import org.sweble.wikitext.parser.nodes.WtText;
import org.sweble.wikitext.parser.nodes.WtUnorderedList;
import org.sweble.wikitext.parser.nodes.WtXmlAttribute;
import org.sweble.wikitext.parser.nodes.WtXmlAttributes;
import org.sweble.wikitext.parser.nodes.WtXmlElement;
import org.sweble.wikitext.parser.utils.SimpleParserConfig;
import xtc.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Extrait les tableaux des pages HTML en format Wikitext
 *
 * @return la liste des tableaux CSV créés
 */
public class WikiTextExtractor implements Extractor {
	private static final Pattern bracket = Pattern.compile("\\{{2}([^\\{}]*)}{2}");// récuperer les doubles brackets
	private static final Pattern attribute = Pattern.compile("([^|]+)");// découpe les attributs dans les doubles
																		// brackets
	private static final Pattern commentHTML = Pattern.compile("(<!--.*-->)"); // trouve les commentaires HTML

	@Override
	public List<List<String>> getCSV(Url purl) {
		String wikitext = getWikitextFromApi(purl);
		return getCSV(wikitext);
	}

	/**
	 * Récupère la page HTML à partir de l'URL
	 *
	 * @return le wikitext de la page en String
	 */
	private String getWikitextFromApi(Url pUrl) {
		String wt = "";
		try {
			String url = "https://" + pUrl.getLang()
					+ ".wikipedia.org/w/api.php?action=parse&format=json&prop=wikitext";

			String oldId = pUrl.getOldId();
			if (oldId != null && !oldId.isEmpty()) {
				url += "&oldid=" + oldId;
			} else {
				url += "&page=" + URLEncoder.encode(pUrl.getPageName(), "UTF-8");
			}

			url += "&redirects=";

			URL apiUrl = new URL(url);

			HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
			connection.setDoOutput(true);
			connection.setInstanceFollowRedirects(false);
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("charset", "utf-8");
			connection.connect();
			InputStream is = connection.getInputStream();
			StringWriter sw = new StringWriter();
			IOUtils.copy(is, sw, "UTF-8");
			String jsonString = sw.toString();
			JSONObject apiResult = new JSONObject(jsonString);
			if (!apiResult.keySet().contains("error")) {
				wt = apiResult.getJSONObject("parse").getJSONObject("wikitext").toMap().get("*").toString();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return wt;
	}

	private List<WtTable> findWikiTable(WtNode root) {
		List<WtTable> list = new ArrayList<WtTable>();

		findWikiTable(root, list);

		return list;
	}

	/**
	 * Trouve les tables avec comme class "wikitable".
	 * 
	 * @param root Noeud dans lequel on navigue.
	 * @param list Liste de tables trouvées.
	 */
	private void findWikiTable(WtNode root, List<WtTable> list) {
		boolean isWikitable = false;

		if (root.getNodeType() == WtNode.NT_TABLE) {

			WtTable table = (WtTable) root;
			WtXmlAttributes attributes = table.getXmlAttributes();

			for (WtNode elem : attributes) {
				if (elem.getNodeType() == WtNode.NT_XML_ATTRIBUTE) {
					WtXmlAttribute attribute = (WtXmlAttribute) elem;

					String name = attribute.getName().getAsString();
					if (name.equals("class")) {

						if (attribute.hasValue()) {
							String value = content(attribute.getValue());

							if (value != null && value.contains("wikitable")) {
								list.add(table);
								isWikitable = true;
								break;
							}
						}
					}
				}
			}
		}

		if (!isWikitable) {
			for (WtNode wtNode : root) {
				findWikiTable(wtNode, list);
			}
		}
	}

	private List<List<String>> getCSV(String wikitext) {

		WikitextParser parser = new WikitextParser(new SimpleParserConfig());
		WtNode root = null;

		try {
			root = parser.parseArticle(wikitext, "titre");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		WikitextPostprocessor postProcessor = new WikitextPostprocessor(new SimpleParserConfig());
		WtNode postprocess = postProcessor.postprocess(root, "titre");

		List<WtTable> wtTables = new ArrayList<WtTable>();
		wtTables = findWikiTable(postprocess);

		List<List<String>> values = new ArrayList<List<String>>();

		for (WtTable wtTable : wtTables) {
			values.add(wtTable(wtTable));
		}

		return values;
	}

	private List<String> wtTable(WtTable wtTable) {
		Table table = new Table();

		int row = 0;
		int col = 0;

		WtTableImplicitTableBody impl = null;
		for (WtNode node : wtTable.getBody()) {
			if (node.getNodeType() == WtNode.NT_TABLE_IMPLICIT_TBODY)
				impl = (WtTableImplicitTableBody) node;
		}

		if (impl == null)
			return null;

		for (WtNode current : impl.getBody()) {

			switch (current.getNodeType()) {
			case WtNode.NT_TABLE_ROW:

				for (Cell elem : wtTableRow((WtTableRow) current)) {
					table.addValue(row, col, elem.getRowspan(), elem.getColspan(), elem.getValue());
					col++;
				}
				col = 0;
				row++;

				break;
			default:
				break;
			}
		}

		return table.getCSVLines();
	}

	private List<Cell> wtTableRow(WtTableRow wtTableRow) {
		List<Cell> values = new ArrayList<Cell>();

		for (WtNode current : wtTableRow.getBody()) {
			switch (current.getNodeType()) {
			case WtNode.NT_TABLE_CELL:
			case WtNode.NT_TABLE_HEADER:
				values.add(wtTableHeaderOrCell(current));
				break;
			default:
				break;
			}

		}

		return values;
	}

	private String content(WtNode elem) {
		StringBuilder sb = new StringBuilder();

		for (WtNode current : elem) {
			switch (current.getNodeType()) {
			case WtNode.NT_PARAGRAPH:
				sb.append(content(current));
				break;
			case WtNode.NT_XML_ELEMENT:
				sb.append(content(((WtXmlElement) current).getBody()));
				break;
			case WtNode.NT_INTERNAL_LINK:
				sb.append(wtInternalLink((WtInternalLink) current));
				break;
			case WtNode.NT_ITALICS:
				sb.append(content(current));
				break;
			case WtNode.NT_BOLD:
				sb.append(content(current));
				break;
			case WtNode.NT_TEXT:
				sb.append(wtText((WtText) current));
				break;
			case WtNode.NT_UNORDERED_LIST:
				sb.append(content(current));
				break;
			case WtNode.NT_LIST_ITEM:
				sb.append(content(current));
				break;
			case WtNode.NT_EXTERNAL_LINK:
				sb.append(wtExternalLink((WtExternalLink) current));
				break;
			case WtNode.NT_URL:
			default:
				break;
			}
		}

		return sb.toString();
	}

	private Cell wtTableHeaderOrCell(WtNode elem) {
		int colspan = 1;
		int rowspan = 1;
		StringBuilder sb = new StringBuilder();

		WtXmlAttributes wtXmlAttributes = null;
		switch (elem.getNodeType()) {
		case WtNode.NT_TABLE_CELL:
			wtXmlAttributes = ((WtTableCell) elem).getXmlAttributes();
			break;
		case WtNode.NT_TABLE_HEADER:
			wtXmlAttributes = ((WtTableHeader) elem).getXmlAttributes();
			break;
		default:
			return null;
		}
		boolean asOtherAttribute = false;
		for (WtNode wtNode : wtXmlAttributes) {
			if (wtNode.getNodeType() == WtNode.NT_XML_ATTRIBUTE) {
				WtXmlAttribute attribute = (WtXmlAttribute) wtNode;

				String name = attribute.getName().getAsString();
				if (name.equals("colspan") || name.equals("rowspan")) {

					if (attribute.hasValue()) {
						String value = content(attribute.getValue());

						if (value != null && !value.isEmpty()) {
							if (name.equals("colspan"))
								colspan = Integer.parseInt(value);
							else
								rowspan = Integer.parseInt(value);
						}
					}
				} else {
					if (!attribute.hasValue()) {
						asOtherAttribute = true;
						sb.append(name);
					}
				}
			} else if (wtNode.getNodeType() == WtNode.NT_XML_ATTRIBUTE_GARBAGE) {
				asOtherAttribute = true;
				sb.append(content(wtNode));
			}
		}

		if (asOtherAttribute)
			sb.append('|');

		WtBody wtBody = null;
		if (elem.getNodeType() == WtNode.NT_TABLE_CELL)
			wtBody = ((WtTableCell) elem).getBody();
		else
			wtBody = ((WtTableHeader) elem).getBody();

		sb.append(content(wtBody));

		return new Cell(rowspan, colspan, processText(sb.toString()));
	}

	private String processText(String s) {
		s = s.replace("\n", " ");
		String[] splitBracket = bracket.split(s);
		StringBuilder sb;

		List<String> processValue = new ArrayList<String>();
		Matcher matcher = bracket.matcher(s);

		while (matcher.find()) {
			String param = matcher.group(1);

			Matcher match = attribute.matcher(param);

			if (match.find()) {

				String param1 = match.group(1);

				switch (param1) {
				case "citation needed":
				case "Citation needed":
				case "cite web":
				case "cite web ":
				case "Cite web":
				case "Cite web ":
				case "Dead link":
				case "webarchive":
				case "ref":
				case "refn":
					break;
				case "cvt":
					while (match.find())
						processValue.add(match.group(1));
					break;
				case "Free":
					if (match.find())
						processValue.add(match.group(1));
					else
						processValue.add(param1);
					break;
				case "date":
					if (match.find())
						processValue.add(match.group(1));
					break;
				default:
					String last = null;
					while (match.find())
						last = match.group(1);
					if (last == null)
						processValue.add(param1);
					else
						processValue.add(last);
				}
			}
		}

		sb = new StringBuilder();

		if (splitBracket.length == 0) {
			for (String val : processValue)
				sb.append(val);
		} else {

			for (int i = 0; i < splitBracket.length; i++) {
				sb.append(splitBracket[i]);
				if (i < processValue.size()) {
					sb.append(processValue.get(i));
				}
			}
		}

		String finalValue = sb.toString();
		if (bracket.matcher(finalValue).find())
			finalValue = processText(sb.toString());

		sb = new StringBuilder();
		String[] splitCommentHTML = commentHTML.split(finalValue);
		for (int i = 0; i < splitCommentHTML.length; i++)
			sb.append(splitCommentHTML[i]);

		finalValue = sb.toString();

		return finalValue.replace(';', ',');
	}

	private String wtInternalLink(WtInternalLink elem) {
		if (elem.hasTitle()) {
			StringBuilder sb = new StringBuilder();
			sb.append(content(elem.getTitle()));
			return sb.toString();
		} else {
			return elem.getTarget().getAsString();
		}
	}

	private String wtExternalLink(WtExternalLink elem) {
		if (elem.hasTitle()) {
			StringBuilder sb = new StringBuilder();
			sb.append(content(elem.getTitle()));
			return sb.toString();
		} else {
			return new String();
		}
	}

	private String wtText(WtText elem) {
		return elem.getContent();
	}
}
