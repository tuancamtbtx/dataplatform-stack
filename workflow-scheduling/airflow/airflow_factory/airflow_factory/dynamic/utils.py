from datetime import datetime
import yaml
import os

def get_folder_name(folder_path):
    absoulte_path = os.path.abspath(folder_path)
    split_path =  absoulte_path.split("/")
    return split_path[len(split_path) - 2]

def get_file_name(file_path):
    return file_path.split("/")[-1].split(".")[0]

def get_file_type(file_path):
    return file_path.split("/")[-1].split(".")[1]

def get_current_time():
    return datetime.now().strftime("%Y-%m-%d %H:%M:%S")

class NoDatesSafeLoader(yaml.SafeLoader):
    @classmethod
    def remove_implicit_resolver(cls, tag_to_remove):
        """
        Remove implicit resolvers for a particular tag

        Takes care not to modify resolvers in super classes.

        We want to load datetimes as strings, not dates, because we
        go on to serialise as json which doesn't have the advanced types
        of yaml, and leads to incompatibilities down the track.
        """
        if not 'yaml_implicit_resolvers' in cls.__dict__:
            cls.yaml_implicit_resolvers = cls.yaml_implicit_resolvers.copy()

        for first_letter, mappings in cls.yaml_implicit_resolvers.items():
            cls.yaml_implicit_resolvers[first_letter] = [(tag, regexp)
                                                         for tag, regexp in mappings
                                                         if tag != tag_to_remove]


NoDatesSafeLoader.remove_implicit_resolver('tag:yaml.org,2002:timestamp')

