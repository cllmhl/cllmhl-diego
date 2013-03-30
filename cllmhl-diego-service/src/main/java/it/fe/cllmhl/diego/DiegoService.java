package it.fe.cllmhl.diego;

import it.fe.cllmhl.core.CoreErrors;
import it.fe.cllmhl.core.ILogger;
import it.fe.cllmhl.core.ServiceLocator;
import it.fe.cllmhl.core.UncheckedException;
import it.fe.cllmhl.sql.SqlErrors;
import it.fe.cllmhl.sql.service.DatasourceManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupDir;
import org.stringtemplate.v4.STGroupFile;

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

    private static void createDirectoryIfNotExists(String pStrFile) {
        File lFile = new File(pStrFile);
        if (!lFile.exists()) {
            lFile.mkdirs();
        }
    }

    public static void generate(DiegoDatabase pDiegoDatabase) {
        mLogger.debug("generate start on ",pDiegoDatabase);
        String lStrPackage = getRootPackage();
        mLogger.debug("using template group located at ", Template.TEMPLATE_HOME, " and package ", lStrPackage);
        for (DiegoTable lTable : pDiegoDatabase.getTables()) {
            mLogger.debug("start generating artifacts for table ", lTable.getName());
            List<Driver> lDrivers = getTableDrivers(lStrPackage, lTable);
            for (Driver lDriver : lDrivers) {
                mLogger.debug("Generating artifact with driver ", lDriver);
                ST lTemplate = lDriver.getTemplate();
                lTemplate.add("table", lTable);
                lTemplate.add("package", lStrPackage);
                generateFile(lTemplate, lDriver.getDirectory(), lDriver.getFileNme());
            }
            mLogger.debug("finish generating artifacts for table ", lTable.getName());
        }
//        mLogger.debug("Generating bean for table ", lTable.getName());
//        String lStrBasePath = Installation.getHomeDirectory() + ".." + File.separator + "src" + File.separator;
//        lStrBasePath += lStrPackage.replace(".", File.separator) + File.separator;
//        createDirectoryIfNotExists(lStrBasePath);
//        mLogger.debug("generating file in ", lStrBasePath);
//        // Bean
//        ST lTemplate = TemplateGroup.JAVA.getInstanceOf("tableBean");
//        mLogger.debug("Using template ", lTemplate.getName());
//        lTemplate.add("table", lTable);
//        lTemplate.add("package", lStrPackage);
//        String lStrModelPath = lStrBasePath + "model" + File.separator;
//        String lStrModelFile = lStrModelPath + lTable.getBean() + ".java";
//        mLogger.info("generating ", lStrModelFile);
//        generateFile(lTemplate, lStrModelPath, lStrModelFile);
//        // Manager
//        lTemplate = TemplateGroup.JAVA.getInstanceOf("tableManager");
//        mLogger.debug("Using template ", lTemplate.getName());
//        lTemplate.add("table", lTable);
//        lTemplate.add("package", lStrPackage);
//        String lStrLogicPath= lStrBasePath + "logic" + File.separator;
//        String lStrLogicFile = lStrLogicPath + lTable.getManager() + ".java";
//        mLogger.info("generating ", lStrLogicFile);
//        generateFile(lTemplate, lStrLogicPath, lStrLogicFile);
//        // Dao
//        lTemplate = TemplateGroup.JAVA.getInstanceOf("tableDao");
//        mLogger.debug("Using template ", lTemplate.getName());
//        lTemplate.add("table", lTable);
//        lTemplate.add("package", lStrPackage);
//        String lStrDaoFile = lStrLogicPath + lTable.getDao() + ".java";
//        mLogger.info("generating ", lStrDaoFile);
//        generateFile(lTemplate, lStrLogicPath, lStrDaoFile);
        mLogger.debug("generate finish");
   }

    public static void generateDao(DiegoDatabase pDiegoDatabase, List<Driver> pDrivers) {
        // mLogger.debug("generateDao start");
        // String lStrPackage = getRootPackage();
        // mLogger.debug("using template group located at ", TemplateGroup.JAVA, " and package ", lStrPackage);
        // for (DiegoTable lTable : pDiegoDatabase.getTables()) {
        // mLogger.debug("Generating bean for table ", lTable.getName());
        // String lStrBasePath = Installation.getHomeDirectory() + ".." + File.separator + "src" + File.separator;
        // lStrBasePath += lStrPackage.replace(".", File.separator) + File.separator;
        // createDirectoryIfNotExists(lStrBasePath);
        // mLogger.debug("generating file in ", lStrBasePath);
        // // Bean
        // ST lTemplate = TemplateGroup.JAVA.getInstanceOf("tableBean");
        // mLogger.debug("Using template ", lTemplate.getName());
        // lTemplate.add("table", lTable);
        // lTemplate.add("package", lStrPackage);
        // String lStrModelPath = lStrBasePath + "model" + File.separator;
        // String lStrModelFile = lStrModelPath + lTable.getBean() + ".java";
        // mLogger.info("generating ", lStrModelFile);
        // generateFile(lTemplate, lStrModelPath, lStrModelFile);
        // // Manager
        // lTemplate = TemplateGroup.JAVA.getInstanceOf("tableManager");
        // mLogger.debug("Using template ", lTemplate.getName());
        // lTemplate.add("table", lTable);
        // lTemplate.add("package", lStrPackage);
        // String lStrLogicPath= lStrBasePath + "logic" + File.separator;
        // String lStrLogicFile = lStrLogicPath + lTable.getManager() + ".java";
        // mLogger.info("generating ", lStrLogicFile);
        // generateFile(lTemplate, lStrLogicPath, lStrLogicFile);
        // // Dao
        // lTemplate = TemplateGroup.JAVA.getInstanceOf("tableDao");
        // mLogger.debug("Using template ", lTemplate.getName());
        // lTemplate.add("table", lTable);
        // lTemplate.add("package", lStrPackage);
        // String lStrDaoFile = lStrLogicPath + lTable.getDao() + ".java";
        // mLogger.info("generating ", lStrDaoFile);
        // generateFile(lTemplate, lStrLogicPath, lStrDaoFile);
        // }
        // mLogger.debug("generateDao finish");
    }

    private static void generateFile(ST pTemplate, String pStrDirectory, String pStrFile) {
        mLogger.debug("Start generateFile ", pStrFile, " in directory ", pStrDirectory, " from template ", pTemplate.getName());
        String lStrDirectoryFull = getProjectDirectory().concat(pStrDirectory).concat(File.separator);
        createDirectoryIfNotExists(lStrDirectoryFull);
        FileWriter lFileWriter = null;
        try {
            lFileWriter = new FileWriter(lStrDirectoryFull.concat(pStrFile));
            lFileWriter.write(pTemplate.render());
        } catch (IOException e) {
            mLogger.error(e);
            throw new UncheckedException(e, CoreErrors.IO, pStrFile);
        } finally {
            if (lFileWriter != null) {
                try {
                    lFileWriter.close();
                } catch (IOException e) {
                    // Ignored
                }
            }
        }
        mLogger.debug("generateFile finish");
    }

    // public static void generateJsp(DiegoDatabase pDiegoDatabase) {
    // mLogger.debug("generateJsp start");
    // StringTemplateGroup lStringTemplateGroup = new StringTemplateGroup("jsp", getTemplateDirectory() + File.separatorChar + "jsp");
    // mLogger.debug("using template group located at ", lStringTemplateGroup.getRootDir());
    // for (DiegoTable lTable : pDiegoDatabase.getTables()) {
    // mLogger.debug("Generating jsp for table ", lTable.getName());
    // String lStrBasePath = getGeneatedRoot() + "jsp" + File.separator + lTable.getJavaName() + File.separator;
    // mLogger.debug("generating file in ", lStrBasePath);
    // createDirectoryIfNotExists(lStrBasePath);
    // // Browser
    // StringTemplate lStringTemplate = lStringTemplateGroup.getInstanceOf("tableBrowser");
    // mLogger.debug("Using template ", lStringTemplate.getName());
    // lStringTemplate.setAttribute("table", lTable);
    // String lStrGeneratedFile = lStrBasePath + lTable.getJavaName() + "Browser.jsp";
    // mLogger.info("generating ", lStrGeneratedFile);
    // generateFile(lStringTemplate, lStrGeneratedFile);
    // // Form
    // lStringTemplate = lStringTemplateGroup.getInstanceOf("tableForm");
    // mLogger.debug("Using template ", lStringTemplate.getName());
    // lStringTemplate.setAttribute("table", lTable);
    // lStrGeneratedFile = lStrBasePath + lTable.getJavaName() + "Form.jsp";
    // mLogger.info("generating ", lStrGeneratedFile);
    // generateFile(lStringTemplate, lStrGeneratedFile);
    // // Row
    // lStringTemplate = lStringTemplateGroup.getInstanceOf("tableRow");
    // mLogger.debug("Using template ", lStringTemplate.getName());
    // lStringTemplate.setAttribute("table", lTable);
    // lStrGeneratedFile = lStrBasePath + lTable.getJavaName() + "Row.jspf";
    // mLogger.info("generating ", lStrGeneratedFile);
    // generateFile(lStringTemplate, lStrGeneratedFile);
    // }
    // mLogger.debug("generateJsp finish");
    // }

    // public static void generateServlet(DiegoDatabase pDiegoDatabase) {
    // mLogger.debug("generateServlet start");
    // StringTemplateGroup lStringTemplateGroup = new StringTemplateGroup("java", getTemplateDirectory() + File.separatorChar + "java");
    // String lStrPackage = getRootPackage();
    // mLogger.debug("using template group located at ", lStringTemplateGroup.getRootDir(), " and package ", lStrPackage);
    // for (DiegoTable lTable : pDiegoDatabase.getTables()) {
    // mLogger.debug("Generating servlet for table ", lTable.getName());
    // String lStrBasePath = getGeneatedRoot() + File.separator;
    // lStrBasePath += lStrPackage.replace(".", File.separator) + File.separator;
    // lStrBasePath += "servlet" + File.separator;
    // mLogger.debug("generating file in ", lStrBasePath);
    // createDirectoryIfNotExists(lStrBasePath);
    // // Servlet
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

    private static String getProjectDirectory() {
        mLogger.debug("getProjectDirectory start");
        ST lTemplate = getProjectGroup().getInstanceOf(Template.POJECT_DIRECTORY.name);
        String lStrProjectDirectory = Template.TEMPLATE_HOME.concat(lTemplate.render());
        mLogger.debug("getProjectDirectory finish returning ", lStrProjectDirectory);
        return lStrProjectDirectory;
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

    public static DiegoDatabase getDatabase(String pStrDatasource) {
        List<DiegoDatabase> lDatabaseList = getDatabases();
        DiegoDatabase myDiegoDatabase = null;
        for (DiegoDatabase lDiegoDatabase : lDatabaseList) {
            if(lDiegoDatabase.getDatasource().equalsIgnoreCase(pStrDatasource)){
                myDiegoDatabase = lDiegoDatabase;
            }
        }
        return myDiegoDatabase;
    }

    private static STGroup getDriverGroup(String pStrDirectory) {
        return new STGroupFile(Template.TEMPLATE_HOME.concat(pStrDirectory).concat(File.separator).concat(Template.DRIVER.name),Template.TEMPLATE_DELIMITER_CHAR,Template.TEMPLATE_DELIMITER_CHAR);
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

    public static List<Driver> getTableDrivers(String pStrPackage, DiegoTable pDiegoTable) {
        mLogger.debug("getProjectDrivers start");
        // directories to enter are defined in SUB_DIRECTORIES template
        List<Driver> lDrivers = new ArrayList<Driver>();
        ST lSTDirectories = getProjectGroup().getInstanceOf(Template.SUB_DIRECTORIES.name);
        String lStrDirectories = lSTDirectories.render();
        mLogger.debug("subdirectorie: ", lStrDirectories);
        // In each sub directory we have a driver.stg
        String[] lDirectories = lStrDirectories.split(",");
        for (String lStrDirectory : lDirectories) {
            mLogger.debug("working in directory: ", lStrDirectory);
            STGroup directoryDriverGroup = getDirectoryDriverGroup(lStrDirectory);
            STGroup driverGroup = getDriverGroup(lStrDirectory);
            Set<String> lTemplateNames = driverGroup.getTemplateNames();
            for (String lStrTemplate : lTemplateNames) {
                ST lTemplate = driverGroup.getInstanceOf(lStrTemplate);
                lTemplate.add("table", pDiegoTable);
                lTemplate.add("package", pStrPackage);
                String driver = lTemplate.render();
                mLogger.debug("building driver: ", driver);
                String[] lStrDriverPiece = driver.split(",");
                Driver lDriver = new Driver(directoryDriverGroup,lStrDriverPiece[0], lStrDriverPiece[1], lStrDriverPiece[2]);
                lDrivers.add(lDriver);
                mLogger.debug("completed driver: ", lDriver);
            }
        }
        mLogger.debug("getProjectDrivers finish returning ", lDrivers);
        return lDrivers;
    }

    private static STGroup getProjectGroup() {
        return new STGroupFile(Template.TEMPLATE_HOME.concat(Template.PROJECT.name),Template.TEMPLATE_DELIMITER_CHAR,Template.TEMPLATE_DELIMITER_CHAR);
    }
    
    private static STGroup getDirectoryDriverGroup(String directory) {
        return new STGroupDir(Template.TEMPLATE_HOME.concat(directory),Template.TEMPLATE_DELIMITER_CHAR,Template.TEMPLATE_DELIMITER_CHAR);
    }

    private static String getRootPackage() {
        mLogger.debug("getRootPackage start");
        ST lSTRootPackage = getProjectGroup().getInstanceOf(Template.ROOT_PACKAGE.name);
        String lStrRootPackage = lSTRootPackage.render();
        mLogger.debug("getRootPackage finish returning ", lStrRootPackage);
        return lStrRootPackage;
    }
}
