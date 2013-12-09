package bio4j.database.direct.oracle.access;

import ru.bio4j.smp.common.types.Params;
import ru.bio4j.smp.common.utils.Utl;
import ru.bio4j.smp.database.api.SQLCommand;
import ru.bio4j.smp.database.api.SQLConnectionPool;
import ru.bio4j.smp.database.api.SQLConnectionPoolConfigBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.sql.Connection;

public class SQLFactoryTest {
    private static final Logger LOG = LoggerFactory.getLogger(SQLFactoryTest.class);
    private static final String testDBUrl = "jdbc:oracle:thin:@192.168.50.32:1521:EKBDB";
    //private static final String testDBUrl = "jdbc:oracle:oci:@GIVCDB_EKBS03";
    private static final String testDBUsr = "GIVCAS";
    private static final String testDBPwd = "qwe";

    private static SQLConnectionPool pool;

    @BeforeTest
    public static void setUpClass() throws Exception {
        pool = OraFactory.CreateSQLConnectionPool("TEST-CONN-POOL",
                new SQLConnectionPoolConfigBuilder()
                        .dbConnectionUrl(testDBUrl)
                        .dbConnectionUsr(testDBUsr)
                        .dbConnectionPwd(testDBPwd)
                        .build()
        );
    }

    @AfterTest
    public static void finClass() throws Exception {
        //pool.
    }

    @Test
    public void testCreateSQLConnectionPool() throws Exception {
        Connection conn = pool.getConnection();
        Assert.assertNotNull(conn);
        LOG.debug(Utl.buildBeanStateInfo(pool.getStat(), null, null));
    }

    @Test(enabled = true)
    public void testCreateSQLCommand() {
        Connection conn = pool.getConnection();

        SQLCommand cmd = OraFactory.CreateSQLCommand();
        String sql = "select user as curuser, :dummy as dm, :dummy1 as dm1 from dual";
        LOG.debug("conn: " + conn);
        cmd.init(conn, sql, new Params().add("dummy", 101));
        cmd.openCursor(null);
        Double dummysum = 0.0;
        while(cmd.next()){
            dummysum += cmd.getValue(Double.class, "DM");
        }
        LOG.debug("dummysum: "+dummysum);
        Assert.assertEquals(dummysum, 101.0);
    }

}
