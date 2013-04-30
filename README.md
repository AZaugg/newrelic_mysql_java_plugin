MySQL plugin for New Relic
==========================

Downloading the MySQL plugin

//TODO


Configuring your environment

  1.  New Relic Account License Key
  2.  MySQL privileges
  3.  MySQL properties
  
  
1. New Relic License

$ cp config/newrelic.properties.example config/newrelic.properties
# Edit config/newrelic.properties and enter your license key found under Account Settings at https://rpm.newrelic.com

2. MySQL Privileges

When deploying a MySQL plugin on each MySQL server, you require the following MySQL user privileges.

mysql> CREATE USER newrelic@localhost IDENTIFIED BY PASSWORD '*FDAF706717E70DB8DDAD0C5214B13770E1A80B0E';
mysql> GRANT PROCESS,REPLICATION CLIENT ON *.* TO newrelic@localhost;

When deploying a MySQL Plugin on a common server, the necessary change is to grant privileges to the applicable hostname,
or host mask, changing the value of localhost in the 2 SQL statements appropriately.


3. MySQL Properties


# cp config/mysql.instance.json.sample config/mysql.instance.json
# Edit config/mysql.instance.json

If you have configured the Agent to run on the MySQL instance, and used the MySQL user privileges in the 
previous step, you only need to give your MySQL instance a meaningful name.  All other options are default.

[
    {
        "name"   : "Server/Instance Name",
    }
]


You can override all necessary MySQL details with the following sytax.

[
    {
        "name"   : "Server/Instance Name",
        "host"   : "localhost",
        "user"   : "newrelic",
        "passwd" : "Your Password Here"
    },
 ]
