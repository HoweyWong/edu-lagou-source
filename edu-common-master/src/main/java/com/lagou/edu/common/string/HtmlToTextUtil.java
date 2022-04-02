package com.lagou.edu.common.string;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description: 将带有html标签的字符串转换成纯文本
 * @Author leo
 * @Date 2015-8-13 下午3:37:47
 */
public final class HtmlToTextUtil implements Serializable {

	private static final long serialVersionUID = -2592196914690157089L;
	private static final Logger LOGGER = LoggerFactory.getLogger(HtmlToTextUtil.class);

	/**
	 * 将html字符串转换成纯文本
	 * 
	 * @param html
	 *            html字符串
	 * @return
	 */
	public static String getPlainText(String html) {
		if (StringUtils.isBlank(html)) {
			return StringUtils.EMPTY;
		}
		try {
			FormattingVisitor formatter = new FormattingVisitor();
			NodeTraversor traversor = new NodeTraversor(formatter);
			traversor.traverse(Jsoup.parse(html));
			return formatter.toString();
		} catch (Exception e) {
			LOGGER.error("转换html发生异常，直接把html字符串返回", e);
			return html;
		}
	}

	// the formatting rules, implemented in a breadth-first DOM traverse
	private static class FormattingVisitor implements NodeVisitor {
		private static final int maxWidth = 1000;
		private int width = 0;
		private StringBuilder accum = new StringBuilder(); // holds the
															// accumulated text

		// hit when the node is first seen
		public void head(Node node, int depth) {
			String name = node.nodeName();
			if (node instanceof TextNode)
				append(((TextNode) node).text()); // TextNodes carry all
													// user-readable text in the
													// DOM.
			else if (name.equals("li"))
				append("\n * ");
			else if (name.equals("dt"))
				append("  ");
			else if (StringUtil.in(name, "p", "h1", "h2", "h3", "h4", "h5", "tr"))
				append("\n");
		}

		// hit when all of the node's children (if any) have been visited
		public void tail(Node node, int depth) {
			String name = node.nodeName();
			if (StringUtil.in(name, "br", "dd", "dt", "p", "h1", "h2", "h3", "h4", "h5"))
				append("\n");
			else if (name.equals("a"))
				append(String.format(" <%s>", node.absUrl("href")));
		}

		// appends text to the string builder with a simple word wrap method
		private void append(String text) {
			if (text.startsWith("\n"))
				width = 0; // reset counter if starts with a newline. only from
							// formats above, not in natural text
			if (text.equals(" ") && (accum.length() == 0 || StrUtils.in(accum.substring(accum.length() - 1), " ", "\n")))
				return; // don't accumulate long runs of empty spaces

			if (text.length() + width > maxWidth) { // won't fit, needs to wrap
				String words[] = text.split("\\s+");
				for (int i = 0; i < words.length; i++) {
					String word = words[i];
					boolean last = i == words.length - 1;
					if (!last) // insert a space if not the last word
						word = word + " ";
					if (word.length() + width > maxWidth) { // wrap and reset
															// counter
						accum.append("\n").append(word);
						width = word.length();
					} else {
						accum.append(word);
						width += word.length();
					}
				}
			} else { // fits as is, without need to wrap text
				accum.append(text);
				width += text.length();
			}
		}

		@Override
		public String toString() {
			return accum.toString();
		}
	}
}