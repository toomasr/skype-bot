Skype Bot
=============
Getting Started
-----------------------

I have written 2 posts about getting started, these will hold your hand on every step. Check them out
and write your own Skype bot today :)

 * [Skype Bot for Fun and Profit - Intro](http://toomasr.com/blog/2013/05/27/skype-bot-for-fun-and-profit/)
 * [Getting Started - everything you need before coding](http://toomasr.com/blog/2013/05/27/skype-bot-for-fun-and-profit-part-I/)
 * [Getting Running - lets get this puppy on the rode](http://toomasr.com/blog/2013/05/27/skype-bot-for-fun-and-profit-part-II/)

Building the Software
---------------------

You need to install the lib/skype...jar into your maven local repository.
I got the file from http://developer.skype.com/ and it did not have a version
number. I just called it 1.0. Easiest is to run the following command:

    mvn install:install-file -Dfile=lib/skypekit.jar \\
                -DgroupId=com.skype -DartifactId=skype-sdk -Dversion=1.1 \\
                -Dpackaging=jar

Running the Software
--------------------

You will need:

 * skype username/password
 * join skype developer program for 10 USD at http://developer.skype.com
 * create an application at http://developer.skype.com 
 * download a runtime for that application and keep it running (bot connects to that)
 * download your application certificate from http://developer.skype.com (the bot will use it)

Most configuration can be done by copying project.properties to personal.properties
and making changes there.

If you have any questions just drop me a line [@toomasr](http://twitter.com/#!/toomasr)

Where does this all fit in?
---------------------------

Here is a graph that explains where this Skype Bot fits into the grand scheme. You will also
see that the bot have means to take messages from 3rd party services via POST requests to the
embedded web server.

![Architecture](https://raw.github.com/toomasr/skype-bot/master/shots/skype-bot.png)
