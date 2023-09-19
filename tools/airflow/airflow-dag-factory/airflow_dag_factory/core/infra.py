
import os
class CacheCheckSumFile():
    """
    This class is used to save and get checksum of a file config
    """
    def __init__(self):
        pass

    def save(self, path: os.PathLike, checksum: str):
        with open(path, "w") as f:
            f.write(checksum)

    def get_checksum_by_path(self, path: os.PathLike):
        return "123456789"