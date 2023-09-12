from os import listdir
from os.path import isfile, join
import os

def get_only_files_in_folder(mypath):
	if mypath[0] != "/":
		mypath = f"/{mypath}"
	return [f for f in listdir(mypath) if isfile(join(mypath, f))]

def remove_file(filepath):
	if os.path.exists(filepath):
		os.remove(filepath)
	else:
		print("The file does not exist")