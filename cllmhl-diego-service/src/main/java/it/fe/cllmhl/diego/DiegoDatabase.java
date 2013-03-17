package it.fe.cllmhl.diego;

import it.fe.cllmhl.core.ILogger;
import it.fe.cllmhl.core.ServiceLocator;
import it.fe.cllmhl.core.UncheckedException;
import it.fe.cllmhl.sql.SqlErrors;
import it.fe.cllmhl.sql.service.DatasourceManager;

import java.sql.Connection;
import java.sql.SQLException;

import schemacrawler.schema.Database;
import schemacrawler.schemacrawler.InclusionRule;
import schemacrawler.schemacrawler.SchemaCrawlerException;
import schemacrawler.schemacrawler.SchemaCrawlerOptions;
import schemacrawler.utility.SchemaCrawlerUtility;

public class DiegoDatabase {
    private static ILogger mLogger = ServiceLocator.getLogService().getLogger(DiegoDatabase.class);
	private String connectionName;
	private Database database;
	
	public DiegoDatabase(String pStrConnection, SchemaCrawlerOptions pSchemaCrawlerOptions) {
		this.connectionName = pStrConnection;
		//Ignore any kind of routine
		pSchemaCrawlerOptions.setRoutineInclusionRule(InclusionRule.EXCLUDE_ALL);
		// Get the schema definition
		Connection lConnection = DatasourceManager.getConnection(pStrConnection);
		try {
			this.database = SchemaCrawlerUtility.getDatabase(lConnection, pSchemaCrawlerOptions);
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
	}
//
//	public String getConnectionName() {
//		return connectionName;
//	}
//
//	public DiegoSchema getSchema(String schema) {
//		return new DiegoSchema(database.getSchema(schema));
//	}
//
//	public List<DiegoSchema> getSchemas() {
//		List<DiegoSchema> schemas = new ArrayList<DiegoSchema>();
//		for (schemacrawler.schema.Schema schema : database.getSchemas()) {
//			schemas.add(new DiegoSchema(schema));
//		}
//		return schemas;
//	}
}
