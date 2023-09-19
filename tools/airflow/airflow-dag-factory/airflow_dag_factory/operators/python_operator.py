import subprocess
import sys
from typing import Dict, List

from airflow.operators.python import PythonOperator as BaseOp
from airflow.utils.decorators import apply_defaults


def fake_init_call():
    """Just by base PythonOperator"""
    pass


class PythonOperator(BaseOp):
    template_fields = ('python_callable_raw',)

    @apply_defaults
    def __init__(
        self,
        python_callable: str,
        requirements: List[str] = None,
        *args,
        **kwargs,
    ) -> None:
        self.requirements = requirements
        self.python_callable_raw = python_callable
        super(PythonOperator, self).__init__(
            python_callable=fake_init_call, *args, **kwargs
        )

    def execute(self, context: Dict):
        if self.requirements:
            self.log.info("Install packages: %s", self.requirements)
            cmds = [sys.executable, "-m", "pip", "install"]
            cmds.extend(self.requirements)
            subprocess.check_call(cmds)

        self.log.info("Render scripts")
        _globals = {}
        exec(str(self.python_callable_raw), _globals)
        if "main" not in _globals:
            raise Exception("python_callable required main functions")

        self.log.info("Invoke main functions")
        self.python_callable = _globals["main"]
        super(PythonOperator, self).execute(context)
        self.log.info("Done!")
