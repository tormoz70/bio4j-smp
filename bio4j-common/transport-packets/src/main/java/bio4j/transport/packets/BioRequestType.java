package bio4j.transport.packets;

public enum BioRequestType {
    Unassigned(0, ""),
    doPing(10, "bio4j.server.service.api.handlers.Ping"),
    doPostLoginForm(20, "Bio.Framework.Server.tm_login_post,Bio.Framework.Server.Types"),
    doLogout(30, "Bio.Framework.Server.tm_logout,Bio.Framework.Server.Types"),
    asmbVer(40, "Bio.Framework.Server.tm_asmb,Bio.Framework.Server.Types"),
    DS(50, "Bio.Framework.Server.tmio_DS,Bio.Framework.Server.Handler.DStore"),
    DSFetch(60, "Bio.Framework.Server.tmio_DSFetch,Bio.Framework.Server.Handler.DSFetch"),
    SQLR(70, "Bio.Framework.Server.tmio_SQLR,Bio.Framework.Server.Handler.SQLR"),
    WebDB(80, "Bio.Framework.Server.tmio_WebDB,Bio.Framework.Server.Handler.WebDB"),
    Rpt(90, "Bio.Framework.Server.tmio_rpt,Bio.Framework.Server.Handler.Rpt"),
    Tree(100, "Bio.Framework.Server.tmio_Tree,Bio.Framework.Server.Handler.Tree"),
    srvPipe(110, "Bio.Framework.Server.tmio_Pipe,Bio.Framework.Server.Handler.Pipe"),
    srvLongOp(120, "Bio.Framework.Server.tmio_LongOp,Bio.Framework.Server.Handler.LongOp"),
    DS2XL(130, "Bio.Framework.Server.tmio_DS2XL,Bio.Framework.Server.Handler.DS2XL"),
    FileSrv(140, "Bio.Framework.Server.tmio_FileSrv,Bio.Framework.Server.Handler.FileSrv");
	
	
	private final int code;
	private final String handlerTypeName;
	
	private BioRequestType(int code, String handlerTypeName) {
		this.code = code;
		this.handlerTypeName = handlerTypeName;
	}

	public int getCode() {
		return this.code;
	}
	
	public String getHandlerTypeName() {
		return this.handlerTypeName;
	}

	
}
