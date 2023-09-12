import time
import logging
import argparse
import os
import sys
from airtrust.logger import loggerFactory
from airtrust.dynamic.io import DagIO
from airtrust.dynamic.team import TeamLoader

from airtrust import resolve_root

logging.getLogger("airflow").setLevel(logging.WARN)
logging.getLogger("airflow").setLevel(logging.WARN)
logging.getLogger("schedule").setLevel(logging.WARN)

LOGGER = loggerFactory(__name__)

TEAM_CONF = os.getenv("AIRTRUST_TEAM_CONF") or  resolve_root("conf", "team.yaml")
RENDER_LOCATION_CONF = os.getenv("AIRTRUST_RENDER_LOCATION") or resolve_root("dags")

def main():
	LOGGER.info(""" 
			░█████╗░██╗██████╗░████████╗██████╗░██╗░░░██╗░██████╗████████╗
			██╔══██╗██║██╔══██╗╚══██╔══╝██╔══██╗██║░░░██║██╔════╝╚══██╔══╝
			███████║██║██████╔╝░░░██║░░░██████╔╝██║░░░██║╚█████╗░░░░██║░░░
			██╔══██║██║██╔══██╗░░░██║░░░██╔══██╗██║░░░██║░╚═══██╗░░░██║░░░
			██║░░██║██║██║░░██║░░░██║░░░██║░░██║╚██████╔╝██████╔╝░░░██║░░░
			╚═╝░░╚═╝╚═╝╚═╝░░╚═╝░░░╚═╝░░░╚═╝░░╚═╝░╚═════╝░╚═════╝░░░░╚═╝░░░
				|supporter: tuan.nguyen3@trustingsocial.com|
		""")
	parser = argparse.ArgumentParser(description="Airtrust CLI")
	parser.add_argument("--team_config", default=TEAM_CONF)
	args = parser.parse_args()
	config_path = args.team_config
	LOGGER.info("Loading Team Config path %s", config_path)
	team_loader = TeamLoader(config_path=config_path)
	dag_io = DagIO(
		team_loader=team_loader,
		num_process=3
	)
	dag_io.start_worker()
	while 1:
		try:
			time.sleep(30)
			LOGGER.info(" >> Checking Dag Sync")
			dag_io.poll()
		except Exception as e:
			LOGGER.error(e)
		except KeyboardInterrupt:
			LOGGER.info(">> Stop Dag Sync")
			try:
				sys.exit(130)
			except SystemExit:
				os._exit(130)

if __name__ == "__main__":
	main()