package it.fe.cllmhl.diego;

import it.fe.cllmhl.core.ILogger;
import it.fe.cllmhl.core.ServiceLocator;
import it.fe.cllmhl.core.UncheckedException;
import it.fe.cllmhl.sql.SqlErrors;
import it.fe.cllmhl.sql.service.DatasourceManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import schemacrawler.schema.Database;
import schemacrawler.schemacrawler.InclusionRule;
import schemacrawler.schemacrawler.SchemaCrawlerException;
import schemacrawler.schemacrawler.SchemaCrawlerOptions;
import schemacrawler.schemacrawler.SchemaInfoLevel;
import schemacrawler.utility.SchemaCrawlerUtility;

public final class DiegoService {

    private static ILogger mLogger = ServiceLocator.getLogService().getLogger(DiegoService.class);

    private static Database crawlDatabase(String pStrConnection, SchemaCrawlerOptions pSchemaCrawlerOptions) {
        Database lDatabase;
        Connection lConnection = DatasourceManager.getConnection(pStrConnection);
        try {
            lDatabase = SchemaCrawlerUtility.getDatabase(lConnection, pSchemaCrawlerOptions);
        } catch (SchemaCrawlerException e) {
            mLogger.error(e);
            throw new UncheckedException(e, SqlErrors.SQL, e.getMessage());
        } finally {
            try {
                lConnection.close();
            } catch (SQLException e) {
                // Ignore
            }
        }
        return lDatabase;
    }

    public static List<DiegoDatabase> getDatabases() {
        mLogger.debug("getDatabases start");
        List<DiegoDatabase> lDatabaseList = new ArrayList<DiegoDatabase>();
        Collection<String> lDatasourceList = DatasourceManager.getAvailableDatasources();
        // Create the options
        SchemaCrawlerOptions lSchemaCrawlerOptions = new SchemaCrawlerOptions();
        lSchemaCrawlerOptions.setSchemaInfoLevel(SchemaInfoLevel.minimum());
        // Create a database for every datasource
        for (String lStrDatasource : lDatasourceList) {
            mLogger.debug("Creating basic info for datasource ", lStrDatasource);
            Database lCrawlDatabase = crawlDatabase(lStrDatasource, lSchemaCrawlerOptions);
            lDatabaseList.add(new DiegoDatabase(lStrDatasource, lCrawlDatabase));
        }
        mLogger.debug("getDatabases finish");
        return lDatabaseList;
    }

    public static DiegoDatabase getMetadata(CodeGeneratorBean pCodeGeneratorBean) {
        mLogger.debug("getMetadata start");
        // Create the options
        final SchemaCrawlerOptions lSchemaCrawlerOptions = new SchemaCrawlerOptions();
        // Using info standard
        lSchemaCrawlerOptions.setSchemaInfoLevel(SchemaInfoLevel.standard());
        // Filters
        if (pCodeGeneratorBean.getSchema() != null) {
            lSchemaCrawlerOptions.setSchemaInclusionRule(new InclusionRule(pCodeGeneratorBean.getSchema(), InclusionRule.NONE));
        }
        // Tables like %test% in every possible catalog/schema
        if (pCodeGeneratorBean.getTable() != null) {
            lSchemaCrawlerOptions.setTableInclusionRule(new InclusionRule(".*\\..*" + pCodeGeneratorBean.getTable() + ".*", InclusionRule.NONE));
        }
        // Crawling
        Database lDatabase = crawlDatabase(pCodeGeneratorBean.getConnection(), lSchemaCrawlerOptions);
        DiegoDatabase lDiegoDatabase = new DiegoDatabase(pCodeGeneratorBean.getConnection(), lDatabase);
        mLogger.debug("getMetadata finish ", lDiegoDatabase);
        return lDiegoDatabase;
    }

