from airtrust.logger import loggerFactory

LOGGER = loggerFactory(__name__)


def return_on_failure(value=None):
    def decorate(f):
        def applicator(*args, **kwargs):
            try:
                return f(*args, **kwargs)
            except Exception as e:
                LOGGER.error(e)
                return value

        return applicator

    return decorate