# Deploy Airflow on Kubernetes

Fow now, all deployments are handled by tikici, checkout [../ci/](heml values) for more details.

This kustomize only apply for managing configmaps.

### First setup
**Init service account**

The service account is required to allow Airflow create pod for each task.

```bash
# choose your namespace first
kubectl create namespace airlock
# create the svc
kubectl create clusterrolebinding sherlock --clusterrole=edit --serviceaccount=airlock:sherlock --namespace=airlock
```

**Add git credentials**
```bash
# in github, only PERSONAL ACCESS TOKEN (PAT) is work as expected (for multi repo setups)
```

**Setups secret**
1. `airlock-secrets-env`
```
# airflow cores
AIRFLOW__CORE__FERNET_KEY=
AIRFLOW__CORE__SQL_ALCHEMY_CONN=mysql://...
AIRFLOW__WEBSERVER__SECRET_KEY=
AIRLOCK_MYSQL_URI=mysql://...

# use for oauth of google
GOOGLE_CLIENT_KEY=
GOOGLE_CLIENT_SECRET=

```

2. *git-creds*

Secret for `git-sync`, works with `stash.tiki.com.vn`. Checkout tutorials https://github.com/kubernetes/git-sync/blob/release-3.x/docs/ssh.md
TLDR;

```
ssh=
known_hosts=
```


### Setup configs
**Always check changes first**
```bash
kustomize build ./ | kubectl diff -f - -n airlock
```

**Apply**

```bash
kustomize build ./ | kubectl apply -f - -n airlock
```

### Extra things
1. Loggings for running pod

We use gcs as main logs store, but there's a drawback, that we only can see the logs after the task is done.

A way to workaround is using NFS to store logs file.

So we need a cron to cleanup these logs to reduce disk size.

2. Metrics exporter
We use statd-exproter to expose metrics from [airflow](https://airflow.apache.org/docs/apache-airflow/stable/logging-monitoring/metrics.html) to prometheus endpoint.

After that k8s will automatic fetch those metrics.

Checkout deployment at [./contrib/contrib.yaml](./contrib/contrib.yaml)
