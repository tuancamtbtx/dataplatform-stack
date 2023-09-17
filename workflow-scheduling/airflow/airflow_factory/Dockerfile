FROM apache/airflow:2.4.3
USER root

# Install OpenJDK-11
RUN apt-get update \
  && apt-get install -y --no-install-recommends \
        heimdal-dev \
        openjdk-11-jre-headless \
  && apt-get autoremove -yqq --purge \
  && apt-get clean \
  && rm -rf /var/lib/apt/lists/*
# Set JAVA_HOME
ENV JAVA_HOME=/usr/lib/jvm/java-11-openjdk-arm64
RUN export JAVA_HOME

# setup airtrust core lib
RUN mkdir -p /airflow_factory
COPY --chown=airflow:airflow . /airflow_factory
COPY requirements.txt /

ENV PYTHONPATH=$PYTHONPATH:/airflow_factory

RUN export PATH=$PATH:$JAVA_HOME/bin
# setup JAVA home to submit spark job
RUN export PATH=$PATH:$SPARK_HOME/bin:$SPARK_HOME/sbin 

WORKDIR $AIRFLOW_HOME
USER airflow
RUN python -m pip install --upgrade pip
RUN pip install --no-cache-dir "apache-airflow==2.4.3" -r /requirements.txt