    //
    // public static void generateJsp(DiegoSchema pSchema) {
    // mLogger.debug("generateJsp start");
    // StringTemplateGroup lStringTemplateGroup = new StringTemplateGroup("jsp", Application.getConfigBean().getAbsolutePath() + ".." + File.separator + "template" + File.separatorChar + "jsp");
    // mLogger.debug("using template group located at ",lStringTemplateGroup.getRootDir());
    // for (DiegoTable lTable : pSchema.getTables()) {
    // mLogger.debug("Generating jsp for table ", lTable.getName());
    // String lStrBasePath = ConfigManager.getConfigBean().getAbsolutePath() + "jsp" + File.separator + lTable.getJavaName() + File.separator;
    // mLogger.debug("generating file in ",lStrBasePath);
    // createDirectoryIfNotExists(lStrBasePath);
    // //Browser
    // StringTemplate lStringTemplate = lStringTemplateGroup.getInstanceOf("tableBrowser");
    // mLogger.debug("Using template ", lStringTemplate.getName());
    // lStringTemplate.setAttribute("table", lTable);
    // String lStrGeneratedFile = lStrBasePath + lTable.getJavaName() + "Browser.jsp";
    // mLogger.info("generating ", lStrGeneratedFile);
    // generateFile(lStringTemplate, lStrGeneratedFile);
    // //Form
    // lStringTemplate = lStringTemplateGroup.getInstanceOf("tableForm");
    // mLogger.debug("Using template ", lStringTemplate.getName());
    // lStringTemplate.setAttribute("table", lTable);
    // lStrGeneratedFile = lStrBasePath + lTable.getJavaName() + "Form.jsp";
    // mLogger.info("generating ", lStrGeneratedFile);
    // generateFile(lStringTemplate, lStrGeneratedFile);
    // //Row
    // lStringTemplate = lStringTemplateGroup.getInstanceOf("tableRow");
    // mLogger.debug("Using template ", lStringTemplate.getName());
    // lStringTemplate.setAttribute("table", lTable);
    // lStrGeneratedFile = lStrBasePath + lTable.getJavaName() + "Row.jspf";
    // mLogger.info("generating ", lStrGeneratedFile);
    // generateFile(lStringTemplate, lStrGeneratedFile);
    // }
    // mLogger.debug("generateJsp finish");
    // }
    //
    // private static void createDirectoryIfNotExists(String pStrFile) {
    // File lFile = new File(pStrFile);
    // if (!lFile.exists())
    // lFile.mkdir();
    // }
    //
    // private static void generateFile(StringTemplate pStringTemplate, String pStrFile) {
    // mLogger.debug("Start generateFile ",pStrFile," from template ", pStringTemplate.getName());
    // FileWriter lFileWriter = null;
    // try {
    // lFileWriter = new FileWriter(pStrFile);
    // lFileWriter.write(pStringTemplate.toString());
    // } catch (IOException e) {
    // mLogger.error(e);
    // throw new UncheckedException(e, EtError.FILE_CREATION, pStrFile);
    // } finally {
    // if (lFileWriter != null)
    // try {
    // lFileWriter.close();
    // } catch (IOException e) {
    // //Ignored
    // }
    // }
    // mLogger.debug("generateFile finish");
    // }
    //
    // public static void generateDao(DiegoSchema pSchema) {
    // mLogger.debug("generateDao start");
    // StringTemplateGroup lStringTemplateGroup = new StringTemplateGroup("java", ConfigManager.getConfigBean().getAbsolutePath() + ".." + File.separator + "template" + File.separatorChar + "java");
    // String lStrPackage = ConfigManager.getConfigBean().getApplicationPackage();
    // mLogger.debug("using template group located at ",lStringTemplateGroup.getRootDir()," and package ",lStrPackage);
    // for (DiegoTable lTable : pSchema.getTables()) {
    // mLogger.debug("Generating servlet for table ", lTable.getName());
    // String lStrBasePath = ConfigManager.getConfigBean().getAbsolutePath() + ".." + File.separator + "src" + File.separator;
    // lStrBasePath += lStrPackage.replace(".", File.separator) + File.separator;
    // createDirectoryIfNotExists(lStrBasePath);
    // mLogger.debug("generating file in ",lStrBasePath);
    // //Bean
    // StringTemplate lStringTemplate = lStringTemplateGroup.getInstanceOf("tableBean");
    // mLogger.debug("Using template ", lStringTemplate.getName());
    // lStringTemplate.setAttribute("table", lTable);
    // lStringTemplate.setAttribute("package", lStrPackage);
    // String lStrModelPath = lStrBasePath + "model" + File.separator;
    // createDirectoryIfNotExists(lStrModelPath);
    // String lStrModelFile = lStrModelPath + lTable.getBean() + ".java";
    // mLogger.info("generating ", lStrModelFile);
    // generateFile(lStringTemplate, lStrModelFile);
    // //Manager
    // lStringTemplate = lStringTemplateGroup.getInstanceOf("tableManager");
    // mLogger.debug("Using template ", lStringTemplate.getName());
    // lStringTemplate.setAttribute("table", lTable);
    // lStringTemplate.setAttribute("package", lStrPackage);
    // String lStrLogicPath = lStrBasePath + "logic" + File.separator;
    // createDirectoryIfNotExists(lStrLogicPath);
    // String lStrLogicFile = lStrLogicPath + lTable.getManager() + ".java";
    // mLogger.info("generating ", lStrLogicFile);
    // generateFile(lStringTemplate, lStrLogicFile);
    // //Dao
    // lStringTemplate = lStringTemplateGroup.getInstanceOf("tableDao");
    // mLogger.debug("Using template ", lStringTemplate.getName());
    // lStringTemplate.setAttribute("table", lTable);
    // lStringTemplate.setAttribute("package", lStrPackage);
    // String lStrDaoFile = lStrLogicPath + lTable.getDao() + ".java";
    // mLogger.info("generating ", lStrDaoFile);
    // generateFile(lStringTemplate, lStrDaoFile);
    // }
    // mLogger.debug("generateDao finish");
    // }
    //
    // public static void generateServlet(DiegoSchema pSchema) {
    // mLogger.debug("generateServlet start");
    // StringTemplateGroup lStringTemplateGroup = new StringTemplateGroup("java", ConfigManager.getConfigBean().getAbsolutePath() + ".." + File.separator + "template" + File.separatorChar + "java");
    // String lStrPackage = ConfigManager.getConfigBean().getApplicationPackage();
    // mLogger.debug("using template group located at ",lStringTemplateGroup.getRootDir()," and package ",lStrPackage);
    // for (DiegoTable lTable : pSchema.getTables()) {
    // mLogger.debug("Generating servlet for table ", lTable.getName());
    // String lStrBasePath = ConfigManager.getConfigBean().getAbsolutePath() + ".." + File.separator + "src" + File.separator;
    // lStrBasePath += lStrPackage.replace(".", File.separator) + File.separator;
    // lStrBasePath += "servlet" + File.separator;
    // mLogger.debug("generating file in ",lStrBasePath);
    // createDirectoryIfNotExists(lStrBasePath);
    // //Servlet
    // StringTemplate lStringTemplate = lStringTemplateGroup.getInstanceOf("tableServlet");
    // mLogger.debug("Using template ", lStringTemplate.getName());
    // lStringTemplate.setAttribute("table", lTable);
    // lStringTemplate.setAttribute("package", lStrPackage);
    // String lStrGeneratedFile = lStrBasePath + lTable.getServlet() + ".java";
    // mLogger.info("generating ", lStrGeneratedFile);
    // generateFile(lStringTemplate, lStrGeneratedFile);
    // }
    // mLogger.debug("generateServlet finish");
    // }
}
