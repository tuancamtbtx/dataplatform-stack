from airflow.sensors.base import BaseSensorOperator
from airflow.utils.decorators import apply_defaults


from airflow_factory.logger import loggerFactory

logger = loggerFactory(__name__)

class HdfsSensor(BaseSensorOperator):
    @apply_defaults
    def __init__(self, prefix_path, *args, **kwargs) -> None:
        super().__init__(*args, **kwargs)
        self.prefix_path = prefix_path

    def poke(self, context):
        return True