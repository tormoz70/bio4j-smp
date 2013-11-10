package bio4j.database.direct.oracle.access;

import java.sql.Connection;

import bio4j.database.api.Session;

public class SessionImpl implements Session {

	@Override
	public String getConnectionString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Connection getConnection() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void storeTransaction(String transactionName, Connection conn) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Connection RestoreTransaction(String transactionName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void killTransaction(String transactionName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void KillTransactions() {
		// TODO Auto-generated method stub
		
	}

}
