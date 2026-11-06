# Configuration
Beluga can be configured through a YAML configuration file. This file will need to be mounted to the container
and the location of the config needs to be configured using the following environment variable: BELUGA_CONFIGURATION_FILE.

All configuration that is mounted through the <b>BELUGA_CONFIGURATION_FILE</b> environment variable support
environment variables so it is not necessary to put passwords in plain text in there you can choose an environment
variable of your own choosing.