import os

_ROOT = os.path.dirname(os.path.dirname(__file__))


def resolve_root(*arg: str):
    return os.path.join(_ROOT, *arg)