#!/usr/bin/env bash
# run this script as root or run as sudo!

# define the variables that is used in building
OMERO_DB_USER=db_user
OMERO_DB_PASS=db_password
OMERO_DB_NAME=omero_database
OMERO_ROOT_PASS=omero_root_password
OMERO_DATA_DIR=/OMERO

apt update

apt -y install unzip wget bc cron apt-transport-https
apt -y install software-properties-common build-essential
apt -y install zlib1g-dev db5.3-util libssl-dev libbz2-dev libmcpp-dev libdb++-dev libdb-dev 

apt -y install python-dev python-{pip,tables,virtualenv,yaml,jinja2,setuptools,wheel,pillow,numpy,sphinx} virtualenv

add-apt-repository -y ppa:openjdk-r/ppa
apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys 5E6DA83306132997
apt-add-repository "deb http://zeroc.com/download/apt/ubuntu`lsb_release -rs` stable main"
add-apt-repository -y "deb https://apt.postgresql.org/pub/repos/apt/ xenial-pgdg main 9.6"
wget --quiet -O - https://www.postgresql.org/media/keys/ACCC4CF8.asc | apt-key add -


apt update
apt -y install openjdk-8-jre zeroc-ice-all-runtime zeroc-ice-all-dev postgresql-9.6

sed -i.bak -re 's/^(host.*)ident/\1md5/' /etc/postgresql/9.6/main/pg_hba.conf
service postgresql start

easy_install -U setuptools
pip install --upgrade pip
pip install "zeroc-ice>3.5,<3.7"

# creating user omero
useradd -m omero
passwd omero
chmod a+X ~omero
mkdir -p "$OMERO_DATA_DIR"
chown omero "$OMERO_DATA_DIR"

echo "CREATE USER $OMERO_DB_USER PASSWORD '$OMERO_DB_PASS'" | su - postgres -c psql
su - postgres -c "createdb -E UTF8 -O '$OMERO_DB_USER' '$OMERO_DB_NAME'"

psql -P pager=off -h localhost -U "$OMERO_DB_USER" -l