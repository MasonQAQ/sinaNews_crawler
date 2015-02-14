package com.lxf.crawler;

import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import com.lxf.dao.bean.NewsBean;
import com.lxf.dao.imp.NewsDao;
import com.lxf.dao.inf.NewsDaoInf;

/**
 * <�������> ��������������ȡ���ŷ��ࡢ���⼰���� (htmlparser.jar)
 * 
 * @author �����
 */
public class RollNews {

	private final static String URL = "http://roll.news.sina.com.cn/s/channel.php";//��������
	private final static String ENCODING = "gb2312";
	private final static String TYPE = "roll";//��������
	private final static String LINK_TO_WORD = "\" target=\"_blank\">";//A��ǩ ��href���ݡ������ı���֮��Ĳ���
	private static NewsDaoInf dao = new NewsDao();// ����dao���ж����ݿ�Ĳ���

	/**
	 * ץȡ���˹�����Ϣ
	 * 
	 * @param type
	 *            �������
	 * @param url
	 *            ���ű����б�ҳ������
	 * @param sLinkToWord
	 *            a��ǩ ��href���ġ�����λ�õ����ı���֮��Ĳ��֣��磺" target="_blank">
	 */
	public static void getNews() {

		NodeFilter filter = new TagNameFilter("ul");
		Parser parser = new Parser();
		NodeList list = null;
		try {
			parser.setURL(URL);// ������ģ��ĵ�ַ
			parser.setEncoding(ENCODING);
			list = parser.extractAllNodesThatMatch(filter);
		} catch (ParserException e) {
			System.out.println("ץȡ��Ϣ����������ϢΪ��");
			e.printStackTrace();
		}
		for (int i = 0; i < list.size(); i++) {
			Tag node = (Tag) list.elementAt(i);
			for (int j = 1; j < node.getChildren().size(); j++) {
				String textStr = node.getChildren().elementAt(j).toHtml()
						.trim();
				String link = getLink(textStr);// ��ȡ����
				String title = getTitle(textStr);// ��ȡ����
				String body = getNewsBody(link);// ��ȡ����
				if (!"".equals(link)&& !"".equals(title) && !"".equals(body)) {
					/** д�����ݿ� */
					NewsBean newsBean = new NewsBean(0, title, body, link,
							link.substring(link.lastIndexOf("/") - 10,
									link.lastIndexOf("/")), TYPE);
					dao.add(newsBean);
				}
			}
		}
	}

	/**
	 * ���A��ǩ�е�����
	 * 
	 * @param texrStr
	 *            ץȡ����ҳ��ת�������ַ���
	 * @return ��ҳ������
	 */
	private static String getLink(String texrStr) {
		// �����ַ���
		String link = "";
		if (texrStr.length() > 0) {
			int linkbegin = texrStr.indexOf("href=\"");// ��ȡ<a>�����ַ�����ʼλ��
			int linkend = texrStr.indexOf(LINK_TO_WORD);// ��ȡ<a>�����ַ�������λ��
			String sublink = texrStr.substring(linkbegin + "href=\"".length(),
					linkend);

			if (sublink.indexOf("target") != -1) {
				link = sublink.substring(0, sublink.indexOf("\""));
			} else {
				link = sublink;// �����ַ���
			}
		}
		return link;
	}

	/**
	 * ��ȡA��ǩ�е��ı�����
	 * 
	 * @param textStr
	 *            ץȡ����ҳ��ת�������ַ���
	 * @return ����
	 */
	private static String getTitle(String textStr) {
		int titlebegin = textStr.indexOf(LINK_TO_WORD);
		int titleend = textStr.indexOf("</a>");
		String title = textStr.substring(titlebegin + LINK_TO_WORD.length(),
				titleend).trim();
		System.out.println("����ץȡ: " + title);
		// ͨ�������жϸ������Ƿ��Ѿ�����
		if (title.contains("��Ƶ:") || title.contains("��Ƶ��")) {
			System.out.println("���޷������Ƶ���š�");
			return "";
		}
		if (title.contains("(ͼ)")) {
			title = title.replace("(ͼ)", "");
		}
		if (dao.hasNews(title)) {
			System.out.println("���ü�¼�Ѿ����ڡ�");
			return "";
		}

		return title;
	}

	/**
	 * �������ݴ���
	 * 
	 * @param link
	 *            ���ݵ�����
	 * @return ����
	 */
	private static String getNewsBody(String link) {
		NodeFilter bodyfilter = new AndFilter(new TagNameFilter("div"),
				new HasAttributeFilter("id", "artibody"));
		Parser bodyparser = new Parser();
		NodeList bodylist = null;
		try {
			bodyparser.setURL(link);// ��ַurl
			bodyparser.setEncoding(ENCODING);
			bodylist = bodyparser.extractAllNodesThatMatch(bodyfilter);
		} catch (ParserException e) {
			System.out.println("ץȡ��Ϣ��ҳ�����������ϢΪ��");
			e.printStackTrace();
			return "";
		}

		// ���������ַ���
		if (bodylist.elementAt(0) == null) {
			System.out.println("�����������ݡ�");
			return "";
		}
		String newstextstr = bodylist.elementAt(0).toHtml().trim();
		// ֻ�����������ݣ�����P��ǩ�Ա������Ű�
		int bodybegin = newstextstr.indexOf("<p>");
		int bodyend = newstextstr.lastIndexOf("</p>") + 4;

		String body = "";
		if (bodybegin < 0 || bodybegin >= bodyend) {
			body = newstextstr;
		} else {
			body = newstextstr.substring(bodybegin, bodyend);
		}
		int bodyimgbegin = newstextstr.indexOf("<div class=\"img_wrapper\">");
		int bodyimgend = newstextstr.lastIndexOf("<span class=\"img_descr\">");
		if (bodyimgbegin >= 0 && bodyimgbegin < bodyimgend) {
			body = newstextstr.substring(bodyimgbegin, bodyimgend) + "</div>"
					+ body;
		}
		int bodyremovebegin = body.indexOf("<div id=\"news_like\"");
		int bodyremoveend = body.lastIndexOf("<p class=\"fr\">");
		if (bodyremovebegin > 0 && bodyremovebegin < bodyremoveend) {
			body = body.replace(body.substring(bodyremovebegin, bodyremoveend),
					"");
		}
		return body;

	}

}