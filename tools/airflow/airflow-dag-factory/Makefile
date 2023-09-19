.PHONY: docker-compose-build
build:
	@export AIRFLOW_UID=500000
	@echo "==> ReBuilding for local testing"
	@docker-compose build

.PHONY: run-local
run:
	@export AIRFLOW_UID=500000
	@export AIRFLOW_UID=500000
	@echo "==> Start local"
	@docker-compose up

.PHONY: stop-local
stop:
	@export AIRFLOW_UID=500000
	@echo "==> Stop local"
	@docker-compose down