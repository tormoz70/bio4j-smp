package bio4j.server.service.srvc;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import bio4j.server.common.BioRequestType;
import bio4j.server.common.BioServletBase;


@SuppressWarnings("serial")
@WebServlet(urlPatterns = "/srvc")
public class BioSrvcServlet extends BioServletBase {
	public static Logger LOG = LoggerFactory.getLogger(BioSrvcServlet.class);

    private final String csRequestTypeParamName = "rqtp";
    private final String csBioCodeParamName = "rqbc";
    private final String csQParamName = "rqpckt";
	
	@Override
	protected void processRequest(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, java.io.IOException {
		BioRequestType rqType = BioRequestType.Unassigned;
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();
		out.println("<HTML> <HEAD> <TITLE> Login " + ((this.getEnvironment() != null) ? this.getEnvironment().getSystemName() : "Err")
				+ "</TITLE> </HEAD> <BODY BGCOLOR=white>");

		try {
			out.println("LocalAddr:"+req.getLocalAddr()+"<p/>");
			out.println("LocalName:"+req.getLocalName()+"<p/>");
			out.println("ContextPath:"+req.getContextPath()+"<p/>");
			out.println("PathInfo:"+req.getPathInfo()+"<p/>");
			out.println("PathTranslated:"+req.getPathTranslated()+"<p/>");
			out.println("QueryString:"+req.getQueryString()+"<p/>");
			out.println("RequestURI:"+req.getRequestURI()+"<p/>");
			out.println("Session:"+req.getSession()+"<p/>");
			out.println("OK");
		} catch (Exception e) {
			out.println("Error:"+e.toString());
		}
		out.println("</BODY> </HTML> ");
	}

	@Override
	protected void processRequestError(Exception ex, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Обработать непредвиденную ошибку! В лог ошибок надо писать.
	}
	
}
