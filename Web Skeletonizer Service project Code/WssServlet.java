import java.io.*;
import java.net.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class WssServlet extends HttpServlet
{
    public static final String PARM = "url";

    /**
     * Just call the doGet method.
     */
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
    throws ServletException, java.io.IOException
    {
	doGet(req,resp);
    }


    public void doGet(HttpServletRequest req, HttpServletResponse resp)
    throws ServletException, java.io.IOException
    {
	resp.setContentType("text/html");
	
	String subjectURL = req.getParameter(PARM);
	HttpSession sess = req.getSession(true);
	PrintWriter out = resp.getWriter();

	// Every page has four parts: title/header, intro,
	// results (if any), and query form/history.
	sendBeginning(subjectURL, out, sess);
	
	if (subjectURL == null) 
	    sendIntro(out, sess);
	else {
	    boolean okay = false;
	    LinkHandler ln = null;
	    URL su = null;
	    try {
		su = new URL(subjectURL);
	    }
	    catch (MalformedURLException mue) {
		sendError("Could not process subject URL " + subjectURL, out, mue);
	    }
	    if (su != null) {
		try {
		    ln = new LinkHandler(su);
		}
		catch (Exception e) {
		    sendError("Could not perform analysis of " + subjectURL, out, e);
		}
	    }
	    if (ln != null) {
		Hashtable headers = new Hashtable();
		Vector r = ln.process(headers);
		sendResults(subjectURL, out, headers, r, req);
		Vector hist = (Vector)(sess.getValue("history"));
		if (hist == null) hist = new Vector();
		if (!hist.contains(subjectURL)) {
		    hist.addElement(subjectURL);
		    sess.putValue("history", hist);
		}
	    }
	}

	Vector hist = (Vector)(sess.getValue("history"));
	sendForm(subjectURL, out, hist, req);

	sendEnd(out);
    }


    void sendBeginning(String subjectURL, PrintWriter out, HttpSession sess) {
	out.println("<!doctype html>");
	out.println("<html lang='en'>");
	out.println("<head>");
	out.println("<meta charset='utf-8'>");
	out.println("<meta name='viewport' content='width=device-width, initial-scale=1'>");
	out.println("<title>Web Skeletonizer Service");
	if (subjectURL != null) {
	    out.println(" - ");
	    out.println(escapeHtml(subjectURL));
	}
	out.println("</title>");
	out.println("<link rel='preconnect' href='https://fonts.googleapis.com'>");
	out.println("<link rel='preconnect' href='https://fonts.gstatic.com' crossorigin>");
	out.println("<link href='https://fonts.googleapis.com/css2?family=Space+Grotesk:wght@400;500;700&display=swap' rel='stylesheet'>");
	out.println("<style>");
	out.println(":root { --bg-1:#f8fbff; --bg-2:#eef3ff; --ink:#121629; --muted:#5b6175; --card:#ffffffcc; --line:#d7def0; --brand:#0057ff; --brand-2:#00b3ff; --ok:#0b8f4d; --radius:16px; --shadow:0 14px 38px rgba(18,22,41,.12); }");
	out.println("* { box-sizing:border-box; }");
	out.println("body { margin:0; font-family:'Space Grotesk',sans-serif; color:var(--ink); background: radial-gradient(1400px 600px at 8% -10%, #dfe9ff, transparent), radial-gradient(1200px 500px at 92% 0%, #d8f8ff, transparent), linear-gradient(165deg,var(--bg-1),var(--bg-2)); }");
	out.println(".wrap { width:min(1080px,94vw); margin:32px auto 56px; }");
	out.println(".hero { background:linear-gradient(120deg,#0f1f4f,#17338b 54%,#0f6fe2); color:#fff; border-radius:22px; padding:28px 30px; box-shadow:0 18px 42px rgba(8,20,56,.35); position:relative; overflow:hidden; }");
	out.println(".hero:after { content:''; position:absolute; right:-60px; top:-80px; width:240px; height:240px; border-radius:50%; background:radial-gradient(circle,#4bc8ff88 0,#4bc8ff00 70%); }");
	out.println(".hero h1 { margin:0; font-size:clamp(1.5rem,2.8vw,2.3rem); letter-spacing:.2px; }");
	out.println(".hero p { margin:10px 0 0; color:#d9e6ff; font-size:.98rem; max-width:65ch; }");
	out.println(".card { background:var(--card); border:1px solid var(--line); border-radius:var(--radius); box-shadow:var(--shadow); margin-top:18px; padding:22px; backdrop-filter: blur(6px); }");
	out.println("h2,h3 { margin:0 0 12px; letter-spacing:.1px; }");
	out.println("h3 { font-size:1.15rem; }");
	out.println(".muted { color:var(--muted); }");
	out.println(".grid { display:grid; grid-template-columns: 1fr; gap:18px; }");
	out.println("@media (min-width:940px){ .grid { grid-template-columns: 1fr 1fr; } .grid .span-2{ grid-column:1 / -1; } }");
	out.println(".table-wrap { overflow:auto; border:1px solid var(--line); border-radius:12px; }");
	out.println("table { width:100%; border-collapse:collapse; min-width:320px; }");
	out.println("th,td { text-align:left; padding:10px 12px; border-bottom:1px solid #e8ecf8; font-size:.95rem; }");
	out.println("th { background:#f3f6ff; color:#25305c; }");
	out.println("tr:last-child td { border-bottom:none; }");
	out.println(".link-list { margin:0; padding-left:22px; }");
	out.println(".link-list li { margin:0 0 12px; line-height:1.4; }");
	out.println(".pill { display:inline-block; margin-left:8px; font-size:.78rem; color:#0b3a8f; background:#e2ecff; border:1px solid #c8d9ff; padding:2px 8px; border-radius:999px; }");
	out.println("a { color:var(--brand); text-decoration:none; font-weight:600; word-break:break-word; }");
	out.println("a:hover { color:#003eb8; text-decoration:underline; }");
	out.println(".analyze-btn { display:inline-block; margin-left:10px; background:linear-gradient(95deg,var(--brand),var(--brand-2)); color:#fff; border-radius:10px; padding:5px 10px; font-size:.83rem; font-weight:700; text-decoration:none; box-shadow:0 6px 16px rgba(0,87,255,.25); }");
	out.println(".analyze-btn:hover { color:#fff; text-decoration:none; filter:brightness(.95); }");
	out.println("form { display:grid; grid-template-columns: 1fr auto auto; gap:10px; align-items:center; }");
	out.println("@media (max-width:760px){ form { grid-template-columns:1fr; } .analyze-btn { margin-left:0; margin-top:8px; } }");
	out.println("input[type=text] { width:100%; padding:12px 14px; border:1px solid #cfd8f5; border-radius:12px; font:inherit; background:#fff; }");
	out.println("input[type=text]:focus { outline:none; border-color:#7aa7ff; box-shadow:0 0 0 4px #7aa7ff33; }");
	out.println("input[type=submit],input[type=reset] { border:none; padding:11px 16px; border-radius:12px; font:700 .93rem 'Space Grotesk',sans-serif; cursor:pointer; }");
	out.println("input[type=submit] { color:#fff; background:linear-gradient(95deg,var(--brand),var(--brand-2)); }");
	out.println("input[type=reset] { color:#25305c; background:#e9eefc; }");
	out.println(".history { margin-top:12px; padding-left:18px; }");
	out.println(".error { border-left:4px solid #d64045; background:#fff6f6; }");
	out.println(".footer { margin-top:20px; color:#6f7795; font-size:.86rem; text-align:center; }");
	out.println(".ok { color:var(--ok); font-weight:700; }");
	out.println("</style>");
	out.println("</head>");
	out.println("<body>");
	out.println("<main class='wrap'>");
	out.println("<section class='hero'>");
	out.println("<h1>Web Skeletonizer Service</h1>");
	out.println("<p>Analyze any public page and inspect its outbound structure with a clean, modern interface optimized for desktop and mobile.</p>");
	out.println("</section>");
    }

    void sendEnd(PrintWriter out) {
	out.println("<div class='footer'>Built with Java Servlet technology • Skeleton analysis engine is running.</div>");
	out.println("</main>");
	out.println("</body>");
	out.println("</html>");
    }

    void sendForm(String subjectURL, PrintWriter out, Vector hist, HttpServletRequest req)
    {
	out.println("<section class='card'>");
	out.println("<h3>Analyze A New URL</h3>");
	out.println("<p class='muted'>Paste a full URL. The service will fetch headers and discover links, forms, and references.</p>");
	out.print("<form method='GET' action=\"");
	out.print(req.getRequestURI());
	out.println("\">");
	out.println("<input type='text' name='"+PARM+"' placeholder='https://example.com' value='" + ((subjectURL==null)?(""):(escapeHtml(subjectURL))) + "'>");
	out.println("<input type='submit' value='Analyze'>");
	out.println("<input type='reset' value='Clear'>");
	out.println("</form>");
	out.println("<p class='muted'>Tip: prefer canonical page URLs to reduce redirects and improve extracted link quality.</p>");
	if (hist != null && hist.size() > 0) {
	    out.println("<h3 style='margin-top:18px'>Recent Queries</h3>");
	    out.println("<p class='muted'>Click any previous URL to instantly re-run analysis.</p>");
	    out.println("<ul class='history'>");
	    Enumeration en = hist.elements();
	    while(en.hasMoreElements()) {
		out.print("<li><a href=\"");
		String u = (String)(en.nextElement());
		out.print(req.getRequestURI()+"?"+PARM+"=");
		out.print(URLEncoder.encode(u));
		out.println("\">"+escapeHtml(u)+"</a></li>");
	    }
	    out.println("</ul>");
	}
	out.println("</section>");
    }

    void sendError(String remark, PrintWriter out, Exception e) {
	out.println("<section class='card error'>");
	out.println("<h3>Error Report</h3>");
	out.println("<p class='muted'>The request could not be completed.</p>");
	out.println("<p><strong>Reason:</strong> " + escapeHtml(remark) + "</p>");
	if (e != null) {
	    out.println("<p><strong>Exception:</strong> <code>" + escapeHtml(e.toString()) + "</code></p>");
	}
	out.println("</section>");
    }

    void sendResults(String subjectURL, PrintWriter out, Hashtable headers, 
		     Vector r, HttpServletRequest req) {
	Enumeration en;
	out.println("<section class='card'>");
	out.println("<h3>Analysis Results</h3>");
	out.println("<p class='muted'><span class='ok'>Target:</span> " + escapeHtml(subjectURL) + "</p>");
	out.println("<div class='grid'>");
	out.println("<div>");
	out.println("<h3>Response Metadata</h3>");
	out.println("<div class='table-wrap'>");
	out.println("<table>");
	out.println("<tr><th>Header</th><th>Value</th></tr>");
	for(en = headers.keys(); en.hasMoreElements(); ) {
	    out.print("<tr><td>");
	    String ki = (String)(en.nextElement());
	    out.print(escapeHtml(ki));
	    out.print("</td>\n<td>");
	    out.print(escapeHtml((String)(headers.get(ki))));
	    out.println("</td></tr>");
	}
	out.println("</table>");
	out.println("</div>");
	out.println("</div>");
	out.println("<div>");
	out.println("<h3>Links And References</h3>");
	out.println("<ol class='link-list'>");
	for(en = r.elements(); en.hasMoreElements(); ) {
	    Ref ref = (Ref)(en.nextElement());
	    out.println("<li><span class='muted'>URL:</span> ");
	    out.print("<a target='wssview' href=\"");
	    out.print(ref.url.toExternalForm());
	    out.println("\">"+escapeHtml(ref.url.toExternalForm())+"</a>");
	    out.println("<br><span class='muted'>Tag:</span> &lt;"+escapeHtml(ref.tag)+"&gt; <span class='pill'>" + escapeHtml(ref.type) + "</span>");
	    if (probablySkeletonizable(ref)) {
		out.print(" <a class='analyze-btn' href=\"");
		out.print(req.getRequestURI() + "?" + PARM + "=");
		out.print(URLEncoder.encode(ref.url.toExternalForm()));
		out.println("\">Analyze This</a>");
	    }
	    out.println("</li>");
	}
	out.println("</ol>");
	out.println("</div>");
	out.println("</div>");
	out.println("</section>");
      return;
    }

    void sendIntro(PrintWriter out, HttpSession sess) {
	out.println("<section class='card'>");
	out.println("<h3>Introduction</h3>");
	out.println("<p class='muted'>Web Skeletonizer Service inspects any target webpage and creates a structured map of discovered links and references. It is useful for quick navigation audits, exploratory crawling, and dependency visibility.</p>");
	out.println("<p class='muted'>Enter a URL below to begin. Previous requests from your session will appear as shortcuts in the history area.</p>");
	out.println("</section>");
	return;
    }

    String escapeHtml(String s) {
	if (s == null) return "";
	StringBuffer b = new StringBuffer();
	for (int i = 0; i < s.length(); i++) {
	    char c = s.charAt(i);
	    switch (c) {
	    case '&': b.append("&amp;"); break;
	    case '<': b.append("&lt;"); break;
	    case '>': b.append("&gt;"); break;
	    case '"': b.append("&quot;"); break;
	    case '\'': b.append("&#39;"); break;
	    default: b.append(c);
	    }
	}
	return b.toString();
    }

    /**
     * Return false if it seems unlikely that this servlet
     * would be able to analyze the given reference, or
     * true otherwise.  Things we cannot analyze include
     * images, scripts, stylesheets.
     */
    boolean probablySkeletonizable(Ref ref) {
	if (ref.tag.equalsIgnoreCase("img")) return false;
	if (ref.tag.equalsIgnoreCase("embed")) return false;
	if (ref.tag.equalsIgnoreCase("script")) return false;
	if (ref.url.toString().indexOf(".gif") > 0) return false;
	if (ref.url.toString().indexOf(".GIF") > 0) return false;
	if (ref.url.toString().indexOf(".jpg") > 0) return false;
	if (ref.url.toString().indexOf(".JPG") > 0) return false;
	if (ref.url.toString().indexOf(".css") > 0) return false;
	if (ref.url.toString().indexOf(".map") > 0) return false;
	if (ref.url.toString().indexOf(".wav") > 0) return false;
	return true;
    }

}

