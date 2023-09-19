from airflow import DAG
import dagfactory
class TrustingDagBuilder():
	def __init__(self,absolute_path) -> None:
		self.absolute_path = absolute_path

	def generate_dags(self):
		self.dag_factory = dagfactory.DagFactory(self.absolute_path)
		self.dag_factory.clean_dags(globals())
		self.dag_factory.generate_dags(globals())
