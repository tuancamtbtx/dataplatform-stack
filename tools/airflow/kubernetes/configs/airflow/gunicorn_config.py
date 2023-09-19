#!/usr/bin/env python

import setproctitle
from airflow import settings

forwarded_allow_ips = "*"
secure_scheme_headers = {
    "X-FORWARDED-PROTOCOL": "ssl",
    "X-FORWARDED-PROTO": "https",
    "X-FORWARDED-SSL": "on",
}


def post_worker_init(_):
    """
    Set process title.
    This is used by airflow.cli.commands.webserver_command to track the status of the worker.
    """
    old_title = setproctitle.getproctitle()
    setproctitle.setproctitle(settings.GUNICORN_WORKER_READY_PREFIX + old_title)
