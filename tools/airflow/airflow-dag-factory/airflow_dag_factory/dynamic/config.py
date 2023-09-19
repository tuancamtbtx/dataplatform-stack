import os

import yaml
import hashlib
import json

from airflow_factory.logger import loggerFactory
from airflow_factory.dynamic.io import TeamConfig
from airflow_factory.dynamic.utils import NoDatesSafeLoader

LOGGER = loggerFactory(__name__)

class YamlConfig:
    def __init__(self) -> None:
        pass

    def hash_content(self, f: os.PathLike, team_conf: TeamConfig):
        conf = self.read_content(f)
        if conf is None:
            return True
        conf_dict = json.dumps(conf, sort_keys = True).encode("utf-8")
        val_hashed = hashlib.md5(conf_dict).hexdigest()
        return val_hashed

    def read_content(self,path: os.PathLike):
        with open(path, "r") as f:
            conf = yaml.load(f, Loader=NoDatesSafeLoader)
        return conf
    
class YamlConverter:
    def __init__(self) -> None:
        pass

    def json_to_yaml(self):
        pass


class JsonLoader:
    def __init__(self) -> None:
        pass

    def load(self, path: os.PathLike):
        pass