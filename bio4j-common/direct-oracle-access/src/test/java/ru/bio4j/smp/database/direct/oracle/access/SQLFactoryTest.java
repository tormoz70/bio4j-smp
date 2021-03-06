package ru.bio4j.smp.database.direct.oracle.access;

import ru.bio4j.smp.common.types.Direction;
import ru.bio4j.smp.common.types.ParamBuilder;
import ru.bio4j.smp.common.types.Params;
import ru.bio4j.smp.common.utils.ConvertValueException;
import ru.bio4j.smp.common.utils.Utl;
import ru.bio4j.smp.database.api.SQLCursor;
import ru.bio4j.smp.database.api.SQLConnectionPool;
import ru.bio4j.smp.database.api.SQLConnectionPoolConfigBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import ru.bio4j.smp.database.api.SQLStoredProc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class SQLFactoryTest {
    private static final Logger LOG = LoggerFactory.getLogger(SQLFactoryTest.class);
    private static final String testDBUrl = "jdbc:oracle:thin:@192.168.50.32:1521:EKBDB";
    //private static final String testDBUrl = "jdbc:oracle:oci:@GIVCDB_EKBS03";
    //private static final String testDBUrl = "jdbc:oracle:thin:@https://databasetrial0901-rugivcmkrftrial07058.db.em1.oraclecloudapps.com/apex:1521:databasetrial0901";
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
                                //.currentSchema("GIVCAPI")
                        .build()
        );
        try {
            try (Connection conn = pool.getConnection()) {
                CallableStatement cs1 = conn.prepareCall( "create table test_tbl(fld1 varchar2(10), fld2 number)");
                cs1.execute();
                CallableStatement cs2 = conn.prepareCall( "create or replace procedure test_stored_prop(p_param1 in varchar2, p_param2 out number) is begin insert into test_tbl values('test', 1); p_param2 := length(p_param1); end;");
                cs2.execute();
                CallableStatement cs3 = conn.prepareCall( "create or replace procedure test_stored_error(p_param1 in varchar2, p_param2 out number) is begin raise_application_error(-20000, 'FTW'); end;");
                cs3.execute();
            }
        } catch (SQLException ex) {
            LOG.error("Error!", ex);
        }
    }

    @AfterTest
    public static void finClass() throws Exception {
        try {
            try (Connection conn = pool.getConnection()) {
                CallableStatement cs1 = conn.prepareCall( "drop procedure test_stored_prop");
                cs1.execute();
                CallableStatement cs2 = conn.prepareCall( "drop table test_tbl");
                cs2.execute();
            }
        } catch (SQLException ex) {
            LOG.error("Error!", ex);
        }
    }

    @Test
    public void testCreateSQLConnectionPool() throws Exception {
        Connection conn = pool.getConnection();
        Assert.assertNotNull(conn);
        LOG.debug(Utl.buildBeanStateInfo(pool.getStat(), null, null));
    }

    @Test(enabled = true)
    public void testSQLCommandOpenCursor() {
        try {
            Double dummysum = 0.0;
            try (Connection conn = pool.getConnection()) {
                String sql = "select user as curuser, :dummy as dm, :dummy1 as dm1 from dual";
                Params prms = new Params().add("dummy", 101);
                try(SQLCursor c = OraFactory.CreateSQLCursor()
                        .init(conn, sql, prms).open();){
                    while(c.next()){
                        dummysum += c.getValue(Double.class, "DM");
                    }
                }
            }
            LOG.debug("dummysum: "+dummysum);
            Assert.assertEquals(dummysum, 101.0);
        } catch (Exception ex) {
            LOG.error("Error!", ex);
            Assert.fail();
        }

    }

    @Test(enabled = true)
    public void testSQLCommandOpenCursor1() {
        try {
            Double dummysum = 0.0;
            byte[] schema = null;
            try (Connection conn = pool.getConnection()) {
                String sql = "select * from table(givcapi.upld.get_schemas)";
                try(SQLCursor c = OraFactory.CreateSQLCursor()
                        .init(conn, sql, null).open();){
                    while(c.next()){
                        dummysum++;
                        if(schema == null){
                            schema = c.getValue(byte[].class, "XSD_BODY");
                        }

                    }
                }
            }
            LOG.debug("dummysum: "+dummysum);
            Assert.assertEquals(dummysum, 4.0);
        } catch (Exception ex) {
            LOG.error("Error!", ex);
            Assert.fail();
        }

    }

    @Test(enabled = true)
    public void testSQLCommandExecSQL() throws Exception {
        try {
            int leng = 0;
            try (Connection conn = pool.getConnection()) {
                LOG.debug("conn: " + conn);

                SQLStoredProc cmd = OraFactory.CreateSQLStoredProc();
                String storedProgName = "test_stored_prop";
                Params params = new Params();
                params.add("p_param1", "FTW")
                      .add(params.builder()
                              .name("p_param2")
                              .type(Integer.class)
                              .direction(Direction.Output)
                              .build());
                cmd.init(conn, storedProgName, params).execSQL();
                leng = Utl.nvl(cmd.getParams().getParam("p_param2").getValue(Integer.class), 0);
                conn.rollback();

            }
            LOG.debug("leng: " + leng);
            Assert.assertEquals(leng, 3);
        } catch (SQLException ex) {
            LOG.error("Error!", ex);
            Assert.fail();
        }
    }

    @Test(enabled = true)
    public void testSQLCommandExecError() throws Exception {
        try {
            try (Connection conn = pool.getConnection()) {
                LOG.debug("conn: " + conn);

                SQLStoredProc cmd = OraFactory.CreateSQLStoredProc();
                String storedProgName = "test_stored_error";
                Params params = new Params();
                params.add("p_param1", "FTW")
                        .add(params.builder()
                                .name("p_param2")
                                .type(Integer.class)
                                .direction(Direction.Output)
                                .build());
                cmd.init(conn, storedProgName, params).execSQL();
            }
        } catch (SQLException ex) {
            LOG.error("Error!", ex);
            Assert.assertEquals(ex.getErrorCode(), 20000);
        }
    }
    
    @Test(enabled = false)
    public void testSQLCommandExecSQLAutoCommit() throws Exception {
        try {
            int leng = 0;
            try (Connection conn = pool.getConnection()) {
                LOG.debug("conn: " + conn);

                SQLStoredProc cmd = OraFactory.CreateSQLStoredProc();
                String storedProgName = "test_stored_prop";
                Params params = new Params();
                params.add("p_param1", "FTW")
                        .add(params.builder()
                                .name("p_param2")
                                .type(Integer.class)
                                .direction(Direction.Output)
                                .build());
                cmd.init(conn, storedProgName, params).execSQL();
                leng = Utl.nvl(cmd.getParams().getParam("p_param21").getValue(Integer.class), 0);
            }
            LOG.debug("leng: " + leng);
            Assert.assertEquals(leng, 3);
        } catch (Exception ex) {
            LOG.error("Error!", ex);
            Assert.fail();
        }
    }
      
    private static <T> T getParamValue(Params params, Class<T> type, String paramName) throws SQLException {
        try {
            return params.getValueByName(type, paramName, true);
        } catch (ConvertValueException ex) {
            throw new SQLException(ex);
        }
    }

    @Test(enabled = false)
    public void testSQLCommandStoredProc() {
        try {
            int role = -1;
            int org_id = -1;
            try (Connection conn = pool.getConnection()) {
                    SQLStoredProc prc = OraFactory.CreateSQLStoredProc();
                    Params params = prc.getParams();
                    params.add("p_user_name", "coordinator")
                          .add("p_password", "siolopon")
                          .add(params.builder().name("v_role_id").type(int.class).direction(Direction.Output).build())
                          .add(params.builder().name("v_org_id").type(int.class).direction(Direction.Output).build());
                    prc.init(conn, "gacc.check_login", params).execSQL();
                    role = getParamValue(params, int.class, "v_role_id");
                    org_id = getParamValue(params, int.class, "v_org_id");
                    LOG.debug(String.format("Login: OK; role: %d; org_id: %d", role, org_id));
            }
            Assert.assertEquals(role, 6);
        } catch (SQLException ex) {
            LOG.error("Error!!!", ex);
        }
    }

//    @Test(enabled = true)
//    public void testCallStoredProc() {
//        try {
//            try (Connection conn = pool.getConnection()) {
//                CallableStatement cs = conn.prepareCall( "{call test_stored_prop(:p_param1, :p_param2)}");
//                cs.setString("p_param1", "FTW");
//                cs.registerOutParameter("p_param2", Types.INTEGER);
//                cs.execute();
//                int leng = cs.getInt("p_param2");
//                Assert.assertEquals(leng, 3);
//            }
//        } catch (SQLException ex) {
//            LOG.error("Error!", ex);
//        }
//    }

}
