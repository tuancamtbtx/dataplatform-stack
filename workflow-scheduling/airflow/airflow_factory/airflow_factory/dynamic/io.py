import os
import multiprocessing
from typing import Tuple
import traceback
import itertools
import hashlib
from pathlib import Path

import subprocess

from airflow_factory.logger import loggerFactory
from airflow_factory.dynamic.render import dump_to_python, dump_notebook_to_python
from airflow_factory.dynamic.entity import Team, TeamConfig, RepoType
from airflow_factory.dynamic.utils import get_file_name, get_file_type, get_folder_name
from airflow_factory.dynamic.config import YamlConfig
LOGGER = loggerFactory(__name__)

hashed_dict = {}
class WorkerProcess(multiprocessing.Process):
	""" 
		Each worker has a internal queue to consume and process
	"""
	def __init__(self, 
			team_loader,
		):
		super().__init__()
		self.queue = multiprocessing.JoinableQueue(3)
		self.team_loader = team_loader
		self.py_yaml_render = RenderYamlToDagFile()
		self.py_notebook_render = RenderNoteBookToDagFile()
		
	def enqueue(self,item: Tuple[os.PathLike, TeamConfig]):
		self.queue.put(item)

	def run(self):
		STOP_VALUE = "DONE"
		for item in iter(self.queue.get, STOP_VALUE):
			try:
				self.process(item)
			except Exception as e:
				LOGGER.error(e)
				LOGGER.error(traceback.format_exc())
			finally:
				self.queue.task_done()
	
	def process(self, item):
		LOGGER.info("Processing %s", item)
		path, team = item
		# check file be changed or not if changed then render or skip
		yaml_conf = YamlConfig()
		hashed_val = yaml_conf.hash_content(path, team)
		if team.active == False:
			LOGGER.info("Team %s is not active, skip", team.name)
			return
		if hashed_val == hashed_dict.get(path, ""):
			LOGGER.info("File %s not changed, skip", path)
			return
		hashed_dict[path] = hashed_val
		file_type = get_file_type(path)
		if file_type == RepoType.Yaml or file_type == RepoType.Yml:
			self.py_yaml_render.render(path, team)
		elif file_type == RepoType.Notebook:
			self.py_notebook_render.render(path, team)


class DagIO():
	def __init__(
			self,
			team_loader: Team,
			num_process=8
		) -> None:
		self.team_loader = team_loader
		self.num_process = num_process
		self.workers =[]
		for _ in range(self.num_process):
			self.workers.append(
				WorkerProcess(
					team_loader=self.team_loader,
				)
			)


	def start_worker(self):
		LOGGER.info("Starting worker processes: %s", len(self.workers))
		for p in self.workers:
			p.start()

	def poll(self):
		LOGGER.info("Polling for tasks")
		for team_conf in self.team_loader.load().teams:
			git_team_folder = team_conf.git_folder
			LOGGER.info("Processing team %s - folder team: %s", team_conf.name,git_team_folder )
			# watcher all files , which be configured in folders (dags_config, notebook)
			config_files = itertools.chain(
				Path(git_team_folder).glob("dags_config/*.yaml"), 
				Path(git_team_folder).glob("dags_config/*.yml"),
				Path(git_team_folder).glob("notebook/**/*.ipynb")
			)
			for f in list(config_files):
				full_path = str(f.absolute())
				LOGGER.info("processing path %s", full_path)
				worker_id = int(
					hashlib.md5(full_path.encode("utf-8")).hexdigest(), 16
				) % len(self.workers)
				self.workers[worker_id].enqueue((full_path, team_conf))
		LOGGER.info("Done Queued!")

	def stop_worker(self):
		for p in self.workers:
			p.close()


class RenderYamlToDagFile():
	def __init__(self) -> None:
		pass

	def render(self, dag_conf:str, team: TeamConfig):
		try:
			LOGGER.debug("Render dag %s", dag_conf)
			render_location = team.render_location
			prefix_name = team.prefix_file
			file_name = get_file_name(dag_conf)
			dest = os.path.join(render_location,"{}_{}.py").format(prefix_name, file_name)
			LOGGER.debug("Write dag rendered to %s", dest)
			dag = dump_to_python(dag_conf)
			os.makedirs(render_location, exist_ok=True)
			with open(dest, "w") as fwriter:
					fwriter.write(dag)
			return True
		except Exception as e:
			LOGGER.error("Render Dag Error: %s", e)
			traceback.print_exc()
			return False
		

class RenderNoteBookToDagFile():
	def __init__(self) -> None:
		pass

	def dump_to_pickle(self, path_conf: os.PathLike):
		subprocess.run([
			'deapp', 'dag_dump', '-i', path_conf, '-o', 'output/dag_pickles/test'
		], stdout=subprocess.PIPE, stderr=subprocess.PIPE)

	def render(self, notebook_conf:str, team: TeamConfig):
		parent_dir = get_folder_name(notebook_conf)
		dag_jsons_dir = f"/opt/airflow/dags/deappconf/{team.prefix_file}/{parent_dir}/dag_jsons"
		pys_dir = f"/opt/airflow/dags/deappconf/{team.prefix_file}/{parent_dir}/pys"
		os.makedirs(dag_jsons_dir, exist_ok=True)
		os.makedirs(pys_dir, exist_ok=True)
		result = subprocess.run([
			'deapp', 'notebook', 'ipynb_parser', 
			'-tf','json',
			'-p',notebook_conf, 
			'-t', pys_dir, 
			'-d',dag_jsons_dir
			], stdout=subprocess.PIPE)
		LOGGER.info(result.stdout.decode('utf-8'))
		dag = dump_notebook_to_python(dag_jsons_dir, dag_name=parent_dir)
		render_location = team.render_location
		prefix_name = team.prefix_file
		file_name = get_file_name(notebook_conf)
		dest = os.path.join(render_location,"{}_{}.py").format(prefix_name, file_name)
		os.makedirs(render_location, exist_ok=True)
		with open(dest, "w") as fwriter:
			fwriter.write(dag)
		return True