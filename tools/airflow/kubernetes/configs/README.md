# Inside Airlock Config in productions

## Airflow core: `airlock-configmap`

- airflow.cfg: core airflow config
- pod_template.yml: k8s pod template
- webserver_config.py: airflow web extra settings (support oauth2)
- gunicorn_config.py: addition settings to run airflow behind a proxy (nginx ingress)
- team_conf.yaml: Setup repo for each team


Some scripts & env:
- ./core.yaml
- ./contrib.yaml:


## `airlock-config-env`

Expose some env
