import logging
import logging.config
import coloredlogs


config_initial = {
    "version": 1,
    "formatters": {
        "detailed": {
            "class": "logging.Formatter",
            "format": "%(asctime)s %(levelname)-8s %(processName)-10s %(message)s",
        }
    },
    "handlers": {
        "console": {
            "class": "logging.StreamHandler",
            "level": logging.DEBUG,
            "formatter": "detailed",
        },
    },
    "root": {"level": logging.DEBUG, "handlers": ["console"]},
    "aiokafka": {
        "level": logging.WARN,
    },
    "kafka": {
        "level": logging.WARN,
    },
}

logging.config.dictConfig(config_initial)
logging.getLogger("peewee").setLevel(logging.WARN)
logging.getLogger("urllib3").setLevel(logging.WARN)
logging.getLogger("gspread_dataframe").setLevel(logging.WARN)


def loggerFactory(name, level=logging.DEBUG):
    """Get a logger foa n module
    loggerFactory(__name__)
    """
    logger = logging.getLogger(name)

    coloredlogs.install(level=level, logger=logger)
    logger.setLevel(level)
    return logger