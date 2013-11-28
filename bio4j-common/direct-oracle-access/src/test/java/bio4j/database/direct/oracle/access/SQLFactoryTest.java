package bio4j.database.direct.oracle.access;

import bio4j.common.types.Params;
import bio4j.common.utils.Utl;
import bio4j.database.api.SQLCommand;
import bio4j.database.api.SQLConnectionPool;
import bio4j.database.api.SQLConnectionPoolConfigBuilder;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.sql.Connection;

public class SQLFactoryTest {
    private static final String testDBUrl = "jdbc:oracle:thin:@192.168.50.32:1521:EKBDB";
    private static final String testDBUsr = "GIVCAS";
    private static final String testDBPwd = "qwe";

    @Test
    public void testCreateSQLConnectionPool() throws Exception {
        SQLConnectionPool pool = SQLFactory.CreateSQLConnectionPool("TEST-CONN-POOL",
                new SQLConnectionPoolConfigBuilder()
                    .dbConnectionUrl(testDBUrl)
                    .dbConnectionUsr(testDBUsr)
                    .dbConnectionPwd(testDBPwd)
                    .build()
                );
        Connection conn = pool.getConnection();
        Assert.assertNotNull(conn);
        System.out.println(Utl.buildBeanStateInfo(pool.getStat(), null, null));
    }

    @Test
    public void testCreateSQLCommand() {
        SQLConnectionPool pool = SQLFactory.CreateSQLConnectionPool("TEST-CONN-POOL",
                new SQLConnectionPoolConfigBuilder()
                        .dbConnectionUrl(testDBUrl)
                        .dbConnectionUsr(testDBUsr)
                        .dbConnectionPwd(testDBPwd)
                        .build()
        );
        Connection conn = pool.getConnection();

        SQLCommand cmd = SQLFactory.CreateSQLCommand();
        String sql = "";
        cmd.init(conn, sql, new Params().add("", 101));
    }

}
