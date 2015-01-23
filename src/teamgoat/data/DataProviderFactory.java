package teamgoat.data;
// Tries to use db2, fallback to local sqlite.
// Our db2 subscription will expire eventually.
public class DataProviderFactory {
	private static UserLocationDataProvider instance;
	public static UserLocationDataProvider create() {
		Db2BigSqlDataProvider db2 = new Db2BigSqlDataProvider();
		if (db2.testConnection()) {
			return db2;
		}
		SqliteDataProvider sqlite = new SqliteDataProvider();
		if (sqlite.testConnection()) {
			return sqlite;
		}
		throw new DataAccessException("No db connection available.");
	}
	
	public static UserLocationDataProvider singleton() {
		if (instance == null) {
			instance = create();
		}
		return instance;
	}
}
