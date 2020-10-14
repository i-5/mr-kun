#!/bin/bash

BEAN_SUFFIX=
HOME_SUFFIX=HomeIntf
REMOTE_SUFFIX=RemoteIntf

if [ -z $WL_HOME ]; then
  echo Weblogic home not set.
  exit 1
fi

if [ -z $WL_SERVER ]; then
  echo Weblogic server not set.
  exit 1
fi

EJB_DIR=./java/jp/ne/sonet/mrkun/sessionEJB
DD_DIR=./META-INF

MYSERVER=$WL_HOME/$WL_SERVER

MYCLASSPATH=$JAVA_HOME/jre/lib/rt.jar:$WL_HOME/classes:$WL_HOME/lib/weblogicaux.jar:$WL_HOME/$WL_SERVER/serverclasses:$WL_HOME/$WL_SERVER/ejbclasses:$WL_HOME/$WL_SERVER/servletclasses

DEPLOYMENT_DIR=$WL_HOME/$WL_SERVER/ejb
JAR_FILE=$1.jar
TEMP_JAR_FILE=$1.temp.jar
echo Environment is set up...

if [ ! -f build ]; then
  mkdir build
fi
if [ ! -f build/META-INF ]; then
  mkdir build/META-INF
fi
cp $DD_DIR/$1/*.xml build/META-INF
echo Created temporary subdirectories...

echo Compiling .java files...
javac -g -classpath $MYCLASSPATH -d build $EJB_DIR/$1$BEAN_SUFFIX.java $EJB_DIR/$1$HOME_SUFFIX.java $EJB_DIR/$1$REMOTE_SUFFIX.java
RETVAL=$?
if [ ! $RETVAL = 0 ]; then
  echo Failed during compilation of .java files
  exit 1
fi
echo Done compiling .java files...

echo Beginning jarring...
jar cv0f $TEMP_JAR_FILE -C ./build .
RETVAL=$?
if [ ! $RETVAL = 0 ]; then
  echo Failed during jarring of compiled files
  exit 1
fi
echo Done jarring...

echo Running ejbc...
java -classpath $MYCLASSPATH -Dweblogic.home=$WL_HOME weblogic.ejbc $TEMP_JAR_FILE $DEPLOYMENT_DIR/$JAR_FILE
RETVAL=$?
if [ ! $RETVAL = 0 ]; then
  echo Failed during ejbc
  exit 1
fi
echo Done running ejbc...
echo Created $JAR_FILE in $DEPLOYMENT_DIR/...

echo Removing temporary files...
rm -f $TEMP_JAR_FILE
rm -rf build

echo
