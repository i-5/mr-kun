#! /bin/sh
# $Id: build.sh,v 1.1.2.3 2001/09/10 04:16:47 rick Exp $

if [ -z "$JAVA_HOME" ]
then
JAVACMD=`which java`
if [ -z "$JAVACMD" ]
then
echo "Cannot find JAVA. Please set your PATH."
exit 1
fi

JAVA_BINDIR=`dirname $JAVACMD`
JAVA_HOME=$JAVA_BINDIR/..
fi

if [ -z "$WL_HOME" ]
then
echo "Cannot find WL_HOME. Please set your WL_HOME variable."
exit 1
fi

if [ -z "$WL_SERVER" ]
then
echo "Cannot find WL_SERVER. Please set your WL_SERVER variable."
exit 1
fi

if [ -z "$ANT_HOME" ] ; then
ANT_HOME="./ant"
fi

ANT_OPTS="-Dwl_home=$WL_HOME -Dwl_server=$WL_SERVER $ANT_OPTS"

JAVACMD=$JAVA_HOME/bin/java

cp=$ANT_HOME/lib/ant.jar:$ANT_HOME/lib/jaxp.jar:$ANT_HOME/lib/parser.jar
cp=$cp:$JAVA_HOME/lib/tools.jar:$CLASSPATH

$JAVACMD -classpath $cp:$CLASSPATH $ANT_OPTS org.apache.tools.ant.Main "$@"
