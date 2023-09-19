# replace "/" by "-", vì docker tag không cho phép dấu "/"
ORIGINAL_BRAND_NAME=$(git symbolic-ref --short HEAD)
BRAND_NAME=${ORIGINAL_BRAND_NAME//[\/]/-}
VERSION=2.4.3
DOCKER_IMAGE=airflow-auto
DOCKER_REGISTRY=vantuan12345
#-----------------------------------------------------------------------
docker build . -t ${DOCKER_REGISTRY}/${DOCKER_IMAGE}:${VERSION}
docker push ${DOCKER_REGISTRY}/${DOCKER_IMAGE}:${VERSION}
