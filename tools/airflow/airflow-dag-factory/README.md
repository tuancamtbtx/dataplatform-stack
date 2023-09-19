# Airtrust
[![Airtrust 2.4.3](https://github.com/tsocial/airtrust/actions/workflows/airtrust_2.4.3.yml/badge.svg)](https://github.com/tsocial/airtrust/actions/workflows/airtrust_2.4.3.yml)
<p align="center">
  <a><img src="./images/logo.png" alt="Airbyte"></a>
</p>
<p align="center">
    <em>Automate generate DAGs from many git repos and easy to config for build DAG</em>
</p>


### Contact:
- Email: tuan.nguyen3@trustingsocial.com
- Tele: Tuancamtbtx

### Note:
```
Please **DO NOT** edit code and data in this project
```

### Getting Started
 
```
	Install Docker vs Docker Compose
```
## How To Run

**Setup Env Var:**
```
	export AIRFLOW_UID=50000
```
**Build docker with docker-compose**
```
	docker-compose build
```
**Run docker**
```
	docker-compose up
```
**Config Airlfow connection for SFTP Server A vs B**

| SFTP Server | connection id |  Host   | Port | User name  | Password  | Connection Type|
|-------------|---------------|---------|------|------------|-----------|----------------|
| `a`         | `sftp_a_conn` | `sftp_a`| 22   | `foo`      | `pass`    |     SFTP		 | 
| `b`         | `sftp_b_conn` | `sftp_b`| 22   | `foo`		| `pass`    |	  SFTP       |

**Config Airlfow connection for Apache Spark Connection**

| Apache Spark Server   | connection id |        Host          |   Port |Connection Type|
|-----------------------|---------------|----------------------|--------|---------------|
| `apache spark cluster`| `spark_local` | `spark://192.168.0.1`| 7077   |     Spark		| 





**Overview System Flow**
![dags](./images/overview.png)

| Variable            | Default value |  Role                |
|---------------------|---------------|----------------------|
| `ARIFLOW USER`      | `airflow`     | admin 				 |
| `ARIFLOW_PASS`      | `airflow`     | admin				 |

## Build Docker
  **Edit image name**

  **Edit image version**

  **Edit docker username**

```
	- bash build_docker.sh
```

## Development (For Me)

Install python3.8 with virtualenv first

```bash
# in virtualenv
# install core airflow
python3 -m pip install -r requirements_airflow.txt --constraint ./constraints.txt --use-deprecated=legacy-resolver
# extra libs used in airtrust
python3 -m pip install -r requirements.txt --constraint ./constraints.txt --use-deprecated=legacy-resolver
```
**Setup Local Airflow**

*setup database*
- `export AIRFLOW__CORE__SQL_ALCHEMY_CONN=postgresql+psycopg2://airflow:airflow@127.0.0.1:5432/airflow`
```bash
airflow db init
```

**Scheduler**
```bash
airflow scheduler
```

**WebServer**
```bash
airflow webserver
```

## Benefits

* Construct DAGs without knowing Python
* Construct DAGs without learning Airflow primitives
* Avoid duplicative code
* Everyone loves YAML! :)

## Add New Team 

### Creating a private git repository and setting up the connection
```shell
  ssh-keygen -t rsa -b 4096 -C "your_email@example.com"
```
Once you hit enter, you will be prompted to enter the file in which to save the key. Write the directory of the .ssh folder (usually inside /home/[username]/.ssh) and choose a name for your key (in this case, airflowsshkey):
![sshkey](./images/sshkey.png)

Now that you have created your keygen, you need to go to your GitHub repository, click into settings and find ‘deploy keys’:
![deploy_app](./images/deploy_app.png)
To add a new deploy key, you will need the content inside the keygen you just generated. It is stored at /home/[username]/.ssh/[your_key_name].pub. Copy the pub key and paste it in GitHub to create your deployment key:

![deploy_app](./images/deploy_app.png)
