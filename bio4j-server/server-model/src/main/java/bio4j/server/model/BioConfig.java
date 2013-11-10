package bio4j.server.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="config")
public class BioConfig {
	
	@XmlElement(name="app_title")
	private String applicationTitle;
	public String getApplicationTitle() {
		return this.applicationTitle;
	}

	@XmlElement(name="db_connection")
	private String dbConnectionString;
	public String getDbConnectionString() {
		return this.dbConnectionString;
	}
	
	@XmlElement(name="workspace_path")
	private String workspacePath;
	public String getWorkspacePath() {
		return this.workspacePath;
	}
	
	@XmlElement(name="debug", type=Boolean.class)
	private Boolean _isDebug;
	public Boolean isDebug() {
		return this._isDebug;
	}
	
}
