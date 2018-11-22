# Export Aggregation Service

This service listens to messages generated as a result of CRUD data operations from various services; for further processing and storage. 

# Building from Source
Clone the git repository using the URL on the Gitlab home page:

    $ git clone git@git.etilizepak.com:burraq/export-aggregation-service.git
    $ cd export-aggregation-service

Existing folder or Git repository

    $ cd existing_folder
    $ git init
    $ git remote add origin http://git.etilizepak.com/burraq/export-aggregation-service.git


## Command Line
Use Maven 2.2 or 3.0, then on the command line:

   $ mvn clean install

## SpringSource Tool Suite (STS)
In STS (or any Eclipse distro or other IDE with Maven support), import the module directories as existing projects.  They should compile and the tests should run with no additional steps.


# Contributing to Burraq Api Aggregator
Here are some ways for you to get involved:
* Create [JIRA](http://jira.etilizepak.com/projects/EAS/) tickets for bugs and new features and comment and vote on the ones that you are interested in.
* If you want to write code, we encourage contributions through merge requests from forks of this repository.
If you want to contribute code this way, please familiarize yourself with the process outlined for contributing to projects here: [Contributor Guidelines](http://git.etilizepak.com/automation/sde/wikis/Contributor-Guidelines).
