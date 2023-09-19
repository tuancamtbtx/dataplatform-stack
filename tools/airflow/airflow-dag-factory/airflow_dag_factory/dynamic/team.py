from datetime import datetime
import yaml
import dacite

from airflow_factory.dynamic.entity import Team
from airflow_factory.dynamic.utils import NoDatesSafeLoader

class TeamLoader(object):
    RELOAD_SECONDS = 120 # 2 minutes
    def __init__(self, config_path) -> None:
        self.config_path = config_path
        self._last_read = None
        self._config = None

    def load(self):
        """load the yaml team config"""
        if self._last_read is None or self._should_reload():
            self._config = self._read_config()
            self._last_read = datetime.now()
        return self._config

    def _should_reload(self) -> bool:
        if self._last_read is None:
            return True
        diff = (datetime.now() - self._last_read).total_seconds()
        return diff >= self.RELOAD_SECONDS

    def _read_config(self):
        with open(self.config_path, "r") as f:
            conf = yaml.load(f, Loader=NoDatesSafeLoader)
        return dacite.from_dict(data_class=Team, data=conf)