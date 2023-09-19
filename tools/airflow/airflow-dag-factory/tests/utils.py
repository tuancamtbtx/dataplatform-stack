# from airtrust.dynamic.config import YamlConfig
# yaml_conf = YamlConfig()
# yaml_conf.hash_content("./data/x.yaml", None)
import os
def get_folder_name(folder_path):
    absoulte_path = os.path.abspath(folder_path)
    split_path =  absoulte_path.split("/")
    return split_path[len(split_path) - 2]


folder = get_folder_name("./data/x.yaml")
print(folder)
