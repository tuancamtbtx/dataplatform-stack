package vn.bigdata.spark.etl.processor.config;

public class ConfigKey {
    public static String PARTITIONS = "PARTITIONS";
    public static String PARTITION_COLS = "PARTITION_COLS";
    public static String ENCODING = "ENCODING";
    public static String OUTPUT_MODE = "OUTPUT_MODE";
    public static String INCLUDE_COLS = "INCLUDE_COLS";
    public static String EXCLUDE_COLS = "EXCLUDE_COLS";

    public static String KEEP_ORG_COLS = "KEEP_ORG_COLS";
    public static String KEEP_ORG_COLS_APPEND = "APPEND";
    public static String KEEP_ORG_COLS_PREPEND = "PREPEND";
    public static String KEEP_ORG_COLS_PAIR = "PAIR"; // Default for `KEEP_ORG_COLS`

    // FILTERING
    public static String NOT_IN_DATASETS = "NOT_IN_DATASETS"; // Comma separated dataset config names

    // JOIN/UNION
    public static String OTHER_DATASETS = "OTHER_DATASETS"; // Comma separated dataset config names
    public static String JOIN_TYPE = "JOIN_TYPE";

    // SOURCE CONF
    public static String PATHS = "PATHS";
    public static String FORMAT = "FORMAT";
    public static String SCHEMA = "SCHEMA";
    public static String TRIGGER = "TRIGGER";
    public static String STREAMING = "STREAMING";
    public static String HIVE_TABLE = "HIVE_TABLE";

    // COMMON app configs
    public static String CONF_APP_NAME = "APP_NAME";
    public static String CONF_QUERY_NAME = "QUERY_NAME";
    public static String VERBOSE = "VERBOSE"; // Value = Int (1,2,3,...) or will be `length` of string (v->1, vv->2, vvv->3, ...)
    public static String ENABLE_STATS = "ENABLE_STATS";
    public static String ENABLE_HIVE_SUPPORT = "ENABLE_HIVE_SUPPORT";
    public static String STORAGE_LEVEL = "STORAGE_LEVEL";

    // Checkpoint configs for batch jobs
    public static String EAGER = "EAGER"; // Value is true or false, default is true
    public static String checkpointDir = "checkpointDir";
    public static String cleanCheckpoints = "cleanCheckpoints";
}
