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
RUN mkdir -p /trustingsocial/airtrust
COPY --chown=airflow:airflow . /trustingsocial/airtrust
COPY requirements.txt /

ENV PYTHONPATH=$PYTHONPATH:/trustingsocial/airtrust

RUN export PATH=$PATH:$JAVA_HOME/bin

# RUN export SPARK_HOME='/opt/spark'



# Build deapp cli - author: Manh Tran
WORKDIR /trustingsocial/airtrust/third-party/de-application
RUN export DEAPP_HOME=$(pwd)
RUN python setup.py install
ENV PYTHONPATH=$PYTHONPATH:/trustingsocial/airtrust/third-party/de-application


# setup state machine 
WORKDIR /trustingsocial/airtrust/third-party/state_machine
RUN python setup.py install
ENV PYTHONPATH=$PYTHONPATH:/trustingsocial/airtrust/third-party/state_machine

# setup trustingsocial dag-factory core lib
WORKDIR /trustingsocial/airtrust/third-party/airflow-dag-factory
RUN python setup.py install
ENV PYTHONPATH=$PYTHONPATH:/trustingsocial/airtrust/third-party/airflow-dag-factory

# RUN export PATH=$PATH:$SPARK_HOME/bin:$SPARK_HOME/sbin
WORKDIR $AIRFLOW_HOME
USER airflow
RUN python -m pip install --upgrade pip
RUN pip install --no-cache-dir "apache-airflow==2.4.3" -r /requirements.txt
