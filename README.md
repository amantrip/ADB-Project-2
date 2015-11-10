# COMS E6111 Project 2

## a. Team Details
- Akhilesh Mantriprgada (am4227)
- Yehuda Stuchins(yss2117)

## b. Files includes 
- Base64.java
- ClassificationNode.java
- Database.java
- DatabaseClassifier.java
- getWordsLynx.java
- Makefile
- Readme.md

## c. Running our program
- First run $make
- Then run $ java DatabaseClassifier
- The program will then prompt you for host, t_es, T_ec

## d. Description
Our design utilizes three main classes: DatabaseClassifier, Database, and ClassificationNode. The DatabaseClassifier is what performs the work classifying the database, the Database class represents the database, and the ClassificationNode class represents one of the nodes in the hierarchical categorization scheme. We begin by performing the classification of the database described in part 1, using the algorithm described in the QProber paper. During this stage, every time we calculate the coverage or specificity for a certain ClassificationNode and the database we are dealing with, we store the result in our Database class, so that it can easily be referenced the next time we need it. In addition, while we are trying to classify our database, since we have already queried Bing, we also extract the top four results. If the database ends up not being classified under that node, then we simply ignore the search results that we saved. After we are done classifying the database, then we retrieve the pages that have been stored as the top results and construct our content summaries.

## e. Bing Key
DhYpklKgt6GsLAFyq1lz7dX3KSwG/LCWxUdBNPhreJk=

## f. Addtional Information
Base64.java was modified from Modified from <a href="http://en.wikibooks.org/wiki/Algorithm_Implementation/Miscellaneous/Base64#Java_2">WikiBooks</a> 