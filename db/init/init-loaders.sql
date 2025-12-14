CREATE DATABASE IF NOT EXISTS loader_demo;
CREATE DATABASE IF NOT EXISTS loader_dinamico;
CREATE DATABASE IF NOT EXISTS loader_estatico;
CREATE DATABASE IF NOT EXISTS loader_metamapa;

GRANT ALL PRIVILEGES ON loader_demo.* TO 'metamapa'@'%';
GRANT ALL PRIVILEGES ON loader_dinamico.* TO 'metamapa'@'%';
GRANT ALL PRIVILEGES ON loader_estatico.* TO 'metamapa'@'%';
GRANT ALL PRIVILEGES ON loader_metamapa.* TO 'metamapa'@'%';
